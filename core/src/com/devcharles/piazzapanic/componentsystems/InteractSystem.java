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
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.Station;
import com.devcharles.piazzapanic.utility.Station.StationType;

public class InteractSystem extends IteratingSystem {

    KeyboardInput input;

    boolean interactingStation = false;

    public InteractSystem(KeyboardInput input) {
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

                Gdx.app.log("interact", "");

                ControllableComponent controllable = Mappers.controllable.get(stationComponent.interactingCook);

                if (stationComponent.type == StationType.ingredient) {
                    // Give the cook the ingredient
                    controllable.currentFood.push(stationComponent.food);
                } else if (!controllable.currentFood.isEmpty() && stationComponent.type == StationType.bin) {
                    // Remove existing food from player
                    controllable.currentFood.pop();
                } else if (!controllable.currentFood.isEmpty()) {
                    // Food processing stations
                    FoodComponent food = Mappers.food.get(controllable.currentFood.peek());

                    FoodType result = Station.recipeMap.get(stationComponent.type).get(food.type);

                    if (result != null) {
                        // success
                        Gdx.app.log("Food processed", String.format("%s turned into %s", food.type, result));
                        Mappers.food.get(controllable.currentFood.peek()).type = result;
                    }
                    // TODO: check if we have any food to pick up from any processing station
                    Gdx.app.log(stationComponent.type + " interact", "");
                }
            }
        }
        return;
    }
}
