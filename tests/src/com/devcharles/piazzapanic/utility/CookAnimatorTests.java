package com.devcharles.piazzapanic.utility;

import org.junit.Before;
import org.junit.Test;

import com.devcharles.piazzapanic.BasicTest;

public class CookAnimatorTests implements BasicTest {

    private CookAnimator animator;

    @Override
    @Before
    public void initialize() throws Exception {
        animator = new CookAnimator();
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        CookAnimator animator = new CookAnimator();
    }

    @Test
    public void getFrameTest() throws Exception {
        animator.getFrame(0, false, 0, 0);
        animator.getFrame(45, false, 0, 0);
        animator.getFrame(180, false, 0, 0);
        animator.getFrame(-90, false, 0, 0);
    }
}
