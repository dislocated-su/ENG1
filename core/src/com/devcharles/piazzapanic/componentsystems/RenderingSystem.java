package com.devcharles.piazzapanic.componentsystems;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.devcharles.piazzapanic.components.AnimationComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.TextureComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.components.WalkingAnimationComponent;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.Pair;
import com.devcharles.piazzapanic.utility.WalkAnimator;
import com.devcharles.piazzapanic.utility.WorldTilemapRenderer;
import com.devcharles.piazzapanic.utility.ZComparator;

public class RenderingSystem extends IteratingSystem {

    private SpriteBatch sb;
    private Array<Entity> entities;
    private OrthographicCamera camera;

    private Comparator<Entity> comparator;

    private float renderingAccumulator = 0f;

    private WorldTilemapRenderer mapRenderer;

    public RenderingSystem(World world, SpriteBatch batch, OrthographicCamera camera) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get());
        // set up reuired objects
        this.sb = batch;
        this.camera = camera;
        this.comparator = new ZComparator();
        this.mapRenderer = new WorldTilemapRenderer(world, camera, batch);
        
        entities = new Array<Entity>(32);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        renderingAccumulator += deltaTime;

        entities.sort(comparator);

        sb.setProjectionMatrix(camera.combined);
        camera.update();
        sb.begin();
        

        mapRenderer.renderBackground();

        for (Entity entity : entities) {
            TextureComponent texture = Mappers.texture.get(entity);
            TransformComponent transform = Mappers.transform.get(entity);

            TextureRegion toRender = texture.region;
            Float rotation = null;
            boolean flip = false;

            // If this is the player, update the camera position.
            if (Mappers.player.has(entity)) {
                camera.position.lerp(new Vector3(transform.position.x, transform.position.y, 0), 0.1f);
                camera.position.x = (float) Math.round(camera.position.x * 1000f) / 1000f;
                camera.position.y = (float) Math.round(camera.position.y * 1000f) / 1000f;
            }
            if (Mappers.animation.has(entity)) {
                // Handle animation logic here
                if (Mappers.walkingAnimation.has(entity)) {
                    // Animation for walking have the correct orientation already.
                    rotation = 0f;
                    WalkAnimator walkAnimator = Mappers.walkingAnimation.get(entity).animator;
                    toRender = walkAnimator.getFrame(transform.rotation, transform.isMoving, renderingAccumulator);

                } else {
                    // Animation<TextureRegion> animation = anMap.get(entity).animation;
                    continue;
                }
            }
            if (toRender == null || transform.isHidden) {
                continue;
            }

            float width = toRender.getRegionWidth();
            float height = toRender.getRegionHeight();

            float originX = width / 2f;
            float originY = height / 2f;

            sb.draw(
                    toRender,
                    transform.position.x - originX,
                    transform.position.y - originY,
                    originX, originY,
                    width, height,
                    transform.scale.x * texture.scale.x * (flip ? -1 : 1),
                    transform.scale.y * texture.scale.y,
                    rotation == null ? transform.rotation : rotation);
        }
        mapRenderer.renderForeground();
        sb.end();
        entities.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entities.add(entity);
    }

}
