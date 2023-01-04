package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.interfaces.Renderable;

/**
 * A station is a base class that does not need to move
 */
public class Station implements Renderable {

    Texture texture;

    Sprite sprite;

    public Vector2 pos = new Vector2(10, 10);
    public Vector2 size = new Vector2(2, 2);

    public Station(Texture texture, World world, float x, float y) {
        this.texture = texture;
        this.sprite = new Sprite(texture);
        this.sprite.setOriginCenter();

        BodyDef stationBodyDef = new BodyDef();
        stationBodyDef.position.set(pos.x, pos.y);

        Body stationBody = world.createBody(stationBodyDef);

        PolygonShape stationBox = new PolygonShape();
        stationBox.setAsBox(1f, 1f);
        stationBody.createFixture(stationBox, 0f);
        stationBox.dispose();
    }

    @Override
    public void render(SpriteBatch batch) {

        sprite.setBounds(pos.x - (size.x / 2), pos.y - (size.y / 2), size.x, size.y);
        sprite.setOrigin(size.x / 2, size.y / 2);
        sprite.draw(batch);

    }

    @Override
    public void dispose() {

    }
}
