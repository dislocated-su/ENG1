package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.math.Vector2;


/**
 * Utility class that contains methods for converting between meters and pixels.
 * Not used at the moment, but might need it in the future.
 * Why it turned out to be not very useful and how units work in the project:
 * https://xoppa.github.io/blog/pixels/
 */
public class UnitConversions {
    
    public final float scaling;

    public UnitConversions(float scaling) {
        this.scaling = scaling;
    }

    public Vector2 metersToPixels(float xM, float yM) {
        return new Vector2(xM * scaling, yM * scaling);
    }

    public Vector2 pixelsToMeters(float xP, float yP) {
        return new Vector2(xP / scaling, yP / scaling);
    }
}
