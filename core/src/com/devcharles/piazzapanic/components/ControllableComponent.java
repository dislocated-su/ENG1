package com.devcharles.piazzapanic.components;

import com.badlogic.ashley.core.Component;
import com.devcharles.piazzapanic.utility.FoodStack;
import com.devcharles.piazzapanic.utility.PowerupStack;

public class ControllableComponent implements Component {
    public FoodStack currentFood = new FoodStack();
    public PowerupStack currentPowerup = new PowerupStack();
    
}
