package com.devcharles.piazzapanic.components;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import com.devcharles.piazzapanic.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CookingComponentTest {

  @Test
  public void testReset(){
    CookingComponent test = new CookingComponent();
    test.timer.start();
    test.timer.setElapsed(5743);
    test.processed = true;

    test.reset();
    assertFalse("The timer should not be running after being reset",
        test.timer.isRunning());
    assertEquals("As the timer has reset, time elapsed should be zero",
        0, test.timer.getElapsed());
    assertFalse("Processed should also be set to false",
        test.processed);

  }
}
