package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.components.StationComponent.StationType;
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.utility.Mappers;

public class CollisionSystem extends IteratingSystem {

    KeyboardInput input;

    boolean interactingStation = false;

    public CollisionSystem(KeyboardInput input) {
        super(Family.all(StationComponent.class).get());
        this.input = input;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // Gdx.app.log("", "");
        StationComponent stationComponent = Mappers.station.get(entity);

        if (stationComponent.interactable) {

            PlayerComponent player = Mappers.player.get(stationComponent.interactingCook);

            if (player != null && player.interacting) {

                player.interacting = false;

                Gdx.app.log("correct", "");

                ControllableComponent controllable = Mappers.controllable.get(stationComponent.interactingCook);

                if (stationComponent.food == null || stationComponent.type == StationType.ingredient) {
                    switch (stationComponent.type) {
                        case oven:

                            break;
                        case grill:
                            if (!controllable.currentFood.empty() && stationComponent.food == null) {
                                FoodComponent foodComponent = Mappers.food.get(controllable.currentFood.peek());
                                if (foodComponent.type == FoodType.patty || foodComponent.type == FoodType.buns)
                                    stationComponent.food = controllable.currentFood.pop();
                                foodComponent.completed = true;
                            }
                            break;
                        case cutting_board:
                            if (!controllable.currentFood.empty()) {
                                FoodComponent foodComponent = Mappers.food.get(controllable.currentFood.peek());
                                if (foodComponent.type == FoodType.lettuce || foodComponent.type == FoodType.tomato)
                                    stationComponent.food = controllable.currentFood.pop();
                                foodComponent.completed = true;
                            }
                            break;
                        case sink:
                            break;
                        case bin:
                            if (!controllable.currentFood.empty()) {
                                controllable.currentFood.pop();
                            }
                            break;
                        case ingredient:
                            controllable.currentFood.push(stationComponent.food);
                            break;
                    }
                } else {
                    if (Mappers.food.get(stationComponent.food).completed
                            || stationComponent.type == StationType.ingredient) {
                        controllable.currentFood.push(stationComponent.food);
                        if (stationComponent.type != StationType.ingredient) {
                            stationComponent.food = null;
                        }
                    }
                }
            }

            return;

        }
    }
}
