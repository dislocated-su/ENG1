package com.devcharles.piazzapanic.components;

import java.util.Map;
import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class StationComponent implements Component {
    public Entity interactingCook = null;
    public boolean interactable = false;
    public StationType type;
    public Entity food;

    public enum StationType {
        oven(1),
        grill(2),
        cutting_board(3),
        sink(4),
        bin(5),
        ingredient(6);

        private int value;

        StationType(int v) {
            this.value = v;
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
