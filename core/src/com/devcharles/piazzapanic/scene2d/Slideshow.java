package com.devcharles.piazzapanic.scene2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.devcharles.piazzapanic.ScenarioGameScreen;
import com.devcharles.piazzapanic.PiazzaPanic;

/**
 * A screen that displays a slideshow of images.
 */
public class Slideshow extends ApplicationAdapter implements Screen {

  OrthographicCamera camera;
  private final Stage stage;
  private final Batch batch;
  private Sprite sprite;

  /**
   * Slideshow type enumeration. To screate new slideshows, add their pages to the assets folder in
   * the format {@code [type][PageNumber].png} and add a type to this enum.
   */
  public enum Type {
    recipe,
    tutorial
  }

  private final Texture[] textures;

  private int currentPage = 0;

  private final TextButton leftButton, rightButton, exit;

  /**
   * Create a new slideshow screen.
   *
   * @param type {@link Type} of slideshow to create.
   */
  private Slideshow(final PiazzaPanic game, Type type) {

    camera = new OrthographicCamera();
    camera.setToOrtho(false, 1280, 720);
    ScalingViewport viewport = new ScalingViewport(Scaling.fit, 1280, 720, camera);
    viewport.apply();
    batch = new SpriteBatch();

    int fileCount = 0;
    if (type == Type.recipe) {
      fileCount = 2;
    } else if (type == Type.tutorial) {
      fileCount = 11;
    }

    textures = new Texture[fileCount];

    for (int i = 0; i < fileCount; i++) {
      textures[i] = game.assetManager.get(type.name() + i + ".png", Texture.class);
    }

    stage = new Stage(viewport);

    // Begin layout
    leftButton = new TextButton("Page Left", game.skin);

    leftButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y) {
        currentPage = currentPage - 1;
        updatePage();
      }
    });

    rightButton = new TextButton("Page Right", game.skin);
    rightButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y) {
        currentPage = currentPage + 1;
        updatePage();
      }
    });

    exit = new TextButton("Exit", game.skin);
  }

  /**
   * Create a new slideshow screen in a running game.
   *
   * @param game        {@link PiazzaPanic} game instance for changing screens.
   * @param type        {@link Type} of slideshow to create.
   * @param savedScreen the {@link ScenarioGameScreen} to return to after this screen is closed.
   */
  public Slideshow(final PiazzaPanic game, Type type, final Screen savedScreen) {
    this(game, type);

    exit.clearListeners();

    exit.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(savedScreen);
      }
    });

    updatePage();
    stage.clear();
    stage.addActor(buildTable());
  }

  private Table buildTable() {
    Table table = new Table();

    table.top();
    table.setFillParent(true);

    table.add(exit).width(180).height(80).expandX().right().colspan(2);

    table.row().expandY();

    table.add(leftButton).width(200).height(50).expandX().left().bottom();

    table.add(rightButton).width(200).height(50).expandX().right().bottom();

    return table;
  }

  private void updatePage() {
    sprite = new Sprite(textures[currentPage]);
    sprite.setSize(1280, 720);

    if (currentPage == 0) {
      leftButton.setVisible(false);
      rightButton.setVisible(true);
    } else if (textures.length - 1 == currentPage) {
      leftButton.setVisible(true);
      rightButton.setVisible(false);
    } else {
      leftButton.setVisible(true);
      rightButton.setVisible(true);
    }
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
    stage.getViewport().apply();
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
    batch.begin();
    sprite.draw(batch);
    batch.end();
    stage.act();
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
    stage.getViewport().apply();
  }

  public void dispose() {
    stage.dispose();
  }

  @Override
  public void hide() {
  }
}
