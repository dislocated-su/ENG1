package com.devcharles.piazzapanic.componentsystems;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.FoodStack;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.Station;
import com.devcharles.piazzapanic.utility.Station.StationType;
import com.devcharles.piazzapanic.GdxTestRunner;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class StationSystemTest {
  KeyboardInput kbInput = new KeyboardInput();
  World world = new World(new Vector2(0, 0), true);
  PooledEngine engine = new PooledEngine();
  EntityFactory factory = new EntityFactory(engine, world);

  @Test
  public void testProcessEntity(){
    StationSystem system = new StationSystem(kbInput, factory);
    engine.addSystem(system);
    engine.update(1f);

    Entity entity = engine.createEntity();

    StationType stationType = StationType.from(2);
    StationComponent stationComponent = engine.createComponent(StationComponent.class);
    stationComponent.type = stationType;
    stationComponent.interactingCook = factory.createCook(0,0);
    stationComponent.interactingCook.add(engine.createComponent(PlayerComponent.class));
    ControllableComponent cookComponent = Mappers.controllable.get(stationComponent.interactingCook);
    PlayerComponent playerComponent = Mappers.player.get(stationComponent.interactingCook);
    playerComponent.putDown = true;

    entity.add(stationComponent);
    engine.addEntity(entity);
    ArrayList<Entity> testExpected = new ArrayList<Entity>(Arrays.asList(new Entity[] { null, null, null, null }));
    engine.update(1f);
    assertEquals("If the chef tries to put food on a cooking station with an empty stack nothing should happen", testExpected, stationComponent.food );

    FoodType foodType = FoodType.from(2);
    Entity food = factory.createFood(foodType);
    testExpected.set(0, food);
    cookComponent.currentFood.pushItem(food, stationComponent.interactingCook);
    playerComponent.putDown = true;
    engine.update(1f);
    assertEquals("If the chef interacts with a cooking station which the correct ingredient, it should be accepted", testExpected, stationComponent.food );
    assertTrue("The food should be removed from the player stack", cookComponent.currentFood.size() == 0);


  }
}
