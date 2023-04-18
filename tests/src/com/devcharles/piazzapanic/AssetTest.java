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
    assertTrue("This test will only pass when font-export.fnt exists",
        Gdx.files.internal("craftacular/raw/font-export.fnt").exists());
    assertTrue("This test will only pass when font-title-export.fnt exists",
        Gdx.files.internal("craftacular/raw/font-title-export.fnt").exists());

    for (int i = 0; i <= 10; i++) {
      assertTrue("This test will only pass when tutorial" + i + ".png exists",
          Gdx.files.internal("tutorial" + i + ".png").exists());
    }
    assertTrue("This test will only pass when recipe0.png exists",
        Gdx.files.internal("recipe0.png").exists());
    assertTrue("This test will only pass when recipe1.png exists",
        Gdx.files.internal("recipe1.png").exists());

    assertTrue("This test will only pass when droplet.png exists",
        Gdx.files.internal("droplet.png").exists());
    for (int i = 1; i < 16; i++) {
      assertTrue("This test will only pass when customer/" + i + ".png exists",
          Gdx.files.internal("v2/customer/" + i + ".png").exists());
      assertTrue("This test will only pass when customer/" + i + "_holding.png exists",
          Gdx.files.internal("v2/customer/" + i + "_holding.png").exists());
    }

    assertTrue("This test will only pass when chef/1.png exists",
        Gdx.files.internal("v2/chef/1.png").exists());
    assertTrue("This test will only pass when chef/1_crate.png exists",
        Gdx.files.internal("v2/chef/1_crate.png").exists());
    assertTrue("This test will only pass when chef/1_holding.png exists",
        Gdx.files.internal("v2/chef/1_holding.png").exists());
    assertTrue("This test will only pass when chef/2.png exists",
        Gdx.files.internal("v2/chef/2.png").exists());
    assertTrue("This test will only pass when chef/2_crate.png exists",
        Gdx.files.internal("v2/chef/2_crate.png").exists());
    assertTrue("This test will only pass when chef/2_holding.png exists",
        Gdx.files.internal("v2/chef/2_holding.png").exists());

    assertTrue("This test will only pass when food.png exists",
        Gdx.files.internal("v2/food.png").exists());
    assertTrue("This test will only pass when map.tmx exists",
        Gdx.files.internal("v2/map.tmx").exists());
  }
}
