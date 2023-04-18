package com.devcharles.piazzapanic;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.devcharles.piazzapanic.utility.CustomerAnimator;

@RunWith(GdxTestRunner.class)
public class CustomerAnimatorTests {

    private CustomerAnimator animator;

    @Before
    public void initialize() throws Exception {
        animator = new CustomerAnimator();
    }

    @Test
    public void StationTypeTest() throws Exception {
        assertTrue("Check a TextureRegion is returned", animator.getFrame(0, false, 0, 0) instanceof TextureRegion);
    }
}
