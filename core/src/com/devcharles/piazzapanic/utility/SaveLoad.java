package com.devcharles.piazzapanic.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.utility.Mappers;

public class SaveLoad {
    private PooledEngine engine;
    private World world;
    private Float[] balance;
    private Integer[] reputation;
    private Difficulty difficulty;

    public SaveLoad(PooledEngine engine, World world, Float[] tillBalance, Integer[] reputation, Difficulty difficulty) {
        this.engine = engine;
        this.world = world;
        this.balance = tillBalance;
        this.reputation = reputation;
        this.difficulty = difficulty;
    }

    public void save() {
        // TODO
        HashMap<String, List<Entity>> data = select();
        System.out.println(Arrays.asList(data));

        //inventories = players.inventory
        //time = customers.time
        //reputation = this.reputation
        //balance = this.balance
        //difficulty = this.difficulty
    }

    public void load() {
        // TODO
        HashMap<String, List<Entity>> data = select();
        System.out.println(Arrays.asList(data));

        //inventories = players.inventory
        //time = customers.time
        //reputation = this.reputation
        //balance = this.balance
        //difficulty = this.difficulty
    }
    
    private HashMap<String, List<Entity>> select() {
        List<Entity> players = new ArrayList<>();
        List<Entity> customers = new ArrayList<>();
        List<Entity> cuttingBoards = new ArrayList();
        List<Entity> ovens = new ArrayList();
        List<Entity> grills = new ArrayList();

        // Select players
        ImmutableArray<Entity> playersList = this.engine.getEntitiesFor(Family.all(ControllableComponent.class).get());
        for (Entity player : playersList) {
            FoodStack inventory = Mappers.controllable.get(player).currentFood;
            customers.add(player);

            for (Entity food : inventory) {
                FoodComponent foodComponent = Mappers.station.get(food);
                switch (foodComponent.type) {
                    case unformedPatty:
                        inventory.add("unformedPatty");
                        break;
                    case formedPatty:
                        inventory.add("formedPatty");
                        break;
                    case grilledPatty:
                        inventory.add("grilledPatty");
                        break;
                    case buns:
                        inventory.add("buns");
                        break;
                    case toastedBuns:
                        inventory.add("toastedBuns");
                        break;
                    case burger:
                        inventory.add("burger");
                        break;
                    case lettuce:
                        inventory.add("lettuce");
                        break;
                    case slicedLettuce:
                        inventory.add("slicedLettuce");
                        break;
                    case tomato:
                        inventory.add("tomato");
                        break;
                    case slicedTomato:
                        inventory.add("slicedTomato");
                        break;
                    case onion:
                        inventory.add("onion");
                        break;
                    case slicedOnion:
                        inventory.add("slicedOnion");
                        break;
                    case salad:
                        inventory.add("salad");
                        break;
                    case potato:
                        inventory.add("potato");
                        break;
                    case butterlessJacketPotato:
                        inventory.add("butterlessJacketPotato");
                        break;
                    case jacketPotato:
                        inventory.add("jacketPotato");
                        break;
                    case butter:
                        inventory.add("butter");
                        break;
                    case dough:
                        inventory.add("dough");
                        break;
                    case rolledDough:
                        inventory.add("rolledDough");
                        break;
                    case tomatoDough:
                        inventory.add("tomatoDough");
                        break;
                    case tomatoCheeseDough:
                        inventory.add("tomatoCheeseDough");
                        break;
                    case pizza:
                        inventory.add("pizza");
                        break;
                    case tomatoPaste:
                        inventory.add("tomatoPaste");
                        break;
                    case cheese:
                        inventory.add("cheese");
                        break;
                    case gratedCheese:
                        inventory.add("gratedCheese");
                        break;
                }
            }
        }

        // Select customers
        ImmutableArray<Entity> customersList = this.engine.getEntitiesFor(Family.all(CustomerComponent.class).get());
        for (Entity customer : customersList) {
            customers.add(customer);
        }

        // Select stations
        ImmutableArray<Entity> stationsList = this.engine.getEntitiesFor(Family.all(StationComponent.class).get());
        for (Entity station : stationsList) {
            StationComponent stationComponent = Mappers.station.get(station);
            switch (stationComponent.type) {
                case cutting_board:
                    cuttingBoards.add(station);
                    break;
                case oven:
                    ovens.add(station);
                    break;
                case grill:
                    grills.add(station);
                    break;
            }
        }

        HashMap<String, List<Entity>> data = new HashMap<String, List<Entity>>();
        data.put("chefs", players);
        data.put("customers", customers);
        data.put("cuttingBoards", cuttingBoards);
        data.put("ovens", ovens);
        data.put("grills", grills);
        return data;
    }
}
