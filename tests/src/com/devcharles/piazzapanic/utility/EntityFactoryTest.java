package com.devcharles.piazzapanic.utility;

import static org.junit.Assert.*;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.components.AnimationComponent;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.components.TextureComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.components.WalkingAnimationComponent;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class EntityFactoryTest {

  @Test
  public void testCreateCook() {
    PooledEngine engine = new PooledEngine();
    World world = new World(Vector2.Zero, true);
    EntityFactory factory = new EntityFactory(engine, world);

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
  public void createFood() {
    PooledEngine engine = new PooledEngine();
    World world = new World(Vector2.Zero, true);
    EntityFactory factory = new EntityFactory(engine, world);

    Entity food = factory.createFood(FoodType.burger);
    ImmutableArray<Entity> entities = engine.getEntitiesFor(
        Family.all(TextureComponent.class, TransformComponent.class, FoodComponent.class).get());

    assertEquals("There should be one entity that has the food components.", 1,
        entities.size());
    assertEquals("The food should be the same as what is in the engine.", food, entities.get(0));
    assertEquals("The food type should be burger.", FoodType.burger, Mappers.food.get(food).type);
  }

  @Test
  public void createStation() {
  }

  @Test
  public void testCutFoodNullPath() {
    EntityFactory.foodTextures.clear();
    assertEquals("Ensure no textures are there initially.", 0, EntityFactory.foodTextures.size());
    EntityFactory.cutFood(null);
    assertEquals("Ensure 13 food textures are loaded.", 13, EntityFactory.foodTextures.size());
  }

  @Test
  public void testCutFoodWithPath() {
    EntityFactory.foodTextures.clear();
    assertEquals("Ensure no textures are there initially.", 0, EntityFactory.foodTextures.size());
    EntityFactory.cutFood("v2/food.png");
    assertEquals("Ensure 13 food textures are loaded.", 13, EntityFactory.foodTextures.size());
  }

  @Test
  public void testGetFoodTexture() {
    EntityFactory.foodTextures.remove(FoodType.buns);
    assertNull("It should return null if food texture does not exist yet.",
        EntityFactory.getFoodTexture(FoodType.buns));
    EntityFactory.cutFood(null);
    for (int i = 1; i < 14; i++) {
      assertNotNull("There should be a texture for this food type: " + FoodType.from(i),
          EntityFactory.getFoodTexture(FoodType.from(i)));
    }
  }

  @Test
  public void createCustomer() {
  }
}