package com.devcharles.piazzapanic.utility;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.TransformComponent;

public class ZComparator implements Comparator<Entity> {

    public ZComparator() {
    }

    @Override
    public int compare(Entity entityA, Entity entityB) {
        boolean aIsWalk = Mappers.controllable.has(entityA);
        boolean bIsWalk = Mappers.controllable.has(entityB);

        float az, bz;

        az = Mappers.transform.get(entityA).position.z;
        bz = Mappers.transform.get(entityB).position.z;

        if (aIsWalk && bIsWalk && az == bz) {
            az = Mappers.transform.get(entityA).position.y;
            bz = Mappers.transform.get(entityB).position.y;
        }

        int res = 0;
        // Comparator inverted so that the list gets sorted in descending order
        if (az < bz) {
            res = 1;
        } else if (az > bz) {
            res = -1;
        }
        return res;
    }
}
