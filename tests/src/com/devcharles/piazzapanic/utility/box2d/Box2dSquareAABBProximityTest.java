package com.devcharles.piazzapanic.utility.box2d;

import static org.junit.Assert.*;

import com.badlogic.gdx.ai.steer.behaviors.Separation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.utility.box2d.Box2dSquareAABBProximity.AABB;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class Box2dSquareAABBProximityTest {

  private static class ProximitySteerable {

    public Box2dRadiusProximity ownerProximity;
    public Body otherBody;
    public Fixture otherFixture;

    ProximitySteerable() {
      World world = new World(new Vector2(0, 0), true);
      BodyDef def = new BodyDef();
      FixtureDef fixtureDef = new FixtureDef();
      CircleShape circle = new CircleShape();
      circle.setRadius(0.5f);
      fixtureDef.shape = circle;

      Body ownerBody = world.createBody(def);
      Box2dSteeringBody ownerSteeringBody = new Box2dSteeringBody(ownerBody, true, 2f);
      ownerBody.setTransform(0, 0, 0);
      ownerProximity = new Box2dRadiusProximity(ownerSteeringBody, world, 2f);

      otherBody = world.createBody(def);
      otherFixture = otherBody.createFixture(fixtureDef);
    }
  }


  @Test
  public void testGetAndSetOwner() {
    World world = new World(new Vector2(0, 0), true);
    BodyDef def = new BodyDef();
    Body ownerBody = world.createBody(def);
    Box2dSteeringBody ownerSteeringBody = new Box2dSteeringBody(ownerBody, true, 2f);
    Box2dSquareAABBProximity proximity = new Box2dSquareAABBProximity(ownerSteeringBody, world, 2f);

    assertEquals("Initial owner should be the constructor parameter owner.", ownerSteeringBody,
        proximity.getOwner());

    proximity.setOwner(null);
    assertNull("Owner should become null after setting it to null.", proximity.getOwner());

    proximity.setOwner(ownerSteeringBody);
    assertEquals("Owner should be the new value set.", ownerSteeringBody, proximity.getOwner());
  }

  @Test
  public void testGetAndSetWorld() {
    World world = new World(new Vector2(0, 0), true);
    BodyDef def = new BodyDef();
    Body ownerBody = world.createBody(def);
    Box2dSteeringBody ownerSteeringBody = new Box2dSteeringBody(ownerBody, true, 2f);
    Box2dSquareAABBProximity proximity = new Box2dSquareAABBProximity(ownerSteeringBody, world, 2f);

    assertEquals("Initial world should be the constructor parameter world.", proximity.getWorld(),
        world);

    proximity.setWorld(null);
    assertNull("World should become null after setting it to null.", proximity.getWorld());

    proximity.setWorld(world);
    assertEquals("World should be the new value set.", world, proximity.getWorld());
  }

  @Test
  public void testGetAndSetDetectionRadius() {
    World world = new World(new Vector2(0, 0), true);
    BodyDef def = new BodyDef();
    Body ownerBody = world.createBody(def);
    Box2dSteeringBody ownerSteeringBody = new Box2dSteeringBody(ownerBody, true, 2f);
    Box2dSquareAABBProximity proximity = new Box2dSquareAABBProximity(ownerSteeringBody, world, 2f);

    assertEquals("Initial detection radius should be 2 as set in the constructor.",
        2f, proximity.getDetectionRadius(), 0.001f);

    proximity.setDetectionRadius(0f);
    assertEquals("The detection radius should be 0.", 0f, proximity.getDetectionRadius(), 0.001f);

    proximity.setDetectionRadius(3f);
    assertEquals("The detection radius should be 3f", 3f, proximity.getDetectionRadius(), 0.001f);
  }

  @Test
  public void testFindNeighborsInRange() {
    World world = new World(new Vector2(0, 0), true);
    BodyDef def = new BodyDef();
    Body ownerBody = world.createBody(def);
    Box2dSteeringBody ownerSteeringBody = new Box2dSteeringBody(ownerBody, true, 2f);
    Box2dSquareAABBProximity ownerProximity = new Box2dSquareAABBProximity(ownerSteeringBody, world,
        2f);
    Separation<Vector2> proximityCallback = new Separation<>(ownerSteeringBody,
        ownerProximity);

    Body otherBody = world.createBody(def);
    FixtureDef fixtureDef = new FixtureDef();
    CircleShape circle = new CircleShape();
    circle.setRadius(4f);
    fixtureDef.shape = circle;
    otherBody.createFixture(fixtureDef);
    new Box2dSteeringBody(otherBody, true, 2f);

    assertEquals("There should be one other body within range", 1, ownerProximity.findNeighbors(proximityCallback));
  }

  @Test
  public void testFindNeighborsOutOfRange() {
    World world = new World(new Vector2(0, 0), true);
    BodyDef def = new BodyDef();
    Body ownerBody = world.createBody(def);
    Box2dSteeringBody ownerSteeringBody = new Box2dSteeringBody(ownerBody, true, 2f);
    Box2dSquareAABBProximity ownerProximity = new Box2dSquareAABBProximity(ownerSteeringBody, world,
        2f);
    Separation<Vector2> proximityCallback = new Separation<>(ownerSteeringBody,
        ownerProximity);

    Body otherBody = world.createBody(def);
    otherBody.setTransform(10, 10, 0);
    FixtureDef fixtureDef = new FixtureDef();
    CircleShape circle = new CircleShape();
    circle.setRadius(4f);
    fixtureDef.shape = circle;
    otherBody.createFixture(fixtureDef);
    new Box2dSteeringBody(otherBody, true, 2f);

    assertEquals("There should be no other body within range", 0, ownerProximity.findNeighbors(proximityCallback));
  }

  @Test
  public void prepareAABB() {
    AABB aabb = new AABB();
    Vector2 position = new Vector2(1, 2);
    float detectionRadius = 2f;

    World world = new World(new Vector2(0, 0), true);
    BodyDef def = new BodyDef();
    Body ownerBody = world.createBody(def);
    ownerBody.setTransform(position, 0f);
    Box2dSteeringBody ownerSteeringBody = new Box2dSteeringBody(ownerBody, true, 2f);
    Box2dSquareAABBProximity proximity = new Box2dSquareAABBProximity(ownerSteeringBody, world,
        detectionRadius);

    proximity.prepareAABB(aabb);
    assertEquals("Lower left X value should be -1", -1f, aabb.lowerX, 0.001f);
    assertEquals("Lower left Y value should be 0", 0f, aabb.lowerY, 0.001f);
    assertEquals("Upper right X value should be 3", 3f, aabb.upperX, 0.001f);
    assertEquals("Upper right Y value should be 4", 4f, aabb.upperY, 0.001f);
  }

  @Test
  public void testGetSteerableInvalidUserData() {
    ProximitySteerable data = new ProximitySteerable();

    data.otherBody.setUserData("String");
    assertNull("AI proximity should not crash with other data types on other fixture.",
        data.ownerProximity.getSteerable(data.otherFixture));
  }

  @Test
  public void testGetSteerableNullUserData() {
    ProximitySteerable data = new ProximitySteerable();
    assertNull("AI proximity should not find steering body on other fixture.",
        data.ownerProximity.getSteerable(data.otherFixture));
  }

  @Test
  public void testGetSteerableValidUserData() {
    ProximitySteerable data = new ProximitySteerable();
    Box2dSteeringBody otherSteeringBody = new Box2dSteeringBody(data.otherBody, true, 2f);
    assertEquals("When steering body is initialised, it should be found in userData.",
        otherSteeringBody, data.ownerProximity.getSteerable(data.otherFixture));
  }


  @Test
  public void testAccept() {
    ProximitySteerable data = new ProximitySteerable();
    Box2dSteeringBody otherSteeringBody = new Box2dSteeringBody(data.otherBody, true, 2f);
    assertTrue("Any steering body should be accepted.",
        data.ownerProximity.accept(otherSteeringBody));
  }

  @Test
  public void reportFixture() {
  }
}