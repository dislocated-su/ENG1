package com.devcharles.piazzapanic.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

public class KeyboardInput implements InputProcessor {

    public boolean left, right, up, down;

    @Override
    public boolean keyDown(int keycode) {
        boolean processed = true;
        switch (keycode) 
        {
            case Keys.LEFT:
                left = true;
                break;
            case Keys.RIGHT:
                right = true;
                break;
            case Keys.UP:
                up = true;
                break;
            case Keys.DOWN:
                down = true;
                break;
            default:
                processed = false;
        }
        return processed;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean processed = true;
        switch (keycode) 
        {
            case Keys.LEFT:
                left = false;
                break;
            case Keys.RIGHT:
                right = false;
                break;
            case Keys.UP:
                up = false;
                break;
            case Keys.DOWN:
                down = false;
                break;
            default:
                processed = false;
        }
        return processed;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
    
}
