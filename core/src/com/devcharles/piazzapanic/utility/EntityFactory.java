package com.devcharles.piazzapanic.utility;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.components.AIAgentComponent;
import com.devcharles.piazzapanic.components.AnimationComponent;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.TextureComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.components.WalkingAnimationComponent;
import com.devcharles.piazzapanic.utility.box2d.Box2dLocation;
import com.devcharles.piazzapanic.utility.box2d.Box2dSteeringBody;
import com.devcharles.piazzapanic.utility.box2d.CollisionCategory;

public class EntityFactory {

    private PooledEngine engine;
    private World world;

    private FixtureDef movingFixtureDef;
    private BodyDef movingBodyDef;
    private CircleShape circleShape;

    public EntityFactory(PooledEngine engine, World world) {
        this.engine = engine;
        this.world = world;

        createDefinitions();
    }

    /**
     * Create reusable definitions for bodies and fixtures. These can be then be used while creating the bodies for entities.
     */
    private void createDefinitions() {
        
        // Moving bodies

        // Bodydef
        movingBodyDef = new BodyDef();
        
        movingBodyDef.type = BodyType.DynamicBody;
        movingBodyDef.linearDamping = 20f;
        movingBodyDef.fixedRotation = true;

        // Shape - needs to be disposed
        circleShape = new CircleShape();
        circleShape.setRadius(0.5f);

        // FixtureDef
        movingFixtureDef = new FixtureDef();
        movingFixtureDef.shape = circleShape;
        movingFixtureDef.density = 20f;
        movingFixtureDef.friction = 0.4f;
        movingFixtureDef.filter.categoryBits = CollisionCategory.ENTITY.getValue();
        movingFixtureDef.filter.maskBits = (short) (CollisionCategory.BOUNDARY.getValue()
                | CollisionCategory.ENTITY.getValue());
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

        // Reuse existing body definition
        movingBodyDef.position.set(x,y);
        b2dBody.body = world.createBody(movingBodyDef);
        b2dBody.body.createFixture(movingFixtureDef).setUserData(entity);

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
    public Entity createStation(float x, float y) {
        Entity entity = engine.createEntity();

        B2dBodyComponent b2dBody = engine.createComponent(B2dBodyComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);

        TransformComponent transform = engine.createComponent(TransformComponent.class);

        StationComponent station = engine.createComponent(StationComponent.class);
        // Texture
        TextureRegion tempRegion = new TextureRegion(new Texture("droplet.png"));

        texture.region = tempRegion;
        // TODO: Set size in viewport units instead of scale
        texture.scale.set(0.05f, 0.05f);

        // Box2d body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(x, y);

        b2dBody.body = world.createBody(bodyDef);

        // Create a PolygonShape and set it to be a box of 1x1
        PolygonShape stationBox = new PolygonShape();
        stationBox.setAsBox(1f, 1f);

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

    public Entity createCustomer(float x, float y) {
        Entity entity = engine.createEntity();

        B2dBodyComponent b2dBody = engine.createComponent(B2dBodyComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);

        TransformComponent transform = engine.createComponent(TransformComponent.class);

        AnimationComponent an = engine.createComponent(AnimationComponent.class);

        WalkingAnimationComponent walkingAnimaton = engine.createComponent(WalkingAnimationComponent.class);

        AIAgentComponent aiAgent = engine.createComponent(AIAgentComponent.class);

        walkingAnimaton.animator = new CustomerAnimator();

        // Reuse existing body definition
        movingBodyDef.position.set(x,y);
        b2dBody.body = world.createBody(movingBodyDef);
        b2dBody.body.createFixture(movingFixtureDef).setUserData(entity);
        
        texture.region = new TextureRegion(new Texture("droplet.png"));
        texture.scale.set(0.05f, 0.05f);

        transform.isHidden = false;

        // Ai agent setup
        aiAgent.steeringBody = new Box2dSteeringBody(b2dBody.body, false, 0.5f);
        
        Box2dLocation target = new Box2dLocation(new Vector2(10, 8), 180);

        Arrive<Vector2> arriveSb = new Arrive<Vector2>(aiAgent.steeringBody, target)
            .setTimeToTarget(0.1f)
            .setArrivalTolerance(0.5f)
            .setDecelerationRadius(2);

        aiAgent.steeringBody.setSteeringBehavior(arriveSb);

        entity.add(b2dBody);
        entity.add(transform);
        entity.add(texture);
        entity.add(an);
        entity.add(walkingAnimaton);
        entity.add(aiAgent);
        engine.addEntity(entity);

        return entity;
    }

}
