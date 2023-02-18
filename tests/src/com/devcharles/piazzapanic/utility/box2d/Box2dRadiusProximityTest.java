package com.devcharles.piazzapanic.utility.box2d;

import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.Test;

public class Box2dRadiusProximityTest {

  @Test
  public void getSteerable() {
    World world = new World(new Vector2(0, 0), true);
    BodyDef def = new BodyDef();
    FixtureDef fixtureDef = new FixtureDef();
    CircleShape circle = new CircleShape();
    circle.setRadius(0.5f);
    fixtureDef.shape = circle;

    Body aiBody = world.createBody(def);
    Box2dSteeringBody aiSteeringBody = new Box2dSteeringBody(aiBody, true, 2f);
    Box2dRadiusProximity aiProximity = new Box2dRadiusProximity(aiSteeringBody, world, 2f);

    Body otherBody = world.createBody(def);
    Fixture fixture = otherBody.createFixture(fixtureDef);

    assertNull("AI proximity should not find steering body on other fixture.",
        aiProximity.getSteerable(fixture));
    fixture.getBody().setUserData("String");
    assertNull("AI proximity should not crash with other data types on other fixture.",
        aiProximity.getSteerable(fixture));
    Box2dSteeringBody otherSteeringBody = new Box2dSteeringBody(otherBody, true, 2f);
    assertEquals("When steering body is initialised, it should be found in userData.",
        aiProximity.getSteerable(fixture), otherSteeringBody);
  }

  @Test
  public void accept() {
    World world = new World(new Vector2(0, 0), true);
    BodyDef def = new BodyDef();
    Body ownerBody = world.createBody(def);
    Box2dSteeringBody ownerSteeringBody = new Box2dSteeringBody(ownerBody, true, 2f);
    ownerBody.setTransform(0, 0, 0);

    Body otherBody = world.createBody(def);
    Box2dSteeringBody otherSteeringBody = new Box2dSteeringBody(otherBody, true, 2f);
    otherBody.setTransform(5, 0, 0);

    Box2dRadiusProximity proximity = new Box2dRadiusProximity(ownerSteeringBody, world, 2f);
    assertFalse("Other body should not be accepted as it should be out of range.",
        proximity.accept(otherSteeringBody));
    otherBody.setTransform(0, 0, 0);
    assertTrue("Other body should be accepted as it should be in range.",
        proximity.accept(otherSteeringBody));
  }
}