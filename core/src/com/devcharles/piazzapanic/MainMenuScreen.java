package com.devcharles.piazzapanic;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.devcharles.piazzapanic.scene2d.Slideshow;

/**
 * Main menu of the game, transitions the player to the Tutorial {@link Slideshow} on button press
 */
public class MainMenuScreen extends ApplicationAdapter implements Screen {

  final PiazzaPanic game;
  OrthographicCamera camera;
  private final Stage stage;
  private final Batch batch;
  private final Sprite sprite;

  public MainMenuScreen(final PiazzaPanic game) {

    this.game = game;
    camera = new OrthographicCamera();
    camera.setToOrtho(false, 1280, 720);
    batch = new SpriteBatch();

    sprite = new Sprite(game.assetManager.get("mainMenuImage.png", Texture.class));
    sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    stage = new Stage(new ScreenViewport());

    Label.LabelStyle menuLabelStyle = new Label.LabelStyle();
    menuLabelStyle.font = game.assetManager.get("craftacular/raw/font-title-export.fnt", BitmapFont.class);

    Label title = new Label("Piazza Panic", menuLabelStyle);

    Table root = new Table();
    root.setFillParent(true);
    stage.addActor(root);

    TextButton startScenarioModeBtn = new TextButton("Start scenario mode", game.skin);
    TextButton startEndlessModeBtn = new TextButton("Start endless mode", game.skin);
    TextButton loadEndlessModeBtn = new TextButton("Load endless mode", game.skin);
    TextButton exitBtn = new TextButton("Exit game", game.skin);

    // Checks if button is clicked and if clicked goes onto the tutorial
    startScenarioModeBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(
            new Slideshow(game, Slideshow.Type.tutorial, new ScenarioGameScreen(game, null)));
        dispose();
      }
    });
    // Checks if button is clicked and if clicked goes onto the tutorial
    startEndlessModeBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(
            new Slideshow(game, Slideshow.Type.tutorial, new EndlessGameScreen(game, null, false)));
        dispose();
      }
    });
    // Checks if button is clicked and if clicked goes onto the tutorial
    loadEndlessModeBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(
            new Slideshow(game, Slideshow.Type.tutorial, new EndlessGameScreen(game, null, true)));
        dispose();
      }
    });
    exitBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        dispose();
        Gdx.app.exit();
      }
    });

    root.add(title).expandX().padBottom(120);
    root.row();
    root.add(startScenarioModeBtn).padBottom(30);
    root.row();
    root.add(startEndlessModeBtn).padBottom(30);
    root.row();
    root.add(loadEndlessModeBtn).padBottom(30);
    root.row();
    root.add(exitBtn);

  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    // draws everything (dont change this order unless you know what youre doing)
    game.assetManager.update(16);
    batch.begin();
    sprite.draw(batch);
    batch.end();
    stage.act();
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void hide() {

  }

  public void dispose() {
    stage.dispose();
  }
}
