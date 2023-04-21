package com.devcharles.piazzapanic.testing.input;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.Key;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Input.Keys;
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class KeyboardInputTests implements BasicTest {

    private KeyboardInput keyboardInput;

    @Override
    @Before
    public void initialize() throws Exception {
        keyboardInput = new KeyboardInput();
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        KeyboardInput keyIn = new KeyboardInput();
    }

    @Test
    public void clearInputsTest() throws Exception {
        keyboardInput.clearInputs();

        assertFalse("Check all booleans are false when inputs are cleared",
                keyboardInput.left ||
                        keyboardInput.right ||
                        keyboardInput.up ||
                        keyboardInput.down ||
                        keyboardInput.changeCooks ||
                        keyboardInput.putDown ||
                        keyboardInput.pickUp ||
                        keyboardInput.interact ||
                        keyboardInput.disableHud);
    }

    @Test
    public void expectedKeyTest() throws Exception {
        keyboardInput.clearInputs();

        // testing using an expected key (Keys.S)

        // keyDown method
        assertFalse("Check the boolean associated with (Keys.S) is false", keyboardInput.down);
        assertTrue("Check the Key (Keys.S) is processed", keyboardInput.keyDown(Keys.S));
        assertTrue("Check the boolean associated with (Keys.S) is true", keyboardInput.down);

        // keyUp method
        assertTrue("Check the Key (Keys.S) is processed", keyboardInput.keyUp(Keys.S));
        assertFalse("Check the boolean associated with (Keys.S) is false ", keyboardInput.down);
    }

    @Test
    public void unexpectedKeyTest() throws Exception {
        keyboardInput.clearInputs();

        // testing using an unexpected key (Keys.APOSTROPHE)
        // keyDown method
        assertFalse("Check the Key (Keys.APOSTROPHE) isn't processed", keyboardInput.keyDown(Keys.APOSTROPHE));
        assertFalse("Check all booleans aren't changed from cleared state",
                keyboardInput.left ||
                        keyboardInput.right ||
                        keyboardInput.up ||
                        keyboardInput.down ||
                        keyboardInput.changeCooks ||
                        keyboardInput.putDown ||
                        keyboardInput.pickUp ||
                        keyboardInput.interact ||
                        keyboardInput.disableHud);

        // keyUp method
        assertFalse("Check the Key (Keys.APOSTROPHE) isn't processed", keyboardInput.keyUp(Keys.APOSTROPHE));
        assertFalse("Check all booleans aren't changed from cleared state",
                keyboardInput.left ||
                        keyboardInput.right ||
                        keyboardInput.up ||
                        keyboardInput.down ||
                        keyboardInput.changeCooks ||
                        keyboardInput.putDown ||
                        keyboardInput.pickUp ||
                        keyboardInput.interact ||
                        keyboardInput.disableHud);
    }

    @Test
    public void keyDownTests() throws Exception {
        keyboardInput.clearInputs();

        char[] keyString = { 'a', 'b', 'c', 'd', 'e', 'f' };
        for (char c : keyString) {
            assertFalse("Check char c isn't processed", keyboardInput.keyDown(c));
            assertFalse("Check all booleans aren't changed from cleared state",
                    keyboardInput.left ||
                            keyboardInput.right ||
                            keyboardInput.up ||
                            keyboardInput.down ||
                            keyboardInput.changeCooks ||
                            keyboardInput.putDown ||
                            keyboardInput.pickUp ||
                            keyboardInput.interact ||
                            keyboardInput.disableHud);
        }

        keyboardInput.keyDown(Keys.A);
        keyboardInput.keyDown(Keys.RIGHT);
        keyboardInput.keyDown(Keys.UP);
        keyboardInput.keyDown(Keys.W);
        keyboardInput.keyDown(Keys.DOWN);
        keyboardInput.keyDown(Keys.D);
        keyboardInput.keyDown(Keys.E);
        keyboardInput.keyDown(Keys.F);
        keyboardInput.keyDown(Keys.R);
        keyboardInput.keyDown(Keys.Q);
        keyboardInput.keyDown(Keys.H);

    }

    @Test
    public void keyUpTests() throws Exception {
        keyboardInput.keyUp(Keys.A);
        keyboardInput.keyUp(Keys.RIGHT);
        keyboardInput.keyUp(Keys.D);
        keyboardInput.keyUp(Keys.UP);
        keyboardInput.keyUp(Keys.W);
        keyboardInput.keyUp(Keys.DOWN);
        keyboardInput.keyUp(Keys.E);
        keyboardInput.keyUp(Keys.F);
        keyboardInput.keyUp(Keys.R);
        keyboardInput.keyUp(Keys.Q);
        keyboardInput.keyUp(Keys.H);

    }

    @Test
    public void touchTests() throws Exception {
        keyboardInput.touchDown(0, 0, 0, 0);
        keyboardInput.touchDragged(0, 0, 0);
        keyboardInput.touchUp(0, 0, 0, 0);
        keyboardInput.mouseMoved(0, 0);
        keyboardInput.scrolled(0, 0);
    }

    @Test
    public void keyTypedTest() throws Exception {
        keyboardInput.keyTyped('w');
    }

}
