package com.devcharles.piazzapanic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.devcharles.piazzapanic.utility.GdxTimer;

@RunWith(GdxTestRunner.class)
public class GdxTimerTests {

    private GdxTimer timer;

    @Before
    public void initialize() throws Exception {
        timer = new GdxTimer(1000, true, false);
    }

    @Test
    public void tickTest() throws Exception {
        assertFalse("Timer returns false when tick is called with delta = 0", timer.tick(0));
        assertTrue("Timer returns true when tick is called with delta*1000 >= delay", timer.tick(1.5f));
    }
}