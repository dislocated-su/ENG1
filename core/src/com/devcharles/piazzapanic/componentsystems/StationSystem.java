package com.devcharles.piazzapanic.componentsystems;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.devcharles.piazzapanic.GameScreen;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.TintComponent;
import com.devcharles.piazzapanic.components.CookingComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.scene2d.Hud;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.Difficulty;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.Station;
import com.devcharles.piazzapanic.utility.Station.StationType;
import com.devcharles.piazzapanic.utility.WorldTilemapRenderer;

import static com.devcharles.piazzapanic.utility.Station.StationType.oven;

/**
 * This system manages player-station interaction and station food processing.
 */
public class StationSystem extends IteratingSystem {

    KeyboardInput input;

    boolean interactingStation = false;

    EntityFactory factory;
    WorldTilemapRenderer mapRenderer;
    Hud hud;

    private GameScreen gameScreen;
    private TintComponent readyTint;
    private float tickAccumulator = 0;
    private final Float[] tillBalance;
    private Difficulty difficulty;
    public Integer timer = 15;

    public StationSystem(KeyboardInput input, EntityFactory factory, WorldTilemapRenderer mapRenderer,
            Float[] tillBalance, Hud hud, Difficulty difficulty, GameScreen gameScreen) {
        super(Family.all(StationComponent.class).get());
        this.input = input;
        this.factory = factory;
        this.mapRenderer = mapRenderer;
        this.tillBalance = tillBalance;
        this.hud = hud;
        this.difficulty = difficulty;
        this.gameScreen = gameScreen;
    }

