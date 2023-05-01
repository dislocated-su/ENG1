package com.devcharles.piazzapanic.testing.components;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.devcharles.piazzapanic.components.CookingComponent;
import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class CookingComponentTests implements BasicTest {

    @Override
    @Before
    public void initialize() throws Exception {
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        CookingComponent c = new CookingComponent();
    }

    @Test
    public void resetTest() throws Exception {
        CookingComponent c = new CookingComponent();
        c.reset();
        assertFalse(c.processed);
    }
}
