package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.graphics.Color;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightBuilder {

    private static final int rays = 1000;
    public static PointLight createPointLight(RayHandler rayHandler, float x, float y, Color c, float dist) {
        PointLight pl = new PointLight(rayHandler, rays, c, dist, x, y);
        pl.setSoftnessLength(10f);
        pl.setXray(false);
        return pl;
    }
}