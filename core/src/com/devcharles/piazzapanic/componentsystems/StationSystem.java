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
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.TintComponent;
import com.devcharles.piazzapanic.components.CookingComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.Station;
import com.devcharles.piazzapanic.utility.Station.StationType;

public class StationSystem extends IteratingSystem {

    KeyboardInput input;

    boolean interactingStation = false;

    EntityFactory factory;

    private TintComponent readyTint;
    private float tickAccumulator = 0;

    public StationSystem(KeyboardInput input, EntityFactory factory) {
        super(Family.all(StationComponent.class).get());
        this.input = input;
        this.factory = factory;
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

    private void processStation(ControllableComponent controllable, StationComponent station) {

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

        // Pop if off player stack
        // Store in station
        int foodIndex = station.food.indexOf(null);
        if (foodIndex != -1) {
            station.food.set(foodIndex, controllable.currentFood.pop());
        } else {
            return;
        }

        CookingComponent cooking = getEngine().createComponent(CookingComponent.class);

        cooking.timer.start();

        station.food.get(foodIndex).add(cooking);

        // success
        Gdx.app.log("Food processed", String.format("%s turned into %s", food.type, result));
        // Mappers.food.get(controllable.currentFood.peek()).type = result;

    }

    private void interactStation(StationComponent station) {
        for (Entity food : station.food) {
            if (food == null || !Mappers.cooking.has(food)) {
                continue;
            }

            CookingComponent cooking = Mappers.cooking.get(food);

            // Check if it's ready without ticking the timer
            boolean ready = cooking.timer.tick(0);

            if (ready && !cooking.processed) {
                food.remove(TintComponent.class);
                cooking.processed = true;
                cooking.timer.reset();
                return;
            }
        }
    }

    private void processServe(Entity cook) {
        ControllableComponent controllable = Mappers.controllable.get(cook);

        if (controllable.currentFood.size() < 2) {
            return;
        }

        int count = 2;
        FoodType result = tryServe(controllable, count);

        if (result == null) {
            result = tryServe(controllable, ++count);
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

    private FoodType tryServe(ControllableComponent controllable, int count) {
        Set<FoodType> ingredients = new HashSet<FoodType>();

        for (Entity foodEntity : controllable.currentFood.subList(0, count)) {
            ingredients.add(Mappers.food.get(foodEntity).type);
        }

        return Station.serveRecipes.get(ingredients);
    }

    private void processBin(ControllableComponent controllable) {
        if (controllable.currentFood.isEmpty()) {
            return;
        }

        Entity e = controllable.currentFood.pop();
        getEngine().removeEntity(e);
    }

    private void stationPickup(StationComponent station, ControllableComponent controllable) {
        for (Entity foodEntity : station.food) {
            if (foodEntity != null && !Mappers.cooking.has(foodEntity)) {
                controllable.currentFood.pushItem(foodEntity, station.interactingCook);
                station.food.set(station.food.indexOf(foodEntity), null);
                Mappers.transform.get(foodEntity).scale.set(1, 1);
                Gdx.app.log("Picked up", Mappers.food.get(foodEntity).type.toString());
                return;
            }
        }
    }

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

            if (ready && cooking.processed) {
                cooking.timer.stop();
                cooking.timer.reset();

                FoodComponent food = Mappers.food.get(foodEntity);
                // Process the food into it's next form
                food.type = Station.recipeMap.get(station.type).get(food.type);
                Mappers.texture.get(foodEntity).region = factory.getFoodTexture(food.type);
                foodEntity.remove(CookingComponent.class);
                Gdx.app.log("Food ready", food.type.name());
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

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        readyTint = getEngine().createComponent(TintComponent.class);
        readyTint.tint = Color.ORANGE;
    }

}
