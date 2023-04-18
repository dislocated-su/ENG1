package com.devcharles.piazzapanic.utility.saving;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.devcharles.piazzapanic.components.AIAgentComponent;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.components.ItemComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.Mappers;

public class SavableCustomer {

  TransformComponent transformComponent;

  public int currentObjective = 0;
  int slot = 0;

  FoodType order;
  SavableFood food;
  SavableTimer timer;

  public static SavableCustomer from(Entity entity) {
    if (entity == null) {
      return null;
    }
    SavableCustomer customer = new SavableCustomer();
    customer.transformComponent = Mappers.transform.get(entity);
    AIAgentComponent aiAgentComponent = Mappers.aiAgent.get(entity);
    customer.currentObjective = aiAgentComponent.currentObjective;
    customer.slot = aiAgentComponent.slot;

    CustomerComponent customerComponent = Mappers.customer.get(entity);
    customer.order = customerComponent.order;
    customer.timer = SavableTimer.from(customerComponent.timer);
    customer.food = SavableFood.from(customerComponent.food);

    return customer;
  }

  public Entity toEntity(EntityFactory factory) {
    Entity customer = factory.createCustomer(
        new Vector2(transformComponent.position.x, transformComponent.position.y), order);
    Mappers.transform.get(customer).copyValues(transformComponent);

    AIAgentComponent aiAgentComponent = Mappers.aiAgent.get(customer);
    aiAgentComponent.currentObjective = currentObjective;
    aiAgentComponent.slot = slot;

    CustomerComponent customerComponent = Mappers.customer.get(customer);
    if (food != null) {
      customerComponent.food = food.toEntity(factory);
      ItemComponent itemComponent = factory.getEngine().createComponent(ItemComponent.class);
      itemComponent.holderTransform = Mappers.transform.get(customer);
      customerComponent.food.add(itemComponent);
    }

    customerComponent.timer = timer.toGdxTimer();
    return customer;
  }
}
