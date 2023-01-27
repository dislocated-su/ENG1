package com.devcharles.piazzapanic.utility;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.utility.box2d.MapBodyBuilder;

import box2dLight.RayHandler;

public class WorldTilemapRenderer {

    private TiledMap map;

    private OrthogonalTiledMapRenderer renderer;

    private OrthographicCamera camera;

    final int ppt = 16;

    private TiledMapTileLayer floor;
    private TiledMapTileLayer front_wall;
    private TiledMapTileLayer station;
    private TiledMapTileLayer countertop;
    private TiledMapTileLayer back_wall;

    public WorldTilemapRenderer(TiledMap map, OrthographicCamera mainCamera, SpriteBatch batch) {
        this.camera = mainCamera;
        this.map = map;

        renderer = new OrthogonalTiledMapRenderer(map, 1f / ppt, batch);

        floor = (TiledMapTileLayer) map.getLayers().get("floor");
        front_wall = (TiledMapTileLayer) map.getLayers().get("wall_f");
        back_wall = (TiledMapTileLayer) map.getLayers().get("wall_b");

        // Station-related layers
        station = (TiledMapTileLayer) map.getLayers().get("station");
        countertop = (TiledMapTileLayer) map.getLayers().get("countertop");

    }

    public void renderBackground() {
        renderer.setView(camera);
        renderer.renderTileLayer(floor);
        renderer.renderTileLayer(back_wall);
        renderer.renderTileLayer(countertop);
        renderer.renderTileLayer(station);
    }

    public void renderForeground() {
        renderer.setView(camera);
        renderer.renderTileLayer(front_wall);
    }

    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}
