package com.devcharles.piazzapanic.componentsystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.components.TransformComponent;

import java.util.Comparator;
 
 
public class ZComparator implements Comparator<Entity> {
    private ComponentMapper<TransformComponent> transformMap;
 
    public ZComparator(){
        transformMap = ComponentMapper.getFor(TransformComponent.class);
    }
 
    @Override
    public int compare(Entity entityA, Entity entityB) {
    	float az = transformMap.get(entityA).position.z;
    	float bz = transformMap.get(entityB).position.z;
    	int res = 0;
    	if(az > bz){
    		res = 1;
    	}else if(az < bz){
    		res = -1;
    	}
        return res;
    }
}
