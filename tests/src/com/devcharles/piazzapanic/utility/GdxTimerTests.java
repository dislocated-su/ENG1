package com.devcharles.piazzapanic.utility;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.devcharles.piazzapanic.BasicTest;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.utility.GdxTimer;

@RunWith(GdxTestRunner.class)
public class GdxTimerTests implements BasicTest {

    private GdxTimer timer;

    @Override
    @Before
    public void initialize() throws Exception {
        timer = new GdxTimer(1000, true, false);
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        GdxTimer t = new GdxTimer(0, false, false);
    }

    @Test
    public void tickTest() throws Exception {
        assertFalse("Timer returns false when tick is called with delta = 0", timer.tick(0));
        assertTrue("Timer returns true when tick is called with delta*1000 >= delay", timer.tick(1.5f));
    }
}