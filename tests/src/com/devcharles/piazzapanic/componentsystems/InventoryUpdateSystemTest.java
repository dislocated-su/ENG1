package com.devcharles.piazzapanic.componentsystems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.scene2d.Hud;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.Mappers;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class InventoryUpdateSystemTest {

  World world = new World(new Vector2(0, 0), true);
  PooledEngine engine = new PooledEngine();
  EntityFactory factory = new EntityFactory(engine, world);
  @Test
  public void testProcessEntity(){
    //Creating test variables
    InventoryUpdateSystem system = new InventoryUpdateSystem(mock(Hud.class));
    engine.addSystem(system);
    Entity cook1 = factory.createCook(0,0);
    ControllableComponent cook1Control = Mappers.controllable.get(cook1);
    cook1.add(engine.createComponent(PlayerComponent.class));
    Entity cook2 = factory.createCook(35, 29);
    ControllableComponent cook2Control = Mappers.controllable.get(cook2);
    Entity burger = factory.createFood(FoodType.burger);
    Entity tomato = factory.createFood(FoodType.tomato);

    //Only one cook has items test
    cook1Control.currentFood.pushItem(burger, cook1);
    cook1Control.currentFood.pushItem(tomato, cook1);
    cook1Control.currentFood.pushItem(burger, cook1);
    engine.update(1f);
    assertTrue("The array containing the food to be displayed should be the same size as the cook's carried ingredients",
        system.foods.length == 3);

    assertEquals("The first item in the array should be a burger",
        FoodType.burger, system.foods[0]);
    assertEquals("The second item in the array should be a tomato",
        FoodType.tomato, system.foods[1]);
    assertEquals("The third item in the array should be a burger",
        FoodType.burger, system.foods[2]);

    //Second cook also has items test
    cook2Control.currentFood.pushItem(tomato, cook2);
    engine.update(1f);

    assertTrue("The array size should not have changed as the other cook is not being controlled",
        system.foods.length == 3);

    assertEquals("The array should be the exact same",
        FoodType.burger, system.foods[0]);
    assertEquals("The array should be the exact same",
        FoodType.tomato, system.foods[1]);
    assertEquals("The array should be the exact same",
        FoodType.burger, system.foods[2]);

    //Switching player component tests
    PlayerComponent swap = Mappers.player.get(cook1);
    cook1.remove(PlayerComponent.class);
    cook2.add(swap);
    engine.update(1f);

    assertTrue("The array size should now be equal to the second cook's stack",
        system.foods.length == 1);

    assertEquals("The first item should be a tomato",
        FoodType.tomato, system.foods[0]);

  }

}
