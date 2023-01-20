package com.devcharles.piazzapanic.utility;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.TextureComponent;
import com.devcharles.piazzapanic.components.TransformComponent;

public class EntityFactory {

    private PooledEngine engine;
    private World world;
    
    public EntityFactory(PooledEngine engine, World world) {
        this.engine = engine;
        this.world = world;
    }

    /**
     * Creates an controllable entity, and adds it to the engine.
     * @return Reference to the entity.
     */
    public Entity createCook(int x, int y) {
        Entity entity = engine.createEntity();

        B2dBodyComponent b2dBody = engine.createComponent(B2dBodyComponent.class);

        TransformComponent transform = engine.createComponent(TransformComponent.class);

        ControllableComponent controllable = engine.createComponent(ControllableComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);

        // Texture
        texture.region = new TextureRegion(new Texture("droplet.png"));
        texture.scale.set(0.02f, 0.02f);

        // Box2d body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.linearDamping = 20f;
        bodyDef.fixedRotation = true;
        bodyDef.awake = true;

        bodyDef.position.set(x, y);

        b2dBody.body = world.createBody(bodyDef);

        // Create a circle shape and set its radius to 1
        CircleShape circle = new CircleShape();
        circle.setRadius(0.75f);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 20f;
        fixtureDef.friction = 0.4f;

        // Create our fixture and attach it to the body
        b2dBody.body.createFixture(fixtureDef).setUserData(this);

        // BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose();

        entity.add(b2dBody);
        entity.add(transform);
        entity.add(controllable);
        entity.add(texture);

        engine.addEntity(entity);

        return entity;
    }
}
