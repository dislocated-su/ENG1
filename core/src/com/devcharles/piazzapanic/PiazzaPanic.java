package com.devcharles.piazzapanic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This class wraps the spritebatch.
 */
public class PiazzaPanic extends Game {

	public final float VIRTUAL_HEIGHT = 20f;

	public SpriteBatch batch;

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