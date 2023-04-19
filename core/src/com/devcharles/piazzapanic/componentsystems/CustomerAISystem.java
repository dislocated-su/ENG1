package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.gdx.math.MathUtils;
import com.devcharles.piazzapanic.utility.saving.SavableCustomer;
import com.devcharles.piazzapanic.utility.saving.SavableCustomerAISystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.components.AIAgentComponent;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.ItemComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.scene2d.Hud;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.GdxTimer;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.box2d.Box2dLocation;
import com.devcharles.piazzapanic.utility.box2d.Box2dRadiusProximity;

/**
 * Controls the AI Customers, creates orders.
 */
public class CustomerAISystem extends IteratingSystem {

  private final Map<Integer, Map<Integer, Box2dLocation>> objectives;
  private final Map<Integer, Boolean> objectiveTaken;

  private final World world;
  protected final GdxTimer spawnTimer = new GdxTimer(30000, false, true);
  private final EntityFactory factory;
  private int totalCustomers = 0;
  private float patienceModifier = 1f;

  private int incomeModifier = 1;
  private final Hud hud;
  private final Integer[] reputationPointsAndMoney;
  private final int MAX_CUSTOMERS = 5;
  private boolean firstSpawn = true;
  private final boolean isEndless;
  private int numQueuedCustomers = 0;
  private final int maxGroupSize;

  // List of customer groups, on removal we move the other customers up a place (queueing).
  protected final ArrayList<ArrayList<Entity>> customers = new ArrayList<ArrayList<Entity>>(
      MAX_CUSTOMERS) {
    @Override
    public boolean remove(Object o) {
      for (ArrayList<Entity> group : customers) {
        if (group == o) {
          objectiveTaken.put(customers.indexOf(o), false);
        } else {
          int objective = -3;
          for (Entity entity : group) {
            AIAgentComponent aiAgent = Mappers.aiAgent.get(entity);

            if (aiAgent.currentObjective - 1 >= 0) {
              objective = aiAgent.currentObjective;
              if (!objectiveTaken.get(aiAgent.currentObjective - 1)) {
                makeItGoThere(aiAgent, aiAgent.currentObjective - 1);
              }
            }
          }
          objectiveTaken.put(objective, false);
          objectiveTaken.put(objective - 1, true);
        }
      }
      return super.remove(o);
    }
  };

  /**
   * Instantiate the system.
   *
   * @param objectives               Map of objectives available
   * @param world                    Box2D {@link World} for AI and disposing of customer entities.
   * @param factory                  {@link EntityFactory} for creating new customers
   * @param hud                      {@link Hud} for updating orders, reputation
   * @param reputationPointsAndMoney array-wrapped integer reputation and money passed by-reference
   *                                 See {@link Hud}
   * @param isEndless                a boolean flag to decide whether there is a limit on customers
   * @param maxGroupSize             the maximum size of a single group of customers
   */
  public CustomerAISystem(Map<Integer, Map<Integer, Box2dLocation>> objectives, World world,
      EntityFactory factory, Hud hud,
      Integer[] reputationPointsAndMoney, boolean isEndless, int maxGroupSize) {
    super(Family.all(AIAgentComponent.class, CustomerComponent.class).get());

    this.hud = hud;
    this.objectives = objectives;
    this.objectiveTaken = new HashMap<>();
    this.reputationPointsAndMoney = reputationPointsAndMoney;
    this.isEndless = isEndless;
    this.maxGroupSize = maxGroupSize;

    // Use a reference to the world to destroy box2d bodies when despawning
    // customers
    this.world = world;
    this.factory = factory;

    spawnTimer.start();
  }