    @Override
    public void update(float deltaTime) {
        tickAccumulator += deltaTime;
        super.update(deltaTime);
        if (tickAccumulator > 0.5f) {
            tickAccumulator -= 0.5f;
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StationComponent station = Mappers.station.get(entity);

        stationTick(station, deltaTime);

        if (station.interactingCook != null) {

            PlayerComponent player = Mappers.player.get(station.interactingCook);

            if (player == null) {
                return;
            }

            if (player.putDown) {
                player.putDown = false;

                ControllableComponent controllable = Mappers.controllable.get(station.interactingCook);

                switch (station.type) {
                    case ingredient:
                        controllable.currentFood.pushItem(factory.createFood(station.ingredient),
                                station.interactingCook);
                        System.out.println(station.ingredient);
                        break;
                    case bin:
                        processBin(controllable);
                        break;

                    case serve:
                        processServe(station.interactingCook);
                        break;

                    default:
                        processStation(controllable, station);
                        break;
                }
            } else if (player.pickUp) {
                player.pickUp = false;

                ControllableComponent controllable = Mappers.controllable.get(station.interactingCook);
                switch (station.type) {
                    case ingredient:
                        controllable.currentFood.pushItem(factory.createFood(station.ingredient),
                                station.interactingCook);
                        break;
                    case bin:
                    case serve:
                        break;
                    default:
                        stationPickup(station, controllable);
                        break;
                }
            } else if (player.interact) {
                player.interact = false;
                interactStation(station);
            }
        }
    }

    /**
     * Try and process the food from the player.
     */
    private void processStation(ControllableComponent controllable, StationComponent station) {

        if (station.locked) {
            tryBuy(station);
            return;
        }

        if (controllable.currentFood.isEmpty()) {
            return;
        }

        Gdx.app.log("putDown", Mappers.food.get(controllable.currentFood.peek()).type.name());

        FoodComponent food = Mappers.food.get(controllable.currentFood.peek());

        HashMap<FoodType, FoodType> recipes = Station.recipeMap.get(station.type);

        if (recipes == null) {
            return;
        }

        FoodType result = recipes.get(food.type);

        if (result == null) {
            return;
        }

        int foodIndex = station.food.indexOf(null);

        // If there is space on the station
        if (foodIndex != -1) {
            // Pop if off player stack
            // Store in station
            station.food.set(foodIndex, controllable.currentFood.pop());
        } else {
            return;
        }

        // success

        CookingComponent cooking = getEngine().createComponent(CookingComponent.class);

        cooking.timer.start();

        // Flag the food as processed if InstaCook is active
        if (gameScreen.InstaCook) {
            cooking.processed = true;

        }

        station.food.get(foodIndex).add(cooking);

        Gdx.app.log("Food processed", String.format("%s turned into %s", food.type, result));

        // If the station is an oven start the cooking animation.
        if (station.type == oven) {
            mapRenderer.animateOven(station.tileMapPosition);
        }

    }

    /**
     * Perform special action (flipping patties, etc.)
     * 
     * @param station the station the action is being performed on.
     */
    private void interactStation(StationComponent station) {
        for (Entity food : station.food) {
            if (food == null || !Mappers.cooking.has(food)) {
                continue;
            }

            CookingComponent cooking = Mappers.cooking.get(food);

            // Check if it's ready without ticking the timer
            boolean ready = cooking.timer.tick(0);

            // Make the food ready if the InstaCook powerup is active
            if (gameScreen.InstaCook) {
                ready = true;
                return;
            }

            if (cooking.processed) {
                food.remove(TintComponent.class);
                return;
            }

            if (ready && !cooking.processed) {
                food.remove(TintComponent.class);
                cooking.processed = true;
                cooking.timer.reset();
                return;
            }
        }
    }

    /**
     * Try to combine the ingredients at the top of the player's inventory stack
     * (max 3) into a ready meal.
     * 
     * @param cook the cook whos inventory is being used for creating the food.
     */
    private void processServe(Entity cook) {
        ControllableComponent controllable = Mappers.controllable.get(cook);

        if (controllable.currentFood.size() < 2) {
            return;
        }

        int count = 2;
        FoodType result = tryAssemble(controllable, count);

        if (result == null) {
            result = tryAssemble(controllable, ++count);
            if (result == null) {
                return;
            }
        }

        for (int i = 0; i < count; i++) {
            Entity e = controllable.currentFood.pop();
            getEngine().removeEntity(e);
        }

        controllable.currentFood.pushItem(factory.createFood(result), cook);

    }

    /**
     * Attempt to create a food.
     * 
     * @param count number of ingredients to combine
     */
    private FoodType tryAssemble(ControllableComponent controllable, int count) {
        Set<FoodType> ingredients = new HashSet<FoodType>();
        int i = 0;
        for (Entity foodEntity : controllable.currentFood) {
            if (i > count - 1) {
                break;
            }
            ingredients.add(Mappers.food.get(foodEntity).type);

            i++;
        }

        return Station.assembleRecipes.get(ingredients);
    }

    /**
     * Destroy the top food in the inventory of a cook.
     */
    private void processBin(ControllableComponent controllable) {
        if (controllable.currentFood.isEmpty()) {
            return;
        }

        Entity e = controllable.currentFood.pop();
        getEngine().removeEntity(e);
    }

    /**
     * Pick up ready food from a station
     */
    private void stationPickup(StationComponent station, ControllableComponent controllable) {
        for (Entity foodEntity : station.food) {
            if (foodEntity != null && !Mappers.cooking.has(foodEntity)) {
                if (controllable.currentFood.pushItem(foodEntity, station.interactingCook)) {
                    station.food.set(station.food.indexOf(foodEntity), null);
                    Mappers.transform.get(foodEntity).scale.set(1, 1);
                    Gdx.app.log("Picked up", Mappers.food.get(foodEntity).type.toString());
                }
                return;
            }
        }
    }

    /**
     * Cook the food in the station. This progresses the timer in the food being
     * cooked in the station.
     * 
     * @param station
     * @param deltaTime
     */
    private void stationTick(StationComponent station, float deltaTime) {

        if (station.type == StationType.cutting_board && station.interactingCook == null) {
            return;
        }

        for (Entity foodEntity : station.food) {

            if (foodEntity == null || !Mappers.cooking.has(foodEntity)) {
                continue;
            }

            CookingComponent cooking = Mappers.cooking.get(foodEntity);

            boolean ready = cooking.timer.tick(deltaTime);
            if (gameScreen.InstaCook) {
                ready = true;
            }

            if (ready && cooking.processed) {
                cooking.timer.stop();
                cooking.timer.reset();

                switch (station.type) {
                    case cutting_board:
                        gameScreen.audio.playChop();
                        break;
                    case grill:
                        gameScreen.audio.playSizzle();
                        break;
                    case oven:
                        gameScreen.audio.playDing();
                        break;
                    case ingredient:
                        gameScreen.audio.playTap();
                        break;
                    case serve:
                        gameScreen.audio.playTap();
                        break;
                    default:
                        break;
                }

                FoodComponent food = Mappers.food.get(foodEntity);
                // Process the food into its next form
                food.type = Station.recipeMap.get(station.type).get(food.type);
                Mappers.texture.get(foodEntity).region = EntityFactory.getFoodTexture(food.type);
                foodEntity.remove(CookingComponent.class);
                Gdx.app.log("Food ready", food.type.name());

                // If the station is an oven turn off the animation.
                if (station.type == oven) {
                    mapRenderer.removeOvenAnimation(station.tileMapPosition);
                }
            } else if (ready) {

                if (tickAccumulator > 0.5f) {

                    if (!Mappers.tint.has(foodEntity)) {
                        foodEntity.add(readyTint);
                    } else {
                        foodEntity.remove(TintComponent.class);
                    }
                }

            }

        }
    }

    /**
     * Unlocks the current station if the player is in endless mode and has enough
     * money.
     * 
     * @param station The current station component with details about the current
     *                station.
     */
    public void tryBuy(StationComponent station) {
        // TODO sound effect for success or failure.
        // TODO set price for new stations.
        if (difficulty == Difficulty.SCENARIO) {
            hud.displayInfoMessage("You can only unlock new stations in endless mode");
            return;
        }
        float priceOfNewStation = 5;
        if (tillBalance[0] - priceOfNewStation < 0) {
            hud.displayInfoMessage("Insufficient funds - Each station costs $" + priceOfNewStation);
        } else {
            mapRenderer.unlockStation(station.tileMapPosition, station.type.getValue());
            tillBalance[0] -= priceOfNewStation;
            station.locked = false;
            hud.displayInfoMessage("New station unlocked!");
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        readyTint = getEngine().createComponent(TintComponent.class);
        readyTint.tint = Color.ORANGE;
    }

}
