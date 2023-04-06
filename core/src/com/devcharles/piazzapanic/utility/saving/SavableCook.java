package com.devcharles.piazzapanic.utility.saving;

import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.utility.FoodStack;
import java.util.ArrayDeque;

public class SavableCook {

  TransformComponent transformComponent;
  ArrayDeque<SavableFood> foodStack = new ArrayDeque<>();

  public void setTransformComponent(
      TransformComponent transformComponent) {
    this.transformComponent = transformComponent;
  }

  public void setSavableFoodStackFromEntities(FoodStack stack) {
    for (Entity foodEntity : stack) {
      foodStack.push(SavableFood.from(foodEntity));
    }
  }
}
