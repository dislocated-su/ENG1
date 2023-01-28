package com.devcharles.piazzapanic.utility;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CookAnimator extends WalkAnimator {

    private static final int COLS = 10, ROWS = 1;

    CookAnimator() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 3);

        // Load the sprite sheet as a Texture
        Texture walkSheet = new Texture("v2/chef_" + randomNum + ".png");
        Texture holdOneSheet = new Texture("v2/chef_" + randomNum + "_holding.png");
        Texture holdManySheet = new Texture("v2/chef_" + randomNum + "_crate.png");

        addTextures(walkSheet, 0);
        addTextures(holdOneSheet, 1);
        addTextures(holdManySheet, 2);
    }

    @Override
    public TextureRegion getFrame(float rotation, boolean isMoving, float frameTime, int holding) {

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

    // add textures to Walk Lists, made a function to avoid repeating code for
    // different states
    public void addTextures(Texture currentSheet, int value) {
        // Split the spritesheet into separate textureregions
        TextureRegion[][] tmp = TextureRegion.split(currentSheet, 32, 32);

        // Flatten the array
        TextureRegion[] frames = new TextureRegion[ROWS * COLS];
        int index = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        walkDown.add(new Animation<TextureRegion>(0.1f, Arrays.copyOfRange(frames, 0, 3)));
        walkUp.add(new Animation<TextureRegion>(0.1f, Arrays.copyOfRange(frames, 3, 6)));
        walkRight.add(new Animation<TextureRegion>(0.1f, Arrays.copyOfRange(frames, 6, 10)));

        TextureRegion[] toCopy = walkRight.get(value).getKeyFrames();
        TextureRegion[] flippedRegions = new TextureRegion[toCopy.length];

        for (int i = 0; i < flippedRegions.length; i++) {
            flippedRegions[i] = new TextureRegion(toCopy[i]);
            flippedRegions[i].flip(true, false);
        }

        walkLeft.add(new Animation<TextureRegion>(0.1f, flippedRegions));
    }
}
