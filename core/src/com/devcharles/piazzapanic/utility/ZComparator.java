package com.devcharles.piazzapanic.utility;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;

public class ZComparator implements Comparator<Entity> {

    public ZComparator() {
    }

    @Override
    public int compare(Entity entityA, Entity entityB) {
        float az, bz;

        az = Mappers.transform.get(entityA).position.z;
        bz = Mappers.transform.get(entityB).position.z;

        return Double.compare(bz, az);
    }
}
