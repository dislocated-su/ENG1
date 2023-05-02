package com.devcharles.piazzapanic;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.CustomerComponent;
import com.devcharles.piazzapanic.components.FoodComponent;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.utility.Difficulty;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.FoodStack;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.components.TransformComponent;

public class SaveLoad {
    private PooledEngine engine;
    private EntityFactory factory;

    private Float[] balance;
    private Integer[] reputation;
    private Difficulty difficulty;
    private Integer[] timer;

    public SaveLoad(PooledEngine engine, World world, Float[] tillBalance, Integer[] reputation, Difficulty difficulty,
            Integer[] timer) {
        this.engine = engine;
        this.factory = new EntityFactory(engine, world);

        this.balance = tillBalance;
        this.reputation = reputation;
        this.difficulty = difficulty;
        this.timer = timer;
    }

    public void save() {
        HashMap<String, List<Entity>> data = select();

        try {
            PrintWriter writer = new PrintWriter("./save.csv", "utf-8");

            // Save reputation
            writer.println(String.join(",", "Reputation", Integer.toString(this.reputation[0])));

            // Save balance
            writer.println(String.join(",", "Balance", Float.toString(this.balance[0])));

            // Save time
            writer.println(String.join(",", "Time", Integer.toString(this.timer[0])));

            // Save difficulty
            switch (this.difficulty) {
                case ENDLESS_EASY:
                    writer.println("Difficulty,Easy");
                    break;
                case ENDLESS_NORMAL:
                    writer.println("Difficulty,Normal");
                    break;
                case ENDLESS_HARD:
                    writer.println("Difficulty,Hard");
                    break;
                default:
                    break;
            }

            // Save players
            List<Entity> players = data.get("players");
            System.out.println("IMPORTANT: " + players.size());
            for (Entity player : players) {
                // Saving player position
                TransformComponent transform = Mappers.transform.get(player);
                writer.println(String.join(",", "Player", Float.toString(transform.position.x),
                        Float.toString(transform.position.y)));

                // Saving player inventory
                FoodStack inventory = Mappers.controllable.get(player).currentFood;
                List<String> stringventory = new ArrayList<>();

                for (Entity food : inventory) {
                    FoodType type = Mappers.food.get(food).type;
                    stringventory.add(FoodComponent.toString(type));
                }

                writer.println(String.join(",", "Inventory", String.join(",", stringventory)));
            }

            writer.close();
        } catch (IOException e) {
        }
    }

    public void load(String saveData) {
        HashMap<String, List<Entity>> data = select();

        int player_counter = 0;
        int inventory_counter = 0;
        int customer_count = 0;

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
                    Float x = Float.parseFloat(vars[1]);
                    Float y = Float.parseFloat(vars[2]);

                    if (player_counter < 2) {
                        Entity player = data.get("players").get(player_counter);
                        Body body = Mappers.b2body.get(player).body;
                        body.setTransform(x, y, 0);
                        ;
                    } else {
                        factory.createCook(Math.round(x), Math.round(y));
                    }

                    player_counter += 1;
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
                case "Order":
                    /*
                     * FoodType dish = FoodComponent.getFood(vars[1]);
                     * int time = Integer.parseInt(vars[2]);
                     * 
                     * Entity customer = factory.createCustomer(new Vector2(100 - customer_count*10,
                     * 25));
                     * CustomerComponent component = Mappers.customer.get(customer);
                     * component.order = dish;
                     * component.setTimer(time);
                     */

                    customer_count++;
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
        }
        scanner.close();
    }

    private HashMap<String, List<Entity>> select() {
        List<Entity> players = new ArrayList<>();
        List<Entity> inventory = new ArrayList<>();
        List<Entity> customers = new ArrayList<>();
        List<Entity> cuttingBoards = new ArrayList<>();
        List<Entity> ovens = new ArrayList<>();
        List<Entity> grills = new ArrayList<>();

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
