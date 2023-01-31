package com.devcharles.piazzapanic.componentsystems;

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

    private Map<Integer, Box2dLocation> objectives;
    private Map<Integer, Boolean> objectiveTaken;

    private World world;
    private GdxTimer spawnTimer = new GdxTimer(30000, false, true);
    private EntityFactory factory;
    private int numOfCustomerTotal = 0;
    private Hud hud;
    private Integer reputationPoints[];
    private final int CUSTOMER = 5;
    private boolean firstSpawn = true;

    // List of customers, on removal we move the other customers up a place (queueing).
    private ArrayList<Entity> customers = new ArrayList<Entity>() {
        @Override
        public boolean remove(Object o) {
            for (Entity entity : customers) {
                if (entity != o) {
                    AIAgentComponent aiAgent = Mappers.aiAgent.get(entity);

                    if (aiAgent.currentObjective - 1 >= 0) {
                        if (!objectiveTaken.get(aiAgent.currentObjective - 1)) {
                            makeItGoThere(aiAgent, aiAgent.currentObjective - 1);
                        }
                    }

                }
            }
            return super.remove(o);
        };
    };

    /**
     * Instantiate the system.
     * @param objectives Map of objectives available
     * @param world Box2D {@link World} for AI and disposing of customer entities.
     * @param factory {@link EntityFactory} for creating new customers
     * @param hud {@link HUD} for updating orders, reputation
     * @param reputationPoints array-wrapped integer reputation passed by-reference See {@link Hud}
     */
    public CustomerAISystem(Map<Integer, Box2dLocation> objectives, World world, EntityFactory factory, Hud hud,
            Integer[] reputationPoints) {
        super(Family.all(AIAgentComponent.class, CustomerComponent.class).get());

        this.hud = hud;
        this.objectives = objectives;
        this.objectiveTaken = new HashMap<Integer, Boolean>();
        this.reputationPoints = reputationPoints;

        // Use a reference to the world to destroy box2d bodies when despawning
        // customers
        this.world = world;
        this.factory = factory;

        spawnTimer.start();
    }

    @Override
    public void update(float deltaTime) {
        if (firstSpawn || (spawnTimer.tick(deltaTime) && numOfCustomerTotal < CUSTOMER)) {
            firstSpawn = false;
            Entity newCustomer = factory.createCustomer(objectives.get(-2).getPosition());
            customers.add(newCustomer);
            numOfCustomerTotal++;
            Mappers.customer.get(newCustomer).timer.start();
        }

        FoodType[] orders = new FoodType[customers.size()];
        int i = 0;
        for (Entity customer : customers) {
            orders[i] = Mappers.customer.get(customer).order;
            i++;
        }

        if (!hud.won && customers.size() == 0 && numOfCustomerTotal == CUSTOMER) {
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

        if (customer.food != null && transform.position.x >= (objectives.get(-1).getPosition().x - 2)) {
            destroyCustomer(entity);
            return;
        }

        if (aiAgent.steeringBody.getSteeringBehavior() == null) {
            makeItGoThere(aiAgent, customers.size() - 1);
        }

        aiAgent.steeringBody.update(deltaTime);

        // lower reputation points if they have waited longer than time alloted (1 min)
        if (customer.timer.tick(deltaTime)) {
            if (reputationPoints[0] > 0) {
                reputationPoints[0]--;
            }
            customer.timer.stop();
            if (!hud.won) {

            }
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
     * @param customer
     */
    private void destroyCustomer(Entity customer) {
        getEngine().removeEntity(Mappers.customer.get(customer).food);
        world.destroyBody(Mappers.b2body.get(customer).body);
        getEngine().removeEntity(customer);
    }

    /**
     * Give the customer an objetive to go to.
     * @param locationID and id from {@link CustomerAISystem.objectives}
     */
    private void makeItGoThere(AIAgentComponent aiAgent, int locationID) {
        objectiveTaken.put(aiAgent.currentObjective, false);

        Box2dLocation there = objectives.get(locationID);

        Arrive<Vector2> arrive = new Arrive<Vector2>(aiAgent.steeringBody)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(0.25f)
                .setDecelerationRadius(2)
                .setTarget(there);

        Proximity<Vector2> proximity = new Box2dRadiusProximity(aiAgent.steeringBody, world, 1f);
        CollisionAvoidance<Vector2> collisionAvoidance = new CollisionAvoidance<Vector2>(
                aiAgent.steeringBody, proximity);

        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<Vector2>(aiAgent.steeringBody)
                .add(collisionAvoidance)
                .add(arrive);

        aiAgent.steeringBody.setSteeringBehavior(prioritySteering);
        aiAgent.currentObjective = locationID;
        objectiveTaken.put(aiAgent.currentObjective, true);

        if (locationID == -1) {
            aiAgent.steeringBody.setOrientation(0);
        } else {
            aiAgent.steeringBody.setOrientation((float) (1.5f * Math.PI));
        }
    }

    /**
     * Give customer food, send them away and remove the order from the list
     * @param entity
     * @param customer
     * @param foodEntity
     */
    private void fulfillOrder(Entity entity, CustomerComponent customer, Entity foodEntity) {

        Engine engine = getEngine();

        customer.order = null;

        ItemComponent heldItem = engine.createComponent(ItemComponent.class);
        heldItem.holderTransform = Mappers.transform.get(entity);

        foodEntity.add(heldItem);

        customer.food = foodEntity;

        AIAgentComponent aiAgent = Mappers.aiAgent.get(entity);
        makeItGoThere(aiAgent, -1);

        customer.timer.stop();
        customer.timer.reset();

        customers.remove(entity);
    }

}
