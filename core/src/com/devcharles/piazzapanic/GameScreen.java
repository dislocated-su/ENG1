package com.devcharles.piazzapanic;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.componentsystems.StationSystem;
import com.devcharles.piazzapanic.componentsystems.CarryItemsSystem;
import com.devcharles.piazzapanic.componentsystems.CustomerAISystem;
import com.devcharles.piazzapanic.componentsystems.InventoryUpdateSystem;
import com.devcharles.piazzapanic.componentsystems.LightingSystem;
import com.devcharles.piazzapanic.componentsystems.PhysicsSystem;
import com.devcharles.piazzapanic.componentsystems.PlayerControlSystem;
import com.devcharles.piazzapanic.componentsystems.RenderingSystem;
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.MapLoader;
import com.devcharles.piazzapanic.utility.box2d.WorldContactListener;
import com.devcharles.piazzapanic.scene2d.Hud;
import box2dLight.RayHandler;

public class GameScreen implements Screen {

    private PooledEngine engine;

    private KeyboardInput kbInput;

    private World world;

    private OrthographicCamera camera;

    private PiazzaPanic game;

    private Hud hud;

    private InputMultiplexer multiplexer;

    private RayHandler rayhandler;

    private MapLoader mapLoader;

    private Integer[] reputationPoints = { 3 };

    public GameScreen(PiazzaPanic game) {
        this.game = game;

        kbInput = new KeyboardInput();

        // Create a world with no gravity.
        world = new World(new Vector2(0, 0), true);

        camera = new OrthographicCamera();

        engine = new PooledEngine();

        // The rayhandler is responsible for rendering the lights.
        rayhandler = new RayHandler(world);

        EntityFactory factory = new EntityFactory(engine, world);
        EntityFactory.cutFood(null);

        hud = new Hud(game.batch, this, game, reputationPoints);

        mapLoader = new MapLoader(null, null, factory);
        mapLoader.buildCollisions(world);
        mapLoader.buildFromObjects(engine, rayhandler);
        mapLoader.buildStations(engine, world);

        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new RenderingSystem(mapLoader.map, game.batch, camera));
        engine.addSystem(new LightingSystem(rayhandler, camera));
        // This can be commented in during debugging.
        // engine.addSystem(new DebugRendererSystem(world, camera));
        engine.addSystem(new PlayerControlSystem(kbInput));
        engine.addSystem(new StationSystem(kbInput, factory));
        engine.addSystem(new CustomerAISystem(mapLoader.getObjectives(), world, factory, hud, reputationPoints));
        engine.addSystem(new CarryItemsSystem());
        engine.addSystem(new InventoryUpdateSystem(hud));

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
