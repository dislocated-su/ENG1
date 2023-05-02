package com.devcharles.piazzapanic.testing.utility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;
import com.devcharles.piazzapanic.utility.AudioSystem;

@RunWith(GdxTestRunner.class)
public class AudioSystemTest implements BasicTest {

    private AudioSystem audioSystem;

    @Override
    @Before
    public void initialize() throws Exception {
        audioSystem = new AudioSystem();
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        AudioSystem aux = new AudioSystem();
    }

    @Test
    public void playSounds() throws Exception {
        // check that audioSystem play methods work
        audioSystem.playBgm();
        audioSystem.playChop();
        audioSystem.playDing();
        audioSystem.playSigh();
        audioSystem.playSizzle();
        audioSystem.playTap();
        audioSystem.playThanks();
    }

    @Test
    public void toggleMuteTest() throws Exception {
        // check that mute toggle method works
        audioSystem.toggleMute(); // mute
        audioSystem.toggleMute(); // unmute
    }

}
