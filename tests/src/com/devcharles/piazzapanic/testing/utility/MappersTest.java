package com.devcharles.piazzapanic.testing.utility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;
import com.devcharles.piazzapanic.utility.Mappers;

@RunWith(GdxTestRunner.class)
public class MappersTest implements BasicTest {

    @Override
    @Before
    public void initialize() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        Mappers mappers = new Mappers();
    }
}
