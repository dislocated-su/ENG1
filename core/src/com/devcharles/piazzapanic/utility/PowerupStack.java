package com.devcharles.piazzapanic.utility;

import java.util.ArrayDeque;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.ItemComponent;


public class PowerupStack extends ArrayDeque<Entity> {

    private Engine engine;

    public void init(Engine e) {
        engine = e;
    }

    public final int capacity = 64;

    /**
     * Put a new Powerup into inventory, use this instead of
     * {@code PowerupStack.push(Entity Powerup)}
     * as it binds the item location to the player and maintains a maximum inventory
     * size of 12.
     * 
     * @param Powerup
     * @param cook
     * @return
     */
    public boolean pushItem(Entity Powerup, Entity cook) {
        if (this.size() < capacity) {
            ItemComponent item = engine.createComponent(ItemComponent.class);
            item.holderTransform = Mappers.transform.get(cook);
            Powerup.add(item);
            this.push(Powerup);
            return true;
        }
        return false;
    }

    /**
     * Used internally, please use {@code PowerupStack.pushItem(Entity Powerup)} instead.
     */
    @Override
    public void push(Entity Powerup) {
        super.push(Powerup);
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
            for (Entity Powerup : this) {
                Mappers.transform.get(Powerup).isHidden = true;
            }
        } else if (size == 1) {
            Mappers.transform.get(this.peek()).isHidden = false;
        }

        if (e != null) {
            Mappers.transform.get(e).isHidden = false;
        }
    }
}