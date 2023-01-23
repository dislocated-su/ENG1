package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.input.KeyboardInput;

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

        if (input.interactStation && entity.getComponent(StationComponent.class).interactable) {
            if (entity.getComponent(StationComponent.class).interactingCook
                    .getComponent(PlayerComponent.class) != null) {

                input.interactStation = false;

                Gdx.app.log("correct", "");

                Gdx.app.log("test", "");
            }
            // action
            return;

        }
    }
}
