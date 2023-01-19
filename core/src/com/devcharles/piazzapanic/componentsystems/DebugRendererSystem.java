package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class DebugRendererSystem extends IteratingSystem {

    private Box2DDebugRenderer debugRenderer;
    private World world;
    private OrthographicCamera camera;
    
    public DebugRendererSystem(World world, OrthographicCamera camera) {
        super(Family.all().get());
        this.world = world;
        this.camera = camera;

        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        debugRenderer.render(world, camera.combined);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
    
}
