package com.devcharles.piazzapanic.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.utility.Station.StationType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class StationTest {

  @Test
  public void testRecipeHashMaps() {
    //Grill station recipes tests
    FoodType testActual = Station.grillRecipes.get(FoodType.buns);
    assertEquals("Buns should be mapped to toasted buns",
        FoodType.toastedBuns, testActual);
    testActual = Station.grillRecipes.get(FoodType.formedPatty);
    assertEquals("Formed patties should be mapped to grilled patties",
        FoodType.grilledPatty, testActual);
    testActual = Station.grillRecipes.get(FoodType.unformedPatty);
    assertNull(
        "Any ingredient that isn't used in the grill station shouldn't be mapped to anything",
        testActual);

    //Cutting board recipes tests
    testActual = Station.cuttingBoardRecipes.get(FoodType.tomato);
    assertEquals("Tomatoes should be mapped to sliced tomatoes",
        FoodType.slicedTomato, testActual);
    testActual = Station.cuttingBoardRecipes.get(FoodType.lettuce);
    assertEquals("Lettuce should be mapped to sliced lettuce",
        FoodType.slicedLettuce, testActual);
    testActual = Station.cuttingBoardRecipes.get(FoodType.unformedPatty);
    assertEquals("Unformed patties should be mapped to formed patties",
        FoodType.formedPatty, testActual);
    testActual = Station.cuttingBoardRecipes.get(FoodType.onion);
    assertEquals("Onions should be mapped to sliced onions",
        FoodType.slicedOnion, testActual);
    testActual = Station.cuttingBoardRecipes.get(FoodType.burger);
    assertNull(
        "Any ingredient that isn't used in the cutting board shouldn't be mapped to anything",
        testActual);

    //Serve recipes tests
    Set<FoodType> testServeActual = new HashSet<>();
    testServeActual.add(FoodType.grilledPatty);
    testServeActual.add(FoodType.toastedBuns);

    testActual = Station.serveRecipes.get(testServeActual);
    assertEquals("Grilled patties with toasted buns should map to a burger",
        FoodType.burger, testActual);
    testServeActual.remove(FoodType.grilledPatty);
    testActual = Station.serveRecipes.get(testServeActual);

    assertNull("Invalid recipes shouldn't be mapped to anything",
        testActual);

    testServeActual.remove(FoodType.toastedBuns);

    testServeActual.add(FoodType.slicedOnion);
    testServeActual.add(FoodType.slicedLettuce);
    testServeActual.add(FoodType.slicedTomato);
    testActual = Station.serveRecipes.get(testServeActual);
    assertEquals("Sliced onion with sliced lettuce and sliced tomato should map to a salad",
        FoodType.salad, testActual);

    //Station to recipe map tests
    HashMap<FoodType, FoodType> recipeMapActual = Station.recipeMap.get(StationType.grill);
    assertEquals("The grill station should be mapped to the grill recipes",
        Station.grillRecipes, recipeMapActual);
    recipeMapActual = Station.recipeMap.get(StationType.cutting_board);
    assertEquals("The cutting board station should map to cutting board recipes",
        Station.cuttingBoardRecipes, recipeMapActual);
    recipeMapActual = Station.recipeMap.get(StationType.bin);
    assertNull("Stations with no recipes shouldn't be mapped to anything", recipeMapActual);

  }

  @Test
  public void testStationType() {
    int testStationTypeValue = StationType.oven.getValue();
    assertEquals("The oven station should have a value of one",
        1, testStationTypeValue);
    testStationTypeValue = StationType.grill.getValue();
    assertEquals("The grill station should have a value of two",
        2, testStationTypeValue);
    testStationTypeValue = StationType.cutting_board.getValue();
    assertEquals("The cutting board station should have a value of three",
        3, testStationTypeValue);
    testStationTypeValue = StationType.sink.getValue();
    assertEquals("The sink station should have a value of four",
        4, testStationTypeValue);
    testStationTypeValue = StationType.bin.getValue();
    assertEquals("The bin station should have a value of five",
        5, testStationTypeValue);
    testStationTypeValue = StationType.ingredient.getValue();
    assertEquals("The ingredient station should have a value of six",
        6, testStationTypeValue);
    testStationTypeValue = StationType.serve.getValue();
    assertEquals("The serve station should have a value of seven",
        7, testStationTypeValue);

    StationType testStationTypeMap = StationType.from(1);
    assertEquals("The number 1 should be mapped to the oven station type",
        StationType.oven, testStationTypeMap);
    testStationTypeMap = StationType.from(2);
    assertEquals("The number 2 should be mapped to the grill station type",
        StationType.grill, testStationTypeMap);
    testStationTypeMap = StationType.from(3);
    assertEquals("The number 3 should be mapped to the cutting board station type",
        StationType.cutting_board, testStationTypeMap);
    testStationTypeMap = StationType.from(4);
    assertEquals("The number 4 should be mapped to the sink station type",
        StationType.sink, testStationTypeMap);
    testStationTypeMap = StationType.from(5);
    assertEquals("The number 5 should be mapped to the bin station type",
        StationType.bin, testStationTypeMap);
    testStationTypeMap = StationType.from(6);
    assertEquals("The number 6 should be mapped to the ingredient station type",
        StationType.ingredient, testStationTypeMap);
    testStationTypeMap = StationType.from(7);
    assertEquals("The number 7 should be mapped to the serve station type",
        StationType.serve, testStationTypeMap);
  }
}
