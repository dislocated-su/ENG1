package com.devcharles.piazzapanic.components;

import com.badlogic.ashley.core.Engine;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.utility.Station.StationType;

public class StationComponent implements Component {

  public int id = 0;
  public float prepModifier = 1f;
  public float chopModifier = 1f;
  public Entity interactingCook = null;
  public StationType type;
  public ArrayList<Entity> food = new ArrayList<>(
      Arrays.asList(new Entity[]{null, null, null, null}));
  public FoodType ingredient = null;

  public void copyValues(StationComponent otherStation, Engine engine) {
    id = otherStation.id;
    interactingCook = otherStation.interactingCook;
    type = otherStation.type;
    for (int i = 0; i < food.size(); i++) {
      Entity foodEntity = food.get(i);
      if (foodEntity != null) {
        engine.removeEntity(foodEntity);
      }
      food.set(i, otherStation.food.get(i));
    }
    ingredient = otherStation.ingredient;
  }
}
