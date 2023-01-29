package com.devcharles.piazzapanic.utility;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CustomerAnimator extends WalkAnimator {

    public CustomerAnimator() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 4);

        // Load the sprite sheet as a Texture
        Texture walkSheet = new Texture("v2/customer/" + randomNum + ".png");
        Texture holdOneSheet = new Texture("v2/customer/" + randomNum + "_holding.png");

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
        int orientation = Math.round(rotation);

        int animationIndex = holding > 0 ? 1 : 0;

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
