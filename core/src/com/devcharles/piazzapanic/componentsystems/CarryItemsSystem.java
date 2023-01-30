package com.devcharles.piazzapanic.componentsystems;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.devcharles.piazzapanic.components.ItemComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.WalkAnimator;
import com.devcharles.piazzapanic.utility.WalkAnimator.Direction;

public class CarryItemsSystem extends IteratingSystem {

    public CarryItemsSystem() {
        super(Family.all(ItemComponent.class, TransformComponent.class).get());
    }

    Map<Direction, Vector3> dirToVector = new HashMap<Direction, Vector3>() {
        {
            put(Direction.down, new Vector3(0, -0.5f, 0));
            put(Direction.up, new Vector3(0, 0.5f, 1));
            put(Direction.left, new Vector3(-1, 0, 1));
            put(Direction.right, new Vector3(1, 0, 1));
        }
    };

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ItemComponent item = Mappers.item.get(entity);
        TransformComponent transform = Mappers.transform.get(entity);

        Direction cookDirection = WalkAnimator.rotationToDirection(item.holderTransform.rotation);

        Vector3 directionVector = dirToVector.get(cookDirection).cpy();

        transform.position.set(item.holderTransform.position.cpy().add(directionVector.scl(1)));
    }

}
