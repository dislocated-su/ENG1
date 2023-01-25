package com.devcharles.piazzapanic.utility;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.TextureComponent;
import com.devcharles.piazzapanic.components.TransformComponent;

public class MapEntityBuilder {

    static String stationLayer = "station";
    static String counterTopLayer = "countertop";

    static String tileIDProperty = "id";

    public static void buildStations(Engine engine, TiledMap map, World world) {
        TiledMapTileLayer stations = (TiledMapTileLayer) (map.getLayers().get(stationLayer));
        TiledMapTileLayer countertops = (TiledMapTileLayer) (map.getLayers().get(counterTopLayer));

        int columns = stations.getWidth();
        int rows = stations.getHeight();

        Cell currentCell;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                currentCell = stations.getCell(i, j);
                if (currentCell != null) {
                    Object object = currentCell.getTile().getProperties().get(tileIDProperty);
                    if (object != null && object instanceof Integer) {
                        StationComponent.StationType stationType = StationComponent.StationType.from((int) object);
                        createStation(stationType, new Vector2((i * 2) + 1, (j * 2) + 1), engine, world);
                    }
                }
            }
        }
    }

    private static void createStation(StationComponent.StationType type, Vector2 position, Engine engine, World world) {
        Entity entity = engine.createEntity();

        float[] size = { 1f, 1f };

        B2dBodyComponent b2dBody = engine.createComponent(B2dBodyComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);

        TransformComponent transform = engine.createComponent(TransformComponent.class);

        StationComponent station = engine.createComponent(StationComponent.class);
        station.type = type;

        // Box2d body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(position.x, position.y);

        b2dBody.body = world.createBody(bodyDef);

        // Create a PolygonShape and set it to be a box of 1x1
        PolygonShape stationBox = new PolygonShape();
        stationBox.setAsBox(size[0], size[1]);

        // Create our fixture and attach it to the body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = stationBox;
        b2dBody.body.createFixture(fixtureDef).setUserData(station);

        // BodyDef and FixtureDef don't need disposing, but shapes do.
        stationBox.dispose();

        // add components to the entity
        entity.add(b2dBody);
        entity.add(transform);
        entity.add(texture);
        entity.add(station);

        engine.addEntity(entity);

    }
}
