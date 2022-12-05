package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.devcharles.piazzapanic.interfaces.Renderable;

public class Entity implements Renderable {

    float drawX = 1;
    float drawY = 1;

    Texture texture;

    Sprite sprite;
    
    public Entity(Texture texture) {
        this.texture = texture;
        this.sprite = new Sprite(texture);
    }
    
    public void render(SpriteBatch batch) {
        batch.draw(sprite, drawX, drawY);
    }

    @Override
    public void dispose() {
        this.texture.dispose();        
    }

}
