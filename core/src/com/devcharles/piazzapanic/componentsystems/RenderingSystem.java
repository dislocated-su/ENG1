package com.devcharles.piazzapanic.componentsystems;

import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.devcharles.piazzapanic.components.TextureComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.WalkAnimator;
import com.devcharles.piazzapanic.utility.WorldTilemapRenderer;
import com.devcharles.piazzapanic.utility.ZComparator;
import com.devcharles.piazzapanic.utility.Station.StationType;

public class RenderingSystem extends IteratingSystem {

    private SpriteBatch sb;
    private Array<Entity> entities;
    private OrthographicCamera camera;

    private Comparator<Entity> comparator;

    private float renderingAccumulator = 0f;

    private WorldTilemapRenderer mapRenderer;

    public RenderingSystem(TiledMap map, SpriteBatch batch, OrthographicCamera camera) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get());
        // set up reuired objects
        this.sb = batch;
        this.camera = camera;
        this.mapRenderer = new WorldTilemapRenderer(map, camera, batch);
        this.comparator = new ZComparator();

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

            if (transform.isHidden) {
                continue;
            }

            TextureRegion toRender = texture.region;
            Float rotation = null;
            boolean flip = false;

            if (Mappers.station.has(entity)) {
                renderStationFood(entity);
            }

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

                    int holdingCount = 0;

                    if (Mappers.controllable.has(entity)) {
                        holdingCount = Mappers.controllable.get(entity).currentFood.size();
                    } else if (Mappers.customer.has(entity) && Mappers.customer.get(entity).food != null) {
                        holdingCount = 1;
                    }
                    toRender = walkAnimator.getFrame(transform.rotation, transform.isMoving, renderingAccumulator,
                            holdingCount);

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

            boolean tint = Mappers.tint.has(entity);

            if (tint) {
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

    private void renderStationFood(Entity station) {
        ArrayList<Entity> foods = Mappers.station.get(station).food;
        // Station.StationType = Mappers.station.get(station).type;

        Vector3 stationPos = Mappers.transform.get(station).position;

        for (Entity entity : foods) {
            if (entity == null) {
                continue;
            }

            Vector3 foodPos = stationPos.cpy();

            int order = foods.indexOf(entity);

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
            transformFood.position.set(foodPos);
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
