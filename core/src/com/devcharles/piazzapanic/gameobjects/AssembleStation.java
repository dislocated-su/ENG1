package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class AssembleStation extends Station {

    public AssembleStation(Texture texture, World world, float x, float y) {
        super(texture, world, x, y);
    }

    @Override
    public void interactStation(Cook cook) {
        Gdx.app.log("Cutting Board Interaction", "");
        if (cook.getTopFood() == null) {

        }
    }

}
