package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.devcharles.piazzapanic.interfaces.Renderable;

public class Entity implements Renderable {

    int drawX = 15;
    int drawY = 15;

    Texture texture;

    public Entity(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void render(SpriteBatch batch) {
        // TODO Auto-generated method stub
        batch.draw(texture, drawX, drawY);
    }

}
