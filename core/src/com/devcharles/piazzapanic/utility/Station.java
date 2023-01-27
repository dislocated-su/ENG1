package com.devcharles.piazzapanic.utility;

import java.util.Map;
import java.util.Set;

import com.devcharles.piazzapanic.components.FoodComponent.FoodType;

import java.util.HashMap;
import java.util.HashSet;

public class Station {

    public StationType type;

    public static HashMap<FoodType, FoodType> grillRecipes = new HashMap<FoodType, FoodType>() {
        {
            put(FoodType.formedPatty, FoodType.grilledPatty);
            put(FoodType.buns, FoodType.toastedBuns);
        }
    };

    public static HashMap<FoodType, FoodType> cuttingBoardRecipes = new HashMap<FoodType, FoodType>() {
        {
            put(FoodType.tomato, FoodType.slicedTomato);
            put(FoodType.lettuce, FoodType.slicedLettuce);
            put(FoodType.unformedPatty, FoodType.formedPatty);
        }
    };

    public static HashMap<Set<FoodType>, FoodType> serveRecipes = new HashMap<Set<FoodType>, FoodType>() {
        {
            put(new HashSet<FoodType>() {
                {
                    add(FoodType.toastedBuns);
                    add(FoodType.grilledPatty);
                }
            }, FoodType.burger);
            put(new HashSet<FoodType>() {
                {
                    add(FoodType.slicedLettuce);
                    add(FoodType.slicedOnion);
                    add(FoodType.slicedTomato);
                }
            }, FoodType.salad);
        }
    };
    public static Map<StationType, HashMap<FoodType, FoodType>> recipeMap = new HashMap<StationType, HashMap<FoodType, FoodType>>() {
        {
            put(StationType.grill, grillRecipes);
            put(StationType.cutting_board, cuttingBoardRecipes);
        }
    };

    public enum StationType {
        oven(1),
        grill(2),
        cutting_board(3),
        sink(4),
        bin(5),
        ingredient(6),
        serve(7);

        private int value;

        StationType(int id) {
            this.value = id;
        }

        public int getValue() {
            return value;
        }

        private static final Map<Integer, StationType> _map = new HashMap<Integer, StationType>();
        static {
            for (StationType stationType : StationType.values()) {
                _map.put(stationType.value, stationType);
            }
        }

        /**
         * Get type from id
         * 
         * @param value id value
         * @return Enum type
         */
        public static StationType from(int value) {
            return _map.get(value);
        }
    }
}
