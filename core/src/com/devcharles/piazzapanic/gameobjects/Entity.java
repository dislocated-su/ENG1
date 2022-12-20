package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.devcharles.piazzapanic.interfaces.Renderable;
/**
 * An entity is a base class that can move.
 */
public abstract class Entity implements Renderable {

    Vector2 size = new Vector2(1,1);

    Texture texture;

    Sprite sprite;

    Body body;
    
    public Entity(Texture texture) {
        this.texture = texture;
        this.sprite = new Sprite(texture);
        sprite.setOriginCenter();
        sprite.setColor(255, 0, 0, 255);
    }
    
    public void render(SpriteBatch batch) {
        Vector2 pos = body.getPosition();
        // Draw in the middle of the position instead of top-left
        
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));

        sprite.setBounds(pos.x - (size.x / 2), pos.y - (size.y / 2), size.x, size.y);
        sprite.setOrigin(size.x / 2, size.y / 2);

        sprite.draw(batch);
    }

    @Override
    public void dispose() {
        this.texture.dispose();   
    }

}
