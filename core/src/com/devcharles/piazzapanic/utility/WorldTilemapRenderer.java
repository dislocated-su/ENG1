package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.utility.box2d.MapBodyBuilder;

public class WorldTilemapRenderer {

    private TiledMap map;

    private OrthogonalTiledMapRenderer renderer;

    private OrthographicCamera camera;

    final int ppt = 16;

    final int scale = 32 / ppt;

    private TiledMapTileLayer floor;

    private TiledMapTileLayer wall;

    public WorldTilemapRenderer(World world, OrthographicCamera mainCamera, SpriteBatch batch) {
        this.camera = mainCamera;

        map = new TmxMapLoader().load("./v2/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1f / ppt, batch);

        MapBodyBuilder.buildShapes(map, ppt, world);

        floor = (TiledMapTileLayer) map.getLayers().get("floor");
        wall = (TiledMapTileLayer) map.getLayers().get("wall");
    }

    public void renderBackground() {
        renderer.setView(camera);
        renderer.renderTileLayer(floor);
    }

    public void renderForeground() {
        renderer.setView(camera);
        renderer.renderTileLayer(wall);
    }

    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}
