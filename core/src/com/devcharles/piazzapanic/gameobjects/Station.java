package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.devcharles.piazzapanic.interfaces.Renderable;

/**
 * A station is a base class that does not need to move
 */
public class Station implements Renderable  {

    Texture texture;

    Sprite sprite;

    public Vector2 pos = new Vector2(1,1);
    public Vector2 size = new Vector2(1,1);
    
    public Station(Texture texture) {
        this.texture = texture;
        this.sprite = new Sprite(texture);
    }

    @Override
    public void render(SpriteBatch batch) {
        // TODO Auto-generated method stub
        batch.draw(sprite, pos.x, pos.y, size.x, size.y);
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }
}
