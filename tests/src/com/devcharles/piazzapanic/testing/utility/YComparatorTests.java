package com.devcharles.piazzapanic.testing.utility;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.testing.BasicTest;
import com.devcharles.piazzapanic.testing.GdxTestRunner;
import com.devcharles.piazzapanic.utility.YComparator;

@RunWith(GdxTestRunner.class)
public class YComparatorTests implements BasicTest {

    @Override
    @Before
    public void initialize() throws Exception {
    }

    @Override
    @Test
    public void constructorTest() throws Exception {
        YComparator comparator = new YComparator();
    }
}
