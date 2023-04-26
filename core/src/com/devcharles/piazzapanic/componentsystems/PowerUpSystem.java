package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.devcharles.piazzapanic.GameScreen;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.components.PowerUpComponent.PowerUpType;
import com.devcharles.piazzapanic.utility.Mappers;



public class PowerUpSystem extends IteratingSystem{

    public Integer timer = 15;
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
        
        ControllableComponent cook = Mappers.controllable.get(entity);

        if(cook.currentPowerup.contains(PowerUpType.DoublePoints)){
            cook.currentPowerup.remove(PowerUpType.DoublePoints);
            FoodType.burger.setPrice();
            FoodType.salad.setPrice();
            FoodType.jacketPotato.setPrice();
            FoodType.pizza.setPrice();
            while(timer != 0){
                timer--;
            }
            FoodType.burger.originalPrice();
            FoodType.salad.originalPrice();
            FoodType.jacketPotato.setPrice();
            FoodType.pizza.setPrice();
        }        

    }

    @Override
    public void update(float deltaTime){
       ImmutableArray<Entity> cook = engine.getEntitiesFor(Family.all(ControllableComponent.class).get());
       Entity chef = cook.first();
       ControllableComponent chef_current = chef.getComponent(ControllableComponent.class);
      
       if(gameScreen.SpeedBoost){
        gameScreen.SpeedBoost = false;
        chef_current.currentPowerup.add(PowerUpType.SpeedBoost);
        System.out.println("SpeedBoost added to the cook");
        //Done
       }
       if(gameScreen.InstaCook){
        if(InstaCookTimer !=0){
            InstaCookTimer = InstaCookTimer -17;
        }
        if(InstaCookTimer == 0){
            gameScreen.InstaOff();
            InstaCookTimer = 30000;
        }
        //Partially Done
       }
       if(gameScreen.BinACustomer){
        gameScreen.BinOff();
        chef_current.currentPowerup.add(PowerUpType.BinACustomer);
        System.out.println("BinACustomer added to the cook");
        //Done
       }
       if(gameScreen.TimeFreeze){
        gameScreen.TimeOff();
        chef_current.currentPowerup.add(PowerUpType.TimeFreeze);
        System.out.println("TimeFreeze added to the cook");
        //Done

       }
       if(gameScreen.DoubleRep){
        gameScreen.DoubleOff();
        chef_current.currentPowerup.add(PowerUpType.DoublePoints);
        System.out.println("DoublePoints added to the cook");
        //Done
       }
    }


}