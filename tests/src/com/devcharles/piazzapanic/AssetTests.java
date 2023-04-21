package com.devcharles.piazzapanic;

import static org.junit.Assert.assertTrue;

import org.junit.Before;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class AssetTests implements BasicTest {

    @Override
    @Before
    public void initialize() throws Exception {
        // no initialization needed
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        // no constructor test needed
    }

    @Test
    public void testFoodAssetsExists() throws Exception {
        assertTrue("Check existance of food assets", Gdx.files.internal("v2/food.png").exists());
    }

    @Test
    public void testTilemapAssetsExists() throws Exception {
        assertTrue("Check existance of tmx file", Gdx.files.internal("v2/map.tmx").exists());
        assertTrue("Check existance of tilemap objects tileset", Gdx.files.internal("v2/objects.tsx").exists());
        assertTrue("Check existance of tilemap tileset_32 tileset", Gdx.files.internal("v2/tileset_32.tsx").exists());
    }

    @Test
    public void testTutorialAssetsExists() throws Exception {
        for (int i = 0; i < 10; i++) {
            assertTrue(String.format("Check existance of tutorial%d.png file", i),
                    Gdx.files.internal(String.format("tutorial%d.png", i)).exists());
        }
    }

    @Test
    public void testChefAssetsExists() throws Exception {
        for (int i = 1; i < 2; i++) {
            assertTrue(String.format("Check existance of chef %d.png file", i),
                    Gdx.files.internal(String.format("v2/chef/%d.png", i)).exists());
            assertTrue(String.format("Check existance of chef %d_holding.png file", i),
                    Gdx.files.internal(String.format("v2/chef/%d_holding.png", i)).exists());
            assertTrue(String.format("Check existance of chef %d_crate.png file", i),
                    Gdx.files.internal(String.format("v2/chef/%d_crate.png", i)).exists());
        }
    }

    @Test
    public void testCustomerAssetsExists() throws Exception {
        for (int i = 1; i < 15; i++) {
            assertTrue(String.format("Check existance of customer %d.png file", i),
                    Gdx.files.internal(String.format("v2/customer/%d.png", i)).exists());
            assertTrue(String.format("Check existance of customer %d_holding.png file", i),
                    Gdx.files.internal(String.format("v2/customer/%d_holding.png", i)).exists());
        }
    }
}