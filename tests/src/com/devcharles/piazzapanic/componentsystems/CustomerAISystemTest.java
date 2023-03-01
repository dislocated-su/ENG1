package com.devcharles.piazzapanic.componentsystems;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.scene2d.Hud;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.box2d.Box2dLocation;
import java.util.HashMap;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CustomerAISystemTest {

  @Test
  public void update() {
  }

  @Test
  public void processEntity() {
  }

  @Test
  public void testDestroyCustomerValid() {
    World world = new World(Vector2.Zero, true);
    PooledEngine engine = new PooledEngine();
    EntityFactory factory = new EntityFactory(engine, world);
    CustomerAISystem system = new CustomerAISystem(new HashMap<Integer, Box2dLocation>(), world,
        factory, mock(Hud.class), new Integer[]{});
    engine.addSystem(system);
    Entity customer = factory.createCustomer(Vector2.Zero);
    Entity food = factory.createFood(FoodType.from(1));
    customer.getComponent(CustomerComponent.class).food = food;

    assertTrue("Food should exist in the ECS engine.", engine.getEntities().contains(food, true));
    assertTrue("Customer should exist in the ECS engine.",
        engine.getEntities().contains(customer, true));
    assertEquals("Customer should have a physical representation", 1, world.getBodyCount());

    system.destroyCustomer(customer);

    assertFalse("Food should no longer exist in the ECS engine.",
        engine.getEntities().contains(food, true));
    assertFalse("Customer should no longer exist in the ECS engine.",
        engine.getEntities().contains(customer, true));
    assertEquals("Customer should not have a physical representation", 0, world.getBodyCount());
  }

  @Test(expected = NullPointerException.class)
  public void testDestroyCustomerInvalidEntity() {
    World world = new World(Vector2.Zero, true);
    PooledEngine engine = new PooledEngine();
    EntityFactory factory = new EntityFactory(engine, world);
    CustomerAISystem system = new CustomerAISystem(new HashMap<Integer, Box2dLocation>(), world,
        factory, mock(Hud.class), new Integer[]{});
    engine.addSystem(system);
    Entity customer = new Entity();

    system.destroyCustomer(customer);
  }

  @Test(expected = NullPointerException.class)
  public void testDestroyCustomerNull() {
    World world = new World(Vector2.Zero, true);
    PooledEngine engine = new PooledEngine();
    EntityFactory factory = new EntityFactory(engine, world);
    CustomerAISystem system = new CustomerAISystem(new HashMap<Integer, Box2dLocation>(), world,
        factory, mock(Hud.class), new Integer[]{});
    engine.addSystem(system);

    system.destroyCustomer(null);
  }
  @Test
  public void makeItGoThere() {
  }

  @Test
  public void fulfillOrder() {
  }
}