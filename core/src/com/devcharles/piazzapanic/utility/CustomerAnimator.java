package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CustomerAnimator extends WalkAnimator {

    public CustomerAnimator() {
        super("v2/workers_F_A.png");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.devcharles.piazzapanic.utility.WalkAnimator#getFrame(float, boolean,
     * float)
     */
    @Override
    public TextureRegion getFrame(float rotation, boolean isMoving, float frameTime) {
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

        Animation<TextureRegion> currentAnimation = walkDown;

        if (!isMoving) {
            frameTime = 0;
        }

        Direction dir = rotationToDirection(rotation);

        switch (dir) {
            case up:
                currentAnimation = walkUp;
                break;
            case down:
                currentAnimation = walkDown;
                break;
            case left:
                currentAnimation = walkLeft;
                break;
            case right:
                currentAnimation = walkRight;
                break;
        }

        return currentAnimation.getKeyFrame(frameTime, true);
    }
}
