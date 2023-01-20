package com.devcharles.piazzapanic.componentsystems;
import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.devcharles.piazzapanic.components.AnimationComponent;
import com.devcharles.piazzapanic.components.TextureComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
public class RenderingSystem extends IteratingSystem {

    private SpriteBatch sb;
    private Array<Entity> entities;
    private OrthographicCamera camera;

    private ComponentMapper<TextureComponent> teMap;
    private ComponentMapper<TransformComponent> trMap;
    private ComponentMapper<AnimationComponent> anMap;

    private Comparator<Entity> comparator;

    public RenderingSystem(SpriteBatch batch, OrthographicCamera camera) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get());
        // set up reuired objects
        this.sb = batch;
        this.camera = camera;
        this.comparator = new ZComparator();
        teMap = ComponentMapper.getFor(TextureComponent.class);
        trMap = ComponentMapper.getFor(TransformComponent.class);
        anMap = ComponentMapper.getFor(AnimationComponent.class);
        entities = new Array<Entity>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        entities.sort(comparator);

        sb.setProjectionMatrix(camera.combined);
        sb.enableBlending();
        sb.begin();

        for (Entity entity: entities) {
            TextureComponent texture = teMap.get(entity);
            TransformComponent transform = trMap.get(entity);

            if (anMap.has(entity)) {
                // Handle animation logic here
                continue;
            }
            if (texture.region == null || transform.isHidden) {
                continue;
            }

            float width = texture.region.getRegionWidth();
            float height = texture.region.getRegionHeight();

            float originX = width / 2f;
            float originY = height / 2f;

            sb.draw(
                texture.region,
                transform.position.x - originX,
                transform.position.y - originY,
                originX, originY,
                width, height,
                transform.scale.x * texture.scale.x,
                transform.scale.y * texture.scale.y,
                transform.rotation
            );
        }
        sb.end();
        entities.clear();
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entities.add(entity);
    }
    
    
}
