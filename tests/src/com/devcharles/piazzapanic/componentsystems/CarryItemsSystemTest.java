package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import com.badlogic.gdx.math.Vector3;
import com.devcharles.piazzapanic.components.ItemComponent;
import com.devcharles.piazzapanic.components.TransformComponent;

import static org.junit.Assert.*;
import com.devcharles.piazzapanic.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CarryItemsSystemTest {

  private PooledEngine engine;

  @Test
  public void processEntityRightTest(){
    engine = new PooledEngine();
    Entity entity = engine.createEntity();
    CarryItemsSystem system = new CarryItemsSystem();
    TransformComponent transform = engine.createComponent(TransformComponent.class);
    ItemComponent item = engine.createComponent(ItemComponent.class);
    item.holderTransform = engine.createComponent(TransformComponent.class);
    entity.add(transform);
    entity.add(item);
    transform.position.set(new Vector3 (0,0,0));
    item.holderTransform.rotation = 0;
    system.processEntity(entity, 1f);
    assertEquals(new Vector3 (1,0,1),transform.position);
  }

  @Test
  public void processEntityLeftTest(){
    engine = new PooledEngine();
    Entity entity = engine.createEntity();
    CarryItemsSystem system = new CarryItemsSystem();
    TransformComponent transform = engine.createComponent(TransformComponent.class);
    ItemComponent item = engine.createComponent(ItemComponent.class);
    item.holderTransform = engine.createComponent(TransformComponent.class);
    entity.add(transform);
    entity.add(item);
    transform.position.set(new Vector3 (0,0,0));
    item.holderTransform.rotation = 180;
    system.processEntity(entity, 1f);
    assertEquals(new Vector3 (-1,0,1),transform.position);
  }

  @Test
  public void processEntityUpTest(){
    engine = new PooledEngine();
    Entity entity = engine.createEntity();
    CarryItemsSystem system = new CarryItemsSystem();
    TransformComponent transform = engine.createComponent(TransformComponent.class);
    ItemComponent item = engine.createComponent(ItemComponent.class);
    item.holderTransform = engine.createComponent(TransformComponent.class);
    entity.add(transform);
    entity.add(item);
    transform.position.set(new Vector3 (0,0,0));
    item.holderTransform.rotation = 90;
    system.processEntity(entity, 1f);
    assertEquals(new Vector3 (0,0.5f,1),transform.position);
  }

  @Test
  public void processEntityDownTest(){
    engine = new PooledEngine();
    Entity entity = engine.createEntity();
    CarryItemsSystem system = new CarryItemsSystem();
    TransformComponent transform = engine.createComponent(TransformComponent.class);
    ItemComponent item = engine.createComponent(ItemComponent.class);
    item.holderTransform = engine.createComponent(TransformComponent.class);
    entity.add(transform);
    entity.add(item);
    transform.position.set(new Vector3 (0,0,0));
    item.holderTransform.rotation = -135;
    system.processEntity(entity, 1f);
    assertEquals(new Vector3 (0,-0.5f,0),transform.position);
  }
}
