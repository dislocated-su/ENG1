package com.devcharles.piazzapanic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class GameScreenTests {

    private PiazzaPanic game;
    private GameScreen gameScreen;

    @Before
    public void initialize() throws Exception {
        game = new PiazzaPanic();
        game.setTESTMODE(true);
    }

    @Test
    public void constructorTest() throws Exception {
        // gamescreen completes all non-graphical initialization
        gameScreen = new GameScreen(game,1,GameScreen.Difficulty.SCENARIO);
    }
}
