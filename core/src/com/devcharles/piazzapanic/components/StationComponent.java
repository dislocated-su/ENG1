package com.devcharles.piazzapanic.components;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.utility.Station.StationType;

public class StationComponent implements Component {

  public int id = 0;
  public Entity interactingCook = null;
  public StationType type;
  public ArrayList<Entity> food = new ArrayList<>(
      Arrays.asList(new Entity[]{null, null, null, null}));
  public FoodType ingredient = null;
}
