package com.devcharles.piazzapanic.utility;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;

/**
 * Compares the entities by their Z coordinate. The z coordinate is only used
 * manually changing render order, see https://en.wikipedia.org/wiki/Z-order.
 */
public class ZComparator implements Comparator<Entity> {

    @Override
    public int compare(Entity entityA, Entity entityB) {
        float az, bz;

        az = Mappers.transform.get(entityA).position.z;
        bz = Mappers.transform.get(entityB).position.z;

        return Double.compare(bz, az);
    }
}
