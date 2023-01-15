package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player {

    public Cook currentCook;

    public Array<Cook> availableCooks;

    private int cookID = 0;
    
    public Player(Array<Cook> cooks) {
        this.availableCooks = cooks;
        this.currentCook = this.availableCooks.items[cookID];
    }

    public void interact(float deltaTime) {

        if (Gdx.input.isKeyJustPressed(Keys.Q)) {
            if(cookID == 0) {
                cookID = availableCooks.size - 1;
            } else {
                cookID -= 1;
            }
        } else if (Gdx.input.isKeyJustPressed(Keys.E)) {
            if (cookID == availableCooks.size - 1) {
                cookID = 0;
            }else {
                cookID += 1;
            }
        }
        currentCook.isControlled = false;
        currentCook = availableCooks.items[cookID];
        currentCook.isControlled = true;

        Vector2 direction = new Vector2(0, 0);

        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            direction.add(-1, 0);
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            direction.add(1, 0);
        }
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            direction.add(0, 1);
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            direction.add(0, -1);
        }

        // Normalise vector (make length 1). This ensures player moves at the same speed in all directions.
        // e.g. if player wants to go left and up at the same time, the vector is (1,1) and length (speed) is sqrt(2)
        // but we need length to be 1
        direction.nor();

        Vector2 finalV = direction.cpy().scl(2000 * deltaTime);
        
        // Rotate the box2d shape in the movement direction
        if (!direction.isZero(0.7f)) {
            currentCook.body.setTransform(currentCook.body.getPosition(), direction.angleRad());
        }

        currentCook.body.applyLinearImpulse(finalV,currentCook.body.getPosition(), true);    
    }
}
