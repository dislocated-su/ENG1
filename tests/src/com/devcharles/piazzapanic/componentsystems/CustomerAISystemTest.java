package com.devcharles.piazzapanic.componentsystems;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.components.AIAgentComponent;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.scene2d.Hud;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.box2d.Box2dLocation;
import java.util.HashMap;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CustomerAISystemTest {

  @Test
  public void update() {
    World world = new World(Vector2.Zero, true);
    PooledEngine engine = new PooledEngine();
    EntityFactory factory = new EntityFactory(engine, world);
    HashMap<Integer, Box2dLocation> objectives = new HashMap<>();
    objectives.put(-2, new Box2dLocation());
    objectives.put(0, new Box2dLocation());
    objectives.put(1, new Box2dLocation());
    CustomerAISystem system = new CustomerAISystem(objectives, world, factory, mock(Hud.class),
        new Integer[]{3});
    engine.addSystem(system);

    assertEquals("There should be no customers to start with.", 0, system.customers.size());
    engine.update(1f);
    assertEquals("There should be a customer on the first frame.", 1, system.customers.size());
    engine.update(29f);
    assertEquals("There should still be 1 customer as the timer is not bigger than the delay.", 1,
        system.customers.size());
    engine.update(0.001f);
    assertEquals("There should be 2 customers on the very next frame past the limit.", 2,
        system.customers.size());
  }

  @Test
  public void testProcessEntityLoseReputation() {
    World world = new World(Vector2.Zero, true);
    PooledEngine engine = new PooledEngine();
    EntityFactory factory = new EntityFactory(engine, world);

    HashMap<Integer, Box2dLocation> objectives = new HashMap<>();
    objectives.put(-2, new Box2dLocation());
    objectives.put(0, new Box2dLocation());

    Integer[] reputationPoints = new Integer[]{3};
    CustomerAISystem system = new CustomerAISystem(objectives, world, factory, mock(Hud.class),
        reputationPoints);
    engine.addSystem(system);

    engine.update(1f);
    system.processEntity(system.customers.get(0), 91f);
    assertEquals("Reputation should decrease after over 90 seconds.", 2,
        reputationPoints[0].intValue());
  }

  @Test
  public void testProcessEntityExitsWithFood() {
    World world = new World(Vector2.Zero, true);
    PooledEngine engine = new PooledEngine();
    EntityFactory factory = new EntityFactory(engine, world);

    HashMap<Integer, Box2dLocation> objectives = new HashMap<>();
    objectives.put(-2, new Box2dLocation());
    objectives.put(-1, new Box2dLocation());
    objectives.put(0, new Box2dLocation());

    Integer[] reputationPoints = new Integer[]{3};
    CustomerAISystem system = new CustomerAISystem(objectives, world, factory, mock(Hud.class),
        reputationPoints);
    engine.addSystem(system);

    engine.update(1f);
    Entity customer = system.customers.get(0);
    TransformComponent transformComponent = Mappers.transform.get(customer);
    CustomerComponent customerComponent = Mappers.customer.get(customer);
    transformComponent.position.x = 1f;
    customerComponent.food = factory.createFood(FoodType.burger);
    system.processEntity(system.customers.get(0), 1f);

    assertEquals("When the customer is to the right of the objective with food, it should be gone",
        0, engine.getEntities().size());
  }

  @Test
  public void testProcessEntityChefInteract() {
    World world = new World(Vector2.Zero, true);
    PooledEngine engine = new PooledEngine();
    EntityFactory factory = new EntityFactory(engine, world);

    HashMap<Integer, Box2dLocation> objectives = new HashMap<>();
    objectives.put(-2, new Box2dLocation());
    objectives.put(-1, new Box2dLocation());
    objectives.put(0, new Box2dLocation());

    Integer[] reputationPoints = new Integer[]{3};
    CustomerAISystem system = new CustomerAISystem(objectives, world, factory, mock(Hud.class),
        reputationPoints);
    engine.addSystem(system);

    engine.update(1f);
    Entity customer = system.customers.get(0);
    CustomerComponent customerComponent = Mappers.customer.get(customer);
    customerComponent.interactingCook = factory.createCook(0, 0);
    system.processEntity(system.customers.get(0), 1f);

    // TODO: Test if player component exists or not
    // TODO: Test if the cook has any food
    // TODO: Test if the food matches the order
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
  public void testMakeItGoThere() {
    World world = new World(Vector2.Zero, true);
    PooledEngine engine = new PooledEngine();
    EntityFactory factory = new EntityFactory(engine, world);
    HashMap<Integer, Box2dLocation> objectives = new HashMap<>();
    objectives.put(1, new Box2dLocation());
    CustomerAISystem system = new CustomerAISystem(objectives, world,
        factory, mock(Hud.class), new Integer[]{});
    engine.addSystem(system);
    Entity customer = factory.createCustomer(Vector2.Zero);
    AIAgentComponent aiAgentComponent = Mappers.aiAgent.get(customer);

    assertNull("There should be no steering behaviour.",
        aiAgentComponent.steeringBody.getSteeringBehavior());

    system.makeItGoThere(aiAgentComponent, -1);
    SteeringBehavior<Vector2> oldBehaviour = aiAgentComponent.steeringBody.getSteeringBehavior();
    assertNotNull("There should be steering behaviour.", oldBehaviour);

    system.makeItGoThere(aiAgentComponent, 0);
    assertNotEquals("The steering behaviour should be different.", oldBehaviour,
        aiAgentComponent.steeringBody.getSteeringBehavior());
  }

  @Test
  public void fulfillOrder() {
    World world = new World(Vector2.Zero, true);
    PooledEngine engine = new PooledEngine();
    EntityFactory factory = new EntityFactory(engine, world);
    CustomerAISystem system = new CustomerAISystem(new HashMap<Integer, Box2dLocation>(), world,
        factory, mock(Hud.class), new Integer[]{});
    engine.addSystem(system);
    Entity customer = factory.createCustomer(Vector2.Zero);
    Entity food = factory.createFood(FoodType.from(1));
    CustomerComponent customerComponent = Mappers.customer.get(customer);
    customerComponent.order = FoodType.burger;

    system.customers.add(customer);
    system.fulfillOrder(customer, customerComponent, food);
    assertEquals("There should be no more customers.", 0, system.customers.size());
    assertFalse("Timer should be stopped.", customerComponent.timer.isRunning());
    assertEquals("Customer's food should be the same as the food above.", food,
        customerComponent.food);
  }
}