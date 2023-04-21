package com.devcharles.piazzapanic.utility;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.devcharles.piazzapanic.BasicTest;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.utility.CustomerAnimator;

@RunWith(GdxTestRunner.class)
public class CustomerAnimatorTests implements BasicTest {

    private CustomerAnimator animator;

    @Override
    @Before
    public void initialize() throws Exception {
        animator = new CustomerAnimator();
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        CustomerAnimator a = new CustomerAnimator();
    }

    @Test
    public void StationTypeTest() throws Exception {
        assertTrue("Check a TextureRegion is returned", animator.getFrame(0, false, 0, 0) instanceof TextureRegion);
    }
}
