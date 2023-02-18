package com.devcharles.piazzapanic.components;

import static org.junit.Assert.*;

import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CustomerComponentTest {

  @Test
  public void reset() {
    CustomerComponent testValue = new CustomerComponent();
    CustomerComponent defaultValue = new CustomerComponent();

    testValue.order = FoodType.unformedPatty;
    testValue.interactingCook = new Entity();
    testValue.food = new Entity();
    testValue.timer.start();
    testValue.timer.tick(1f);

    // The function to test
    testValue.reset();

    assertEquals("Expect order to be default.", testValue.order, defaultValue.order);
    assertEquals("Expect interactingCook to be default.", testValue.interactingCook,
        defaultValue.interactingCook);
    assertEquals("Expect food to be default.", testValue.food, defaultValue.food);

    assertEquals("Expect timer to have a time of zero.", testValue.timer.getElapsed(), 0);
    assertFalse("Expect timer to not be running.", testValue.timer.isRunning());
  }
}