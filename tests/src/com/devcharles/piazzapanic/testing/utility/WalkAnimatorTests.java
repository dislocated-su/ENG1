package com.devcharles.piazzapanic.testing.utility;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;
import com.devcharles.piazzapanic.utility.WalkAnimator;
import com.devcharles.piazzapanic.utility.WalkAnimator.Direction;

@RunWith(GdxTestRunner.class)
public class WalkAnimatorTests implements BasicTest {

    @Override
    @Before
    public void initialize() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        // TODO Auto-generated constructor stub
    }

    @Test
    public void rotationToDirectionTest() throws Exception {
        assertTrue("Check a direction is returned", WalkAnimator.rotationToDirection(0) instanceof Direction);
        WalkAnimator.rotationToDirection(0);
        WalkAnimator.rotationToDirection(45);
        WalkAnimator.rotationToDirection(-90);
        WalkAnimator.rotationToDirection(180);

    }
}
