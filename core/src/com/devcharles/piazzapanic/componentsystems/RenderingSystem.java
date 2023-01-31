package com.devcharles.piazzapanic.componentsystems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.devcharles.piazzapanic.components.TextureComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.WalkAnimator;
import com.devcharles.piazzapanic.utility.WorldTilemapRenderer;
import com.devcharles.piazzapanic.utility.YComparator;
import com.devcharles.piazzapanic.utility.ZComparator;
import com.devcharles.piazzapanic.utility.Station.StationType;

public class RenderingSystem extends IteratingSystem {

    private SpriteBatch sb;
    private OrthographicCamera camera;
    private float renderingAccumulator = 0f;
    private WorldTilemapRenderer mapRenderer;

    private ZComparator Zcomparator;
    private YComparator Ycomparator;

    List<Entity> entities = new ArrayList<Entity>();

    public RenderingSystem(TiledMap map, SpriteBatch batch, OrthographicCamera camera) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get());
        this.sb = batch;
        this.camera = camera;
        this.mapRenderer = new WorldTilemapRenderer(map, camera, batch);
        this.Zcomparator = new ZComparator();
        this.Ycomparator = new YComparator();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        renderingAccumulator += deltaTime;

        Collections.shuffle(entities);
        Collections.sort(entities, Zcomparator);
        Collections.sort(entities, Ycomparator);


        sb.setProjectionMatrix(camera.combined);
        camera.update();
        sb.begin();

        mapRenderer.renderBackground();

        for (Entity entity : entities) {
            if (entity == null) {
                continue;
            }
            TextureComponent texture = Mappers.texture.get(entity);
            TransformComponent transform = Mappers.transform.get(entity);

            // Hidden flag skips rendering
            if (transform.isHidden) {
                continue;
            }
            
            TextureRegion toRender = texture.region;
            Float rotation = null;
            boolean flip = false;

            // If this is a station, update locations of the food.
            if (Mappers.station.has(entity)) {
                setFoodTransform(entity);
            }

            // If this is the player, update the camera position.
            if (Mappers.player.has(entity)) {
                camera.position.lerp(new Vector3(transform.position.x, transform.position.y, 0), 0.1f);
                camera.position.x = (float) Math.round(camera.position.x * 1000f) / 1000f;
                camera.position.y = (float) Math.round(camera.position.y * 1000f) / 1000f;
            }
            // If this is an animated TextureRegion
            if (Mappers.animation.has(entity)) {
                // Handle animation logic here
                if (Mappers.walkingAnimation.has(entity)) {
                    // Animation for walking have the correct orientation already.
                    rotation = 0f;
                    WalkAnimator walkAnimator = Mappers.walkingAnimation.get(entity).animator;

                    int holdingCount = 0;

                    if (Mappers.controllable.has(entity)) {
                        holdingCount = Mappers.controllable.get(entity).currentFood.size();
                    } else if (Mappers.customer.has(entity) && Mappers.customer.get(entity).food != null) {
                        holdingCount = 1;
                    }
                    // Set the rendering texture to the current frame of the animation.
                    toRender = walkAnimator.getFrame(transform.rotation, transform.isMoving, renderingAccumulator,
                            holdingCount);

                } else {
                    // Other animations can be handled like:
                    // Animation<TextureRegion> animation = anMap.get(entity).animation;
                    continue;
                }
            }

            // If by this point there is no texture, skip rendering.
            if (toRender == null) {
                continue;
            }

            // Rendering logic.
            float width = toRender.getRegionWidth();
            float height = toRender.getRegionHeight();

            float originX = width / 2f;
            float originY = height / 2f;

            boolean tint = Mappers.tint.has(entity);

            if (tint) {
                // Apply a tint for this draw call.
                sb.setColor(Mappers.tint.get(entity).tint);
            }

            sb.draw(
                    toRender,
                    transform.position.x - originX,
                    transform.position.y - originY,
                    originX, originY,
                    width, height,
                    transform.scale.x * texture.scale.x * (flip ? -1 : 1),
                    transform.scale.y * texture.scale.y,
                    rotation == null ? transform.rotation : rotation);
            if (tint) {
                sb.setColor(Color.WHITE);
            }
        }
        mapRenderer.renderForeground();
        sb.end();
        entities.clear();
    }

    private void setFoodTransform(Entity station) {
        ArrayList<Entity> foods = Mappers.station.get(station).food;

        Vector3 stationPos = Mappers.transform.get(station).position;

        for (Entity entity : foods) {
            if (entity == null) {
                continue;
            }

            Vector3 foodPos = stationPos.cpy();

            int order = foods.indexOf(entity);

            // move food to different spots on the station
            switch (order) {
                case 0:
                    foodPos.add(-0.5f, 0.65f, 0);
                    break;
                case 1:
                    foodPos.add(-0.1f, 0.65f, 0);
                    break;
                case 2:
                    foodPos.add(0.3f, 0.65f, 0);
                    break;
                case 3:
                    foodPos.add(0.7f, 0.65f, 0);
                    break;
            }

            TransformComponent transformFood = Mappers.transform.get(entity);
            transformFood.position.set(foodPos.cpy());
            
            if (Mappers.station.get(station).type == StationType.cutting_board) {
                transformFood.scale.set(0.4f, 0.4f);
            } else if (Mappers.station.get(station).type == StationType.oven) {
                transformFood.isHidden = true;
            } else {
                transformFood.scale.set(0.5f, 0.5f);
            }
        }
    }

    protected void processEntity(Entity entity, float deltaTime) {
        entities.add(entity);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }
}
