package com.devcharles.piazzapanic.utility.saving;

import com.devcharles.piazzapanic.componentsystems.PowerUpSystem;

public class SavablePowerUpSystem {

  public int numSpeedUp = 0;
  public int numPrepSpeed = 0;
  public int numChopSpeed = 0;
  public int numSalePrice = 0;
  public int numPatienceIncrease = 0;

  public static SavablePowerUpSystem from(PowerUpSystem system) {
    SavablePowerUpSystem savableSystem = new SavablePowerUpSystem();
    savableSystem.numSpeedUp = system.getNumSpeedUp();
    savableSystem.numPrepSpeed = system.getNumPrepSpeed();
    savableSystem.numChopSpeed = system.getNumChopSpeed();
    savableSystem.numSalePrice = system.getNumSalePrice();
    savableSystem.numPatienceIncrease = system.getNumPatienceIncrease();
    return savableSystem;
  }
}
