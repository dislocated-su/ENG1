package com.devcharles.piazzapanic.utility;

import java.util.Map;

import com.devcharles.piazzapanic.components.FoodComponent.FoodType;

import java.util.HashMap;

public class Station {

    public StationType type;

    public static HashMap<Object, FoodType> grillRecipes = new HashMap<Object, FoodType>() {
        {
            put(FoodType.formedPatty, FoodType.grilledPatty);
            put(FoodType.buns, FoodType.toastedBuns);
        }
    };

    public static HashMap<Object, FoodType> cuttingBoardRecipes = new HashMap<Object, FoodType>() {
        {
            put(FoodType.tomato, FoodType.slicedTomato);
            put(FoodType.lettuce, FoodType.slicedLettuce);
        }
    };

    public static HashMap<Object, FoodType> serveRecipes = new HashMap<Object, FoodType>() {
        {
            
        }
    };
    public static Map<StationType, HashMap<Object, FoodType>> recipeMap = new HashMap<StationType, HashMap<Object, FoodType>>() {
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

    public Station() {

    }

}
