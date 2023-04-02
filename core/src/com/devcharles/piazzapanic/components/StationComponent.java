package com.devcharles.piazzapanic.components;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.utility.Station.StationType;

public class StationComponent implements Component, Json.Serializable {

  public int id = 0;
  public Entity interactingCook = null;
  public StationType type;
  public ArrayList<Entity> food = new ArrayList<>(
      Arrays.asList(new Entity[]{null, null, null, null}));
  public FoodType ingredient = null;

  @Override
  public void write(Json json) {
    json.writeValue("id", id);
    json.writeValue("type", type);
    ArrayList<FoodType> foodTypes = new ArrayList<>(4);
    for (Entity entity : food) {
      if (entity == null) break;
      FoodComponent foodComponent = entity.getComponent(FoodComponent.class);
      foodTypes.add(foodComponent.type);
    }
    json.writeValue("foodTypes", foodTypes, ArrayList.class, FoodType.class);
    json.writeValue("ingredient", ingredient);
  }

  @Override
  public void read(Json json, JsonValue jsonData) {
    id = jsonData.getInt("id", id);
    type = json.readValue(StationType.class, jsonData.get("type"));
    ArrayList foodTypes = json.readValue(ArrayList.class, FoodType.class, new ArrayList(), jsonData.get("foodTypes"));
    for (int i = 0; i < food.size(); i++) {
      if (food.get(i) != null) {
        if (foodTypes.size() <= i) {
          food.get(i).getComponent(FoodComponent.class).type = (FoodType) foodTypes.get(i);
        } else {
          food.set(i, null);
        }
      }
    }
    ingredient = json.readValue(FoodType.class, jsonData.get("ingredient"));
  }
}
