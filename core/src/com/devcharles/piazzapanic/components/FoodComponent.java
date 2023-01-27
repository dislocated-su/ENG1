package com.devcharles.piazzapanic.components;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Component;

public class FoodComponent implements Component {
    public FoodType type;

    public enum FoodType {
        lettuce(1),
        tomato(2),
        onion(3),
        unformedPatty(4),
        buns(5),
        slicedLettuce(6),
        slicedTomato(7),
        slicedOnion(8),
        formedPatty(9),
        grilledPatty(10),
        toastedBuns(11),
        burger(12),
        salad(13);

        
        private int value;

        FoodType(int id) {
            this.value = id;
        }

        public int getValue() {
            return value;
        }

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
}
