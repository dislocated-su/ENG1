package com.devcharles.piazzapanic.utility.saving;


import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.CookingComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.Mappers;

public class SavableFood {

  TransformComponent transformComponent;
  FoodComponent foodComponent;

  boolean hasCookingComponent = false;
  SavableTimer cookingTimer = null;
  boolean processed = false;


  public static SavableFood from(Entity foodEntity) {
    if (foodEntity == null) {
      return null;
    }
    SavableFood food = new SavableFood();
    food.transformComponent = Mappers.transform.get(foodEntity);
    food.foodComponent = Mappers.food.get(foodEntity);
    CookingComponent cookingComponent = Mappers.cooking.get(foodEntity);
    if (cookingComponent != null) {
      food.hasCookingComponent = true;
      food.cookingTimer = SavableTimer.from(cookingComponent.timer);
      food.processed = cookingComponent.processed;
    }
    return food;
  }

  public Entity toEntity(EntityFactory factory) {
    Entity food = factory.createFood(foodComponent.type);
    Mappers.transform.get(food).copyValues(transformComponent);
    if (hasCookingComponent) {
      CookingComponent cookingComponent = factory.getEngine()
          .createComponent(CookingComponent.class);
      cookingComponent.processed = processed;
      cookingComponent.timer = cookingTimer.toGdxTimer();
      food.add(cookingComponent);
    }
    return food;
  }
}
