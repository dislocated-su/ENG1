package com.devcharles.piazzapanic.utility;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;

public class YComparator implements Comparator<Entity> {
    @Override
    public int compare(Entity entityA, Entity entityB) {

        float az, bz;

        az = Mappers.transform.get(entityA).position.y;
        bz = Mappers.transform.get(entityB).position.y;

        return Double.compare(bz, az);
    }
}
