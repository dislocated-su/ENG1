package com.devcharles.piazzapanic.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.devcharles.piazzapanic.components.PowerUpComponent.PowerUpType;
import com.devcharles.piazzapanic.utility.FoodStack;

public class ControllableComponent implements Component {
    public FoodStack currentFood = new FoodStack();
    public ArrayList<PowerUpType> currentPowerup = new ArrayList<PowerUpType>();
    
}
