package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.devcharles.piazzapanic.components.AIAgentComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.utility.Mappers;

public class CustomerAISystem extends IteratingSystem {

    public CustomerAISystem() {
        super(Family.all(AIAgentComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // TODO Auto-generated method stub
        AIAgentComponent aiAgent = Mappers.aiAgent.get(entity);
        TransformComponent transform = Mappers.transform.get(entity);

        aiAgent.steeringBody.update(deltaTime);
        transform.isMoving = aiAgent.steeringBody.getBody().isAwake();
    }
    
}
