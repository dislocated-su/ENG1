package com.devcharles.piazzapanic.testing.components;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.devcharles.piazzapanic.components.PowerUpComponent;
import com.devcharles.piazzapanic.components.PowerUpComponent.PowerUpType;
import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class PowerUpComponentTests implements BasicTest {

    private PowerUpComponent component;

    @Override
    @Before
    public void initialize() throws Exception {
        component = new PowerUpComponent();
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        PowerUpComponent c = new PowerUpComponent();
        c.type = PowerUpType.InstaCook;
    }

    @Test
    public void expectedFromTest() {
        // test normal function
        assertTrue("Check expected int returns normal value", PowerUpType.from(1) == PowerUpType.BinACustomer);

    }

    @Test
    public void unexpectedFromTest() {
        // test unexpected functionality
        assertTrue("Check unexpected int returns null", PowerUpType.from(10000) == null);
    }
}
