package com.devcharles.piazzapanic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This class wraps the spritebatch.
 */
public class PiazzaPanic extends Game {

	public final float VIRTUAL_HEIGHT = 20f;

	public SpriteBatch batch;

	// TESTMODE allows the game to run without graphics
	protected boolean TESTMODE = false;

	public void setTESTMODE(boolean b) {
		this.TESTMODE = true;
	}

	public void create() {
		batch = new SpriteBatch();
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		// Renders the game, important.
		super.render();
	}

	public void dispose() {
		batch.dispose();
	}

}