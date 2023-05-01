package com.devcharles.piazzapanic.utility;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Animations specific to the cooks.
 */
public class CookAnimator extends WalkAnimator {

    public CookAnimator() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 3);

        // Load the sprite sheet as a Texture
        Texture walkSheet = new Texture("v2/chef/" + randomNum + ".png");
        Texture holdOneSheet = new Texture("v2/chef/" + randomNum + "_holding.png");
        Texture holdManySheet = new Texture("v2/chef/" + randomNum + "_crate.png");

        addTextures(walkSheet, 0);
        addTextures(holdOneSheet, 1);
        addTextures(holdManySheet, 2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.devcharles.piazzapanic.utility.WalkAnimator#getFrame(float, boolean,
     * float)
     */
    @Override
    public TextureRegion getFrame(float rotation, boolean isMoving, float frameTime, int holding) {

        // holding > 1 -> 2
        // holding == 1 -> 1
        // 0 otherwise
        int index = holding > 1 ? 2 : (holding == 1 ? 1 : 0);

        Animation<TextureRegion> currentAnimation = walkDown.get(index);

        if (!isMoving) {
            frameTime = 0;
        }

        Direction dir = rotationToDirection(rotation);

        switch (dir) {
            case up:
                currentAnimation = walkUp.get(index);
                break;
            case down:
                currentAnimation = walkDown.get(index);
                break;
            case left:
                currentAnimation = walkLeft.get(index);
                break;
            case right:
                currentAnimation = walkRight.get(index);
                break;
        }

        return currentAnimation.getKeyFrame(frameTime, true);
    }
}
