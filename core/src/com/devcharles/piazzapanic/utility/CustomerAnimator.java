package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.assets.AssetManager;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Animations specific to the customers.
 */
public class CustomerAnimator extends WalkAnimator {

  public CustomerAnimator(AssetManager assetManager) {
    int randomNum = ThreadLocalRandom.current().nextInt(1, 16);

    // Load the sprite sheet as a Texture
    Texture walkSheet = assetManager.get("v2/customer/" + randomNum + ".png", Texture.class);
    Texture holdOneSheet = assetManager.get("v2/customer/" + randomNum + "_holding.png",
        Texture.class);

    addTextures(walkSheet, 0);
    addTextures(holdOneSheet, 1);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.devcharles.piazzapanic.utility.WalkAnimator#getFrame(float, boolean,
   * float)
   */
  @Override
  public TextureRegion getFrame(float rotation, boolean isMoving, float frameTime, int holding) {

    int animationIndex = holding > 0 ? 1 : 0;

    Animation<TextureRegion> currentAnimation = walkDown.get(animationIndex);

    if (!isMoving) {
      frameTime = 0;
    }

    Direction dir = rotationToDirection(rotation);

    switch (dir) {
      case up:
        currentAnimation = walkUp.get(animationIndex);
        break;
      case down:
        currentAnimation = walkDown.get(animationIndex);
        break;
      case left:
        currentAnimation = walkLeft.get(animationIndex);
        break;
      case right:
        currentAnimation = walkRight.get(animationIndex);
        break;
    }

    return currentAnimation.getKeyFrame(frameTime, true);
  }
}
