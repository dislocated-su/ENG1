package com.devcharles.piazzapanic.utility;

import java.security.InvalidParameterException;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class WalkAnimator {
    Texture walkSheet;

    Animation<TextureRegion> walkSide;
    Animation<TextureRegion> walkUp;
    Animation<TextureRegion> walkDown;

    enum Direction {
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
    public abstract Pair<TextureRegion, Float> getFrame(float rotation, boolean isMoving, float frameTime);

    HashMap<Integer, Direction> directionMap = new HashMap<Integer, Direction>() {
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

    protected Direction rotationToDirection(float rotation) throws InvalidParameterException {
        Direction dir = directionMap.get((int) Math.round(rotation));

        if (dir == null) {
            throw new InvalidParameterException("Unknown direction, must be specified in the directionMap.");
        }

        return dir;
    }
}
