package com.devcharles.piazzapanic.utility;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;

import com.devcharles.piazzapanic.components.AnimationComponent;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.TextureComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.components.WalkingAnimationComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.utility.Station.StationType;
import com.devcharles.piazzapanic.utility.box2d.Box2dLocation;
import box2dLight.RayHandler;
import java.util.Map;

import static org.junit.Assert.*;
import com.devcharles.piazzapanic.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class MapLoaderTest {

  @Test
  public void buildFromObjectsTest(){
    World world = new World(new Vector2(0, 0), true);
    PooledEngine engine = new PooledEngine();
    EntityFactory factory = new EntityFactory(engine, world);
    RayHandler rayhandler = new RayHandler(world);
    MapLoader mapLoader = new MapLoader("v2/mapTest.tmx", null, factory);
    mapLoader.buildFromObjects(engine, rayhandler);
    ImmutableArray<Entity> entities = engine.getEntitiesFor(
        Family.all(B2dBodyComponent.class, TransformComponent.class,
            ControllableComponent.class, TextureComponent.class, AnimationComponent.class,
            WalkingAnimationComponent.class).get());
    assertEquals("Checks that 2 entities with cook components.",2, entities.size());
    Map<Integer, Map<Integer, Box2dLocation>> aiObj = mapLoader.getObjectives();
    assertTrue("Checks an ai objective with key 0 exists.",aiObj.containsKey(0));
    assertTrue("Checks an ai objective with key -1 exists.",aiObj.containsKey(-1));
    assertTrue("Checks an ai objective with key -2 exists.",aiObj.containsKey(-2));
  }

  @Test
  public void buildStationsTest(){
    World world = new World(new Vector2(0, 0), true);
    PooledEngine engine = new PooledEngine();
    EntityFactory factory = new EntityFactory(engine, world);
    MapLoader mapLoader = new MapLoader("v2/mapTest.tmx", null, factory);
    mapLoader.buildStations(engine, world);
    ImmutableArray<Entity> entities = engine.getEntitiesFor(
        Family.all(B2dBodyComponent.class, TransformComponent.class,
            TextureComponent.class, StationComponent.class).get());
    assertEquals("Check that all 7 stations are built.",7, entities.size());
    assertEquals("Checks sink station exists.", StationType.sink, Mappers.station.get(entities.get(0)).type);
    assertEquals("Checks cutting board station exists.", StationType.cutting_board, Mappers.station.get(entities.get(1)).type);
    assertEquals("Checks grill station exists.", StationType.grill, Mappers.station.get(entities.get(2)).type);
    assertEquals("Checks serve station exists.", StationType.serve, Mappers.station.get(entities.get(3)).type);
    assertEquals("Checks oven station exists.", StationType.oven, Mappers.station.get(entities.get(4)).type);
    assertEquals("Checks bin station exists.", StationType.bin, Mappers.station.get(entities.get(5)).type);
    assertEquals("Checks ingredient station exists.", StationType.ingredient, Mappers.station.get(entities.get(6)).type);
  }
}
