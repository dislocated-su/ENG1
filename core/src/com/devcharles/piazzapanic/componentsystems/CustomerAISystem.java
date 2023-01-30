package com.devcharles.piazzapanic.componentsystems;

import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.components.AIAgentComponent;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.ItemComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.GdxTimer;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.box2d.Box2dLocation;

public class CustomerAISystem extends IteratingSystem {

    private Map<Integer, Box2dLocation> objectives;
    private World world;
    private GdxTimer spawnTimer = new GdxTimer(5000, false, true);
    private EntityFactory factory;
    private int numOfCustomers = 0;
    private int numOfCutstomerTotal = 0;

    public CustomerAISystem(Map<Integer, Box2dLocation> objectives, World world, EntityFactory factory) {
        super(Family.all(AIAgentComponent.class, CustomerComponent.class).get());

        this.objectives = objectives;

        // Use a reference to the world to destroy box2d bodies when despawning
        // customers
        this.world = world;
        this.factory = factory;
        spawnTimer.start();
    }

    @Override
    public void update(float deltaTime) {
        if (spawnTimer.tick(deltaTime) && numOfCutstomerTotal < 5) {
            factory.createCustomer(objectives.get(-2).getPosition());
            numOfCustomers++;
            numOfCutstomerTotal++;
        }

        super.update(deltaTime);
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
            makeItGoThere(aiAgent, numOfCustomers - 1);
        }

        aiAgent.steeringBody.update(deltaTime);

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

    private void destroyCustomer(Entity customer) {
        getEngine().removeEntity(Mappers.customer.get(customer).food);
        world.destroyBody(Mappers.b2body.get(customer).body);
        getEngine().removeEntity(customer);
    }

    private void makeItGoThere(AIAgentComponent aiAgent, int locationID) {
        Box2dLocation there = objectives.get(locationID);
        Arrive<Vector2> arriveSb = new Arrive<Vector2>(aiAgent.steeringBody)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(0.5f)
                .setDecelerationRadius(2)
                .setTarget(there);
        aiAgent.steeringBody.setSteeringBehavior(arriveSb);
        aiAgent.steeringBody.setIndependentFacing(true);

        if (locationID == -1) {
            aiAgent.steeringBody.setOrientation(0);
        } else {
            aiAgent.steeringBody.setOrientation((float) (1.5f * Math.PI));
        }
    }

    private void fulfillOrder(Entity entity, CustomerComponent customer, Entity foodEntity) {

        Engine engine = getEngine();

        customer.order = null;

        ItemComponent heldItem = engine.createComponent(ItemComponent.class);
        heldItem.holderTransform = Mappers.transform.get(entity);

        foodEntity.add(heldItem);

        customer.food = foodEntity;

        AIAgentComponent aiAgent = Mappers.aiAgent.get(entity);
        makeItGoThere(aiAgent, -1);
        numOfCustomers--;
    }

}
