package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Renders a {@link TiledMap} that is loaded using {@link MapLoader}.
 */
public class WorldTilemapRenderer {

    private final TiledMap map;

    private final OrthogonalTiledMapRenderer renderer;

    private final OrthographicCamera camera;

    /**
     * Pixels per tile. The tileset we are using is 32x32, so this effectively doubles apparent size.
     */
    final int ppt = 16;

    private final TiledMapTileLayer floor;
    private final TiledMapTileLayer front_wall;
    private final TiledMapTileLayer station;
    private final TiledMapTileLayer countertop;
    private final TiledMapTileLayer back_wall;

    private final TiledMapTileLayer countertop_f;
    private final TiledMapTileLayer station_f;

    /**
     * Create a new renderer with existing {@link TiledMap}, camera and {@link SpriteBatch}.
     * The renderer will not own any of these objects.
     * @param map {@link TiledMap} instance with required layers, see constructor.
     * @param mainCamera camera to render to. 
     * @param batch spritebatch to use.
     */
    public WorldTilemapRenderer(TiledMap map, OrthographicCamera mainCamera, SpriteBatch batch) {
        this.camera = mainCamera;
        this.map = map;

        renderer = new OrthogonalTiledMapRenderer(map, 1f / ppt, batch);

        floor = (TiledMapTileLayer) map.getLayers().get("floor");
        front_wall = (TiledMapTileLayer) map.getLayers().get("wall_f");
        back_wall = (TiledMapTileLayer) map.getLayers().get("wall_b");

        // Station-related layers
        station = (TiledMapTileLayer) map.getLayers().get("station");
        station_f = (TiledMapTileLayer) map.getLayers().get("station_f");
        countertop = (TiledMapTileLayer) map.getLayers().get("countertop");
        countertop_f = (TiledMapTileLayer) map.getLayers().get("countertop_f");

    }

    /**
     * Render all layers that appear behind entities.
     */
    public void renderBackground() {
        renderer.setView(camera);
        renderer.renderTileLayer(floor);
        renderer.renderTileLayer(back_wall);
        renderer.renderTileLayer(countertop);
        renderer.renderTileLayer(station);
    }

    /**
     * Render all layers that appear in front of entities.
     */
    public void renderForeground() {
        renderer.setView(camera);
        renderer.renderTileLayer(countertop_f);
        renderer.renderTileLayer(station_f);
        renderer.renderTileLayer(front_wall);
    }

    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}
