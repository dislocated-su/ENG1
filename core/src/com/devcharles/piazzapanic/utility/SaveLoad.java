package com.devcharles.piazzapanic.utility;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.TransformComponent;

public class SaveLoad {
    private World world;
    private Float[] balance;
    private Integer[] reputation;
    private Difficulty difficulty;

    public SaveLoad(World world, Float[] tillBalance, Integer[] reputation, Difficulty difficulty) {
        this.world = world;
        this.balance = tillBalance;
        this.reputation = reputation;
        this.difficulty = difficulty;
    }

    public void save() {
        // TODO

        Family players = Family.all(PlayerComponent.class, TransformComponent.class).get();
        Family customers = Family.all(CustomerComponent.class).get();
        Family stations = Family.all(StationComponent.class).get();

        Family cuttingBoards = stations.getEntitiesFor();
        Family ovens = Family.all(StationComponent.class).get();
        Family grills = Family.all(StationComponent.class).get();

        //inventories = players.inventory
        //time = customers.time
        //reputation = this.reputation
        //balance = this.balance
        //difficulty = this.difficulty
    }

    public void load() {
        // TODO
    }
}
