package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.utility.LightBuilder;

import box2dLight.RayHandler;

public class LightingSystem extends EntitySystem {

    private RayHandler rayHandler;
    private OrthographicCamera camera;

    public LightingSystem(World world, OrthographicCamera camera) {
        this.rayHandler = new RayHandler(world);
        this.camera = camera;

        LightBuilder.createPointLight(rayHandler, 10, 10, Color.CYAN, 15);
        LightBuilder.createPointLight(rayHandler, 20, 10, Color.MAGENTA, 15);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
    }

    @Override
    public void removedFromEngine(Engine engine) {
        // TODO Auto-generated method stub
        rayHandler.dispose();
    }
 }
