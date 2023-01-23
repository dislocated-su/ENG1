package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.StationComponent;
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

                Gdx.app.log("test", "");
            }
            // action
            return;

        }
    }
}
