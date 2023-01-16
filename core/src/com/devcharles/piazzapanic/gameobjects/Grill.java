package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class Grill extends Station {
    public Grill(Texture texture, World world, float x, float y) {
        super(texture, world, x, y);
    }

    @Override
    public void interactStation(Cook cook) {
        Gdx.app.log("Grill Interaction", "");
        if (cook.getTopFood() == null) {

        }
    }
}