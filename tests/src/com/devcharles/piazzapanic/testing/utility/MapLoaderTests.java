package com.devcharles.piazzapanic.testing.utility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.MapLoader;

import box2dLight.RayHandler;

@RunWith(GdxTestRunner.class)
public class MapLoaderTests implements BasicTest {

    private PooledEngine engine;
    private World world;

    @Override
    @Before
    public void initialize() throws Exception {
        engine = new PooledEngine();
        world = new World(new Vector2(0, 0), false);
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        MapLoader mapLoader = new MapLoader("v2/map.tmx", 1, new EntityFactory(engine, world));
    }

    @Test
    public void buildStationsTest() throws Exception {
        MapLoader mapLoader = new MapLoader("v2/map.tmx", 1, new EntityFactory(engine, world));
        mapLoader.buildStations(engine, world);
    }

    @Test
    public void buildCollisionsTest() throws Exception {
        MapLoader mapLoader = new MapLoader("v2/map.tmx", 1, new EntityFactory(engine, world));
        mapLoader.buildCollisions(world);
    }
}
