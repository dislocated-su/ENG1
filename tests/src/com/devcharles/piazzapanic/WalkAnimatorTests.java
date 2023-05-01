package com.devcharles.piazzapanic;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.devcharles.piazzapanic.utility.WalkAnimator;
import com.devcharles.piazzapanic.utility.WalkAnimator.Direction;

@RunWith(GdxTestRunner.class)
public class WalkAnimatorTests {

    @Test
    public void rotationToDirectionTest() throws Exception {
        assertTrue("Check a direction is returned", WalkAnimator.rotationToDirection(0) instanceof Direction);
    }
}
