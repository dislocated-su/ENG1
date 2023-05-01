package com.devcharles.piazzapanic.testing.utility.box2d;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;
import com.devcharles.piazzapanic.utility.box2d.MapBodyBuilder;

@RunWith(GdxTestRunner.class)
public class MapBodyBuilderTests implements BasicTest {

    @Override
    @Before
    public void initialize() throws Exception {
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        MapBodyBuilder builder = new MapBodyBuilder();
    }
}
