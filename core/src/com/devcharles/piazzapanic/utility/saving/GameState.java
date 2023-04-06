package com.devcharles.piazzapanic.utility.saving;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.componentsystems.CustomerAISystem;
import com.devcharles.piazzapanic.utility.Mappers;
import java.util.ArrayList;
import java.util.HashMap;

public class GameState {

  private Integer customerTimer = 0;
  private HashMap<Integer, StationComponent> stations = new HashMap<>();
  private ArrayList<SavableCook> cooks = new ArrayList<>();

  private SavableCustomerAISystem customerAISystem;

  public Integer getCustomerTimer() {
    return customerTimer;
  }

  public void setCustomerTimer(Integer customerTimer) {
    this.customerTimer = customerTimer;
  }

  public void setFromEngine(PooledEngine engine) {
    // Save stations
    for (Entity stationEntity : engine.getEntitiesFor(Family.all(StationComponent.class).get())) {
      StationComponent component = stationEntity.getComponent(StationComponent.class);
      stations.put(component.id, component);
    }

    for (Entity cookEntity : engine.getEntitiesFor(
        Family.all(TransformComponent.class, ControllableComponent.class).get())) {
      SavableCook cook = new SavableCook();
      cook.setTransformComponent(Mappers.transform.get(cookEntity));
      cook.setSavableFoodStackFromEntities(Mappers.controllable.get(cookEntity).currentFood);
      cooks.add(cook);
    }

    customerAISystem = SavableCustomerAISystem.from(engine.getSystem(CustomerAISystem.class));
  }

  public HashMap<Integer, StationComponent> getStations() {
    return stations;
  }

  public ArrayList<SavableCook> getCooks() {
    return cooks;
  }
}
