package com.devcharles.piazzapanic.componentsystems;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.devcharles.piazzapanic.BasicTest;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.componentsystems.CarryItemsSystem;
import com.badlogic.ashley.core.PooledEngine;

@RunWith(GdxTestRunner.class)
public class CarryItemsSystemTests implements BasicTest {

    private PooledEngine engine;
    private CarryItemsSystem carrySystem;

    @Override
    @Before
    public void initialize() throws Exception {
        engine = new PooledEngine();
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        this.carrySystem = new CarryItemsSystem();

        assertTrue("Check a CarryItemsSystem is returned when constructed", carrySystem instanceof CarryItemsSystem);
    }

    // @Test
    // public void processEntityTest() throws Exception {
    // engine.addSystem(carrySystem);
    // engine.update(0.1f);
    // }

}
