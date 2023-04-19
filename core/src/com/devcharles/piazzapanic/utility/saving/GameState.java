package com.devcharles.piazzapanic.utility.saving;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.componentsystems.CustomerAISystem;
import com.devcharles.piazzapanic.componentsystems.PowerUpSystem;
import com.devcharles.piazzapanic.utility.Mappers;
import java.util.ArrayList;
import java.util.HashMap;

public class GameState {

  public static String SAVE_LOCATION = "save.json";

  private final Integer[] reputationAndMoney = {0, 0};
  private Integer customerTimer = 0;
  private int numCustomersServed = 0;
  private final HashMap<String, SavableStation> stations = new HashMap<>();
  private final ArrayList<SavableCook> cooks = new ArrayList<>();
  private SavableCustomerAISystem customerAISystem;

  private SavablePowerUpSystem powerUpSystem;

  public Integer getCustomerTimer() {
    return customerTimer;
  }

  public void setCustomerTimer(Integer customerTimer) {
    this.customerTimer = customerTimer;
  }

  public void setFromEngine(PooledEngine engine) {
    // Save stations
    for (Entity stationEntity : engine.getEntitiesFor(Family.all(StationComponent.class).get())) {
      StationComponent component = Mappers.station.get(stationEntity);
      stations.put(String.valueOf(component.id), SavableStation.from(component));
    }

    // Save cooks
    for (Entity cookEntity : engine.getEntitiesFor(
        Family.all(TransformComponent.class, ControllableComponent.class).get())) {
      SavableCook cook = SavableCook.from(cookEntity);
      cooks.add(cook);
    }

    // Save customers
    customerAISystem = SavableCustomerAISystem.from(engine.getSystem(CustomerAISystem.class));

    // Save power ups
    powerUpSystem = SavablePowerUpSystem.from(engine.getSystem(PowerUpSystem.class));
  }

  public HashMap<String, SavableStation> getStations() {
    return stations;
  }

  public ArrayList<SavableCook> getCooks() {
    return cooks;
  }

  public SavableCustomerAISystem getCustomerAISystem() {
    return customerAISystem;
  }

  public int getNumCustomersServed() {
    return numCustomersServed;
  }

  public void setNumCustomersServed(int numCustomersServed) {
    this.numCustomersServed = numCustomersServed;
  }

  public SavablePowerUpSystem getPowerUpSystem() {
    return powerUpSystem;
  }

  public Integer getReputation() {
    return reputationAndMoney[0];
  }

  public Integer getMoney() {
    return reputationAndMoney[1];
  }

  public void setReputation(int reputation) {
    reputationAndMoney[0] = reputation;
  }

  public void setMoney(int money) {
    reputationAndMoney[1] = money;
  }
}
