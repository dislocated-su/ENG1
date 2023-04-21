package com.devcharles.piazzapanic.testing.utility;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.Station.StationType;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

@RunWith(GdxTestRunner.class)
public class EntityFactoryTests implements BasicTest {

        /*
         * Tests for EntityFactory's methods.
         */

        private EntityFactory entityFactory;
        private PooledEngine engine;
        private World world;

        @Override
        @Before
        public void initialize() throws Exception {
                engine = new PooledEngine();
                world = new World(new Vector2(0, 0), true);
                entityFactory = new EntityFactory(engine, world);
                EntityFactory.cutFood(null);
        }

        @Override
        @Test
        public void constructorTest() throws Exception {
                PooledEngine e = new PooledEngine();
                World w = new World(new Vector2(0, 0), true);
                EntityFactory entFact = new EntityFactory(e, w);
        }

        @Test
        public void createCookTest() throws Exception {
                assertTrue("check createCook method returns an entity",
                                entityFactory.createCook(0, 0) instanceof Entity);

                Entity cook = entityFactory.createCook(0, 0);
                assertTrue("check createCook method adds the new cook entity to the engine",
                                engine.getEntities().contains(cook, true));
        }

        @Test
        public void createFoodTest() throws Exception {
                assertTrue("check createFood method returns an entity",
                                entityFactory.createFood(FoodType.burger) instanceof Entity);

                Entity buns = entityFactory.createFood(FoodType.buns);
                assertTrue("check createCook method adds the new food entity to the engine",
                                engine.getEntities().contains(buns, true));
        }

        @Test
        public void createStationTest() throws Exception {
                assertTrue("check createStation method returns an entity",
                                entityFactory.createStation(StationType.grill, new Vector2(0, 0),
                                                FoodType.grilledPatty, new Vector2(0, 0), false) instanceof Entity);

                Entity station = (entityFactory.createStation(StationType.grill, new Vector2(0, 0),
                                FoodType.grilledPatty, new Vector2(), false));
                assertTrue("check createStation method adds the new station entity to the engine",
                                engine.getEntities().contains(station, true));
        }

        @Test
        public void getFoodTextureTest() throws Exception {
                assertTrue("check getFoodTexture returns a TextureRegion",
                                EntityFactory.getFoodTexture(FoodType.lettuce) instanceof TextureRegion);
        }

        @Test
        public void createCustomerTest() throws Exception {
                assertTrue("check createCustomer returns an entity",
                                entityFactory.createCustomer(new Vector2(0, 0)) instanceof Entity);

                Entity customer = entityFactory.createCustomer(new Vector2(0, 0));
                assertTrue("check createCustomer adds the new customer to the engine",
                                engine.getEntities().contains(customer, true));
        }

}