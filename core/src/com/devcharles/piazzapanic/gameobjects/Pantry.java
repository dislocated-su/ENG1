package com.devcharles.piazzapanic.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class Pantry extends Station {

    public Food foodType;

    public Pantry(Texture texture, World world, float x, float y, Food food) {
        super(texture, world, x, y);
        foodType = food;
    }

    @Override
    public void interactStation(Cook cook) {
        cook.carryingFood.add(foodType);
        Gdx.app.log("Pantry Interaction", "");
    }
}
