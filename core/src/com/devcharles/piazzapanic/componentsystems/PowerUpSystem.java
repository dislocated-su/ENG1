package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.devcharles.piazzapanic.GameScreen;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.PowerUpComponent.PowerUpType;


public class PowerUpSystem extends IteratingSystem{

    // This system makes sure the powerups function as intended in scenario mode
    public Integer timer = 30000;
    public GameScreen gameScreen;
    Integer InstaCookTimer = 30000;

    public Engine engine;
    public PowerUpSystem(Engine engine, GameScreen gameScreen) {
        
        super(Family.all(PlayerComponent.class, ControllableComponent.class).get());
        this.engine = engine;
        this.gameScreen = gameScreen;
        

    }
    
    
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    // When a powerup is purchased add it to the player
    @Override
    public void update(float deltaTime){
       ImmutableArray<Entity> cook = engine.getEntitiesFor(Family.all(ControllableComponent.class).get());
       Entity chef = cook.first();
       ControllableComponent chef_current = chef.getComponent(ControllableComponent.class);
      
       if(gameScreen.SpeedBoost){
        gameScreen.SpeedBoost = false;
        chef_current.currentPowerup.add(PowerUpType.SpeedBoost);
       }
       if(gameScreen.InstaCook){
        if(InstaCookTimer !=0){
            InstaCookTimer = InstaCookTimer -17;
        }
        if(InstaCookTimer == 0){
            gameScreen.InstaOff();
            InstaCookTimer = 30000;
        }
       }
       if(gameScreen.BinACustomer){
        gameScreen.BinOff();
        chef_current.currentPowerup.add(PowerUpType.BinACustomer);
       }
       if(gameScreen.TimeFreeze){
        gameScreen.TimeOff();
        chef_current.currentPowerup.add(PowerUpType.TimeFreeze);

       }
       if(gameScreen.DoubleRep){
        gameScreen.DoubleOff();
        chef_current.currentPowerup.add(PowerUpType.DoublePoints);
       }
    }


}