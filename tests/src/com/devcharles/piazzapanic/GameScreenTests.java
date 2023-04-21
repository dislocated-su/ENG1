package com.devcharles.piazzapanic;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(GdxTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GameScreenTests implements BasicTest {

    public PiazzaPanic game;
    public GameScreen gameScreen;

    @Override
    @Before
    public void initialize() throws Exception {
        game = new PiazzaPanic();
        game.setTESTMODE(true);
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        // gamescreen completes all non-graphical initialization
        this.gameScreen = new GameScreen(game, 1, GameScreen.Difficulty.SCENARIO);

        assertTrue("Check a GameScreen is created", gameScreen instanceof GameScreen);
    }

    // @Test
    // public void updateTest() throws Exception {
    // gameScreen.render(0.1f);
    // }
}