  public void loadFromSave(SavableCustomerAISystem savedSystem) {
    // Set objective taken.
    objectiveTaken.clear();
    // Event though objectiveTaken has a key of type Integer, the JSON loader loads it as a String,
    // so type casting is necessary.
    for (Object key : savedSystem.objectiveTaken.keySet()) {
      objectiveTaken.put(Integer.valueOf((String) key), savedSystem.objectiveTaken.get(key));
    }

    // Set spawn timer
    spawnTimer.setElapsed(savedSystem.spawnTimer.elapsed);
    spawnTimer.setDelay(savedSystem.spawnTimer.delay);
    if (savedSystem.spawnTimer.running) {
      spawnTimer.start();
    } else {
      spawnTimer.stop();
    }

    // Set flags
    totalCustomers = savedSystem.totalCustomers;
    firstSpawn = savedSystem.firstSpawn;
    numQueuedCustomers = savedSystem.numQueuedCustomers;
    patienceModifier = savedSystem.patienceModifier;
    incomeModifier = savedSystem.incomeModifier;

    for (ArrayList<Entity> group : customers) {
      for (Entity customer : group) {
        destroyCustomer(customer);
      }
      group.clear();
    }
    customers.clear();

    // Load customers
    for (ArrayList<SavableCustomer> savedGroup : savedSystem.customers) {
      ArrayList<Entity> group = new ArrayList<>(3);
      for (SavableCustomer savedCustomer : savedGroup) {
        Entity newCustomer = savedCustomer.toEntity(factory);
        group.add(newCustomer);
        makeItGoThere(Mappers.aiAgent.get(newCustomer), savedCustomer.currentObjective);
      }
      customers.add(group);
    }
  }

  @Override
  public void update(float deltaTime) {
    // Ensure timer actually ticks, and is not short-circuited by firstSpawn being true
    boolean timerComplete = spawnTimer.tick(deltaTime);
    if (timerComplete) {
      numQueuedCustomers++;
    }
    int numCustomers = isEndless ? customers.size() : totalCustomers;
    if (firstSpawn || (numQueuedCustomers > 0 && numCustomers < MAX_CUSTOMERS)) {
      if (!firstSpawn) {
        numQueuedCustomers--;
        if (isEndless) {
          spawnTimer.setDelay((int) (spawnTimer.getDelay() * 0.98f));
          Gdx.app.log("Spawn Timer", String.valueOf(spawnTimer.getDelay()));
        }
      }
      firstSpawn = false;

      ArrayList<Entity> group = new ArrayList<>();
      for (int i = 0; i < MathUtils.random(1, maxGroupSize); i++) {
        Entity newCustomer = factory.createCustomer(objectives.get(-2).get(0).getPosition(), null);
        Mappers.aiAgent.get(newCustomer).slot = i;
        group.add(newCustomer);
        GdxTimer timer =Mappers.customer.get(newCustomer).timer;
        timer.setDelay((int) (timer.getDelay() * patienceModifier));
        timer.start();
      }
      customers.add(group);
      totalCustomers++;
    }

    ArrayList<FoodType> orders = new ArrayList<>(customers.size() * maxGroupSize);
    for (ArrayList<Entity> group : customers) {
      for (Entity customer : group) {
        orders.add(Mappers.customer.get(customer).order);
      }
    }

    if (!isEndless && !hud.won && customers.size() == 0 && totalCustomers == MAX_CUSTOMERS) {
      hud.triggerWin = true;
    } else if (isEndless && !hud.won && reputationPointsAndMoney[0] == 0) {
      hud.triggerWin = true;
    }

    super.update(deltaTime);

    hud.updateOrders(orders);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    AIAgentComponent aiAgent = Mappers.aiAgent.get(entity);
    CustomerComponent customer = Mappers.customer.get(entity);
    TransformComponent transform = Mappers.transform.get(entity);

    if (customer.food != null &&
        transform.position.x >= (objectives.get(-1).get(0).getPosition().x - 2)) {
      destroyCustomer(entity);
      return;
    }

    if (aiAgent.steeringBody.getSteeringBehavior() == null) {
      makeItGoThere(aiAgent, customers.size() - 1);
    }

    aiAgent.steeringBody.update(deltaTime);

    // lower reputation points if they have waited longer than time allotted (1 min)
    if (customer.timer.tick(deltaTime)) {
      if (reputationPointsAndMoney[0] > 0) {
        reputationPointsAndMoney[0]--;
      }
      customer.timer.stop();
    }

    if (customer.interactingCook != null) {
      PlayerComponent player = Mappers.player.get(customer.interactingCook);

      // In order, check if the player is touching and pressing
      // the correct key to interact with the customer.
      if (player == null || !player.putDown) {
        return;
      }
      player.putDown = false;

      ControllableComponent cook = Mappers.controllable.get(customer.interactingCook);

      if (cook.currentFood.isEmpty()) {
        return;
      }

      Entity food = cook.currentFood.pop();

      if (Mappers.food.get(food).type == customer.order) {
        // Fulfill order
        Gdx.app.log("Order success", customer.order.name());
        fulfillOrder(entity, customer, food);

      } else {
        getEngine().removeEntity(food);
      }

    }
  }

