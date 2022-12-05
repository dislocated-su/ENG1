package com.devcharles.piazzapanic;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.devcharles.piazzapanic.gameobjects.Cook;
import com.devcharles.piazzapanic.gameobjects.Entity;
import com.devcharles.piazzapanic.gameobjects.Player;
import com.devcharles.piazzapanic.interfaces.Renderable;

public class GameScreen implements Screen {
    
    private Array<Renderable> objects;
    
    final PiazzaPanic game;
    
    OrthographicCamera camera;

    private Player player;
    
    public GameScreen(PiazzaPanic game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        objects = new Array<Renderable>();
        objects.add(new Entity(new Texture("bucket.png")));
        
        Array<Cook> cooks = new Array<Cook>(new Cook[] {new Cook(0f, 30f), new Cook(4.5f,4.5f), new Cook(1.5f,1.5f)});

        player = new Player(cooks);
        
        for (Cook c : cooks) {
            objects.add(c);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        
        for (Renderable r : objects) {
            r.render(game.batch);
        }
        game.batch.end();

        player.interact();
    }

    @Override
    public void resize(int width, int height) {

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
