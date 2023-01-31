package com.devcharles.piazzapanic.utility;

import java.util.ArrayDeque;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.ItemComponent;

public class FoodStack extends ArrayDeque<Entity> {

    private Engine engine;

    public void init(Engine e) {
        engine = e;
    }

    public final int capacity = 12;

    /**
     * Put a new food into inventory, use this instead of
     * {@code FoodStack.push(Entity food)}
     * as it binds the item location to the player and maintains a maximum inventory
     * size of 12.
     * 
     * @param food
     * @param cook
     * @return
     */
    public boolean pushItem(Entity food, Entity cook) {
        if (this.size() < capacity) {
            ItemComponent item = engine.createComponent(ItemComponent.class);
            item.holderTransform = Mappers.transform.get(cook);
            food.add(item);
            this.push(food);
            return true;
        }
        return false;
    }

    /**
     * Used internally, please use {@code FoodStack.pushItem(Entity food)} instead.
     */
    @Override
    public void push(Entity food) {
        super.push(food);
        setVisibility(this.size(), null);
        return;
    }

    
    /* (non-Javadoc)
     * @see java.util.ArrayDeque#pop()
     */
    @Override
    public Entity pop() {
        Entity e = super.pop();
        e.remove(ItemComponent.class);
        setVisibility(this.size(), e);
        return e;
    }

    private void setVisibility(int size, Entity e) {
        if (size > 1) {
            for (Entity food : this) {
                Mappers.transform.get(food).isHidden = true;
            }
        } else if (size == 1) {
            Mappers.transform.get(this.peek()).isHidden = false;
        }

        if (e != null) {
            Mappers.transform.get(e).isHidden = false;
        }
    }
}
