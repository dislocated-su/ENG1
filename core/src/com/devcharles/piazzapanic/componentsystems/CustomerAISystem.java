package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.devcharles.piazzapanic.components.AIAgentComponent;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.ItemComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.utility.Mappers;

public class CustomerAISystem extends IteratingSystem {

    public CustomerAISystem() {
        super(Family.all(AIAgentComponent.class, CustomerComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AIAgentComponent aiAgent = Mappers.aiAgent.get(entity);
        CustomerComponent customer = Mappers.customer.get(entity);
        TransformComponent transform = Mappers.transform.get(entity);

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

    private void fulfillOrder(Entity entity, CustomerComponent customer, Entity foodEntity) {

        Engine engine = getEngine();

        customer.order = null;

        // Mappers.transform.get(foodEntity).isHidden = false;
        ItemComponent heldItem = engine.createComponent(ItemComponent.class);
        heldItem.holderTransform = Mappers.transform.get(entity);

        foodEntity.add(heldItem);

        customer.food = foodEntity;
    }

}
