package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.assets.AssetManager;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.utility.Station.StationType;
import com.devcharles.piazzapanic.utility.box2d.Box2dLocation;
import com.devcharles.piazzapanic.utility.box2d.LightBuilder;
import com.devcharles.piazzapanic.utility.box2d.MapBodyBuilder;

import box2dLight.RayHandler;

/**
 * Loads and owns the {@link TiledMap} object. Creates entities from map metadata.
 */
public class MapLoader {

  public TiledMap map;

  private int ppt = 16;

  private final EntityFactory factory;

  // Object properties
  static final String lightIdProperty = "lightID";
  static final String cookSpawnPoint = "cookspawnpoint";
  static final String aiSpawnPoint = "aispawnpoint";
  static final String aiObjective = "aiobjective";
  static final String aiSlot = "aislot";

  // Layers relevant to loading the map
  static final String objectLayer = "MapObjects";
  static final String collisionLayer = "Obstacles";
  static final String stationLayer = "station";
  static final String counterTopLayer = "countertop";

  // Tile properties
  static final String stationIdProperty = "stationID";
  static final String ingredientTypeProperty = "ingredientType";

  private Map<Integer, Map<Integer, Box2dLocation>> aiObjectives;

  /**
   * Load the {@link TiledMap} from a {@code .tmx} file.
   *
   * @param path    Path to map file.
   * @param ppt     Pixels per tile (default 16).
   * @param factory {@link EntityFactory} instance to create entities based on the map metadata.
   */
  public MapLoader(String path, Integer ppt, EntityFactory factory, AssetManager assetManager) {
    if (ppt != null) {
      this.ppt = ppt;
    }
    if (path == null) {
      path = "v2/map.tmx";
    }
    map = assetManager.get(path, TiledMap.class);

    this.factory = factory;
  }

  /**
   * @param world The Box2D world instance to add bodies to.
   * @return The bodies created from the map.
   */
  public Array<Body> buildCollisions(World world) {
    return MapBodyBuilder.buildShapes(map, ppt, world);
  }

  /**
   * Create lights, spawnpoints, AI paths from map metadata.
   *
   * @param rayHandler {@link RayHandler} to add lights to.
   */
  public void buildFromObjects(RayHandler rayHandler) {
    MapObjects objects = map.getLayers().get(objectLayer).getObjects();

    aiObjectives = new HashMap<>();

    for (MapObject mapObject : objects) {

      if (mapObject instanceof RectangleMapObject) {
        MapProperties properties = mapObject.getProperties();
        RectangleMapObject point = (RectangleMapObject) mapObject;

        Vector2 pos = new Vector2(point.getRectangle().x / ppt, point.getRectangle().y / ppt);

        if (properties.containsKey(lightIdProperty)) {
          int lightID = (int) properties.get(lightIdProperty);
          Gdx.app.log("map parsing",
              String.format("Light typeid %d at x:%.2f y:%.2f", lightID, pos.x, pos.y));
          switch (lightID) {
            case 0:
              LightBuilder.createPointLight(rayHandler, pos.x, pos.y,
                  Color.TAN.cpy().sub(0, 0, 0, 0.25f), 10, true);
              break;
            case 1:
              LightBuilder.createPointLight(rayHandler, pos.x, pos.y,
                  Color.TAN.cpy().sub(0, 0, 0, 0.5f), 0.8f, false);
              break;
            case 2:
              LightBuilder.createRoomLight(rayHandler, pos.x, pos.y,
                  Color.TAN.cpy().sub(0, 0, 0, 0.25f), 25, true);
              break;
            case 3:
              LightBuilder.createRoomLight(rayHandler, pos.x, pos.y,
                  Color.TAN.cpy().sub(0, 0, 0, 0.25f), 25, false);
              break;
          }

        } else if (properties.containsKey(cookSpawnPoint)) {
          int cookID = (int) properties.get(cookSpawnPoint);
          Gdx.app.log("map parsing",
              String.format("Cook spawn point at x:%.2f y:%.2f", pos.x, pos.y));
          Entity cook = factory.createCook((int) pos.x, (int) pos.y);
          if (cookID == 0) {
            cook.add(new PlayerComponent());
          }

        } else if (properties.containsKey(aiSpawnPoint)) {
          Gdx.app.log("map parsing",
              String.format("Ai spawn point at x:%.2f y:%.2f", pos.x, pos.y));
          HashMap<Integer, Box2dLocation> spawn = new HashMap<>();
          spawn.put(0, new Box2dLocation(pos, 0));
          aiObjectives.put(-2, spawn);
        } else if (properties.containsKey(aiObjective)) {
          int objective = properties.get(aiObjective, Integer.class);
          int slot = properties.containsKey(aiSlot) ? properties.get(aiSlot, Integer.class) : 0;
          Map<Integer, Box2dLocation> slots = aiObjectives.get(objective);
          if (slots == null) {
            slots = new HashMap<>();
            aiObjectives.put(objective, slots);
          }
          slots.put(slot, new Box2dLocation(new Vector2(pos.x, pos.y), (float) (1.5f * Math.PI)));
          Gdx.app.log("map parsing",
              String.format("Ai objective %d at x:%.2f y:%.2f", objective, pos.x, pos.y));
        }
      }
    }
  }

  /**
   * Get the {@link Map} of objectives the AI can travel to. See the map file in the Tiled editor to
   * preview ai objectives.
   *
   * @return
   */
  public Map<Integer, Map<Integer, Box2dLocation>> getObjectives() {
    return aiObjectives;
  }

  /**
   * Create station entities from map metadata. Metadata is given to the tile in Edit Tileset ->
   * Tile Properties.
   */
  public Map<Integer, Entity> buildStations() {
    TiledMapTileLayer stations = (TiledMapTileLayer) (map.getLayers().get(stationLayer));
    TiledMapTileLayer stations_f = (TiledMapTileLayer) (map.getLayers().get(stationLayer + "_f"));

    int columns = stations.getWidth();
    int rows = stations.getHeight();

    Cell currentCell;

    Map<Integer, Entity> stationsMap = new HashMap<>();

    int id = 0;
    for (int i = 0; i < columns; i++) {
      for (int j = 0; j < rows; j++) {
        currentCell =
            stations.getCell(i, j) != null ? stations.getCell(i, j) : stations_f.getCell(i, j);
        if (currentCell == null) {
          continue;
        }
        Integer object = currentCell.getTile().getProperties()
            .get(stationIdProperty, Integer.class);
        if (object == null) {
          continue;
        }
        StationType stationType = StationType.from(object);

        FoodType ingredientType = null;

        if (stationType == StationType.ingredient) {
          ingredientType = FoodType.from(
              (Integer) currentCell.getTile().getProperties().get(ingredientTypeProperty));
        }

        stationsMap.put(id,
            factory.createStation(id, stationType, new Vector2((i * 2) + 1, (j * 2) + 1),
                ingredientType));
        id++;
      } // Rows Loop
    } // Columns Loop
    return stationsMap;
  }
}
