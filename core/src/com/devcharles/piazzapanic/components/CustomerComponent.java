package com.devcharles.piazzapanic.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;

public class CustomerComponent implements Component, Poolable {
    public FoodType order = null;
    public Entity interactingCook = null;
    public Entity food = null;

    @Override
    public void reset() {
        order = null;
        interactingCook = null;
        food = null;
    }
}
