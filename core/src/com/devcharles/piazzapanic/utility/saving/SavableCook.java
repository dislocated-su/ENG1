package com.devcharles.piazzapanic.utility.saving;

import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.utility.FoodStack;
import com.devcharles.piazzapanic.utility.Mappers;
import java.util.ArrayDeque;

public class SavableCook {

  public TransformComponent transformComponent;
  public ArrayDeque<SavableFood> foodStack = new ArrayDeque<>();

  public static SavableCook from(Entity cookEntity) {
    SavableCook cook = new SavableCook();
    cook.transformComponent = Mappers.transform.get(cookEntity);
    cook.setSavableFoodStackFromEntities(Mappers.controllable.get(cookEntity).currentFood);
    return cook;
  }

  public void setSavableFoodStackFromEntities(FoodStack stack) {
    for (Entity foodEntity : stack) {
      foodStack.push(SavableFood.from(foodEntity));
    }
  }
}
