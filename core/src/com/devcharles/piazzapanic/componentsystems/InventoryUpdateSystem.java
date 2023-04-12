package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.scene2d.Hud;
import com.devcharles.piazzapanic.utility.Mappers;
/**
 * Links up the cook's inventory to the hud.
 */
public class InventoryUpdateSystem extends IteratingSystem {

    private Hud hud;
    FoodType[] foods;
    public InventoryUpdateSystem(Hud hud) {
        super(Family.all(PlayerComponent.class, ControllableComponent.class).get());
        this.hud = hud;
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ControllableComponent cook = Mappers.controllable.get(entity);

        foods = new FoodType[cook.currentFood.size()];

        int i = 0;
        for (Entity food : cook.currentFood){
            if (Mappers.food.get(food).type != null) {
                foods[i] = Mappers.food.get(food).type;
                i++;
            }            
        }
        hud.updateInventory(foods);
        
    }
    
}
