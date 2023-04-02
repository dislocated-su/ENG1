package com.devcharles.piazzapanic.utility;

import static org.junit.Assert.*;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.badlogic.ashley.core.*;
import com.devcharles.piazzapanic.components.TransformComponent;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(GdxTestRunner.class)
public class FoodStackTest {


  @Test
  public void pushItemTest() {

    FoodStack stack = new FoodStack();
    World world = new World(Vector2.Zero, true);
    PooledEngine engine = new PooledEngine();

    Entity food = engine.createEntity();
    Entity cook = engine.createEntity();

    TransformComponent cookTransform = engine.createComponent(TransformComponent.class);
    TransformComponent foodTransform = engine.createComponent(TransformComponent.class);

    cook.add(cookTransform);
    cookTransform.position.x = 2;
    cookTransform.position.y = 2;
    food.add(foodTransform);

    engine.addEntity(food);
    engine.addEntity(cook);

    stack.init(engine);
    stack.pushItem(food, cook);

    assertEquals("Checks too see if the stack size is one", stack.size(), 1);
    assertEquals("Sees if the item is pushed onto the stack", stack.getLast(), food);
    assertEquals("Checks the food's item component has an updated transform",
        Mappers.item.get(food).holderTransform.position, cookTransform.position);
  }

  @Test
  public void pushTest() {

    PooledEngine engine = new PooledEngine();
    FoodStack stack = new FoodStack();

    Entity food1 = new Entity();
    Entity food2 = new Entity();
    TransformComponent transform = engine.createComponent(TransformComponent.class);
    food1.add(transform);
    food2.add(transform);

    stack.push(food1);
    stack.push(food2);

    assertEquals("Sees if the items are being pushed", 2, stack.size());
    assertTrue("Checks that the isHidden attribute is updated accordingly",
        Mappers.transform.get(food1).isHidden);
  }

  @Test
  public void popTest() {
    PooledEngine engine = new PooledEngine();
    FoodStack stack = new FoodStack();
    stack.init(engine);

    Entity food = new Entity();
    Entity cook = new Entity();

    TransformComponent transform = engine.createComponent(TransformComponent.class);
    food.add(transform);
    cook.add(transform);

    stack.pushItem(food, cook);

    Entity pop_return = stack.pop();

    assertEquals("Test that the pop works correctly", 0, stack.size());
    assertFalse("The isHidden attribute should be false after an entity is popped",
        Mappers.transform.get(pop_return).isHidden);
  }

  @Test
  public void setVisibilityTest() {
    PooledEngine engine = new PooledEngine();
    FoodStack stack = new FoodStack();
    stack.init(engine);

    Entity food = new Entity();
    Entity cook = new Entity();

    TransformComponent transform = engine.createComponent(TransformComponent.class);

    food.add(transform);
    cook.add(transform);

    for (int i = 0; i < 3; i++) {
      stack.pushItem(food, cook);
    }

    for (Entity entity : stack) {
      assertTrue(
          " All entities transform component should have true set for isHidden when size > 1",
          Mappers.transform.get(entity).isHidden);
    }

    stack.pop();
    stack.pop();
    assertFalse("isHidden should be set to false if stack size is one",
        Mappers.transform.get(stack.peek()).isHidden);

    Entity entity = stack.pop();
    assertFalse("When an entity is popped its isHidden attribute should be set to false",
        Mappers.transform.get(entity).isHidden);
  }
}
