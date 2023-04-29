package com.devcharles.piazzapanic.components;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Component;

public class FoodComponent implements Component {
    public FoodType type;

    public enum FoodType {
        // These ids correspond to the order of the food in the sprite!
        // price is the price of the dish.
        unformedPatty(1,0),
        formedPatty(2,0),
        grilledPatty(3,0),
        buns(4,0),
        toastedBuns(5,0),
        burger(6,6),
        lettuce(7,0),
        slicedLettuce(8,0),
        tomato(9,0),
        slicedTomato(10,0),
        onion(11,0),
        slicedOnion(12,0),
        salad(13,3),
        potato(14,0),
        butterlessJacketPotato(15,0),
        jacketPotato(16,4),
        butter(17,0),
        dough(18,0),
        rolledDough(19,0),
        tomatoDough(20,0),
        tomatoCheeseDough(21,0),
        pizza(22,11),
        tomatoPaste(23,0),
        cheese(24,0),
        gratedCheese(25,0);

        private int value;
        private float price;

        FoodType(int id, float price) {
            this.value = id;
            this.price= price;
        }

        public int getValue() {
            return value;
        }
        public float getPrice(){return price;}
        private static final Map<Integer, FoodType> _map = new HashMap<Integer, FoodType>();
        static {
            for (FoodType stationType : FoodType.values()) {
                _map.put(stationType.value, stationType);
            }
        }

        /**
         * Get type from id
         * 
         * @param value id value
         * @return Enum type
         */
        public static FoodType from(int value) {
            return _map.get(value);
        }
    }

    static public FoodType getFood(String name) {
        switch (name) {
            case "UnformedPatty":
                return FoodType.unformedPatty;
            case "FormedPatty":
                return FoodType.formedPatty;
            case "GrilledPatty":
                return FoodType.grilledPatty;
            case "Buns":
                return FoodType.buns;
            case "ToastedBuns":
                return FoodType.toastedBuns;
            case "Burger":
                return FoodType.burger;
            case "Lettuce":
                return FoodType.lettuce;
            case "SlicedLettuce":
                return FoodType.slicedLettuce;
            case "Tomato":
                return FoodType.tomato;
            case "SlicedTomato":
                return FoodType.slicedTomato;
            case "Onion":
                return FoodType.onion;
            case "SlicedOnion":
                return FoodType.slicedOnion;
            case "Salad":
                return FoodType.salad;
            case "Potato":
                return FoodType.potato;
            case "ButterlessJacketPotato":
                return FoodType.butterlessJacketPotato;
            case "JacketPotato":
                return FoodType.jacketPotato;
            case "Butter":
                return FoodType.butter;
            case "Dough":
                return FoodType.dough;
            case "RolledDough":
                return FoodType.rolledDough;
            case "TomatoDough":
                return FoodType.tomatoDough;
            case "TomatoCheeseDough":
                return FoodType.tomatoCheeseDough;
            case "Pizza":
                return FoodType.pizza;
            case "TomatoPaste":
                return FoodType.tomatoPaste;
            case "Cheese":
                return FoodType.cheese;
            case "GratedCheese":
                return FoodType.gratedCheese;
        }

        return FoodType.dough;
    }
}
