package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.interfaces.Renderable;

public class GameWorld implements Renderable {

    Texture texture;

    Sprite sprite;

    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;

    public Vector2[] pos = { new Vector2(0f, 0f), new Vector2(35f, 0f), new Vector2(0f, 20f), new Vector2(0f, 0f), };
    public Vector2[] size = { new Vector2(0f, 20f), new Vector2(0f, 20f), new Vector2(35f, 0f), new Vector2(35f, 0f) };

    public GameWorld(Texture texture, World world, float x, float y) {

        /**
         * Tile Map Creation to be added, prioritised physics walls
         */
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        map = new TmxMapLoader().load("temp.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        this.texture = texture;
        this.sprite = new Sprite(texture);
        this.sprite.setOriginCenter();

        BodyDef[] wallBodyDefs = new BodyDef[4];
        for (int i = 0; i < wallBodyDefs.length; i++) {
            wallBodyDefs[i] = new BodyDef();
            wallBodyDefs[i].position.set(pos[i].x, pos[i].y);

            Body wallBody = world.createBody(wallBodyDefs[i]);

            PolygonShape[] wallBox = new PolygonShape[4];
            wallBox[i] = new PolygonShape();
            wallBox[i].setAsBox(size[i].x, size[i].y);
            wallBody.createFixture(wallBox[i], 0f);
            wallBox[i].dispose();
        }

    }

    @Override
    public void render(SpriteBatch batch) {

        renderer.setView(camera);
        renderer.render();

        for (int i = 0; i < pos.length; i++) {
            sprite.setBounds(pos[i].x - (size[i].x / 2), pos[i].y - (size[i].y / 2), size[i].x, size[i].y);
            sprite.setOrigin(size[i].x / 2, size[i].y / 2);
            sprite.draw(batch);
        }

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}
