package com.devcharles.piazzapanic.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Abstract class that helps with setting up animations for characters in the game. To use,
 * initialise  each directional animation.
 */
public abstract class WalkAnimator {

  protected ArrayList<Animation<TextureRegion>> walkRight = new ArrayList<>();
  protected ArrayList<Animation<TextureRegion>> walkLeft = new ArrayList<>();
  protected ArrayList<Animation<TextureRegion>> walkUp = new ArrayList<>();
  protected ArrayList<Animation<TextureRegion>> walkDown = new ArrayList<>();

  /**
   * All directions that the animations support.
   */
  public enum Direction {
    left,
    right,
    up,
    down
  }

  /**
   * Tuple Representing the dimensions of the spritesheet to be used. The values represent columns
   * and rows, respectively.
   */
  private static final Pair<Integer, Integer> dimensions = new Pair<>(10, 1);

  /**
   * @param rotation  box2d body rotation
   * @param isMoving  whether the entity is moving or not
   * @param frameTime elapsed time for the animation
   * @param holding   how many items the character is holding
   * @return A texture region to draw, and a rotation used for rendering the region.
   */
  public abstract TextureRegion getFrame(float rotation, boolean isMoving, float frameTime,
      int holding);

  private static final HashMap<Integer, Direction> directionMap = new HashMap<Integer, Direction>() {
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

  private static final int[] directions = {-135, -90, -45, 0, 45, 90, 135, 180};

  /**
   * Approximate the Box2D rotation to the nearest mapped value.
   *
   * @param rotation body rotation in degrees.
   * @return {@link Direction} the character should be facing in.
   */
  public static Direction rotationToDirection(float rotation) {

    int orientation = Math.round(rotation);

    // Find closest mapped integer value in directions
    int minDistance = Math.abs(directions[0] - orientation);

    int index = 0;
    for (int i = 0; i < directions.length; i++) {
      int currentDistance = Math.abs(directions[i] - orientation);

      if (currentDistance < minDistance) {
        index = i;
        minDistance = currentDistance;
      }
    }

    rotation = directions[index];

    return directionMap.get(Math.round(rotation));
  }

  /**
   * Generate Animations from a {@link Texture}, override this if you want to parse the texture
   * spritesheet differently.
   *
   * @param currentSheet The {@link Texture} containing all the frames of the animation.
   * @param variant        Variant of the spritesheet (0 -> walk, 1 -> hold one item, 2 -> hold
   *                     crate)
   */
  protected void addTextures(Texture currentSheet, int variant) {
    // Split the spritesheet into separate textureregions
    TextureRegion[][] tmp = TextureRegion.split(currentSheet, 32, 32);

    // Flatten the array
    TextureRegion[] frames = new TextureRegion[dimensions.first * dimensions.second];
    int index = 0;
    for (int i = 0; i < dimensions.second; i++) {
      for (int j = 0; j < dimensions.first; j++) {
        frames[index++] = tmp[i][j];
      }
    }

    walkDown.add(new Animation<>(0.1f, Arrays.copyOfRange(frames, 0, 3)));
    walkUp.add(new Animation<>(0.1f, Arrays.copyOfRange(frames, 3, 6)));
    walkRight.add(new Animation<>(0.1f, Arrays.copyOfRange(frames, 6, 10)));

    // Create the left animation by copying and flipping textures.
    TextureRegion[] toCopy = walkRight.get(variant).getKeyFrames();
    TextureRegion[] flippedRegions = new TextureRegion[toCopy.length];

    for (int i = 0; i < flippedRegions.length; i++) {
      flippedRegions[i] = new TextureRegion(toCopy[i]);
      flippedRegions[i].flip(true, false);
    }

    walkLeft.add(new Animation<>(0.1f, flippedRegions));
  }
}
