package com.devcharles.piazzapanic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * This class wraps the spritebatch.
 */
public class PiazzaPanic extends Game {

  public final float VIRTUAL_HEIGHT = 20f;
  public final AssetManager assetManager = new AssetManager();

  public Skin skin;
  public SpriteBatch batch;

  public void create() {
    batch = new SpriteBatch();
    assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    skin = new Skin(Gdx.files.internal("craftacular/skin/craftacular-ui.json"));
    loadAssets(assetManager);
    this.setScreen(new MainMenuScreen(this));
  }

  public static void loadAssets(AssetManager assetManager) {
    assetManager.load("mainMenuImage.png", Texture.class);
    assetManager.load("craftacular/raw/font-export.fnt", BitmapFont.class);
    assetManager.load("craftacular/raw/font-title-export.fnt", BitmapFont.class);

    for (int i = 0; i <= 10; i++) {
      assetManager.load("tutorial" + i + ".png", Texture.class);
    }
    assetManager.load("recipe0.png", Texture.class);
    assetManager.load("recipe1.png", Texture.class);
    assetManager.finishLoading();

    assetManager.load("droplet.png", Texture.class);
    for (int i = 1; i < 16; i++) {
      assetManager.load("v2/customer/" + i + ".png", Texture.class);
      assetManager.load("v2/customer/" + i + "_holding.png", Texture.class);
    }

    assetManager.load("v2/chef/1.png", Texture.class);
    assetManager.load("v2/chef/1_crate.png", Texture.class);
    assetManager.load("v2/chef/1_holding.png", Texture.class);
    assetManager.load("v2/chef/2.png", Texture.class);
    assetManager.load("v2/chef/2_crate.png", Texture.class);
    assetManager.load("v2/chef/2_holding.png", Texture.class);

    assetManager.load("v2/food.png", Texture.class);
    assetManager.load("v2/map.tmx", TiledMap.class);
  }

  public void render() {
    // Renders the game, important.
    super.render();
  }

  public void dispose() {
    batch.dispose();
    assetManager.dispose();
    skin.dispose();
  }

}