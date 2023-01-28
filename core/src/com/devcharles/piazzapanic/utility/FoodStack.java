package com.devcharles.piazzapanic.utility;

import java.util.Stack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.ItemComponent;

public class FoodStack extends Stack<Entity> {

    private Engine engine;

    public void init(Engine e) {
        engine = e;
    }

    // Use this instead of push
    public Entity pushItem(Entity food, Entity cook) {
        ItemComponent item = engine.createComponent(ItemComponent.class);
        item.holderTransform = Mappers.transform.get(cook);
        food.add(item);
        return this.push(food);
    }

    @Override
    public Entity push(Entity food) {
        Entity e = super.push(food);
        setVisibility(this.size(), null);
        return e;
    }

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
