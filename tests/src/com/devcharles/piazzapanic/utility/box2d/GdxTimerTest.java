package com.devcharles.piazzapanic.utility.box2d;


import static org.junit.Assert.*;

import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.utility.GdxTimer;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class GdxTimerTest {

    @Test
    public void tickTestNormal(){
        GdxTimer spawnTimer = new GdxTimer(30000, true, true);
        int incrementOriginalTime = spawnTimer.getElapsed()+1000;
        spawnTimer.tick(1);
        assertEquals("Expect timer to increment by 1.",incrementOriginalTime,spawnTimer.getElapsed());
    }

    @Test
    public void tickTestDeltaZero(){
        GdxTimer spawnTimer = new GdxTimer(30000, true, true);
        int incrementOriginalTime = spawnTimer.getElapsed();
        spawnTimer.tick(0);
        assertEquals("Expect no time to be added.",incrementOriginalTime,spawnTimer.getElapsed());
    }

    @Test
    public void tickTestElapsedGreater(){
        GdxTimer spawnTimer = new GdxTimer(30000, true, true);
        int incrementOriginalTime = spawnTimer.getElapsed()+1000;
        spawnTimer.tick(31);
        assertEquals("Expect timer to reset after 30s then continue.",incrementOriginalTime,spawnTimer.getElapsed());
    }

    @Test
    public void tickTestDeltaSmall(){
        GdxTimer spawnTimer = new GdxTimer(30000, true, true);
        int incrementOriginalTime = (int) (spawnTimer.getElapsed()+0.1);
        spawnTimer.tick(0.0001f);
        assertEquals("Expect timer to increase by a small amount.",incrementOriginalTime,spawnTimer.getElapsed());
    }

    @Test
    public void tickTestDeltaLarge(){
        GdxTimer spawnTimer = new GdxTimer(30000, true, true);
        int incrementOriginalTime = spawnTimer.getElapsed()+970000;
        spawnTimer.tick(1000);
        assertEquals("Expect timer to increase by large amount while resetting to 0 after 30s.",incrementOriginalTime,spawnTimer.getElapsed());
    }

    @Test
    public void tickTestNotRunning() {
        GdxTimer spawnTimer = new GdxTimer(30000, false, true);
        int incrementOriginalTime = spawnTimer.getElapsed();
        assertFalse("Expect timer to not increase.",spawnTimer.tick(10));
        spawnTimer.tick(10);
        assertEquals("Expect time to be 0.",incrementOriginalTime,spawnTimer.getElapsed());
    }
    @Test
    public void tickTestGetRunningFalse(){
        GdxTimer spawnTimer = new GdxTimer(30000, false, true);
        assertFalse("Expect timer to not be running.",spawnTimer.isRunning());
    }

    @Test
    public void tickTestGetRunningTrue(){
        GdxTimer spawnTimer = new GdxTimer(30000, true, true);
        assertTrue("Expect timer to run as expected.",spawnTimer.isRunning());
    }

    @Test
    public void tickTestStart(){
        GdxTimer spawnTimer = new GdxTimer(30000, false, true);
        spawnTimer.start();
        assertTrue("Expect timer to have started.",spawnTimer.isRunning());
    }

    @Test
    public void tickTestStop(){
        GdxTimer spawnTimer = new GdxTimer(30000, true, true);
        spawnTimer.stop();
        assertFalse("Expect timer to stop.",spawnTimer.isRunning());
    }

    @Test
    public void tickTestReset(){
        GdxTimer spawnTimer = new GdxTimer(30000, true, true);
        spawnTimer.tick(1000);
        spawnTimer.reset();
        assertEquals("Expect timer to go to 0.",0,spawnTimer.getElapsed());
    }
}
