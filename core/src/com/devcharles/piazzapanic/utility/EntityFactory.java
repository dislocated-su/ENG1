package com.devcharles.piazzapanic.utility;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
//import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
//import com.badlogic.gdx.physics.box2d.Filter;
//import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.devcharles.piazzapanic.components.AnimationComponent;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.TextureComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.components.WalkingAnimationComponent;
import com.devcharles.piazzapanic.components.StationComponent.StationType;
import com.devcharles.piazzapanic.utility.box2d.CollisionCategory;

public class EntityFactory {

    private PooledEngine engine;
    private World world;

    public EntityFactory(PooledEngine engine, World world) {
        this.engine = engine;
        this.world = world;
    }

    /**
     * Creates an controllable entity, and adds it to the engine.
     * 
     * @return Reference to the entity.
     */
    public Entity createCook(int x, int y) {
        Entity entity = engine.createEntity();

        B2dBodyComponent b2dBody = engine.createComponent(B2dBodyComponent.class);

        TransformComponent transform = engine.createComponent(TransformComponent.class);

        ControllableComponent controllable = engine.createComponent(ControllableComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);

        AnimationComponent an = engine.createComponent(AnimationComponent.class);

        WalkingAnimationComponent animation = engine.createComponent(WalkingAnimationComponent.class);

        animation.animator = new CookAnimator();
        // Texture
        TextureRegion[][] tempRegions = TextureRegion.split(new Texture("v2/chef_a.png"), 32, 32);

        texture.region = tempRegions[0][0];
        // TODO: Set size in viewport units instead of scale
        texture.scale.set(0.1f, 0.1f);

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
        circle.setRadius(0.5f);
        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 20f;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = CollisionCategory.ENTITY.getValue();
        fixtureDef.filter.maskBits = (short) (CollisionCategory.BOUNDARY.getValue()
                | CollisionCategory.ENTITY.getValue());

        // Create our fixture and attach it to the body
        b2dBody.body.createFixture(fixtureDef).setUserData(entity);

        // BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose();

        entity.add(b2dBody);
        entity.add(transform);
        entity.add(controllable);
        entity.add(texture);
        entity.add(an);
        entity.add(animation);

        engine.addEntity(entity);

        return entity;
    }

    /**
     * 
     * @param x, y coordinates of the station
     * @return Reference to the station entity
     */
    public Entity createStation(float x, float y, StationType stationType) {
        Entity entity = engine.createEntity();

        float[] size = { 1f, 1f };

        B2dBodyComponent b2dBody = engine.createComponent(B2dBodyComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);

        TransformComponent transform = engine.createComponent(TransformComponent.class);

        StationComponent station = engine.createComponent(StationComponent.class);
        station.type = stationType;

        // Texture
        TextureRegion[][] tempRegions = TextureRegion.split(new Texture("v2/stations_chef.png"), 32, 32);

        switch (stationType) {
            case oven:
                texture.region = tempRegions[0][0];
                break;
            default:
                texture.region = tempRegions[1][0];
                break;
        }
        // TODO: Set size in viewport units instead of scale
        // Gdx.graphics.getHeight/getWidth
        texture.scale.set(0.065f, 0.065f);

        // Box2d body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(x, y);

        b2dBody.body = world.createBody(bodyDef);

        // Create a PolygonShape and set it to be a box of 1x1
        PolygonShape stationBox = new PolygonShape();
        stationBox.setAsBox(size[0], size[1]);

        // Create our fixture and attach it to the body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = stationBox;
        b2dBody.body.createFixture(fixtureDef).setUserData(station);

        // BodyDef and FixtureDef don't need disposing, but shapes do.
        stationBox.dispose();

        // add components to the entity
        entity.add(b2dBody);
        entity.add(transform);
        entity.add(texture);
        entity.add(station);

        engine.addEntity(entity);

        return entity;
    }

    public Entity createFood(float x, float y) {
        Entity entity = engine.createEntity();

        TextureComponent texture = engine.createComponent(TextureComponent.class);

        TransformComponent transform = engine.createComponent(TransformComponent.class);

        FoodComponent food = engine.createComponent(FoodComponent.class);
        // Texture
        TextureRegion tempRegion = new TextureRegion(new Texture("bucket.png"));

        texture.region = tempRegion;

        // TODO: Set size in viewport units instead of scale
        texture.scale.set(0.15f, 0.15f);

        // add components to the entity
        entity.add(transform);
        entity.add(texture);
        entity.add(food);

        engine.addEntity(entity);

        return entity;
    }

}
