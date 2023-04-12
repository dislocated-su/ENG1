package com.devcharles.piazzapanic.utility.box2d;

import static com.devcharles.piazzapanic.utility.box2d.MapBodyBuilder.*;
import static org.junit.Assert.*;

import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.devcharles.piazzapanic.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(GdxTestRunner.class)
public class MapBodyBuilderTest {

  @Test
  public void testBuildShapesCorrectNumber() {
    World world = new World(new Vector2(0, 0), true);
    TiledMap mapTest = new TmxMapLoader().load("v2/map.tmx");
    int ppt = 16;
    Array<Body> mapBodyTest = MapBodyBuilder.buildShapes(mapTest, ppt, world);
    assertEquals("The correct number of objects should be detected", 29, mapBodyTest.size);
  }

  @Test
  public void testGetRectangle() {
    World world = new World(new Vector2(0, 0), true);
    RectangleMapObject testRectObj = new RectangleMapObject();
    Rectangle mapObjRect = testRectObj.getRectangle();
    Vector2 actualVector = new Vector2(mapObjRect.getX() / 16, mapObjRect.getY() / 16);
    PolygonShape testPoly = getRectangle(testRectObj);

    List<Vector2> actualOutput = new ArrayList<Vector2>();
    for (int i = 0; i < 4; i++) {
      Vector2 testVector = new Vector2(0, 0);
      testPoly.getVertex(i, testVector);
      actualOutput.add(testVector);
    }

    assertEquals("Test that the shape has the correct number of vertices", 4,
        testPoly.getVertexCount());

    assertTrue("Test that the bottom left corner of the shape is in the correct position",
        actualOutput.contains(actualVector));

    actualVector.x = (mapObjRect.getX() + mapObjRect.getWidth()) / 16;
    actualVector.y = mapObjRect.getY() / 16;
    assertTrue("Test that the bottom right corner of the shape is in the correct position",
        actualOutput.contains(actualVector));

    actualVector.x = (mapObjRect.getX() + mapObjRect.getWidth()) / 16;
    actualVector.y = (mapObjRect.getY() + mapObjRect.getHeight()) / 16;
    assertTrue("Test that the top right corner of the shape is in the correct position",
        actualOutput.contains(actualVector));

    actualVector.x = mapObjRect.getX() / 16;
    actualVector.y = (mapObjRect.getY() + mapObjRect.getHeight()) / 16;
    assertTrue("Test that the top left corner of the shape is in the correct position",
        actualOutput.contains(actualVector));
  }

  @Test
  public void testGetCircle() {
    World world = new World(new Vector2(0, 0), true);
    CircleMapObject testCircleObj = new CircleMapObject();
    CircleShape testCircle = getCircle(testCircleObj);
    Vector2 testVector = testCircle.getPosition();
    Vector2 actualVector = new Vector2(0, 0);

    assertEquals("Test that the circle shape is instantiated in the correct position", actualVector,
        testVector);

    float testRadius = testCircle.getRadius();
    assertEquals("The circle has the correct radius", 0.0625f, testRadius, 0.001f);
  }

  @Test
  public void testGetPolygon() {
    World world = new World(new Vector2(0, 0), true);
    float[] polyVertices = {82, 0, 146, 40, 385, 268, 322, 341};
    PolygonMapObject testPolyObj = new PolygonMapObject(polyVertices);
    Vector2 actualVector = new Vector2(polyVertices[0] / 16, polyVertices[1] / 16);
    PolygonShape testPolygon = getPolygon(testPolyObj);

    assertEquals("Test that the shape has the correct number of vertices", polyVertices.length / 2,
        testPolygon.getVertexCount());

    List<Vector2> actualOutput = new ArrayList<Vector2>();
    for (int i = 0; i < testPolygon.getVertexCount(); i++) {
      Vector2 testVector = new Vector2(0, 0);
      testPolygon.getVertex(i, testVector);
      actualOutput.add(testVector);
    }
    assertTrue(actualOutput.contains(actualVector));

    actualVector.x = polyVertices[2] / 16;
    actualVector.y = polyVertices[3] / 16;
    assertTrue(actualOutput.contains(actualVector));

    actualVector.x = polyVertices[4] / 16;
    actualVector.y = polyVertices[5] / 16;
    assertTrue(actualOutput.contains(actualVector));

    actualVector.x = polyVertices[6] / 16;
    actualVector.y = polyVertices[7] / 16;
    assertTrue(actualOutput.contains(actualVector));
  }

  @Test
  public void testGetPolyline() {
    World world = new World(new Vector2(0, 0), true);
    float[] polyVertices = {650, 0, 253, 32, 947, 312, 579, 541};
    PolylineMapObject testPolylineObj = new PolylineMapObject(polyVertices);
    Vector2 actualVector = new Vector2(polyVertices[0] / 16, polyVertices[1] / 16);
    ChainShape testPolygon = getPolyline(testPolylineObj);

    assertEquals("Test that the ChainShape has the correct number of vertices",
        polyVertices.length / 2, testPolygon.getVertexCount());

    List<Vector2> actualOutput = new ArrayList<Vector2>();
    for (int i = 0; i < testPolygon.getVertexCount(); i++) {
      Vector2 testVector = new Vector2(0, 0);
      testPolygon.getVertex(i, testVector);
      actualOutput.add(testVector);
    }

    assertTrue("Test that the first vertex is in the ChainShape",
        actualOutput.contains(actualVector));

    actualVector.x = polyVertices[2] / 16;
    actualVector.y = polyVertices[3] / 16;
    assertTrue("Test that the second vertex is in the ChainShape",
        actualOutput.contains(actualVector));

    actualVector.x = polyVertices[4] / 16;
    actualVector.y = polyVertices[5] / 16;
    assertTrue("Test that the third vertex is in the ChainShape",
        actualOutput.contains(actualVector));

    actualVector.x = polyVertices[6] / 16;
    actualVector.y = polyVertices[7] / 16;
    assertTrue("Test that the fourth vertex is in the ChainShape",
        actualOutput.contains(actualVector));
  }
}
