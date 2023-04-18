package com.devcharles.piazzapanic.componentsystems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.PiazzaPanic;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.input.KeyboardInput;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.Mappers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class PlayerControlSystemTest {

  KeyboardInput kbInput = new KeyboardInput();
  World world;
  PooledEngine engine;
  EntityFactory factory;

  @Before
  public void setup() {
    world = new World(new Vector2(0, 0), true);
    engine = new PooledEngine();
    AssetManager manager = new AssetManager();
    manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    PiazzaPanic.loadAssets(manager);
    manager.finishLoading();
    factory = new EntityFactory(engine, world, manager);
  }

  @Test
  public void testProcessEntityInteractInputs() {
    PlayerControlSystem system = new PlayerControlSystem(kbInput);
    engine.addSystem(system);
    Entity cook1 = factory.createCook(0, 0);
    Entity cook2 = factory.createCook(50, -27);

    cook1.add(engine.createComponent(PlayerComponent.class));

    //Put down tests
    kbInput.putDown = true;
    engine.update(1f);
    assertTrue("The cook with the player component should have the put down variable set to true",
        Mappers.player.get(cook1).putDown);

    assertFalse(
        "The other cook should not have a player component and therefore no changes should be applied",
        Mappers.player.has(cook2));

    assertFalse("The keyboard input for putDown should be set to false to prevent repeats",
        kbInput.putDown);

    //Pick up tests
    kbInput.pickUp = true;
    engine.update(1f);
    assertTrue("The cook with the player component should have the pick up variable set to true",
        Mappers.player.get(cook1).pickUp);

    assertFalse(
        "The other cook should not have a player component and therefore no changes should be applied",
        Mappers.player.has(cook2));

    assertFalse("The keyboard input for putDown should be set to false to prevent repeats",
        kbInput.pickUp);

    //Interact tests
    kbInput.interact = true;
    engine.update(1f);
    assertTrue("The cook with the player component should have the interact variable set to true",
        Mappers.player.get(cook1).interact);

    assertFalse(
        "The other cook should not have a player component and therefore no changes should be applied",
        Mappers.player.has(cook2));

    assertFalse("The keyboard input for interact should be set to false to prevent repeats",
        kbInput.interact);

  }

  @Test
  public void testProcessEntityMovementInputs() {
    PlayerControlSystem system = new PlayerControlSystem(kbInput);
    engine.addSystem(system);
    Entity cook1 = factory.createCook(0, 0);
    Entity cook2 = factory.createCook(50, -27);

    cook1.add(engine.createComponent(PlayerComponent.class));

    //The physics system needs to be created for movement testing
    PhysicsSystem physicsSystem = new PhysicsSystem(world);
    engine.addSystem(physicsSystem);
    B2dBodyComponent cook1Body = Mappers.b2body.get(cook1);
    B2dBodyComponent cook2Body = Mappers.b2body.get(cook2);

    kbInput.left = true;
    engine.update(1f);
    assertTrue("The cook with the player component should have moved to the left",
        cook1Body.body.getPosition().x < 0);

    assertEquals("The cook with the player component should not have moved on the y-axis", 0,
        cook1Body.body.getPosition().y, 0.001f);

    assertEquals("The position of the other cook should not have changed", new Vector2(50, -27),
        cook2Body.body.getPosition());

    kbInput.left = false;
    //reset velocity
    cook1Body.body.setLinearVelocity(new Vector2(0, 0));
    engine.update(1f);
    Vector2 cook1PreviousPosition = cook1Body.body.getPosition().cpy();

    kbInput.up = true;
    engine.update(1f);

    assertEquals("The cook with the player component should not have moved on the x-axis",
        cook1PreviousPosition.x, cook1Body.body.getPosition().x, 0.001f);

    assertTrue("The cook with the player component should have moved upwards",
        cook1Body.body.getPosition().y > cook1PreviousPosition.y);

    assertEquals("The position of the other cook should not have changed", new Vector2(50, -27),
        cook2Body.body.getPosition());

    kbInput.up = false;

    //resetting velocity and update position check
    cook1Body.body.setLinearVelocity(new Vector2(0, 0));
    engine.update(1f);
    cook1PreviousPosition = cook1Body.body.getPosition().cpy();

    //Multiple directions
    kbInput.right = true;
    kbInput.down = true;
    engine.update(1f);
    assertTrue("The cook with the player component should have moved to the right",
        cook1Body.body.getPosition().x > cook1PreviousPosition.x);

    assertTrue("The cook with the player component should have moved down on the y-axis",
        cook1Body.body.getPosition().y < cook1PreviousPosition.y);

    assertEquals("The position of the other cook should not have changed", new Vector2(50, -27),
        cook2Body.body.getPosition());

    kbInput.right = false;
    kbInput.down = false;

    //resetting velocity and update position check
    cook1Body.body.setLinearVelocity(new Vector2(0, 0));
    engine.update(1f);
    cook1PreviousPosition = cook1Body.body.getPosition().cpy();

    //Contradictory directions
    kbInput.left = true;
    kbInput.right = true;
    kbInput.up = true;
    kbInput.down = true;
    engine.update(1f);
    assertEquals("No change should be made on the x-axis", cook1Body.body.getPosition().x,
        cook1PreviousPosition.x, 0.001f);

    assertEquals("No change should be made on the y-axis", cook1Body.body.getPosition().y,
        cook1PreviousPosition.y, 0.001f);
  }

  @Test
  public void testProcessEntitySwitchCooks() {
    PlayerControlSystem system = new PlayerControlSystem(kbInput);
    engine.addSystem(system);
    Entity cook1 = factory.createCook(0, 0);
    Entity cook2 = factory.createCook(50, -27);

    cook1.add(engine.createComponent(PlayerComponent.class));
    kbInput.changeCooks = true;
    engine.update(1f);
    assertFalse("The first cook should no longer have the player component",
        Mappers.player.has(cook1));

    assertTrue("The second cook should now have the player component", Mappers.player.has(cook2));

    assertFalse("The input should be set to false to prevent constantly switching",
        kbInput.changeCooks);

    assertFalse("The boolean to change cooks in the system should also be set to false afterwards",
        system.changingCooks);

    //Changing from second cook to first cook
    kbInput.changeCooks = true;
    //Due to the nature of the system, the update function must be called twice to loop
    //back to the first cook
    engine.update(1f);
    assertTrue(
        "As the process of switching cooks is still ongoing, the system variable for switching cooks should be true",
        system.changingCooks);

    assertFalse("The input should be set to false to prevent constantly switching",
        kbInput.changeCooks);

    engine.update(1f);
    assertTrue("The first cook should now have the player component", Mappers.player.has(cook1));

    assertFalse("The second cook should have the player component removed",
        Mappers.player.has(cook2));

    assertFalse("The boolean to change cooks in the system should also be set to false afterwards",
        system.changingCooks);
  }
}
