package com.devcharles.piazzapanic.testing;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public interface BasicTest {

    @Test
    public void initialize() throws Exception;

    @Test
    public void constructorTest() throws Exception;

}
