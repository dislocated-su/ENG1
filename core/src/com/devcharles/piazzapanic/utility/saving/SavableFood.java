package com.devcharles.piazzapanic.utility.saving;


import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.Mappers;

public class SavableFood {

  TransformComponent transformComponent;
  FoodComponent foodComponent;

  public static SavableFood from(Entity foodEntity) {
    if (foodEntity == null) {
      return null;
    }
    SavableFood food = new SavableFood();
    food.transformComponent = Mappers.transform.get(foodEntity);
    food.foodComponent = Mappers.food.get(foodEntity);
    return food;
  }

  public Entity toEntity(EntityFactory factory) {
    Entity food = factory.createFood(foodComponent.type);
    Mappers.transform.get(food).copyValues(transformComponent);
    return food;
  }
}
