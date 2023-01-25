package com.devcharles.piazzapanic.components;

import com.badlogic.ashley.core.Component;

public class FoodComponent implements Component {
    public boolean completed = false;
    public FoodType type;

    public enum FoodType {
        lettuce,
        tomato,
        onion,
        patty,
        buns
    }
}
