package com.devcharles.piazzapanic.testing.utility.box2d;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.utility.box2d.WorldContactListener;

public class WorldContactListenerTests implements BasicTest {

    private WorldContactListener worldContactListener;
    private Contact contact;
    private World world;

    @Override
    @Before
    public void initialize() throws Exception {
        worldContactListener = new WorldContactListener();
        world = new World(new Vector2(0, 0), false);
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        WorldContactListener worldContactListener = new WorldContactListener();
    }

}
