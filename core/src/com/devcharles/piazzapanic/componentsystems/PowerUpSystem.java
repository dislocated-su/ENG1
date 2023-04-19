package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.utility.GdxTimer;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.saving.SavablePowerUpSystem;

public class PowerUpSystem extends EntitySystem {

  private static final int MAX_SINGLE_POWER_UP = 5;

  private int numSpeedUp = 0;
  private static final float speedUpModifier = 1.2f;
  private int numPrepSpeed = 0;
  private static final float prepSpeedModifier = 1.2f;
  private int numChopSpeed = 0;
  private static final float chopSpeedModifier = 1.2f;
  private int numSalePrice = 0;
  private int numPatienceIncrease = 0;
  private static final float patienceModifier = 1.2f;

  public boolean isMaxSpeedUp() {
    return numSpeedUp == MAX_SINGLE_POWER_UP;
  }
  public void addSpeedUp() {
    if (numSpeedUp >= MAX_SINGLE_POWER_UP) {
      return;
    }
    numSpeedUp++;
    ImmutableArray<Entity> cooks = getEngine().getEntitiesFor(
        Family.all(ControllableComponent.class).get());
    for (Entity cook : cooks) {
      Mappers.controllable.get(cook).speedModifier *= speedUpModifier;
    }
  }

  public void removeSpeedUp() {
    if (numSpeedUp == 0) {
      return;
    }
    numSpeedUp--;
    ImmutableArray<Entity> cooks = getEngine().getEntitiesFor(
        Family.all(ControllableComponent.class).get());
    for (Entity cook : cooks) {
      Mappers.controllable.get(cook).speedModifier /= speedUpModifier;
    }
  }

  public boolean isMaxPrepSpeed() {
    return numPrepSpeed == MAX_SINGLE_POWER_UP;
  }
  public void addPrepSpeed() {
    if (numPrepSpeed >= MAX_SINGLE_POWER_UP) {
      return;
    }
    numPrepSpeed++;
    ImmutableArray<Entity> cooks = getEngine().getEntitiesFor(
        Family.all(StationComponent.class).get());
    for (Entity cook : cooks) {
      Mappers.station.get(cook).prepModifier *= prepSpeedModifier;
    }
  }

  public void removePrepSpeed() {
    if (numPrepSpeed == 0) {
      return;
    }
    numPrepSpeed--;
    ImmutableArray<Entity> cooks = getEngine().getEntitiesFor(
        Family.all(StationComponent.class).get());
    for (Entity cook : cooks) {
      Mappers.station.get(cook).prepModifier /= prepSpeedModifier;
    }
  }

  public boolean isMaxChopSpeed() {
    return numChopSpeed == MAX_SINGLE_POWER_UP;
  }
  public void addChopSpeed() {
    if (numChopSpeed >= MAX_SINGLE_POWER_UP) {
      return;
    }
    numChopSpeed++;
    ImmutableArray<Entity> cooks = getEngine().getEntitiesFor(
        Family.all(StationComponent.class).get());
    for (Entity cook : cooks) {
      Mappers.station.get(cook).chopModifier *= chopSpeedModifier;
    }
  }

  public void removeChopSpeed() {
    if (numChopSpeed == 0) {
      return;
    }
    numChopSpeed--;
    ImmutableArray<Entity> cooks = getEngine().getEntitiesFor(
        Family.all(StationComponent.class).get());
    for (Entity cook : cooks) {
      Mappers.station.get(cook).chopModifier /= chopSpeedModifier;
    }
  }

  public boolean isMaxSalePrice() {
    return numSalePrice == MAX_SINGLE_POWER_UP;
  }
  public void addSalePrice() {
    if (numSalePrice >= MAX_SINGLE_POWER_UP) {
      return;
    }
    CustomerAISystem aiSystem = getEngine().getSystem(CustomerAISystem.class);
    aiSystem.setIncomeModifier(aiSystem.getIncomeModifier() + 1);
    numSalePrice++;
  }

  public void removeSalePrice() {
    if (numSalePrice == 0) {
      return;
    }
    CustomerAISystem aiSystem = getEngine().getSystem(CustomerAISystem.class);
    aiSystem.setIncomeModifier(aiSystem.getIncomeModifier() - 1);
    numSalePrice--;
  }

  public boolean isMaxPatience() {
    return numPatienceIncrease == MAX_SINGLE_POWER_UP;
  }
  public void addPatience() {
    if (numPatienceIncrease >= MAX_SINGLE_POWER_UP) {
      return;
    }
    numPatienceIncrease++;

    ImmutableArray<Entity> customers = getEngine().getEntitiesFor(
        Family.all(CustomerComponent.class).get());
    for (Entity customer : customers) {
      GdxTimer timer = Mappers.customer.get(customer).timer;
      timer.setDelay((int) (timer.getDelay() * patienceModifier));
    }

    CustomerAISystem aiSystem = getEngine().getSystem(CustomerAISystem.class);
    if (aiSystem != null) {
      aiSystem.setPatienceModifier(aiSystem.getPatienceModifier() * patienceModifier);
    }
  }

  public void removePatience() {
    if (numPatienceIncrease == 0) {
      return;
    }

    ImmutableArray<Entity> customers = getEngine().getEntitiesFor(
        Family.all(CustomerComponent.class).get());
    for (Entity customer : customers) {
      GdxTimer timer = Mappers.customer.get(customer).timer;
      timer.setDelay((int) (timer.getDelay() / patienceModifier));
    }

    numPatienceIncrease--;
    CustomerAISystem aiSystem = getEngine().getSystem(CustomerAISystem.class);
    if (aiSystem != null) {
      aiSystem.setPatienceModifier(aiSystem.getPatienceModifier() / patienceModifier);
    }
  }

  public void loadFromSave(SavablePowerUpSystem savedSystem) {
    numSpeedUp = savedSystem.numSpeedUp;
    numPrepSpeed = savedSystem.numPrepSpeed;
    numChopSpeed = savedSystem.numChopSpeed;
    numSalePrice = savedSystem.numSalePrice;
    numPatienceIncrease = savedSystem.numPatienceIncrease;
  }

  public int getNumSpeedUp() {
    return numSpeedUp;
  }

  public int getNumPrepSpeed() {
    return numPrepSpeed;
  }

  public int getNumChopSpeed() {
    return numChopSpeed;
  }

  public int getNumSalePrice() {
    return numSalePrice;
  }

  public int getNumPatienceIncrease() {
    return numPatienceIncrease;
  }

}
