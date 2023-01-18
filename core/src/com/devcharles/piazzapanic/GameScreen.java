package com.devcharles.piazzapanic;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.devcharles.piazzapanic.gameobjects.Cook;
import com.devcharles.piazzapanic.gameobjects.Customer;
import com.devcharles.piazzapanic.gameobjects.GameWorld;
import com.devcharles.piazzapanic.gameobjects.Player;
import com.devcharles.piazzapanic.gameobjects.Station;
import com.devcharles.piazzapanic.interfaces.Renderable;
import com.devcharles.piazzapanic.interfaces.Simulated;
import com.devcharles.piazzapanic.utility.WorldContactListener;

public class GameScreen implements Screen {

    final float VIRTUAL_HEIGHT = 20f;

    private Array<Renderable> objects;

    private Array<Simulated> simulatedObjects;

    final PiazzaPanic game;

    OrthographicCamera camera;

    private Player player;

    World world = new World(new Vector2(0, 0), true);

    Box2DDebugRenderer debugRenderer;

    public GameScreen(PiazzaPanic game) {
        this.game = game;

        camera = new OrthographicCamera();

        this.debugRenderer = new Box2DDebugRenderer();

        objects = new Array<Renderable>();

        objects.add(new GameWorld(world, camera, game.batch));

        objects.add(new Station(new Texture("bucket.png"), world, 1f, 1f));

        Array<Cook> cooks = new Array<Cook>(new Cook[] {
                new Cook(world, 1f, 10f),
                new Cook(world, 4.5f, 4.5f),
                new Cook(world, 1.5f, 1.5f)
        });

        Array<Customer> customersArray = new Array<Customer>(new Customer[] {
            new Customer(world, 5, 5)
        });

        player = new Player(cooks);

        simulatedObjects = new Array<Simulated>();

        for (Cook c : cooks) {
            objects.add(c);
            simulatedObjects.add(c);
        }

        for (Customer c : customersArray) {
            objects.add(c);
        }

        world.setContactListener(new WorldContactListener());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        Vector2 playerPos = player.currentCook.body.getPosition();

        camera.position.set(camera.position.lerp(new Vector3(playerPos, 0), 0.05f));

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        for (Renderable r : objects) {
            r.render(game.batch);
        }
        game.batch.end();

        player.interact(delta);

        debugRenderer.render(world, camera.combined);

        this.doPhysicsStep(delta);
    }

    private float accumulator = 0;

    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;

        while (accumulator >= 1 / 60f) {
            world.step(1 / 60f, 6, 2);
            accumulator -= 1 / 60f;
        }

        for (Simulated s : simulatedObjects) {
            s.simulate();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        for (Renderable renderable : objects) {
            renderable.dispose();
        }
    }
}
