package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.TransformComponent;

public class PhysicsSystem extends IteratingSystem {

    private World world;
    private Array<Entity> bodies;

    // Map components to entities
    private ComponentMapper<B2dBodyComponent> bodyMap = ComponentMapper.getFor(B2dBodyComponent.class);
    private ComponentMapper<TransformComponent> transformMap = ComponentMapper.getFor(TransformComponent.class);

    private static final float MAX_FRAMETIME = 1/60f;
    private static float accumulator = 0f;

    public PhysicsSystem(World world) {
        // Iterate over all entities with a body and a transform
        super(Family.all(B2dBodyComponent.class, TransformComponent.class).get());
        this.world = world;
        this.bodies = new Array<Entity>();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodies.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;

        if (accumulator >= MAX_FRAMETIME) {
            world.step(MAX_FRAMETIME, 6, 2);
            accumulator -= MAX_FRAMETIME;

            for (Entity entity: bodies) {
                TransformComponent bodyTransform = transformMap.get(entity);
                B2dBodyComponent bodyC = bodyMap.get(entity);

                Vector2 position = bodyC.body.getPosition();

                // update our transform to match body position
                bodyTransform.position.x = position.x;
                bodyTransform.position.y = position.y;
                bodyTransform.rotation = bodyC.body.getAngle() * MathUtils.radiansToDegrees;
            }

        }
    }
    
}
