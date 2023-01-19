package com.devcharles.piazzapanic;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.devcharles.piazzapanic.componentsystems.DebugRendererSystem;
import com.devcharles.piazzapanic.componentsystems.PhysicsSystem;
import com.devcharles.piazzapanic.componentsystems.PlayerControlSystem;
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.utility.EntityCreator;

public class GameScreenNew implements Screen {

    private PooledEngine engine;
    
    private KeyboardInput kbInput;
    
    private World world;
    
    private OrthographicCamera camera;

    private PiazzaPanic game;


    public GameScreenNew(PiazzaPanic game) {
        this.game = game;

        kbInput = new KeyboardInput();

        world = new World(new Vector2(0,0), true);

        camera = new OrthographicCamera();

        engine = new PooledEngine();

        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new DebugRendererSystem(world, camera));
        engine.addSystem(new PlayerControlSystem(kbInput));

        EntityCreator creator = new EntityCreator(engine, world);

        creator.createPlayer(1, 1);
        
        // set the input processor
        Gdx.input.setInputProcessor(kbInput);
    }

    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        engine.update(delta);
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
