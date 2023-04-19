package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.utility.Mappers;

/**
 * Controls the one cook that has the PlayerComponent
 */
public class PlayerControlSystem extends IteratingSystem {

  KeyboardInput input;

  boolean changingCooks = false;
  PlayerComponent playerComponent;

  public PlayerControlSystem(KeyboardInput input) {
    super(Family.all(ControllableComponent.class).get());

    this.input = input;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {

    // Collect toggleable inputs
    // Those need to be toggled off once recieved to prevent registering the input
    // twice
    if (this.changingCooks) {
      this.changingCooks = false;
      entity.add(this.playerComponent);
    }

    if (!Mappers.player.has(entity)) {
      return;
    }

    if (input.changeCooks) {
      input.changeCooks = false;

      this.changingCooks = true; // Next cook in the queue will get playercomponent
      this.playerComponent = Mappers.player.get(entity);
      entity.remove(PlayerComponent.class);
      return;
    }

    if (input.putDown) {
      input.putDown = false;
      Mappers.player.get(entity).putDown = true;
    }
    if (input.pickUp) {
      input.pickUp = false;
      Mappers.player.get(entity).pickUp = true;
    }
    if (input.interact) {
      input.interact = false;
      Mappers.player.get(entity).interact = true;
    }

    B2dBodyComponent b2body = Mappers.b2body.get(entity);

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

    Vector2 finalV = direction.cpy()
        .scl(2000 * deltaTime * Mappers.controllable.get(entity).speedModifier);

    // Rotate the box2d shape in the movement direction
    if (!direction.isZero(0.7f)) {
      b2body.body.setTransform(b2body.body.getPosition(), direction.angleRad());
      b2body.body.applyLinearImpulse(finalV, b2body.body.getPosition(), true);
    }
  }

}
