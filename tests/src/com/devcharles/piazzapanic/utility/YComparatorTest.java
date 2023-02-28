package com.devcharles.piazzapanic.utility;

import static org.junit.Assert.*;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.GdxTestRunner;
import com.devcharles.piazzapanic.components.B2dBodyComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import org.junit.runner.RunWith;
import org.junit.Test;


@RunWith(GdxTestRunner.class)
public class YComparatorTest {
    @Test
    public void EqualityTest() {

        World world = new World(new Vector2(0,0),true);
        PooledEngine engine = new PooledEngine();
        TransformComponent transform = engine.createComponent(TransformComponent.class);
        B2dBodyComponent body = engine.createComponent(B2dBodyComponent.class);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(1,2);

        body.body = world.createBody(bodyDef);

        YComparator YComparator = new YComparator();

        Entity a = new Entity();
        Entity b = new Entity();

        a.add(body);
        a.add(transform);
        b.add(body);
        b.add(transform);

        engine.addEntity(a);
        engine.addEntity(b);

        int comp_value = YComparator.compare(a,b);

        assertEquals("Checks that the both entities have the same y value", Math.round(Mappers.transform.get(a).position.y) ,
                Math.round(Mappers.transform.get(b).position.y));
        assertEquals("Checks to see if the comparator returns a 0", 0, comp_value);
    }
    @Test
    public void NotEqualTest() {

        World world = new World(new Vector2(0,0),true);
        PooledEngine engine = new PooledEngine();

        TransformComponent transformA = engine.createComponent(TransformComponent.class);
        TransformComponent transformB = engine.createComponent(TransformComponent.class);
        B2dBodyComponent bodyA = engine.createComponent(B2dBodyComponent.class);
        B2dBodyComponent bodyB = engine.createComponent(B2dBodyComponent.class);

        BodyDef bodyDefA = new BodyDef();
        bodyDefA.type = BodyDef.BodyType.StaticBody;
        bodyDefA.position.set(1,4);

        bodyA.body = world.createBody(bodyDefA);

        BodyDef bodyDefB = new BodyDef();
        bodyDefB.type = BodyDef.BodyType.StaticBody;
        bodyDefB.position.set(2,2);


        bodyB.body = world.createBody(bodyDefB);


        YComparator YComparator = new YComparator();

        Entity a = new Entity();
        Entity b = new Entity();

        a.add(bodyA);
        a.add(transformA);
        b.add(bodyB);
        b.add(transformB);

        engine.addEntity(a);
        engine.addEntity(b);

        int comp_value = YComparator.compare(a,b);

        assertNotEquals("Checks that they don't have the same y value", Math.round(Mappers.transform.get(a).position.y) ,
                Math.round(Mappers.transform.get(b).position.y));
        assertNotEquals("Checks to see if the comparator does not return a 0", 0, comp_value);

    }



}
