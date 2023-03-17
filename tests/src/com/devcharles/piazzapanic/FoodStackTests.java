package com.devcharles.piazzapanic;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.FoodStack;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;

@RunWith(GdxTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
        stack.clear();
        assertTrue("check pushItem method returns true", stack.pushItem(burger, cook));
        assertTrue("check burger entity is pushed on to the stack", stack.contains(burger));
    }

    @Test
    public void pushItemFullStackTest() throws Exception {
        stack.clear();
        for (int i = 0; i < stack.capacity; i++) {
            stack.pushItem(burger, cook);
        }
        assertFalse("check pushItem method returns false", stack.pushItem(burger, cook));
    }

    @Test
    public void popItemStackTest() throws Exception {
        stack.clear();
        for (int i = 0; i < stack.capacity; i++) {
            stack.pushItem(burger, cook);
        }
        assertTrue("check popped entity is returned", stack.pop() instanceof Entity);
    }

    @Test(expected = NoSuchElementException.class)
    public void popItemEmptyStackTest() throws Exception {
        stack.clear();
        stack.pop();
    }

}
