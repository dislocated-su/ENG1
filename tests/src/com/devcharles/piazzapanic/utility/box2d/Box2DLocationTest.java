package com.devcharles.piazzapanic.utility.box2d;

import static org.junit.Assert.*;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.devcharles.piazzapanic.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class Box2DLocationTest {

    @Test
    public void newLocationTest(){
        Box2dLocation location = new Box2dLocation(new Vector2(5,10),-1);
        Location<Vector2> newLocation = location.newLocation();
        assertEquals("Expect new location to have vector of (0,0).",new Vector2(0,0), newLocation.getPosition());
        assertEquals("Expect new location to have orientation of 0.",0, newLocation.getOrientation(),0);
    }

    @Test
    public void vectorToAngleTest(){
        Box2dLocation location = new Box2dLocation(new Vector2(),0);
        assertEquals("Expect calculated angle to be correct.",(float) -0.78539816,location.vectorToAngle(new Vector2(5,5)),0);
        assertEquals("Expect calculated angle to be correct with negative x.",(float) 1.10714872,location.vectorToAngle(new Vector2(-10,5)),0);
        assertEquals("Expect calculated angle to be correct with negative y.",(float) -1.9513027,location.vectorToAngle(new Vector2(25,-10)),0);
        assertEquals("Expect calculated angle to be correct with negative x and y.",(float) 1.735945,location.vectorToAngle(new Vector2(-30,-5)),0);
    }

    @Test
    public void angleToVectorTest(){
        Box2dLocation location = new Box2dLocation(new Vector2(),0);
        assertEquals("Expect calculated vector to be correct.",new Vector2((float) -0.70710678, (float) 0.70710678), location.angleToVector(new Vector2 (), (float) 0.78539816));
        assertEquals("Expect calculated vector to be correct with negative angle.", new Vector2((float) 0.70710678, (float) -0.70710678), location.angleToVector(new Vector2 (), (float) -2.35619449));
    }
}
