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

        YComparator YComparator = new YComparator();

        Entity a = new Entity();
        Entity b = new Entity();

        transform.position.y = 1;

        a.add(transform);
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


        YComparator YComparator = new YComparator();

        Entity a = new Entity();
        Entity b = new Entity();

        a.add(transformA);
        b.add(transformB);

        a.getComponent(TransformComponent.class).position.y = 3.0f;
        b.getComponent(TransformComponent.class).position.y = 6.0f;

        engine.addEntity(a);
        engine.addEntity(b);

        int comp_value_less = YComparator.compare(a,b);

        Entity c = new Entity();
        Entity d = new Entity();

        c.add(transformB);
        d.add(transformA);

        engine.addEntity(c);
        engine.addEntity(d);

        int comp_value_greater = YComparator.compare(c,d);

        System.out.println(Math.round(Mappers.transform.get(a).position.y));
        System.out.println(Math.round(Mappers.transform.get(b).position.y));
        System.out.println(Math.round(Mappers.transform.get(c).position.y));
        System.out.println(Math.round(Mappers.transform.get(d).position.y));

        // Comparator returns a 1 if the first argument's Y is less
        assertEquals("A 1 is returned if the first argument's Y is less ", 1, comp_value_less);
        assertEquals("A -1 is returned if the first argument's Y is greater ", -1, comp_value_greater);

    }



}
