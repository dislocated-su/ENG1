package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.interfaces.Renderable;
import com.devcharles.piazzapanic.utility.MapBodyBuilder;

public class GameWorld implements Renderable {

    TiledMap map;

    OrthogonalTiledMapRenderer renderer;
    
    OrthographicCamera camera;

    final int ppt = 16;

    final int scale = 32 / ppt;

    public GameWorld(World world, OrthographicCamera mainCamera, SpriteBatch batch) {
        this.camera = mainCamera;

        map = new TmxMapLoader().load("testMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1f/ppt);

        MapBodyBuilder.buildShapes(map, ppt, world);
    }

    @Override
    public void render(SpriteBatch batch) {

        renderer.setView(camera);

        for (MapObject object: map.getLayers().get("Obstacles").getObjects()) {
            if (object instanceof TiledMapTileMapObject) {
                TiledMapTileMapObject tileobject = (TiledMapTileMapObject) object;
                batch.draw(tileobject.getTextureRegion(), tileobject.getX() * 1f/ppt, tileobject.getY() * 1f/ppt, scale, scale);
            }
        }
        renderer.render();

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}
