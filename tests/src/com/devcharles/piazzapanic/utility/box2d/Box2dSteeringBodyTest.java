package com.devcharles.piazzapanic.utility.box2d;

import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.Test;

public class Box2dSteeringBodyTest {

  @Test
  public void testVectorToAngle() {
    World world = new World(Vector2.Zero, true);
    BodyDef def = new BodyDef();
    Body body = world.createBody(def);
    Box2dSteeringBody steeringBody = new Box2dSteeringBody(body, true, 1f);
    assertEquals("Expect the angle to be 0.", 0, steeringBody.vectorToAngle(new Vector2(0, 2)),
        0.001f);
    assertEquals("Expect the angle to turn to be positive -90 degrees", -Math.PI / 2f,
        steeringBody.vectorToAngle(new Vector2(2, 0)), 0.001f);
    assertEquals("Expect the angle to turn to be positive 90 degrees", Math.PI / 2f,
        steeringBody.vectorToAngle(new Vector2(-2, 0)), 0.001f);
    assertEquals("Expect the angle to be negative PI as it would turn right for positive 0.",
        -Math.PI, steeringBody.vectorToAngle(new Vector2(0f, -2)), 0.001f);
  }

  @Test
  public void testAngleToVector() {
    World world = new World(Vector2.Zero, true);
    BodyDef def = new BodyDef();
    Body body = world.createBody(def);
    Box2dSteeringBody steeringBody = new Box2dSteeringBody(body, true, 1f);
    Vector2 resultVector = new Vector2();
    steeringBody.angleToVector(resultVector, -0f);
    assertEquals("Expect the vector x to be 0.", 0f, resultVector.x, 0.0001f);
    assertEquals("Expect the vector y to be 1.", 1f, resultVector.y, 0.0001f);

    steeringBody.angleToVector(resultVector, (float) (-Math.PI / 2f));
    assertEquals("Expect the vector x to be 1.", 1f, resultVector.x, 0.0001f);
    assertEquals("Expect the vector y to be 0.", 0f, resultVector.y, 0.0001f);

    steeringBody.angleToVector(resultVector, (float) (Math.PI / 2f));
    assertEquals("Expect the vector x to be -1.", -1f, resultVector.x, 0.0001f);
    assertEquals("Expect the vector y to be 0.", 0f, resultVector.y, 0.0001f);

    steeringBody.angleToVector(resultVector, (float) -Math.PI);
    assertEquals("Expect the vector x to be 0.", 0f, resultVector.x, 0.0001f);
    assertEquals("Expect the vector y to be -1.", -1f, resultVector.y, 0.0001f);
  }

  @Test
  public void applySteering() {
    // TODO: test this. Relies up updating steeringOutput.
  }

  @Test(expected = UnsupportedOperationException.class)
  public void setZeroLinearSpeedThreshold() {
    World world = new World(Vector2.Zero, true);
    BodyDef def = new BodyDef();
    Body body = world.createBody(def);
    Box2dSteeringBody steeringBody = new Box2dSteeringBody(body, true, 1f);
    steeringBody.setZeroLinearSpeedThreshold(0f);
  }
}