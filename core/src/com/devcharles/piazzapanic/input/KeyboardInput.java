package com.devcharles.piazzapanic.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

public class KeyboardInput implements InputProcessor {

    public boolean left, right, up, down;

    public boolean changeCooks;

    public boolean interact;

    public boolean pickUp;


    @Override
    public boolean keyDown(int keycode) {
        boolean processed = true;
        switch (keycode) {
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
            case Keys.F:
                interact = true;
                break;
            case Keys.E:
                changeCooks = true;
                break;
            case Keys.Q:
                pickUp = true;
                break;
            default:
                processed = false;
        }
        return processed;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean processed = true;
        switch (keycode) {
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
            case Keys.E:
                changeCooks = false;
            case Keys.F:
                interact = false;
                break;
            case Keys.Q:
                pickUp = false;
                break;
            default:
                processed = false;
        }
        return processed;
    }

    @Override
    public boolean keyTyped(char character) {
        boolean processed = true;
        switch (character) {
            default:
                processed = false;
        }
        return processed;
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
