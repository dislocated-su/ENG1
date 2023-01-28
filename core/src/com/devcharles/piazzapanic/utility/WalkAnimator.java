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
     * Tuple Representing the dimensions of the spritesheet to be used.
     * The values represent columns and rows, respectively.
     */
    private static final Pair<Integer, Integer> dimensions = new Pair<Integer, Integer>(10, 1);

    /**
     * Cut the standard (for this project) animation spritesheet and initialise the
     * animation.
     * 
     * @param path Relative path to the spritesheet.
     */
    public WalkAnimator(String path) {
        // Load the sprite sheet as a Texture
        walkSheet = new Texture(path);

        // Split the spritesheet into separate textureregions
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, 32, 32);

        // Flatten the array
        TextureRegion[] frames = new TextureRegion[dimensions.first * dimensions.second];
        int index = 0;
        for (int i = 0; i < dimensions.second; i++) {
            for (int j = 0; j < dimensions.first; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        walkDown = new Animation<TextureRegion>(0.1f, Arrays.copyOfRange(frames, 0, 3));
        walkUp = new Animation<TextureRegion>(0.1f, Arrays.copyOfRange(frames, 3, 6));
        walkRight = new Animation<TextureRegion>(0.1f, Arrays.copyOfRange(frames, 6, 10));

        TextureRegion[] toCopy = walkRight.getKeyFrames();
        TextureRegion[] flippedRegions = new TextureRegion[toCopy.length];

        for (int i = 0; i < flippedRegions.length; i++) {
            flippedRegions[i] = new TextureRegion(toCopy[i]);
            flippedRegions[i].flip(true, false);
        }

        walkLeft = new Animation<TextureRegion>(0.1f, flippedRegions);
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

    protected int[] directions = { -135, -90, -45, 0, 45, 90, 135, 180 };
    
    public static Direction rotationToDirection(float rotation) throws InvalidParameterException {
        Direction dir = directionMap.get((int) Math.round(rotation));

        if (dir == null) {
            throw new InvalidParameterException("Unknown direction, must be specified in the directionMap.");
        }

        return dir;
    }
}
