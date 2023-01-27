package com.devcharles.piazzapanic.utility;

import com.badlogic.ashley.core.Engine;
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
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.utility.box2d.LightBuilder;
import com.devcharles.piazzapanic.utility.box2d.MapBodyBuilder;

import box2dLight.RayHandler;

public class MapLoader {

    public TiledMap map;

    private int ppt = 16;

    private EntityFactory factory;

    // Object properties
    static String lightIdProperty = "lightID";
    static String cookSpawnPoint = "cookspawnpoint";
    static String aiSpawnPoint = "aispawnpoint";
    static String aiObjective = "aiobjective";

    // Layers relevant to loading the map
    static String objectLayer = "MapObjects";
    static String collisionLayer = "Obstacles";
    static String stationLayer = "station";
    static String counterTopLayer = "countertop";

    // Tile properties
    static String stationIdProperty = "stationID";

    public MapLoader(String path, Integer ppt, EntityFactory factory) {
        if (ppt != null) {
            this.ppt = ppt;
        }
        if (path != null) {
            map = new TmxMapLoader().load(path);
        } else {
            map = new TmxMapLoader().load("./v2/map.tmx");
        }

        this.factory = factory;
    }

    public Array<Body> buildCollisions(World world) {
        return MapBodyBuilder.buildShapes(map, ppt, world);
    }

    public void buildFromObjects(Engine engine, RayHandler rayHandler) {
        MapObjects objects = map.getLayers().get(objectLayer).getObjects();

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
                            LightBuilder.createPointLight(rayHandler, pos.x, pos.y, Color.TAN.cpy().sub(0, 0, 0, 0.1f),
                                    5);
                            break;
                        case 1:
                            LightBuilder.createPointLight(rayHandler, pos.x, pos.y, Color.TAN.cpy().sub(0, 0, 0, 0.4f),
                                    7.5f);
                            break;
                        case 2:
                            LightBuilder.createRoomLight(rayHandler, pos.x, pos.y, Color.TAN.cpy().sub(0, 0, 0, 0.2f),
                                    20);
                            break;
                    }

                } else if (properties.containsKey(cookSpawnPoint)) {
                    int cookID = (int) properties.get(cookSpawnPoint);
                    Gdx.app.log("map parsing", String.format("Cook spawn point at x:%.2f y:%.2f", pos.x, pos.y));
                    Entity cook = factory.createCook((int) pos.x, (int) pos.y);
                    if (cookID == 0) {
                        cook.add(new PlayerComponent());
                    }

                } else if (properties.containsKey(aiSpawnPoint)) {
                    Gdx.app.log("map parsing", String.format("Ai spawn point at x:%.2f y:%.2f", pos.x, pos.y));
                } else if (properties.containsKey(aiObjective)) {
                    int objective = (int) properties.get(aiObjective);
                    Gdx.app.log("map parsing",
                            String.format("Ai objective %d at x:%.2f y:%.2f", objective, pos.x, pos.y));
                }
            }
        }
    }

    public void buildStations(Engine engine, World world) {
        TiledMapTileLayer stations = (TiledMapTileLayer) (map.getLayers().get(stationLayer));
        TiledMapTileLayer stations_f = (TiledMapTileLayer) (map.getLayers().get(stationLayer + "_f"));

        int columns = stations.getWidth();
        int rows = stations.getHeight();

        Cell currentCell;

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                currentCell = stations.getCell(i, j) != null ? stations.getCell(i, j) : stations_f.getCell(i, j);
                if (currentCell != null) {
                    Object object = currentCell.getTile().getProperties().get(stationIdProperty);
                    if (object != null && object instanceof Integer) {
                        Station.StationType stationType = Station.StationType.from((int) object);
                        factory.createStation(stationType, new Vector2((i * 2) + 1, (j * 2) + 1));
                    }
                }
            }
        }
    }
}
