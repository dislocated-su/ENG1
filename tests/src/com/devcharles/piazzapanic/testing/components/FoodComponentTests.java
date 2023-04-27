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

    @Override
    @Before
    public void initialize() throws Exception {
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        FoodComponent c = new FoodComponent();
    }
}
