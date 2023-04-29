package com.devcharles.piazzapanic.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.utility.EntityFactory;

public class SaveLoad {
    private PooledEngine engine;
    private World world;
    private EntityFactory factory;

    private Float[] balance;
    private Integer[] reputation;
    private Difficulty difficulty;
    private Integer[] timer;

    public SaveLoad(PooledEngine engine, World world, Float[] tillBalance, Integer[] reputation, Difficulty difficulty, Integer[] timer) {
        this.engine = engine;
        this.world = world;
        this.factory = new EntityFactory(engine, world);

        this.balance = tillBalance;
        this.reputation = reputation;
        this.difficulty = difficulty;
        this.timer = timer;
    }

    public void save() {
        // TODO
        HashMap<String, List<Entity>> data = select();
        System.out.println(Arrays.asList(data));

        // Save players
        List<Entity> players = data.get("chefs");
        for (Entity player : players) {
            Vector3 position = Mappers.transform.get(player).position;
            //save("player", position.x, position.y, position.z)
        }

        //List<Entity> customers = new ArrayList<>();
        //List<Entity> cuttingBoards = new ArrayList();
        //List<Entity> ovens = new ArrayList();
        //List<Entity> grills = new ArrayList();


        //inventories = players.inventory
        //time = customers.time
        //reputation = this.reputation
        //balance = this.balance
        //difficulty = this.difficulty
    }

    public void load(String saveData) {
        // TODO
        HashMap<String, List<Entity>> data = select();

        int player_counter = 0;
        int inventory_counter = 0;
        
        Scanner scanner = new Scanner(saveData);
        while (scanner.hasNextLine()) {
          String[] vars = scanner.nextLine().split(",");

          switch (vars[0]) {
            case "Reputation":
                this.reputation[0] = Integer.parseInt(vars[1]);
                break;
            case "Balance":
                this.balance[0] = Float.parseFloat(vars[1]);
                break;
            case "Time":
                this.timer[0] = Integer.parseInt(vars[1]);
                break;
            case "Difficulty":
                switch (vars[1]) {
                    case "Easy":
                        this.difficulty = Difficulty.ENDLESS_EASY;
                    case "Normal":
                        this.difficulty = Difficulty.ENDLESS_NORMAL;
                        break;
                    case "Hard":
                        this.difficulty = Difficulty.ENDLESS_HARD;
                        break;
                }
                break;
            case "Player":
                Entity player = data.get("players").get(player_counter);
                Vector3 position = Mappers.transform.get(player).position;
                Body bodyC = Mappers.b2body.get(player).body;
                //System.out.println(position);
                position.x = Float.parseFloat(vars[1]);
                position.y = Float.parseFloat(vars[2]);
                position.z = Float.parseFloat(vars[3]);
                //System.out.println(position);

                player_counter = (player_counter + 1) % 2;
                break;
            case "Inventory":
                Entity guy = data.get("players").get(inventory_counter);
                FoodStack foodStack = Mappers.controllable.get(guy).currentFood;

                for (int i = 1; i < vars.length; i++) {
                    String food_name = vars[i];
                    Entity food = this.factory.createFood(FoodComponent.getFood(food_name));
                    foodStack.pushItem(food, guy);
                }

                inventory_counter = (inventory_counter + 1) % 2;
                break;
            case "Orders":
                for (Entity customer : data.get("customers")) {
                    CustomerComponent customerComponent = Mappers.customer.get(customer);
                    customerComponent.order = FoodType.buns;
                    //int time = customerComponent.timer.getElapsed();
                }
                break;
            case "CuttingBoard":
                break;
            case "Oven":
                break;
            case "Grill":
                break;
            default:
                break;
          }
          // process the line
        }
        scanner.close();
        
        //inventories = players.inventory
        //time = customers.time
        //reputation = this.reputation
        //balance = this.balance
        //difficulty = this.difficulty
    }
    
    private HashMap<String, List<Entity>> select() {
        List<Entity> players = new ArrayList<>();
        List<Entity> inventory = new ArrayList<>();
        List<Entity> customers = new ArrayList<>();
        List<Entity> cuttingBoards = new ArrayList();
        List<Entity> ovens = new ArrayList();
        List<Entity> grills = new ArrayList();

        // Select players
        ImmutableArray<Entity> playersList = this.engine.getEntitiesFor(Family.all(ControllableComponent.class).get());
        for (Entity player : playersList) {
            players.add(player);
            FoodStack foodStack = Mappers.controllable.get(player).currentFood;
            for (Entity food : foodStack) {
                inventory.add(food);
            }
        }

        // Select customers
        ImmutableArray<Entity> customersList = this.engine.getEntitiesFor(Family.all(CustomerComponent.class).get());
        for (Entity customer : customersList) {
            customers.add(customer);
            //CustomerComponent customerComponent = Mappers.customer.get(customer);
            //FoodType order = customerComponent.order;
            //int time = customerComponent.timer.getElapsed();
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
                default:
                    break;
            }
        }

        HashMap<String, List<Entity>> data = new HashMap<String, List<Entity>>();
        data.put("players", players);
        data.put("inventory", inventory);
        data.put("customers", customers);
        data.put("cuttingBoards", cuttingBoards);
        data.put("ovens", ovens);
        data.put("grills", grills);
        return data;
    }
}
