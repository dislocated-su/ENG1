package com.devcharles.piazzapanic.testing.componentsystems;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class DebugRendererSystemTests implements BasicTest {

    private World world;
    private OrthographicCamera camera;

    @Override
    @Before
    public void initialize() throws Exception {
        world = new World(new Vector2(0, 0), false);
        camera = new OrthographicCamera(0, 0);

    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        // DebugRendererSystem drs = new DebugRendererSystem(world, camera);
    }
}
