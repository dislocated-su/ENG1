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
import com.devcharles.piazzapanic.componentsystems.PowerUpSystem;
import com.devcharles.piazzapanic.componentsystems.RenderingSystem;
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.MapLoader;
import com.devcharles.piazzapanic.utility.WorldTilemapRenderer;
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
    private WorldTilemapRenderer mapRenderer;

    private Integer[] reputationPoints = { 3 };
    private Float[] tillBalance = {0f};

    private Integer[] customersServed = { 0 };

    public Boolean SpeedBoost = false;

    public Boolean InstaCook = false;

    public Boolean BinACustomer = false;

    public Boolean TimeFreeze = false;
    
    public Boolean DoubleRep = false;

    public enum Difficulty {
        SCENARIO("Scenario",30000),
        ENDLESS_EASY("Endless - Easy",120000),
        ENDLESS_NORMAL("Endless - Normal",60000),
        ENDLESS_HARD("Endless - Hard",30000);

        private String displayName;
        private int spawnFrequency;
        Difficulty(String displayName, int spawnFrequency){
            this.displayName=displayName;
            this.spawnFrequency=spawnFrequency;
        }
        public String getDisplayName(){
            return this.displayName;
        }
        public int getSpawnFrequency(){ return this.spawnFrequency;}
    }

    public GameScreen(PiazzaPanic game, int numOfCustomers, Difficulty difficulty) {
        this.game = game;
        kbInput = new KeyboardInput();

        // Create a world with no gravity.
        world = new World(new Vector2(0, 0), true);

        camera = new OrthographicCamera();

        engine = new PooledEngine();

        // The rayhandler is responsible for rendering the lights.
        rayhandler = new RayHandler(world);
        rayhandler.setAmbientLight(0.4f);

        EntityFactory factory = new EntityFactory(engine, world);
        EntityFactory.cutFood(null);

        hud = new Hud(game.batch, this, game, reputationPoints,difficulty,tillBalance,customersServed,this, factory);

        mapLoader = new MapLoader(null, null, factory);
        mapLoader.buildCollisions(world);
        mapLoader.buildFromObjects(engine, rayhandler);
        mapLoader.buildStations(engine, world);
        mapRenderer = new WorldTilemapRenderer(mapLoader.map,camera,game.batch);
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new RenderingSystem(mapLoader.map, game.batch, camera,mapRenderer));
        engine.addSystem(new LightingSystem(rayhandler, camera));
        // This can be commented in during debugging.
        // engine.addSystem(new DebugRendererSystem(world, camera));
        engine.addSystem(new PlayerControlSystem(kbInput));
        engine.addSystem(new CustomerAISystem(mapLoader.getObjectives(), world, factory, hud, reputationPoints,numOfCustomers,difficulty,tillBalance,customersServed, this));
        engine.addSystem(new StationSystem(kbInput, factory,mapRenderer,tillBalance,hud,difficulty,this));
        engine.addSystem(new CarryItemsSystem());
        engine.addSystem(new InventoryUpdateSystem(hud));
        engine.addSystem(new PowerUpSystem(engine, this));

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

    public void SpeedActive(){
        SpeedBoost = true;
    }

    public void InstaActive(){
        InstaCook = true;
    }

    public void BinActive(){
        BinACustomer = true;
    }

    public void TimeActive(){
        TimeFreeze = true;
    }

    public void DoubleActive(){
        DoubleRep = true;
    }

    public void SpeedOff(){
        SpeedBoost = false;
    }

    public void InstaOff(){
        InstaCook = false;
    }

    public void BinOff(){
        BinACustomer = false;
        System.out.println("BinACustomer is off");
    }

    public void TimeOff(){
        TimeFreeze = false;
    }

    public void DoubleOff(){
        DoubleRep = false;
    }
}
