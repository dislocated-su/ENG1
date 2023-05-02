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
import com.devcharles.piazzapanic.GameScreen;
import com.devcharles.piazzapanic.components.*;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.scene2d.Hud;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.Difficulty;
import com.devcharles.piazzapanic.utility.GdxTimer;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.box2d.Box2dLocation;
import com.devcharles.piazzapanic.utility.box2d.Box2dRadiusProximity;

/**
 * Controls the AI Customers, creates orders.
 */
public class CustomerAISystem extends IteratingSystem {

    private final Map<Integer, Box2dLocation> objectives;
    private final Map<Integer, Boolean> objectiveTaken;

    private final World world;
    private GdxTimer spawnTimer;
    private final EntityFactory factory;
    private int numOfCustomerTotal = 0;
    private final Hud hud;
    private final Difficulty difficulty;
    private final Integer[] reputationPoints;
    private final Float[] tillBalance;
    private final Integer[] customersServed;
    private int CUSTOMER;
    private boolean firstSpawn = true;
    private GameScreen gameScreen;
    private Integer timeFrozen = 30000;
    private Integer DoubleRepCounter = 30000;

    // List of customers, on removal we move the other customers up a place
    // (queueing).
    private final ArrayList<Entity> customers = new ArrayList<Entity>() {
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
        }
    };

    /**
     * Instantiate the system.
     * 
     * @param objectives       Map of objectives available
     * @param world            Box2D {@link World} for AI and disposing of customer
     *                         entities.
     * @param factory          {@link EntityFactory} for creating new customers
     * @param hud              {@link HUD} for updating orders, reputation
     * @param reputationPoints array-wrapped integer reputation passed by-reference
     *                         See {@link Hud}
     */
    public CustomerAISystem(Map<Integer, Box2dLocation> objectives, World world, EntityFactory factory, Hud hud,
            Integer[] reputationPoints, int customer, Difficulty difficulty, Float[] tillBalance,
            Integer[] customersServed, GameScreen gameScreen) {
        super(Family.all(AIAgentComponent.class, CustomerComponent.class).get());

        this.CUSTOMER = customer;
        this.hud = hud;
        this.objectives = objectives;
        this.objectiveTaken = new HashMap<Integer, Boolean>();
        this.reputationPoints = reputationPoints;
        this.difficulty = difficulty;
        this.tillBalance = tillBalance;
        this.customersServed = customersServed;
        this.gameScreen = gameScreen;

        // Use a reference to the world to destroy box2d bodies when despawning
        // customers
        this.world = world;
        this.factory = factory;

        this.spawnTimer = new GdxTimer(difficulty.getSpawnFrequency(), true, true);
        // spawnTimer.start();
    }

    @Override
    public void update(float deltaTime) {
        if (firstSpawn || (spawnTimer.tick(deltaTime) && CUSTOMER > 0)) {
            firstSpawn = false;

            // Only add a customer is there is space in the queue and there are customers
            // still remaining.
            // The number of customers in the queue cannot be more than the number of
            // customers remaining.
            // There are 5 queue spots on the map.
            if (numOfCustomerTotal < 5 && !(numOfCustomerTotal + 1 > CUSTOMER)) {

                // The first customer will arrive alone but after that there is a chance
                // customers
                // will arrive in groups of two or three.
                int customersToSpawn = getRandomCustomerGroupSize();
                if (numOfCustomerTotal + customersToSpawn > 5 || firstSpawn) {
                    customersToSpawn = 1;
                }
                Vector2 pos = new Vector2(objectives.get(-2).getPosition());

                // Each customer in group will have spawn point offset to stop entity overlap
                // and queue blocking.
                for (int i = 0; i < customersToSpawn; i++) {
                    pos.x += 0.5;
                    Entity newCustomer = factory.createCustomer(pos);
                    customers.add(newCustomer);
                    numOfCustomerTotal++;
                    Mappers.customer.get(newCustomer).timer.start();
                    processEntity(newCustomer, deltaTime);
                }
                Gdx.app.log("Info", customersToSpawn + " customer(s) have arrived.");
            }

            // If endless mode then decrease customer spawn frequency by one second every
            // time a customer is served.
            // Result is customers will arrive more often over time in endless mode.
            // If the time freeze powerup has been purchased pause the spawn timer until the
            // powerup time has passed.
            if (firstSpawn == false && difficulty != Difficulty.SCENARIO) {
                if (gameScreen.TimeFreeze) {
                    if (timeFrozen <= 0) {
                        spawnTimer.start();
                        timeFrozen = 30000;
                    }
                    spawnTimer.stop();
                    timeFrozen = timeFrozen - 25;
                }
                spawnTimer = new GdxTimer((difficulty.getSpawnFrequency() - ((999 - CUSTOMER) * 1000)), true, true);
                Gdx.app.log("Info",
                        "Spawn frequency is now " + (difficulty.getSpawnFrequency() - ((999 - CUSTOMER) * 1000)));
            }
        }

        FoodType[] orders = new FoodType[customers.size()];
        int[] orderTimes = new int[customers.size()];
        int i = 0;
        for (Entity customer : customers) {
            orders[i] = Mappers.customer.get(customer).order;
            orderTimes[i] = (120000 - Mappers.customer.get(customer).timer.getElapsed()) / 1000;
            i++;
        }

        if ((!hud.gameOver && customers.size() == 0 && CUSTOMER == 0) || reputationPoints[0] == 0) {
            hud.triggerGameOver = true;
        }

        super.update(deltaTime);
        hud.updateOrders(orders, orderTimes);
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
            Gdx.app.log("customer", "this customer is moving to objective" + (customers.size() - 1));
            makeItGoThere(aiAgent, customers.size() - 1);
        }

        aiAgent.steeringBody.update(deltaTime);

        // Lower reputation points only in endless if they have waited longer than time
        // alloted.
        if (customer.timer.tick(deltaTime)) {
            if (reputationPoints[0] > 0) {
                if (difficulty != Difficulty.SCENARIO) {
                    reputationPoints[0]--;
                }
            }
            customer.timer.stop();
        }

        // Remove a customer upon activation of the BinACustomer powerup.
        if (gameScreen.BinACustomer) {
            if (CUSTOMER == 0) {
                gameScreen.BinOff();
            }
            fulfillOrder(entity, customer, entity, gameScreen.BinACustomer, gameScreen.DoubleRep);
            gameScreen.BinOff();
        }

        // Freeze the customer timers for all customers whilst the powerup is active.
        if (gameScreen.TimeFreeze) {
            timeFreeze(customer);
        }

        // Decrease the timer for the double money powerup.
        if (gameScreen.DoubleRep) {
            DoubleRepCounter -= 17;
            if (DoubleRepCounter <= 0) {
                gameScreen.DoubleOff();
                DoubleRepCounter = 30000;

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
                fulfillOrder(entity, customer, food, gameScreen.BinACustomer, gameScreen.DoubleRep);
                gameScreen.audio.playThanks();
            } else {
                getEngine().removeEntity(food);
                gameScreen.audio.playSigh();
            }

        }
    }

    /**
     * 
     * @param customer for freezeing the customers order timer whilst the powerup is
     *                 active
     */
    private void timeFreeze(CustomerComponent customer) {
        customer.timer.stop();
        if (timeFrozen <= 0) {
            customer.timer.start();
            gameScreen.TimeOff();
        }
    }

    /**
     * Remove the customer from the {@link World} and remove their entity.
     */
    private void destroyCustomer(Entity customer) {
        getEngine().removeEntity(Mappers.customer.get(customer).food);
        world.destroyBody(Mappers.b2body.get(customer).body);
        getEngine().removeEntity(customer);
    }

    /**
     * Give the customer an objetive to go to.
     * 
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
     */
    private void fulfillOrder(Entity entity, CustomerComponent customer, Entity foodEntity, Boolean BinACustomer,
            Boolean DoubleRep) {

        if (BinACustomer) {
            getEngine().removeEntity(entity);
            customer.timer.stop();
            customer.timer.reset();
            customer.order = null;
            customers.remove(entity);
            numOfCustomerTotal--;
            CUSTOMER--;

        }

        Engine engine = getEngine();
        if (customer.order != null) {
            float customerTip = getRandomCustomerTip(customer.order.getPrice());
            if (customerTip > 0) {
                hud.displayInfoMessage("Customer has tipped $ " + Float.toString(customerTip));
            }

            // if double rep powerup is active double the price of the order
            if (DoubleRep) {
                float doublePrice = customer.order.getPrice() * 2f;
                tillBalance[0] += doublePrice + customerTip;
            }

            tillBalance[0] += customer.order.getPrice() + customerTip;
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
            numOfCustomerTotal--;
            CUSTOMER--;
            customersServed[0]++;
        }

    }

    /**
     * Calculates how many customers should arrive at once.
     * Weighted so that customers arrive alone most of the time.
     * 
     * @return groupSize
     */
    private int getRandomCustomerGroupSize() {
        if (difficulty == Difficulty.SCENARIO) {
            return 1;
        }
        double x = Math.random();
        if (x < 0.7) {
            return 1;
        }
        if (x >= 0.7 && x < 0.9) {
            return 2;
        }
        if (x >= 0.9) {
            return 3;
        }
        return 1;
    }

    /**
     * Calculates the amount a customer will tip.
     * Customers will tip a random amount up to the price of their dish and
     * will do so 20% of the time.
     * In scenario mode there are no tips.
     * 
     * @param dishPrice The price of the customer's meal which is used to determine
     *                  their tip.
     * @return The calculated tip.
     */
    private int getRandomCustomerTip(float dishPrice) {
        if (difficulty == Difficulty.SCENARIO) {
            return 0;
        }
        double x = Math.random();
        if (x > 0.8) {
            x = (1 + Math.random());
            return Math.round((float) dishPrice * (float) x);
        }
        return 0;
    }
}
