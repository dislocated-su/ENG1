package com.devcharles.piazzapanic.utility;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CookAnimator extends WalkAnimator {

    private static final int COLS = 10, ROWS = 1;

    CookAnimator() {
        // Load the sprite sheet as a Texture
        walkSheet = new Texture("v2/chef_a.png");

        // Split the spritesheet into separate textureregions
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, 32, 32);

        // Flatten the array
        TextureRegion[] frames = new TextureRegion[ROWS * COLS];
        int index = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
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

    @Override
    public TextureRegion getFrame(float rotation, boolean isMoving, float frameTime) {
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
