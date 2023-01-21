package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.input.KeyboardInput;

public class PlayerControlSystem extends IteratingSystem {

    ComponentMapper<PlayerComponent> playerMapper;
    ComponentMapper<B2dBodyComponent> bodyMapper;
    ComponentMapper<ControllableComponent> controlMapper;

    KeyboardInput input;

    boolean changingCooks = false;
    PlayerComponent playerComponent;

    public PlayerControlSystem(KeyboardInput input) {
        super(Family.all(ControllableComponent.class).get());

        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        bodyMapper = ComponentMapper.getFor(B2dBodyComponent.class);
        controlMapper = ComponentMapper.getFor(ControllableComponent.class);

        this.input = input;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if (this.changingCooks) {
            this.changingCooks = false;
            entity.add(this.playerComponent);
        }

        if (!playerMapper.has(entity)) {
            return;
        }

        if (input.changeCooks) {
            input.changeCooks = false;

            this.changingCooks = true; // Next cook in the queue will get playercomponent
            this.playerComponent = entity.getComponent(PlayerComponent.class);
            entity.remove(PlayerComponent.class);
            return;
        }

        B2dBodyComponent b2body = bodyMapper.get(entity);

        Vector2 direction = new Vector2(0, 0);

        // collect all the movement inputs
        if (input.left) {
            direction.add(-1, 0);
        }
        if (input.right) {
            direction.add(1, 0);
        }
        if (input.up) {
            direction.add(0, 1);
        }
        if (input.down) {
            direction.add(0, -1);
        }

        // Normalise vector (make length 1). This ensures player moves at the same speed
        // in all directions.
        // e.g. if player wants to go left and up at the same time, the vector is (1,1)
        // and length (speed) is sqrt(2)
        // but we need length to be 1
        direction.nor();

        Vector2 finalV = direction.cpy().scl(2000 * deltaTime);

        // Rotate the box2d shape in the movement direction
        if (!direction.isZero(0.7f)) {
            b2body.body.setTransform(b2body.body.getPosition(), direction.angleRad());
            b2body.body.applyLinearImpulse(finalV, b2body.body.getPosition(), true);
        }
    }

}
