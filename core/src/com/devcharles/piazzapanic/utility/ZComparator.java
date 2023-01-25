package com.devcharles.piazzapanic.utility;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.TransformComponent;

public class ZComparator implements Comparator<Entity> {
    private ComponentMapper<TransformComponent> transformMap;
    private ComponentMapper<ControllableComponent> controllableMap;

    public ZComparator() {
        transformMap = ComponentMapper.getFor(TransformComponent.class);
        controllableMap = ComponentMapper.getFor(ControllableComponent.class);
    }

    @Override
    public int compare(Entity entityA, Entity entityB) {
        boolean aIsWalk = controllableMap.has(entityA);
        boolean bIsWalk = controllableMap.has(entityB);

        float az, bz;

        if (aIsWalk || bIsWalk) {
            az = transformMap.get(entityA).position.y;
            bz = transformMap.get(entityB).position.y;
        } else {
            az = transformMap.get(entityA).position.z;
            bz = transformMap.get(entityB).position.z;
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
