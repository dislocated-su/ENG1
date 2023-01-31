package com.devcharles.piazzapanic.utility.box2d;

import com.badlogic.gdx.graphics.Color;

import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 * Helper class that stores light definitions.
 */
public class LightBuilder {

    private static final int rays = 500;

    /**
     * Create a point light.
     * @param rayHandler the rayhandler to be added to.
     * @param x x-position in world coordinates
     * @param y y-position in world coordinates
     * @param c Colour of the light.
     * @param dist distance of light, how far does it reach.
     * @param soft Enables/disables softness on tips of this light beams
     * @return {@link PointLight} reference.
     */
    public static PointLight createPointLight(RayHandler rayHandler, float x, float y, Color c, float dist,
            boolean soft) {
        PointLight pl = new PointLight(rayHandler, rays, c, dist, x, y);
        pl.setContactFilter(CollisionCategory.LIGHTS.getValue(), (short) 0, CollisionCategory.BOUNDARY.getValue());
        pl.setSoft(soft);
        pl.setSoftnessLength(8f);
        pl.setXray(false);
        return pl;
    }
    /**
     * Create a softer light to cover the whole room.
     * @param rayHandler the rayhandler to be added to.
     * @param x x-position in world coordinates
     * @param y y-position in world coordinates
     * @param c Colour of the light.
     * @param dist distance of light, how far does it reach.
     * @param xray If true, light will bleed trough objects
     * @return {@link PointLight} reference.
     */
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