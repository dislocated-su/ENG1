package com.devcharles.piazzapanic.componentsystems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.PiazzaPanic;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.CookingComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.FoodStack;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.Station.StationType;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class StationSystemTest {

  KeyboardInput kbInput = new KeyboardInput();

  World world;
  PooledEngine engine = new PooledEngine();
  EntityFactory factory;

  @Before
  public void setup() {
    world  = new World(new Vector2(0, 0), true);
    engine = new PooledEngine();
    AssetManager manager = new AssetManager();
    manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    PiazzaPanic.loadAssets(manager);
    manager.finishLoading();
    factory = new EntityFactory(engine, world, manager);
  }

  @Test
  public void testProcessStation() {
    StationSystem system = new StationSystem(kbInput, factory);
    engine.addSystem(system);

    //creating grill station
    StationType stationType = StationType.from(2);
    StationComponent stationComponent = engine.createComponent(StationComponent.class);
    stationComponent.type = stationType;
    stationComponent.interactingCook = factory.createCook(0, 0);
    stationComponent.interactingCook.add(engine.createComponent(PlayerComponent.class));
    ControllableComponent cookComponent = Mappers.controllable.get(
        stationComponent.interactingCook);

    ArrayList<Entity> testExpected = new ArrayList<>(
        Arrays.asList(new Entity[]{null, null, null, null}));
    system.processStation(cookComponent, stationComponent);
    assertEquals(
        "If the chef tries to put food on a cooking station with an empty stack nothing should happen",
        testExpected, stationComponent.food);

    FoodType foodType = FoodType.from(2);
    Entity food = factory.createFood(foodType);
    testExpected.set(0, food);
    cookComponent.currentFood.pushItem(food, stationComponent.interactingCook);
    system.processStation(cookComponent, stationComponent);
    assertEquals(
        "If the chef interacts with a cooking station which the correct ingredient, it should be accepted",
        testExpected, stationComponent.food);
    assertEquals("The ingredient should be removed from the player stack", 0,
        cookComponent.currentFood.size());

    FoodType incorrectFoodType = FoodType.from(7);
    Entity incorrectFood = factory.createFood(incorrectFoodType);
    cookComponent.currentFood.pushItem(incorrectFood, stationComponent.interactingCook);
    system.processStation(cookComponent, stationComponent);
    assertEquals("The station should not accept incompatible ingredients", testExpected,
        stationComponent.food);
    assertEquals("The ingredient should not be removed from the player stack", 1,
        cookComponent.currentFood.size());
    cookComponent.currentFood.pop();

    stationComponent.food = new ArrayList<>(Arrays.asList(food, food, food, food));
    testExpected = new ArrayList<>(Arrays.asList(food, food, food, food));
    FoodType newFoodType = FoodType.from(4);
    Entity newFood = factory.createFood(newFoodType);
    cookComponent.currentFood.push(newFood);
    system.processStation(cookComponent, stationComponent);
    assertEquals("The station should not accept ingredients when full", testExpected,
        stationComponent.food);
    assertEquals("The ingredient should not be removed from the player stack", 1,
        cookComponent.currentFood.size());

    //creating a bin station
    StationType newStationType = StationType.from(5);
    StationComponent newStationComponent = engine.createComponent(StationComponent.class);
    stationComponent.type = newStationType;
    newStationComponent.interactingCook = stationComponent.interactingCook;
    testExpected = new ArrayList<>(Arrays.asList(new Entity[]{null, null, null, null}));
    system.processStation(cookComponent, newStationComponent);
    assertEquals(
        "Stations that do not process ingredients should not accept ingredients via this function",
        testExpected, newStationComponent.food);
    assertEquals("The ingredient should not be removed from the player stack", 1,
        cookComponent.currentFood.size());
  }

  @Test
  public void testInteractStation() {
    StationSystem system = new StationSystem(kbInput, factory);
    engine.addSystem(system);
    engine.update(1f);

    //creating grill station
    Entity entity = new Entity();
    StationType stationType = StationType.from(2);
    StationComponent stationComponent = engine.createComponent(StationComponent.class);
    stationComponent.type = stationType;
    stationComponent.interactingCook = factory.createCook(0, 0);
    stationComponent.interactingCook.add(engine.createComponent(PlayerComponent.class));
    ControllableComponent cookComponent = Mappers.controllable.get(
        stationComponent.interactingCook);
    entity.add(stationComponent);
    engine.addEntity(entity);

    //create patty and cook it
    FoodType foodType = FoodType.from(2);
    Entity food = factory.createFood(foodType);
    cookComponent.currentFood.pushItem(food, stationComponent.interactingCook);
    system.processStation(cookComponent, stationComponent);
    system.interactStation(stationComponent);
    assertFalse("The patty should not be processed until 5 seconds have passed",
        Mappers.cooking.get(stationComponent.food.get(0)).processed);

    engine.update(5.001f);
    system.interactStation(stationComponent);
    assertTrue("The patty should be processed after 5 seconds have passed",
        Mappers.cooking.get(stationComponent.food.get(0)).processed);

    //Multiple patties test
    stationComponent.food = new ArrayList<>(
        Arrays.asList(new Entity[]{null, null, null, null}));
    Entity firstFood = factory.createFood(foodType);
    Entity secondFood = factory.createFood(foodType);
    cookComponent.currentFood.pushItem(firstFood, stationComponent.interactingCook);
    system.processStation(cookComponent, stationComponent);
    cookComponent.currentFood.pushItem(secondFood, stationComponent.interactingCook);
    system.processStation(cookComponent, stationComponent);
    engine.update(5.001f);
    system.interactStation(stationComponent);
    assertTrue(
        "If multiple patties are being cooked and can be processed, only the first patty should be processed",
        Mappers.cooking.get(stationComponent.food.get(0)).processed);
    assertFalse("Second patty check", Mappers.cooking.get(stationComponent.food.get(1)).processed);

    //Multiple patties in different order test
    Entity testOrderFood1 = factory.createFood(foodType);
    cookComponent.currentFood.pushItem(testOrderFood1, stationComponent.interactingCook);

    Entity testOrderFood2 = factory.createFood(foodType);
    CookingComponent cooking = engine.createComponent(CookingComponent.class);
    cooking.timer.start();
    testOrderFood2.add(cooking);

    stationComponent.food = new ArrayList<>(
        Arrays.asList(null, testOrderFood2, null, null));
    engine.update(5.001f);
    system.processStation(cookComponent, stationComponent);
    system.interactStation(stationComponent);
    assertFalse(
        "If multiple patties are being cooked, only the first patty that can be processed should be processed",
        Mappers.cooking.get(stationComponent.food.get(0)).processed);
    assertTrue("Second patty check", Mappers.cooking.get(stationComponent.food.get(1)).processed);

    //Test ingredients without the cooking component should be ignored
    Entity noComponentFood = factory.createFood(foodType);
    stationComponent.food = new ArrayList<>(
        Arrays.asList(null, null, null, noComponentFood));
    system.interactStation(stationComponent);
    assertFalse("The ingredient should not have been given the cooking component",
        Mappers.cooking.has(stationComponent.food.get(3)));
  }

  @Test
  public void testProcessServe() {
    //Creating necessary variables
    StationSystem system = new StationSystem(kbInput, factory);
    engine.addSystem(system);
    Entity cook = factory.createCook(0, 0);
    ControllableComponent controllable = Mappers.controllable.get(cook);
    Entity toastedBuns = factory.createFood(FoodType.toastedBuns);
    Entity grilledPatty = factory.createFood(FoodType.grilledPatty);
    Entity slicedTomato = factory.createFood(FoodType.slicedTomato);
    Entity slicedLettuce = factory.createFood(FoodType.slicedLettuce);
    Entity slicedOnion = factory.createFood(FoodType.slicedOnion);

    //Test creation of a Burger
    controllable.currentFood.pushItem(toastedBuns, cook);
    controllable.currentFood.pushItem(grilledPatty, cook);
    system.processServe(cook);
    assertEquals("There should only be one ingredient left on the stack", 1,
        controllable.currentFood.size());
    FoodComponent actualResult = Mappers.food.get(controllable.currentFood.pop());
    assertEquals("A burger should be created when the correct ingredients are present",
        FoodType.burger, actualResult.type);

    //Test creation of a salad
    controllable.currentFood.pushItem(slicedLettuce, cook);
    controllable.currentFood.pushItem(slicedOnion, cook);
    controllable.currentFood.pushItem(slicedTomato, cook);
    system.processServe(cook);
    assertEquals("There should only be one ingredient left on the stack", 1,
        controllable.currentFood.size());
    actualResult = Mappers.food.get(controllable.currentFood.pop());
    assertEquals("A salad should be created when the correct ingredients are present",
        FoodType.salad, actualResult.type);

    //Test when all ingredients are on the stack
    toastedBuns = factory.createFood(FoodType.toastedBuns);
    grilledPatty = factory.createFood(FoodType.grilledPatty);
    slicedTomato = factory.createFood(FoodType.slicedTomato);
    slicedLettuce = factory.createFood(FoodType.slicedLettuce);
    slicedOnion = factory.createFood(FoodType.slicedOnion);

    controllable.currentFood.pushItem(toastedBuns, cook);
    controllable.currentFood.pushItem(grilledPatty, cook);
    controllable.currentFood.pushItem(slicedLettuce, cook);
    controllable.currentFood.pushItem(slicedOnion, cook);
    controllable.currentFood.pushItem(slicedTomato, cook);
    system.processServe(cook);
    assertEquals("There should be three ingredients left on the stack", 3,
        controllable.currentFood.size());
    actualResult = Mappers.food.get(controllable.currentFood.pop());
    assertEquals("Only a salad should be made as its ingredients are at the top of the stack",
        FoodType.salad, actualResult.type);

    system.processServe(cook);
    assertEquals("There should be only the burger left on the stack", 1,
        controllable.currentFood.size());
    actualResult = Mappers.food.get(controllable.currentFood.pop());
    assertEquals("A burger should be made afterwards", FoodType.burger, actualResult.type);

    //invalid combinations tests
    system.processServe(cook);
    assertEquals("If the stack is empty nothing should be added", 0,
        controllable.currentFood.size());
    toastedBuns = factory.createFood(FoodType.toastedBuns);
    grilledPatty = factory.createFood(FoodType.grilledPatty);
    slicedTomato = factory.createFood(FoodType.slicedTomato);
    slicedLettuce = factory.createFood(FoodType.slicedLettuce);
    slicedOnion = factory.createFood(FoodType.slicedOnion);

    controllable.currentFood.pushItem(toastedBuns, cook);
    system.processServe(cook);
    assertEquals("If the stack is less than 2 nothing should change", 1,
        controllable.currentFood.size());
    assertEquals("The ingredient in the stack should still be the same", FoodType.toastedBuns,
        Mappers.food.get(controllable.currentFood.peek()).type);

    controllable.currentFood.pushItem(slicedOnion, cook);
    controllable.currentFood.pushItem(grilledPatty, cook);
    controllable.currentFood.pushItem(slicedLettuce, cook);
    controllable.currentFood.pushItem(slicedTomato, cook);
    system.processServe(cook);
    actualResult = Mappers.food.get(controllable.currentFood.pop());
    assertEquals("As the top 3 ingredients cannot create a salad or burger, nothing should happen",
        FoodType.slicedTomato, actualResult.type);
  }

  @Test
  public void testTryServe() {
    //Create test variables
    StationSystem system = new StationSystem(kbInput, factory);
    engine.addSystem(system);
    Entity cook = factory.createCook(0, 0);
    ControllableComponent controllable = Mappers.controllable.get(cook);

    Entity toastedBuns = factory.createFood(FoodType.toastedBuns);
    Entity grilledPatty = factory.createFood(FoodType.grilledPatty);
    Entity slicedTomato = factory.createFood(FoodType.slicedTomato);
    Entity slicedLettuce = factory.createFood(FoodType.slicedLettuce);
    Entity slicedOnion = factory.createFood(FoodType.slicedOnion);

    //Test invalid counts
    controllable.currentFood.pushItem(slicedLettuce, cook);
    controllable.currentFood.pushItem(toastedBuns, cook);
    controllable.currentFood.pushItem(grilledPatty, cook);
    FoodType result = system.tryServe(controllable, 0);
    assertNull("Should return null as you can't create a food item with 0 ingredients", result);

    result = system.tryServe(controllable, 1);
    assertNull("Should return null as you can't create a food item with 1 ingredients", result);

    result = system.tryServe(controllable, 3);
    assertNull("Should return null as too many ingredients are passed in to make a burger", result);

    result = system.tryServe(controllable, -1);
    assertNull("Should return null as you cannot have -1 ingredients", result);

    //Test valid burger combination
    result = system.tryServe(controllable, 2);
    assertEquals("A burger should be created", FoodType.burger, result);

    //creating a new FoodStack requires passing the engine via the init function
    controllable.currentFood = new FoodStack();
    controllable.currentFood.init(engine);

    controllable.currentFood.pushItem(grilledPatty, cook);
    controllable.currentFood.pushItem(toastedBuns, cook);
    result = system.tryServe(controllable, 2);
    assertEquals("A burger should be created no matter what order the valid ingredients are in",
        FoodType.burger, result);

    //Salad tests
    controllable.currentFood = new FoodStack();
    controllable.currentFood.init(engine);

    controllable.currentFood.pushItem(toastedBuns, cook);
    controllable.currentFood.pushItem(slicedLettuce, cook);
    controllable.currentFood.pushItem(slicedOnion, cook);
    controllable.currentFood.pushItem(slicedTomato, cook);

    result = system.tryServe(controllable, 4);
    assertNull("As the count now includes the toasted buns, result should return null", result);

    result = system.tryServe(controllable, 2);
    assertNull(
        "As the count does not include all of the required ingredients, result should be null",
        result);

    result = system.tryServe(controllable, 3);
    assertEquals("A salad should be created if the correct ingredients are present", FoodType.salad,
        result);

    //Edge cases test
    controllable.currentFood = new FoodStack();
    controllable.currentFood.init(engine);

    controllable.currentFood.pushItem(toastedBuns, cook);
    controllable.currentFood.pushItem(grilledPatty, cook);
    result = system.tryServe(controllable, 4);
    assertEquals(
        "Even though the count is greater than 2, a burger should still be made as the rest of the stack is null",
        FoodType.burger, result);

    controllable.currentFood = new FoodStack();
    controllable.currentFood.init(engine);

    controllable.currentFood.pushItem(slicedLettuce, cook);
    controllable.currentFood.pushItem(slicedOnion, cook);
    controllable.currentFood.pushItem(slicedTomato, cook);
    result = system.tryServe(controllable, 50);
    assertEquals(
        "Even though the count is greater than 3, a salad should still be made as the rest of the stack is null",
        FoodType.salad, result);

  }

  @Test
  public void processBinTest() {
    //Create test variables
    StationSystem system = new StationSystem(kbInput, factory);
    engine.addSystem(system);
    Entity cook = factory.createCook(0, 0);
    ControllableComponent controllable = Mappers.controllable.get(cook);

    Entity toastedBuns = factory.createFood(FoodType.toastedBuns);
    controllable.currentFood.pushItem(toastedBuns, cook);
    system.processBin(controllable);
    assertEquals("The stack should be empty", 0, controllable.currentFood.size());

    system.processBin(controllable);
    assertEquals("Nothing should happen if the stack is already empty", 0,
        controllable.currentFood.size());

    toastedBuns = factory.createFood(FoodType.toastedBuns);
    Entity lettuce = factory.createFood(FoodType.lettuce);

    controllable.currentFood.pushItem(toastedBuns, cook);
    controllable.currentFood.pushItem(lettuce, cook);
    system.processBin(controllable);
    assertEquals("Only the top item is removed", 1, controllable.currentFood.size());
    FoodComponent actualResult = Mappers.food.get(controllable.currentFood.pop());
    assertEquals("The remaining ingredient should be toasted buns", FoodType.toastedBuns,
        actualResult.type);
  }

  @Test
  public void stationPickupTest() {
    //Create test variables
    StationSystem system = new StationSystem(kbInput, factory);
    engine.addSystem(system);
    Entity cook = factory.createCook(0, 0);
    ControllableComponent controllable = Mappers.controllable.get(cook);

    Entity grillStation = factory.createStation(0, StationType.grill, new Vector2(0, 0), null);
    StationComponent grillComponent = Mappers.station.get(grillStation);
    grillComponent.interactingCook = cook;

    Entity chopStation = factory.createStation(1, StationType.cutting_board, new Vector2(0, 0),
        null);
    StationComponent chopComponent = Mappers.station.get(chopStation);
    chopComponent.interactingCook = cook;

    Entity patty = factory.createFood(FoodType.formedPatty);
    Entity lettuce = factory.createFood(FoodType.lettuce);
    controllable.currentFood.pushItem(lettuce, cook);
    controllable.currentFood.pushItem(patty, cook);
    system.processStation(controllable, grillComponent);
    system.processStation(controllable, chopComponent);

    system.stationPickup(grillComponent, controllable);
    assertEquals("As there is a cooking timer attached to the patty, it should not be picked up", 0,
        controllable.currentFood.size());
    assertEquals("The patty should still be on the food stack", FoodType.formedPatty,
        Mappers.food.get(grillComponent.food.get(0)).type);
    system.stationPickup(chopComponent, controllable);
    assertEquals("The same should be true for the lettuce", 0, controllable.currentFood.size());

    grillComponent.food.get(0).remove(CookingComponent.class);
    chopComponent.food.get(0).remove(CookingComponent.class);
    system.stationPickup(grillComponent, controllable);
    assertEquals("As the cooking component is removed, the patty should be picked up",
        FoodType.formedPatty, Mappers.food.get(controllable.currentFood.pop()).type);
    assertNull("The patty in the station component should be removed", grillComponent.food.get(0));

    system.stationPickup(chopComponent, controllable);
    assertEquals("As the cooking component is removed, the lettuce should be picked up",
        FoodType.lettuce, Mappers.food.get(controllable.currentFood.pop()).type);
    assertNull("The lettuce in the station component should be removed", chopComponent.food.get(0));

    system.stationPickup(grillComponent, controllable);
    assertEquals("All null values should be ignored and therefore nothing should be picked up", 0,
        controllable.currentFood.size());
    assertNull("The value of the first item in the station food array should still be null",
        grillComponent.food.get(0));

    chopComponent.food.add(1, lettuce);
    chopComponent.food.add(3, lettuce);
    system.stationPickup(chopComponent, controllable);
    assertEquals("Only one ingredient should be picked up", 1, controllable.currentFood.size());
    assertEquals("And that ingredient should be lettuce", FoodType.lettuce,
        Mappers.food.get(controllable.currentFood.pop()).type);
    assertNull("The closest lettuce in the food array should now be null",
        chopComponent.food.get(1));
    assertEquals("The second lettuce entity should still be in the array", FoodType.lettuce,
        Mappers.food.get(chopComponent.food.get(3)).type);

  }

  @Test
  public void stationTickTest() {
    //Creating test variables
    StationSystem system = new StationSystem(kbInput, factory);
    engine.addSystem(system);
    Entity cook = factory.createCook(0, 0);
    ControllableComponent controllable = Mappers.controllable.get(cook);

    Entity chopStation = factory.createStation(0, StationType.cutting_board, new Vector2(0, 0),
        null);
    StationComponent chopComponent = Mappers.station.get(chopStation);

    Entity grillStation = factory.createStation(1, StationType.grill, new Vector2(0, 0), null);
    StationComponent grillComponent = Mappers.station.get(grillStation);

    Entity buns = factory.createFood(FoodType.buns);
    Entity tomato = factory.createFood(FoodType.tomato);

    controllable.currentFood.pushItem(buns, cook);
    controllable.currentFood.pushItem(tomato, cook);
    system.processStation(controllable, chopComponent);
    system.processStation(controllable, grillComponent);

    //NOTE: For these sets of tests I will be using the engine.update() function rather than calling
    //the function itself as it works in tandem with the update() function.

    engine.update(5.001f);
    CookingComponent tomatoTimeCheck = Mappers.cooking.get(chopComponent.food.get(0));
    CookingComponent bunsTimeCheck = Mappers.cooking.get(grillComponent.food.get(0));
    assertEquals("The timer for the tomato should not be changed as there is no interacting cook",
        0, tomatoTimeCheck.timer.getElapsed());
    assertEquals("This should not be the case for the buns", 5001,
        bunsTimeCheck.timer.getElapsed());
    assertEquals("The buns should not be turned into toasted buns as it has not been processed",
        FoodType.buns, Mappers.food.get(grillComponent.food.get(0)).type);
    assertTrue(
        "A tint component should have been added to the buns as enough time has passed for it to be processed",
        Mappers.tint.has(grillComponent.food.get(0)));

    chopComponent.interactingCook = cook;
    engine.update(5.001f);
    assertEquals("The timer for the tomato should now be changed as there is an interacting cook",
        5001, tomatoTimeCheck.timer.getElapsed());
    assertEquals(
        "The tomato should not be turned into a sliced tomato as it has not been processed",
        FoodType.tomato, Mappers.food.get(chopComponent.food.get(0)).type);
    assertTrue(
        "However, the tomato should now be ready to processed so a tintComponent should be added",
        Mappers.tint.has(chopComponent.food.get(0)));

    system.interactStation(grillComponent);
    system.interactStation(chopComponent);
    assertFalse("The tint component should now be removed",
        Mappers.tint.has(grillComponent.food.get(0)));
    assertFalse("Same for the tomato", Mappers.tint.has(chopComponent.food.get(0)));

    engine.update(5.001f);
    assertEquals("The buns should now be turned into toasted buns as it has been processed",
        FoodType.toastedBuns, Mappers.food.get(grillComponent.food.get(0)).type);
    assertFalse("The cooking component should be removed",
        Mappers.cooking.has(grillComponent.food.get(0)));

    assertEquals("The tomato should now be turned into sliced tomato as it has been processed",
        FoodType.slicedTomato, Mappers.food.get(chopComponent.food.get(0)).type);
    assertFalse("The cooking component should be removed",
        Mappers.cooking.has(chopComponent.food.get(0)));

    //Tests for multiple ingredients on the same station
    Entity formedPatty = factory.createFood(FoodType.formedPatty);
    Entity buns2 = factory.createFood(FoodType.buns);
    controllable.currentFood.pushItem(formedPatty, cook);
    controllable.currentFood.pushItem(buns2, cook);

    system.processStation(controllable, grillComponent);
    system.processStation(controllable, grillComponent);

    engine.update(5.001f);
    CookingComponent pattyTimeCheck = Mappers.cooking.get(grillComponent.food.get(2));
    bunsTimeCheck = Mappers.cooking.get(grillComponent.food.get(1));
    assertEquals("The buns should have the correct time elapsed", 5001,
        bunsTimeCheck.timer.getElapsed());
    assertEquals("The buns should not be turned into toasted buns as it has not been processed",
        FoodType.buns, Mappers.food.get(grillComponent.food.get(1)).type);
    assertTrue(
        "A tint component should have been added to the buns as enough time has passed for it to be processed",
        Mappers.tint.has(grillComponent.food.get(1)));

    assertEquals("The patty should have the correct time elapsed", 5001,
        pattyTimeCheck.timer.getElapsed());
    assertEquals("The patty should not be turned into a grilled patty as it has not been processed",
        FoodType.formedPatty, Mappers.food.get(grillComponent.food.get(2)).type);
    assertTrue(
        "A tint component should have been added to the patty as enough time has passed for it to be processed",
        Mappers.tint.has(grillComponent.food.get(2)));

    system.interactStation(grillComponent);
    system.interactStation(grillComponent);
    engine.update(5.001f);
    assertEquals("The buns should now be turned into toasted buns as it has been processed",
        FoodType.toastedBuns, Mappers.food.get(grillComponent.food.get(1)).type);
    assertEquals("The patty should now be turned into a grilled patty as it has been processed",
        FoodType.grilledPatty, Mappers.food.get(grillComponent.food.get(2)).type);

  }

  @Test
  public void processEntityTest() {
    //NOTE: This test will only be testing the ingredient station as the rest of the functions have been
    //covered by the previous tests

    //Creating test variables
    StationSystem system = new StationSystem(kbInput, factory);
    engine.addSystem(system);
    Entity cook = factory.createCook(0, 0);
    ControllableComponent controllable = Mappers.controllable.get(cook);
    PlayerComponent player = engine.createComponent(PlayerComponent.class);
    cook.add(player);

    Entity ingredientStation = factory.createStation(0, StationType.ingredient, new Vector2(0, 0),
        FoodType.buns);
    StationComponent ingredientComponent = Mappers.station.get(ingredientStation);
    ingredientComponent.interactingCook = cook;

    //Test pick up
    player.pickUp = true;
    engine.update(1f);
    assertEquals("One ingredient should be picked up by the player", 1,
        controllable.currentFood.size());
    assertEquals("And that ingredient should be buns", FoodType.buns,
        Mappers.food.get(controllable.currentFood.pop()).type);
    ingredientComponent.ingredient = FoodType.onion;
    player.pickUp = true;
    engine.update(1f);
    assertEquals("Same tests but with a different ingredient type", 1,
        controllable.currentFood.size());
    assertEquals("Ingredient should be an onion", FoodType.onion,
        Mappers.food.get(controllable.currentFood.pop()).type);

    //Test put down
    ingredientComponent.ingredient = FoodType.tomato;
    player.putDown = true;
    engine.update(1f);
    assertEquals("Even though the player is putting down, an item should be added to the stack", 1,
        controllable.currentFood.size());
    assertEquals("The ingredient should be a tomato", FoodType.tomato,
        Mappers.food.get(controllable.currentFood.pop()).type);
    ingredientComponent.ingredient = FoodType.unformedPatty;
    player.pickUp = true;
    engine.update(1f);
    assertEquals("Same tests but with a different ingredient type", 1,
        controllable.currentFood.size());
    assertEquals("Ingredient should be a unformed patty", FoodType.unformedPatty,
        Mappers.food.get(controllable.currentFood.pop()).type);

    //Testing when the player isn't picking up or putting down
    engine.update(1f);
    assertEquals("The stack should be empty", 0, controllable.currentFood.size());


  }
}
