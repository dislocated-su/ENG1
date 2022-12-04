package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Cook extends Entity{

    public Cook() {      
        super(new Texture("droplet.png"));
        this.drawX = 120;
        this.drawY = 120;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, this.drawX, this.drawY);

    }
}
