package com.devcharles.piazzapanic.utility.box2d;

import com.devcharles.piazzapanic.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.devcharles.piazzapanic.utility.box2d.CollisionCategory;

@RunWith(GdxTestRunner.class)
public class CollisionCategoryTest {

    @Test
    public void CollisionCategoryTest(){

        assertEquals("0x0001 should be returned for BOUNDARY", 0x0001 ,CollisionCategory.BOUNDARY.getValue() );
        assertEquals("0x0002 returned for NO_SHADOW_BOUNDARY ",0x0002 , CollisionCategory.NO_SHADOWBOUNDARY.getValue());
        assertEquals("0x0004 returned for ENTITY Category", 0x0004, CollisionCategory.ENTITY.getValue());
        assertEquals("0x0010 returned for LIGHTS", 0x0010, CollisionCategory.LIGHTS.getValue());

    }


}
