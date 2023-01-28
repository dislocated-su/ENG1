package com.devcharles.piazzapanic;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.componentsystems.CollisionSystem;
import com.devcharles.piazzapanic.componentsystems.DebugRendererSystem;
import com.devcharles.piazzapanic.componentsystems.LightingSystem;
import com.devcharles.piazzapanic.componentsystems.PhysicsSystem;
import com.devcharles.piazzapanic.componentsystems.PlayerControlSystem;
import com.devcharles.piazzapanic.componentsystems.RenderingSystem;
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.scene2d.CookCarryHud;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.box2d.WorldContactListener;
import com.devcharles.piazzapanic.scene2d.Hud;

public class GameScreen extends ApplicationAdapter implements Screen {

    private PooledEngine engine;

    private KeyboardInput kbInput;

    private World world;

    private OrthographicCamera camera;

    private PiazzaPanic game;

    public int total_cooks;
    
    private Hud hud;
    private CookCarryHud cookCarryHud;
    private InputMultiplexer multiplexer;

    public GameScreen(PiazzaPanic game, int total_cooks) {
        this.game = game;
        this.total_cooks = total_cooks;

        kbInput = new KeyboardInput();

        world = new World(new Vector2(0, 0), true);

        camera = new OrthographicCamera();

        engine = new PooledEngine();

        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new RenderingSystem(world, game.batch, camera));
        engine.addSystem(new LightingSystem(world, camera));
        engine.addSystem(new DebugRendererSystem(world, camera));
        engine.addSystem(new PlayerControlSystem(kbInput));
        engine.addSystem(new CollisionSystem(kbInput));

        EntityFactory creator = new EntityFactory(engine, world);

        creator.createCook(10, 10).add(new PlayerComponent());

        for (int i = 0; i < total_cooks - 1; i++) {
            creator.createCook(2 * (i + 4), 2 * (i + 4));
        }

        creator.createStation(9f, 10f);

        world.setContactListener(new WorldContactListener());

        hud = new Hud(game.batch, this, game);
        cookCarryHud = new CookCarryHud(game.batch);
        // set the input processor
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(kbInput);
        multiplexer.addProcessor(hud.gameStage);

        Gdx.input.setInputProcessor(multiplexer);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(delta);
        game.batch.setProjectionMatrix(hud.gameStage.getCamera().combined);
        hud.update(delta);
        cookCarryHud.update(delta);



    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, game.VIRTUAL_HEIGHT * width / (float) height, game.VIRTUAL_HEIGHT);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
