package com.devcharles.piazzapanic.utility;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;

/**
 * Compares entities by their Y coordinate, so entities that are lower on the
 * screen appear in front for a consistent perspective.
 */
public class YComparator implements Comparator<Entity> {
    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Entity entityA, Entity entityB) {

        float az, bz;

        az = Mappers.transform.get(entityA).position.y;
        bz = Mappers.transform.get(entityB).position.y;

        return Double.compare(bz, az);
    }
}
