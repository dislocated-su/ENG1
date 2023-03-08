package com.devcharles.piazzapanic.utility.box2d;

import static org.junit.Assert.*;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class LightBuilderTest {

  @Test
  public void testCreatePointLight() {
    World world = new World(new Vector2(0, 0), true);
    RayHandler handler = new RayHandler(world);

    assertFalse("The centre of the light should not be lit.", handler.pointAtLight(1, 2));
    PointLight light = LightBuilder.createPointLight(handler, 1, 2, Color.RED, 5f, true);
    handler.update();
    assertTrue("The centre of the light should be lit.", handler.pointAtLight(1, 2));

    assertEquals("The light should be at a specific position.", light.getPosition(), new Vector2(1, 2));
    assertEquals("The light should be red.", light.getColor(), Color.RED);
    assertEquals("The light distance should be approximately 5.", light.getDistance(), 5f, 0.001f);
    assertTrue("The light should be soft.", light.isSoft());
    // TODO: check for null RayHandler
  }

  @Test
  public void testCreateRoomLight() {
    World world = new World(new Vector2(0, 0), true);
    RayHandler handler = new RayHandler(world);

    assertFalse("The centre of the light should not be lit.", handler.pointAtLight(1, 2));
    PointLight light = LightBuilder.createRoomLight(handler, 1, 2, Color.RED, 5f, true);
    handler.update();
    assertTrue("The centre of the light should be lit.", handler.pointAtLight(1, 2));

    assertEquals("The light should be at a specific position.", light.getPosition(), new Vector2(1, 2));
    assertEquals("The light should be red.", light.getColor(), Color.RED);
    assertEquals("The light distance should be approximately 5.", light.getDistance(), 5f, 0.001f);
    assertTrue("The light should have xray turned on.", light.isXray());
  }
}