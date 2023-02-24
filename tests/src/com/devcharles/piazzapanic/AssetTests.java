package com.devcharles.piazzapanic;

import static org.junit.Assert.assertTrue;
import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testDropletAssetExists() throws Exception {
        assertTrue("Check existance of droplet assets", Gdx.files.internal("v2/food.png").exists());
    }
}
