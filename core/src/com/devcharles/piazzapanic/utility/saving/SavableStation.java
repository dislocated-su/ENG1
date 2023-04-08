package com.devcharles.piazzapanic.utility.saving;

import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.Station.StationType;
import java.util.ArrayList;
import java.util.Arrays;

public class SavableStation {

  int id;
  StationType type;
  ArrayList<SavableFood> food = new ArrayList<>(
      Arrays.asList(new SavableFood[]{null, null, null, null}));
  FoodType ingredient;
  public float prepModifier = 1f;
  public float chopModifier = 1f;

  public static SavableStation from(StationComponent station) {
    SavableStation savableStation = new SavableStation();
    savableStation.id = station.id;
    savableStation.type = station.type;
    for (int i = 0; i < savableStation.food.size(); i++) {
      savableStation.food.set(i, SavableFood.from(station.food.get(i)));
    }
    savableStation.ingredient = station.ingredient;
    savableStation.prepModifier = station.prepModifier;
    savableStation.chopModifier = station.chopModifier;
    return savableStation;
  }

  public StationComponent toStationComponent(EntityFactory factory) {
    StationComponent stationComponent = new StationComponent();
    stationComponent.id = id;
    stationComponent.interactingCook = null;
    stationComponent.type = type;

    for (int i = 0; i < food.size(); i++) {
      if (food.get(i) != null) {
        stationComponent.food.set(i, food.get(i).toEntity(factory));
      }
    }

    stationComponent.ingredient = ingredient;
    stationComponent.prepModifier = prepModifier;
    stationComponent.chopModifier = chopModifier;

    return stationComponent;
  }
}
