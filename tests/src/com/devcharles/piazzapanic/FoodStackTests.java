package com.devcharles.piazzapanic;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.FoodStack;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;

@RunWith(GdxTestRunner.class)
public class FoodStackTests {

    /*
     * Tests for FoodStack's methods
     */

    private FoodStack stack;
    private Entity cook;
    private PooledEngine engine;
    private EntityFactory entityFactory;
    private World world;
    private Entity burger;

    @Before
    public void initialize() throws Exception {
        stack = new FoodStack();
        engine = new PooledEngine();
        world = new World(new Vector2(0, 0), true);
        entityFactory = new EntityFactory(engine, world);
        cook = entityFactory.createCook(0, 0);
        burger = entityFactory.createFood(FoodType.burger);
        stack.init(engine);
    }

    @Test
    public void pushItemEmptyStackTest() throws Exception {
        stack.pushItem(burger, cook);
        assertTrue("check burger entity is pushed on to the stack", stack.contains(burger));
        stack.clear();
    }

    @Test
    public void pushItemFullStackTest() throws Exception {

        for (int i = 0; i < 12; i++) {

        }
        stack.pushItem(burger, cook);
    }
}
