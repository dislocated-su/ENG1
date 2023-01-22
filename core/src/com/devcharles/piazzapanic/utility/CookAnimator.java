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
        walkSide = new Animation<TextureRegion>(0.1f, Arrays.copyOfRange(frames, 6, 10));
    }

    @Override
    public Pair<TextureRegion, Float> getFrame(float rotation, boolean isMoving, float frameTime) {
        Animation<TextureRegion> currentAnimation = walkDown;

        if (!isMoving) {
            frameTime = 0;
        }

        Direction dir = rotationToDirection(rotation);

        rotation = 0;
        switch (dir) {
            case up:
                currentAnimation = walkUp;
                break;
            case down:
                currentAnimation = walkDown;
                break;
            case left:
                currentAnimation = walkSide;
                rotation = 180;
                break;
            case right:
                currentAnimation = walkSide;
                break;
        }

        return new Pair<TextureRegion, Float>(currentAnimation.getKeyFrame(frameTime, true), rotation);
    }
}
