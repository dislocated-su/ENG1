package com.devcharles.piazzapanic.utility;

import com.devcharles.piazzapanic.utility.WalkAnimator.Direction;
import static com.devcharles.piazzapanic.utility.WalkAnimator.rotationToDirection;
import static org.junit.Assert.*;
import com.devcharles.piazzapanic.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class WalkAnimatorTest {

  @Test
  public void rotationToDirectionTest(){
    assertEquals("Direction should be right.",Direction.right,rotationToDirection(0));
    assertEquals("Direction should be up.",Direction.up,rotationToDirection(90));
    assertEquals("Direction should be down.",Direction.down,rotationToDirection(-120));
    assertEquals("Direction should be left after rounding up.",Direction.left,rotationToDirection(158));
    assertEquals("Direction should be up after rounding down.",Direction.up,rotationToDirection(157));
  }
}
