package com.devcharles.piazzapanic.gameobjects;

import java.security.Key;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class Player {

    public Cook currentCook;

    public Player(Cook c) {
        this.currentCook = c;
    }

    public void interact() {
        // speed is 0.7071 of left or right
        // if(Gdx.input.isKeyPressed(Keys.LEFT)) {
        //     if(Gdx.input.isKeyPressed(Keys.DOWN)) {
        //         currentCook.drawX -= 424 * Gdx.graphics.getDeltaTime();
        //         currentCook.drawY -= 424* Gdx.graphics.getDeltaTime();
            
        //     } else if(Gdx.input.isKeyPressed(Keys.UP)) {
        //         currentCook.drawX -= 424 * Gdx.graphics.getDeltaTime();
        //         currentCook.drawY += 424 * Gdx.graphics.getDeltaTime();
            
        //     } else{
        //         currentCook.drawX -= 600 * Gdx.graphics.getDeltaTime();
        //     }
        // }
        // else if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
        //     if(Gdx.input.isKeyPressed(Keys.DOWN)) {
        //         currentCook.drawX += 424 * Gdx.graphics.getDeltaTime();
        //         currentCook.drawY -= 424 * Gdx.graphics.getDeltaTime();
            
        //     } else if(Gdx.input.isKeyPressed(Keys.UP)) {
        //         currentCook.drawX += 424 * Gdx.graphics.getDeltaTime();
        //         currentCook.drawY += 424 * Gdx.graphics.getDeltaTime();
            
        //     } else{
        //         currentCook.drawX += 600 * Gdx.graphics.getDeltaTime();
        //     }
        // }
        // else if(Gdx.input.isKeyPressed(Keys.DOWN)) 
        //     currentCook.drawY -= 600 * Gdx.graphics.getDeltaTime();
        // else if(Gdx.input.isKeyPressed(Keys.UP)) 
        //     currentCook.drawY += 600 * Gdx.graphics.getDeltaTime();


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
