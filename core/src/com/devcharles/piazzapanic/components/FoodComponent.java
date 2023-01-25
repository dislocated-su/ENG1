package com.devcharles.piazzapanic.components;

import com.badlogic.ashley.core.Component;

public class FoodComponent implements Component {
    // public boolean completed = false;
    public FoodType type;

    public enum FoodType {
        lettuce,
        slicedLettuce,
        tomato,
        slicedTomato,
        onion,
        unformedPatty,
        formedPatty,
        grilledPatty,
        buns,
        toastedBuns
    }
}
