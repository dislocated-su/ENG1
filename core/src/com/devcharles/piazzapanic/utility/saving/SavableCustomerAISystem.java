package com.devcharles.piazzapanic.utility.saving;

import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.componentsystems.CustomerAISystem;
import java.util.ArrayList;
import java.util.Map;

public class SavableCustomerAISystem {

  Map<Integer, Boolean> objectiveTaken;
  SavableTimer spawnTimer;
  int totalCustomers;
  boolean firstSpawn;
  int numQueuedCustomers;
  ArrayList<ArrayList<SavableCustomer>> customers;

  public static SavableCustomerAISystem from(CustomerAISystem system) {
    SavableCustomerAISystem savableSystem = new SavableCustomerAISystem();
    savableSystem.objectiveTaken = system.getObjectiveTaken();
    savableSystem.spawnTimer = SavableTimer.from(system.getSpawnTimer());
    savableSystem.totalCustomers = system.getTotalCustomers();
    savableSystem.firstSpawn = system.isFirstSpawn();
    savableSystem.numQueuedCustomers = system.getNumQueuedCustomers();

    ArrayList<ArrayList<Entity>> customers = system.getCustomers();
    savableSystem.customers = new ArrayList<>(customers.size());
    for (ArrayList<Entity> group : customers) {
      ArrayList<SavableCustomer> savableGroup = new ArrayList<>(3);
      for (Entity customer : group) {
        savableGroup.add(SavableCustomer.from(customer));
      }
      savableSystem.customers.add(savableGroup);
    }
    return savableSystem;
  }
}
