package com.devcharles.piazzapanic;


import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.scene2d.Hud;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.MapLoader;
import com.devcharles.piazzapanic.utility.box2d.WorldContactListener;
import java.util.Map;

public abstract class BaseGameScreen implements Screen {

  protected final PooledEngine engine;

  protected final KeyboardInput kbInput;

  protected final World world;

  protected final OrthographicCamera camera;

  protected final PiazzaPanic game;

  protected final Hud hud;

  protected final InputMultiplexer multiplexer;

  protected final RayHandler rayhandler;

  protected final MapLoader mapLoader;
  protected final EntityFactory factory;

  protected final AssetManager assetManager;

  protected final Integer[] reputationPointsAndMoney = {3, 0};
  protected final Map<Integer, Entity> stationsMap;

  public BaseGameScreen(PiazzaPanic game, String mapPath) {
    this.game = game;
    assetManager = game.assetManager;
    assetManager.finishLoading();

    kbInput = new KeyboardInput();

    // Create a world with no gravity.
    world = new World(new Vector2(0, 0), true);

    camera = new OrthographicCamera();

    engine = new PooledEngine();

    // The rayhandler is responsible for rendering the lights.
    rayhandler = new RayHandler(world);

    factory = new EntityFactory(engine, world, assetManager);
    factory.cutFood(null);

    hud = new Hud(game.batch, this, game, reputationPointsAndMoney);

    mapLoader = new MapLoader(mapPath, null, factory, assetManager);
    mapLoader.buildCollisions(world);
    mapLoader.buildFromObjects(rayhandler);
    stationsMap = mapLoader.buildStations();

    world.setContactListener(new WorldContactListener());

    // set the input processor
    multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(kbInput);
    multiplexer.addProcessor(hud.stage);
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(multiplexer);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    if (hud.paused) {
      engine.update(0);
    } else {
      engine.update(delta);
    }

    game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

    if (!kbInput.disableHud) {
      hud.update(delta);
    }
  }

  public PooledEngine getEngine() {
    return engine;
  }

  @Override
  public void resize(int width, int height) {
    camera.setToOrtho(false, game.VIRTUAL_HEIGHT * width / (float) height, game.VIRTUAL_HEIGHT);
    hud.resize(width, height);
  }

  @Override
  public void pause() {
    kbInput.clearInputs();
    Gdx.input.setInputProcessor(hud.stage);
  }

  @Override
  public void resume() {
    kbInput.clearInputs();
    Gdx.input.setInputProcessor(multiplexer);
  }

  @Override
  public void hide() {
  }

  @Override
  public void dispose() {
    // TODO Figure out what to dispose
    world.dispose();
  }
}
