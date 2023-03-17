package com.devcharles.piazzapanic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.ashley.core.Engine;
import com.devcharles.piazzapanic.utility.FoodStack;

@RunWith(GdxTestRunner.class)
public class FoodStackTests {

    /*
     * Tests for FoodStack's methods
     */

    private FoodStack stack;

    @Before
    public void initialize() throws Exception {
        stack = new FoodStack();
    }

    @Test
    public void initalizeTest() throws Exception {
        Engine e = new Engine();
        stack.init(null);
    }
}
