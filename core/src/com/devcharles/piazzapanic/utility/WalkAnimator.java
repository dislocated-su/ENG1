package com.devcharles.piazzapanic.utility;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class WalkAnimator {

    ArrayList<Animation<TextureRegion>> walkRight = new ArrayList<>();
    ArrayList<Animation<TextureRegion>> walkLeft = new ArrayList<>();
    ArrayList<Animation<TextureRegion>> walkUp = new ArrayList<>();
    ArrayList<Animation<TextureRegion>> walkDown = new ArrayList<>();

    public enum Direction {
        left,
        right,
        up,
        down
    }

    /**
     * @param rotation  box2d body rotation
     * @param isMoving  whether the entity is moving or not
     * @param frameTime elapsed time for the animation
     * @return A texture region to draw, and a rotation used for rendering the
     *         region.
     */
    public abstract TextureRegion getFrame(float rotation, boolean isMoving, float frameTime, int holding);

    private static HashMap<Integer, Direction> directionMap = new HashMap<Integer, Direction>() {
        {
            put(0, Direction.right);
            put(45, Direction.up);
            put(-45, Direction.down);
            put(90, Direction.up);
            put(-90, Direction.down);
            put(135, Direction.up);
            put(-135, Direction.down);
            put(180, Direction.left);
        }
    };

    public static Direction rotationToDirection(float rotation) throws InvalidParameterException {
        Direction dir = directionMap.get((int) Math.round(rotation));

        if (dir == null) {
            throw new InvalidParameterException("Unknown direction, must be specified in the directionMap.");
        }

        return dir;
    }
}
