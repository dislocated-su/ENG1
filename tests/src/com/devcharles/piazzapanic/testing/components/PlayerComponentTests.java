package com.devcharles.piazzapanic.testing.components;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class PlayerComponentTests implements BasicTest {

    @Override
    @Before
    public void initialize() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        PlayerComponent c = new PlayerComponent();
    }

    @Test
    public void initialStateTest() throws Exception {
        PlayerComponent c = new PlayerComponent();
        assertFalse(c.interact);
        assertFalse(c.pickUp);
        assertFalse(c.putDown);
    }
}
