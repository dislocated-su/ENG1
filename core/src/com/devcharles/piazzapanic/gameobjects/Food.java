package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.interfaces.Renderable;

public class Food implements Renderable {
    public String name;

    Texture texture;

    Sprite sprite;

    Fixture fixture;

    public Vector2 pos = new Vector2();
    public Vector2 size = new Vector2(1, 1);

    public Food(Texture texture, World world, float x, float y) {
        this.texture = texture;
        this.sprite = new Sprite(texture);
        this.sprite.setOriginCenter();

        pos.x = x;
        pos.y = y;

        BodyDef foodBodyDef = new BodyDef();
        foodBodyDef.position.set(pos.x, pos.y);

        Body foodBody = world.createBody(foodBodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape foodBox = new PolygonShape();
        foodBox.setAsBox(size.x / 2, size.y / 2);
        fixtureDef.shape = foodBox;
        foodBody.createFixture(fixtureDef).setUserData(this);
        foodBox.dispose();
    }

    @Override
    public void render(SpriteBatch batch) {

        sprite.setBounds(pos.x - (size.x / 2), pos.y - (size.y / 2), size.x, size.y);
        sprite.setOrigin(size.x / 2, size.y / 2);
        sprite.draw(batch);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

}
