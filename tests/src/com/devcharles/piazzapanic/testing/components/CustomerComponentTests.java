package com.devcharles.piazzapanic.testing.components;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class CustomerComponentTests implements BasicTest {

    @Override
    @Before
    public void initialize() throws Exception {
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        CustomerComponent c = new CustomerComponent();
    }

    @Test
    public void resetTest() {
        CustomerComponent c = new CustomerComponent();
        c.reset();
        assertTrue(c.order == null);
        assertTrue(c.interactingCook == null);
        assertTrue(c.food == null);
    }
}
