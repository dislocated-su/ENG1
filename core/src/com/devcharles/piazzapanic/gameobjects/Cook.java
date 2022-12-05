package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.graphics.Texture;

public class Cook extends Entity{

    public Cook() {      
        super(new Texture("droplet.png"));
    }

    public Cook(float x, float y) {
        this();
        this.drawX = x;
        this.drawY = y;
    }
}
