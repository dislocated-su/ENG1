package com.devcharles.piazzapanic.utility.box2d;

import com.badlogic.gdx.graphics.Color;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightBuilder {

    private static final int rays = 300;
    public static PointLight createPointLight(RayHandler rayHandler, float x, float y, Color c, float dist) {
        PointLight pl = new PointLight(rayHandler, rays, c, dist, x, y);
        pl.setContactFilter(CollisionCategory.LIGHTS.getValue(), (short)0, CollisionCategory.BOUNDARY.getValue());
        pl.setSoftnessLength(10f);
        pl.setXray(false);
        return pl;
    }
}