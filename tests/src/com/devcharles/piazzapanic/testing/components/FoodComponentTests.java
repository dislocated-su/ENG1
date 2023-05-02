package com.devcharles.piazzapanic.testing.components;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class FoodComponentTests implements BasicTest {

    private FoodComponent component;

    @Override
    @Before
    public void initialize() throws Exception {
        component = new FoodComponent();
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        FoodComponent c = new FoodComponent();
    }

    @Test
    public void getFoodTest() throws Exception {
        String[] strings = { "UnformedPatty", "FormedPatty", "GrilledPatty", "Buns", "ToastedBuns", "Burger", "Lettuce",
                "SlicedLettuce", "Tomato", "SlicedTomato", "Onion", "SlicedOnion", "Salad", "Potato",
                "ButterlessJacketPotato", "JacketPotato", "Butter", "Dough", "RolledDough", "TomatoDough",
                "TomatoCheeseDough", "Pizza", "TomatoPaste", "Cheese", "GratedCheese" };

        for (String str : strings) {
            FoodComponent.getFood(str);
        }
    }

    @Test
    public void toStringTest() throws Exception {
        FoodType[] foodTypes = { FoodType.unformedPatty, FoodType.formedPatty, FoodType.grilledPatty, FoodType.buns,
                FoodType.toastedBuns, FoodType.burger, FoodType.lettuce, FoodType.slicedLettuce, FoodType.tomato,
                FoodType.slicedTomato, FoodType.onion, FoodType.slicedOnion, FoodType.salad, FoodType.potato,
                FoodType.butterlessJacketPotato, FoodType.jacketPotato, FoodType.butter, FoodType.dough,
                FoodType.rolledDough, FoodType.tomatoDough, FoodType.tomatoCheeseDough, FoodType.pizza,
                FoodType.tomatoPaste, FoodType.cheese, FoodType.gratedCheese };

        for (FoodType food : foodTypes) {
            FoodComponent.toString(food);
        }

    }
}
