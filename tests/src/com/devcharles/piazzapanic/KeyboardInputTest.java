package com.devcharles.piazzapanic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Input.Keys;
import com.devcharles.piazzapanic.input.KeyboardInput;

@RunWith(GdxTestRunner.class)
public class KeyboardInputTest {

    private KeyboardInput keyboardInput;

    @Before
    public void initialize() throws Exception {
        keyboardInput = new KeyboardInput();
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
    public void keyTypedTest() throws Exception {
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
    }
}
