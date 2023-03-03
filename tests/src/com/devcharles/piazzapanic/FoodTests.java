package com.devcharles.piazzapanic;

import static org.junit.Assert.assertTrue;

import org.junit.Before;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.utility.EntityFactory;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class FoodTests {

    private EntityFactory entityFactory;
    private PooledEngine engine;
    private World world;

    @Before
    public void initialize() throws Exception {
        world = new World(new Vector2(0, 0), true);
        entityFactory = new EntityFactory(engine, world);
        EntityFactory.cutFood(null);
    }

    @Test
    public void createBurgerTest() throws Exception {
        assertTrue(true);
    }
}
