package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;

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
    private AnimatedTiledMapTile ovenAnimation;
    private TiledMapTile ovenTile;
    private final HashMap<Integer,TiledMapTile> unlockedTiles = new HashMap<>(3);

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

        findUnlockedTiles();
        buildOvenAnimation();
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
        AnimatedTiledMapTile.updateAnimationBaseTime();
    }

    /**
     * Creates an animated tile where the frames are the oven tiles in the tileset with the fire property.
     * Stores the default oven tile in the ovenTile variable so that the animation can be undone.
     */
    public void buildOvenAnimation() {
        Array<StaticTiledMapTile> frameTiles = new Array<>(2);
        for (TiledMapTile tile : map.getTileSets().getTileSet("objects")) {
            if (tile.getProperties().containsKey("fire") && !tile.getProperties().get("fire", String.class).equals("0")) {
                frameTiles.add((StaticTiledMapTile) tile);
            }
            if (tile.getProperties().containsKey("fire") && tile.getProperties().get("fire", String.class).equals("0")) {
                ovenTile = tile;
            }
        }
        ovenAnimation = new AnimatedTiledMapTile(1 / 3f, frameTiles);
    }

    /**
     * Changes an oven tile at a specified position into an animated oven tile.
     * @param position - The position on the tile map.
     */
    public void animateOven(Vector2 position){
        TiledMapTileLayer.Cell cell = station.getCell((int)position.x,(int)position.y);
        cell.setTile(ovenAnimation);
        station.setCell((int)position.x,(int)position.y,cell);
    }

    /**
     * Removes oven animation for oven at specified position.
     * @param position - The position on the tile map.
     */
    public void removeOvenAnimation(Vector2 position){
        TiledMapTileLayer.Cell cell = station.getCell((int)position.x,(int)position.y);
        cell.setTile(ovenTile);
        station.setCell((int)position.x,(int)position.y,cell);
    }

    /**
     * Changes a locked tile on the tile map into the unlocked version of it.
     * @param position - The position of the tile on the tile map
     * @param stationId - The id for the station. eg 1 for oven.
     */
    public void unlockStation(Vector2 position, int stationId){
        TiledMapTileLayer.Cell cell = station.getCell((int)position.x,(int)position.y);
        cell.setTile(unlockedTiles.get(stationId));
        station.setCell((int)position.x,(int)position.y,cell);
    }

    /**
     * Ovens, cutting boards and grills need to be unlockable.
     * Maps the station id of these three stations to their tile image.
     * Allows the locked station tile to be replaced with the unlocked version.
     */
    public void findUnlockedTiles(){
        for(TiledMapTile tile : map.getTileSets().getTileSet("objects")){
            if(tile.getProperties().get("stationID") instanceof Integer){
                int id = (int) tile.getProperties().get("stationID");
                if(id == 1 || id == 2 || id == 3){
                    unlockedTiles.put(id,tile);
                }
            }
        }
    }

    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}
