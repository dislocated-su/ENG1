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
public class Box2dRadiusProximityTest {

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
  public void testAcceptInRange() {
    ProximitySteerable data = new ProximitySteerable();
    Box2dSteeringBody otherSteeringBody = new Box2dSteeringBody(data.otherBody, true, 2f);
    data.otherBody.setTransform(0, 0, 0);

    assertTrue("Other body should be accepted as it should be in range.",
        data.ownerProximity.accept(otherSteeringBody));
  }

  @Test
  public void testAcceptOutOfRange() {
    ProximitySteerable data = new ProximitySteerable();
    Box2dSteeringBody otherSteeringBody = new Box2dSteeringBody(data.otherBody, true, 2f);
    data.otherBody.setTransform(5, 0, 0);

    assertFalse("Other body should not be accepted as it should be out of range.",
        data.ownerProximity.accept(otherSteeringBody));
  }

  @Test
  public void testAcceptBorderRange() {
    ProximitySteerable data = new ProximitySteerable();
    Box2dSteeringBody otherSteeringBody = new Box2dSteeringBody(data.otherBody, true, 2f);
    data.otherBody.setTransform(4, 0, 0);

    assertTrue("Other body should be accepted as it should be just in range.",
        data.ownerProximity.accept(otherSteeringBody));
  }
}