  /**
   * Remove the customer from the {@link World} and remove their entity.
   */
  protected void destroyCustomer(Entity customer) {
    getEngine().removeEntity(Mappers.customer.get(customer).food);
    world.destroyBody(Mappers.b2body.get(customer).body);
    getEngine().removeEntity(customer);
  }

  /**
   * Give the customer an objective to go to.
   *
   * @param locationID and id from {@link CustomerAISystem.objectives}
   */
  protected void makeItGoThere(AIAgentComponent aiAgent, int locationID) {
    Box2dLocation there = objectives.get(locationID).get(aiAgent.slot);
    if (there == null) {
      there = objectives.get(locationID).get(0);
    }

    Arrive<Vector2> arrive = new Arrive<>(aiAgent.steeringBody)
        .setTimeToTarget(0.1f)
        .setArrivalTolerance(0.25f)
        .setDecelerationRadius(2)
        .setTarget(there);

    Proximity<Vector2> proximity = new Box2dRadiusProximity(aiAgent.steeringBody, world, 1f);
    CollisionAvoidance<Vector2> collisionAvoidance = new CollisionAvoidance<>(
        aiAgent.steeringBody, proximity);

    PrioritySteering<Vector2> prioritySteering = new PrioritySteering<>(aiAgent.steeringBody)
        .add(collisionAvoidance)
        .add(arrive);

    aiAgent.steeringBody.setSteeringBehavior(prioritySteering);
    aiAgent.currentObjective = locationID;

    if (locationID == -1) {
      aiAgent.steeringBody.setOrientation(0);
    } else {
      aiAgent.steeringBody.setOrientation((float) (1.5f * Math.PI));
    }
  }

  /**
   * Give customer food, send them away and remove the order from the list
   */
  protected void fulfillOrder(Entity entity, CustomerComponent customer, Entity foodEntity) {

    Engine engine = getEngine();

    customer.order = null;

    ItemComponent heldItem = engine.createComponent(ItemComponent.class);
    heldItem.holderTransform = Mappers.transform.get(entity);

    foodEntity.add(heldItem);

    customer.food = foodEntity;

    AIAgentComponent aiAgent = Mappers.aiAgent.get(entity);
    makeItGoThere(aiAgent, -1);
    // Give money for completion of order
    reputationPointsAndMoney[1] += 8 + incomeModifier;

    customer.timer.stop();
    customer.timer.reset();
    ArrayList<Entity> groupToRemove = null;
    for (ArrayList<Entity> group : customers) {
      group.remove(entity);
      if (group.isEmpty()) {
        groupToRemove = group;
      }
    }
    if (groupToRemove != null) {
      customers.remove(groupToRemove);
    }
    hud.incrementCompletedOrders();
  }

  public Map<Integer, Boolean> getObjectiveTaken() {
    return objectiveTaken;
  }

  public GdxTimer getSpawnTimer() {
    return spawnTimer;
  }

  public int getTotalCustomers() {
    return totalCustomers;
  }

  public boolean isFirstSpawn() {
    return firstSpawn;
  }

  public int getNumQueuedCustomers() {
    return numQueuedCustomers;
  }

  public ArrayList<ArrayList<Entity>> getCustomers() {
    return customers;
  }

  public float getPatienceModifier() {
    return patienceModifier;
  }

  public void setPatienceModifier(float patienceModifier) {
    this.patienceModifier = patienceModifier;
  }

  public int getIncomeModifier() {
    return incomeModifier;
  }

  public void setIncomeModifier(int incomeModifier) {
    this.incomeModifier = incomeModifier;
  }
}
