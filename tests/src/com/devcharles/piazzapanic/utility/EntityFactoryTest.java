package com.devcharles.piazzapanic.utility;

import static org.junit.Assert.*;

import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class EntityFactoryTest {

  @Test
  public void createCook() {
  }

  @Test
  public void createFood() {
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