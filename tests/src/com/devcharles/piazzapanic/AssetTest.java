package com.devcharles.piazzapanic;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class AssetTest {

  @Test
  public void testMainMenuImageExists() {
    assertTrue("This test will only pass when mainMenuImage.png exists",
        Gdx.files.internal("mainMenuImage.png").exists());
  }

  @Test
  public void testShouldFail() {
    fail("This should fail.");
  }
}
