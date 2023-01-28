package com.devcharles.piazzapanic.utility.box2d;

import com.badlogic.gdx.graphics.Color;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightBuilder {

    private static final int rays = 500;

    public static PointLight createPointLight(RayHandler rayHandler, float x, float y, Color c, float dist,
            boolean soft) {
        PointLight pl = new PointLight(rayHandler, rays, c, dist, x, y);
        pl.setContactFilter(CollisionCategory.LIGHTS.getValue(), (short) 0, CollisionCategory.BOUNDARY.getValue());
        pl.setSoft(soft);
        pl.setSoftnessLength(8f);
        pl.setXray(false);
        return pl;
    }

    public static PointLight createRoomLight(RayHandler rayHandler, float x, float y, Color c, float dist,
            boolean xray) {
        PointLight pl = new PointLight(rayHandler, rays, c, dist, x, y);
        pl.setContactFilter(CollisionCategory.LIGHTS.getValue(), (short) 0,
                (short) (CollisionCategory.BOUNDARY.getValue() | CollisionCategory.LIGHTS.getValue()));
        pl.setSoftnessLength(12.5f);
        pl.setXray(xray);

        return pl;
    }
}