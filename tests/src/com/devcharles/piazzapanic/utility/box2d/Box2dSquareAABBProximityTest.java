package com.devcharles.piazzapanic.utility.box2d;

import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.GdxTestRunner;
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

    assertEquals("Initial owner should be the constructor parameter owner.",
        proximity.getOwner(), ownerSteeringBody);

    proximity.setOwner(null);
    assertNull("Owner should become null after setting it to null.", proximity.getOwner());

    proximity.setOwner(ownerSteeringBody);
    assertEquals("Owner should be the new value set.", proximity.getOwner(), ownerSteeringBody);
  }

  @Test
  public void testGetAndSetWorld() {
    World world = new World(new Vector2(0, 0), true);
    BodyDef def = new BodyDef();
    Body ownerBody = world.createBody(def);
    Box2dSteeringBody ownerSteeringBody = new Box2dSteeringBody(ownerBody, true, 2f);
    Box2dSquareAABBProximity proximity = new Box2dSquareAABBProximity(ownerSteeringBody, world, 2f);

    assertEquals("Initial world should be the constructor parameter world.",
        proximity.getWorld(), world);

    proximity.setWorld(null);
    assertNull("World should become null after setting it to null.", proximity.getWorld());

    proximity.setWorld(world);
    assertEquals("World should be the new value set.", proximity.getWorld(), world);
  }

  @Test
  public void testGetAndSetDetectionRadius() {
    World world = new World(new Vector2(0, 0), true);
    BodyDef def = new BodyDef();
    Body ownerBody = world.createBody(def);
    Box2dSteeringBody ownerSteeringBody = new Box2dSteeringBody(ownerBody, true, 2f);
    Box2dSquareAABBProximity proximity = new Box2dSquareAABBProximity(ownerSteeringBody, world, 2f);

    assertEquals("Initial detection radius should be 2 as set in the constructor.",
        proximity.getDetectionRadius(), 2f, 0.001f);

    proximity.setDetectionRadius(0f);
    assertEquals("The detection radius should be 0.",
        proximity.getDetectionRadius(), 0f, 0.001f);

    proximity.setDetectionRadius(3f);
    assertEquals("The detection radius should be 3f", proximity.getDetectionRadius(), 3f, 0.001f);
  }

  @Test
  public void findNeighbors() {
  }

  @Test
  public void prepareAABB() {
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
        data.ownerProximity.getSteerable(data.otherFixture), otherSteeringBody);
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