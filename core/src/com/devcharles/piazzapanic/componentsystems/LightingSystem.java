package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import box2dLight.RayHandler;

public class LightingSystem extends EntitySystem {

    private RayHandler rayHandler;
    private OrthographicCamera camera;

    public LightingSystem(RayHandler rayHandler, OrthographicCamera camera) {
        this.rayHandler = rayHandler;
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
    }
 }
