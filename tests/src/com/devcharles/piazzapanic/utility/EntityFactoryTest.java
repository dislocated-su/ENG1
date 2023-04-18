package com.devcharles.piazzapanic.utility;

import static org.junit.Assert.*;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.PiazzaPanic;
import com.devcharles.piazzapanic.components.AIAgentComponent;
import com.devcharles.piazzapanic.components.AnimationComponent;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.TextureComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.components.WalkingAnimationComponent;
import com.devcharles.piazzapanic.utility.Station.StationType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class EntityFactoryTest {

  PooledEngine engine;
  World world;
  AssetManager manager;
  EntityFactory factory;

  @Before
  public void setup() {
    engine = new PooledEngine();
    world = new World(Vector2.Zero, true);
    manager = new AssetManager();
    manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    PiazzaPanic.loadAssets(manager);
    manager.finishLoading();
    factory = new EntityFactory(engine, world, manager);
  }

  @Test
  public void testCreateCook() {
    Entity cook = factory.createCook(0, 0);
    ImmutableArray<Entity> entities = engine.getEntitiesFor(
        Family.all(B2dBodyComponent.class, TransformComponent.class,
            ControllableComponent.class, TextureComponent.class, AnimationComponent.class,
            WalkingAnimationComponent.class).get());

    assertEquals("There should be one entity that has the cook components.", 1,
        entities.size());
    assertEquals("The cook should be the same as what is in the engine.", cook, entities.get(0));

    Array<Fixture> fixtures = new Array<>();
    world.getFixtures(fixtures);
    Fixture cookFixture = fixtures.first();

    assertNotNull("There should be a cook fixture.", cookFixture);
    assertEquals("The cook entity should be added as the user data in the fixture.", cook,
        cookFixture.getUserData());
  }

  @Test
  public void testCreateFood() {
    Entity food = factory.createFood(FoodType.burger);
    ImmutableArray<Entity> entities = engine.getEntitiesFor(
        Family.all(TextureComponent.class, TransformComponent.class, FoodComponent.class).get());

    assertEquals("There should be one entity that has the food components.", 1,
        entities.size());
    assertEquals("The food should be the same as what is in the engine.", food, entities.get(0));
    assertEquals("The food type should be burger.", FoodType.burger, Mappers.food.get(food).type);
  }

  @Test
  public void testCreateIngredientStation() {
    Entity station = factory.createStation(0, StationType.ingredient, Vector2.Zero,
        FoodType.tomato);
    ImmutableArray<Entity> entities = engine.getEntitiesFor(
        Family.all(B2dBodyComponent.class, TransformComponent.class,
            TextureComponent.class, StationComponent.class).get());

    assertEquals("There should be one entity that has the station components.", 1,
        entities.size());
    assertEquals("The station should be the same as what is in the engine.", station,
        entities.get(0));
    assertEquals("The Ingredient station should be for tomatoes.", FoodType.tomato,
        Mappers.station.get(station).ingredient);
    assertEquals("The station type should be a ingredient.", StationType.ingredient,
        Mappers.station.get(station).type);

    Array<Fixture> fixtures = new Array<>();
    world.getFixtures(fixtures);
    Fixture stationFixture = fixtures.first();

    assertNotNull("There should be a station fixture.", stationFixture);
    assertEquals("The station component should be added as the user data in the fixture.",
        Mappers.station.get(station),
        stationFixture.getUserData());
  }

  @Test
  public void testCreateOtherStation() {
    Entity station = factory.createStation(0, StationType.grill, Vector2.Zero, null);
    ImmutableArray<Entity> entities = engine.getEntitiesFor(
        Family.all(B2dBodyComponent.class, TransformComponent.class,
            TextureComponent.class, StationComponent.class).get());

    assertEquals("There should be one entity that has the station components.", 1,
        entities.size());
    assertEquals("The station should be the same as what is in the engine.", station,
        entities.get(0));
    assertEquals("The station type should be a grill.", StationType.grill,
        Mappers.station.get(station).type);

    Array<Fixture> fixtures = new Array<>();
    world.getFixtures(fixtures);
    Fixture stationFixture = fixtures.first();

    assertNotNull("There should be a station fixture.", stationFixture);
    assertEquals("The station component should be added as the user data in the fixture.",
        Mappers.station.get(station),
        stationFixture.getUserData());
  }

  @Test
  public void testCutFoodNullPath() {
    EntityFactory.foodTextures.clear();
    assertEquals("Ensure no textures are there initially.", 0, EntityFactory.foodTextures.size());
    factory.cutFood(null);
    assertEquals("Ensure 13 food textures are loaded.", 13, EntityFactory.foodTextures.size());
  }

  @Test
  public void testCutFoodWithPath() {
    EntityFactory.foodTextures.clear();
    assertEquals("Ensure no textures are there initially.", 0, EntityFactory.foodTextures.size());
    factory.cutFood("v2/food.png");
    assertEquals("Ensure 13 food textures are loaded.", 13, EntityFactory.foodTextures.size());
  }

  @Test
  public void testGetFoodTexture() {
    EntityFactory.foodTextures.remove(FoodType.buns);
    assertNull("It should return null if food texture does not exist yet.",
        EntityFactory.getFoodTexture(FoodType.buns));
    factory.cutFood(null);
    for (int i = 1; i < 14; i++) {
      assertNotNull("There should be a texture for this food type: " + FoodType.from(i),
          EntityFactory.getFoodTexture(FoodType.from(i)));
    }
  }

  @Test
  public void testCreateCustomer() {
    Entity customer = factory.createCustomer(Vector2.Zero, null);
    ImmutableArray<Entity> entities = engine.getEntitiesFor(
        Family.all(B2dBodyComponent.class, TransformComponent.class,
            CustomerComponent.class, TextureComponent.class, AnimationComponent.class,
            WalkingAnimationComponent.class, AIAgentComponent.class).get());

    assertEquals("There should be one entity that has the customer components.", 1,
        entities.size());
    assertEquals("The customer should be the same as what is in the engine.", customer,
        entities.get(0));

    Array<Fixture> fixtures = new Array<>();
    world.getFixtures(fixtures);
    Fixture customerFixture = fixtures.first();

    assertNotNull("There should be a customer fixture.", customerFixture);
    assertEquals("The cook entity should be added as the user data in the fixture.", customer,
        customerFixture.getUserData());
    assertEquals("The customer steering body should be added as the user data in the body.",
        Mappers.aiAgent.get(customer).steeringBody,
        customerFixture.getBody().getUserData());
  }
}