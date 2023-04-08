package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.utility.Mappers;

public class PowerUpSystem extends EntitySystem {

  private final int MAX_SINGLE_POWER_UP = 5;
  private int numSpeedUp = 0;
  private final float speedUpModifier = 1.5f;
  private int numPrepSpeed = 0;
  private final float prepSpeedModifier = 1.5f;
  private int numChopSpeed = 0;
  private final float chopSpeedModifier = 1.5f;
  private int numSalePrice = 0;
  private final float saleModifier = 1.5f;
  private int numPatienceIncrease = 0;
  private final float patienceModifier = 1.5f;

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

  public void addSalePrice() {

  }

  public void removeSalePrice() {

  }

  public void addPatience() {

  }

  public void removePatience() {

  }
}
