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
    }

    public void interact() {

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
        
        currentCook = availableCooks.items[cookID];
        
        Vector2 v = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            v.add(-1, 0);
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            v.add(1, 0);
        }
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            v.add(0, 1);
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            v.add(0, -1);
        }

        v = v.nor();

        currentCook.drawX += v.x * 600 * Gdx.graphics.getDeltaTime();
        currentCook.drawY += v.y * 600 * Gdx.graphics.getDeltaTime();
        
    }
}
