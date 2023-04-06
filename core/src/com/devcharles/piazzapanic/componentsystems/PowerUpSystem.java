package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.PowerUpComponent.PowerUpType;
import com.devcharles.piazzapanic.scene2d.Hud;
import com.devcharles.piazzapanic.utility.Mappers;


public class PowerUpSystem extends IteratingSystem{

    private Hud hud;
    public PowerUpSystem(Hud hud) {
        
        super(Family.all(PlayerComponent.class, ControllableComponent.class).get());
        this.hud = hud;
        
    }
    
    
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ControllableComponent cook = Mappers.controllable.get(entity);

        PowerUpType[] powerups = new PowerUpType[cook.currentPowerup.size()];

        int i = 0;
        for (Entity powerup : cook.currentPowerup){
            if(Mappers.food.get(powerup).type != null){
                powerups[i] = Mappers.powerup.get(powerup).type;
                i++; 
            }
        }
        hud.updateInventory(null);
    }


}