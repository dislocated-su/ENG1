package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.TransformComponent;

import static org.junit.Assert.*;
import com.devcharles.piazzapanic.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)

public class PhysicsSystemTest {

  @Test
  public void updatePositiveXTest(){
    World world = new World(new Vector2(0, 0), true);
    PooledEngine engine = new PooledEngine();
    Entity entity = engine.createEntity();
    PhysicsSystem system = new PhysicsSystem(world);
    engine.addEntity(entity);
    engine.addSystem(system);
    TransformComponent transform = engine.createComponent(TransformComponent.class);
    B2dBodyComponent bodyC = engine.createComponent(B2dBodyComponent.class);
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.position.set(0,0);
    bodyC.body = world.createBody(bodyDef);
    entity.add(transform);
    entity.add(bodyC);
    transform.position.set(new Vector3 (0,0,0));
    assertFalse("Checks transform is not moving before force applied.", transform.isMoving);
    bodyC.body.applyForceToCenter(100.0f, 0.0f, true);
    engine.update(0.25f);
    float newTransX = transform.position.x;
    float newBodyX = bodyC.body.getPosition().x;
    float newTransY = transform.position.y;
    float newBodyY = bodyC.body.getPosition().y;
    assertEquals("Checks X of body and transform are equal is +X force",newBodyX,newTransX,0.01f);
    assertEquals("Checks Y of body and transform are equal is +X force",newBodyY,newTransY,0.01f);
    assertTrue("Checks transform is moving after force applied.", transform.isMoving);
  }

  @Test
  public void updateNegativeXTest(){
    World world = new World(new Vector2(0, 0), true);
    PooledEngine engine = new PooledEngine();
    Entity entity = engine.createEntity();
    PhysicsSystem system = new PhysicsSystem(world);
    engine.addEntity(entity);
    engine.addSystem(system);
    TransformComponent transform = engine.createComponent(TransformComponent.class);
    B2dBodyComponent bodyC = engine.createComponent(B2dBodyComponent.class);
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.position.set(0,0);
    bodyC.body = world.createBody(bodyDef);
    entity.add(transform);
    entity.add(bodyC);
    transform.position.set(new Vector3 (0,0,0));
    assertFalse("Checks transform is not moving before force applied.", transform.isMoving);
    bodyC.body.applyForceToCenter(350.0f, 0.0f, true);
    engine.update(0.25f);
    float newTransX = transform.position.x;
    float newBodyX = bodyC.body.getPosition().x;
    float newTransY = transform.position.y;
    float newBodyY = bodyC.body.getPosition().y;
    assertEquals("Checks X of body and transform are equal is -X force",newBodyX,newTransX,0.01f);
    assertEquals("Checks Y of body and transform are equal is -X force",newBodyY,newTransY,0.01f);
    assertTrue("Checks transform is moving after force applied.", transform.isMoving);
  }

  @Test
  public void updatePositiveYTest(){
    World world = new World(new Vector2(0, 0), true);
    PooledEngine engine = new PooledEngine();
    Entity entity = engine.createEntity();
    PhysicsSystem system = new PhysicsSystem(world);
    engine.addEntity(entity);
    engine.addSystem(system);
    TransformComponent transform = engine.createComponent(TransformComponent.class);
    B2dBodyComponent bodyC = engine.createComponent(B2dBodyComponent.class);
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.position.set(0,0);
    bodyC.body = world.createBody(bodyDef);
    entity.add(transform);
    entity.add(bodyC);
    transform.position.set(new Vector3 (0,0,0));
    assertFalse("Checks transform is not moving before force applied.", transform.isMoving);
    bodyC.body.applyForceToCenter(0.0f, 900.0f, true);
    engine.update(0.25f);
    float newTransX = transform.position.x;
    float newBodyX = bodyC.body.getPosition().x;
    float newTransY = transform.position.y;
    float newBodyY = bodyC.body.getPosition().y;
    assertEquals("Checks X of body and transform are equal is +Y force",newBodyX,newTransX,0.01f);
    assertEquals("Checks Y of body and transform are equal is +Y force",newBodyY,newTransY,0.01f);
    assertTrue("Checks transform is moving after force applied.", transform.isMoving);
  }

  @Test
  public void updateNegativeYTest(){
    World world = new World(new Vector2(0, 0), true);
    PooledEngine engine = new PooledEngine();
    Entity entity = engine.createEntity();
    PhysicsSystem system = new PhysicsSystem(world);
    engine.addEntity(entity);
    engine.addSystem(system);
    TransformComponent transform = engine.createComponent(TransformComponent.class);
    B2dBodyComponent bodyC = engine.createComponent(B2dBodyComponent.class);
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.position.set(0,0);
    bodyC.body = world.createBody(bodyDef);
    entity.add(transform);
    entity.add(bodyC);
    transform.position.set(new Vector3 (0,0,0));
    assertFalse("Checks transform is not moving before force applied.",  transform.isMoving);
    bodyC.body.applyForceToCenter(0.0f, -700.0f, true);
    engine.update(0.25f);
    float newTransX = transform.position.x;
    float newBodyX = bodyC.body.getPosition().x;
    float newTransY = transform.position.y;
    float newBodyY = bodyC.body.getPosition().y;
    assertEquals("Checks X of body and transform are equal is -Y force",newBodyX,newTransX,0.01f);
    assertEquals("Checks Y of body and transform are equal is -Y force",newBodyY,newTransY,0.01f);
    assertTrue("Checks transform is moving after force applied.", transform.isMoving);
  }
}
