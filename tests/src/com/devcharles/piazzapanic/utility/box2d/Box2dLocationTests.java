package com.devcharles.piazzapanic.utility.box2d;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.math.Vector2;
import com.devcharles.piazzapanic.BasicTest;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.utility.box2d.Box2dLocation;

@RunWith(GdxTestRunner.class)
public class Box2dLocationTests implements BasicTest {

    private Box2dLocation b2dloc;
    private Vector2 vec;
    private float orient;

    @Override
    @Before
    public void initialize() throws Exception {
        // initialize not needed
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        b2dloc = new Box2dLocation();

        assertTrue("Check orientation is default value", b2dloc.getOrientation() == 0);
        assertFalse("Check position isn't null", b2dloc.getPosition() == null);
        assertTrue("Check position is a Vector2 ", b2dloc.getPosition() instanceof Vector2);
    }

    @Test
    public void getTests() throws Exception {
        orient = 4.0f;
        vec = new Vector2(0, 0);
        b2dloc = new Box2dLocation(vec, orient);

        assertTrue("Check Box2dLocation's attribute vector is the parameter given in the constructor",
                b2dloc.getPosition() == vec);
        assertTrue("Check Box2dLocation's attribute orientation is the parameter given in the constructor",
                b2dloc.getOrientation() == orient);
    }

    @Test
    public void setTests() throws Exception {
        b2dloc = new Box2dLocation();
        assertTrue("Check orientation is default value", b2dloc.getOrientation() == 0);
        assertTrue("Check position is a Vector2 ", b2dloc.getPosition() instanceof Vector2);

        orient = 4.0f;
        b2dloc.setOrientation(orient);
        assertTrue("Check orientation is updated to new value", b2dloc.getOrientation() == orient);
    }

}
