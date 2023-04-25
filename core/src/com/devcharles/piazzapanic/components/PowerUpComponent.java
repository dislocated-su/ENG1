package com.devcharles.piazzapanic.components;

import com.badlogic.ashley.core.Component;
import java.util.HashMap;
import java.util.Map;

public class PowerUpComponent implements Component {
    
    public PowerUpType type;

    public enum PowerUpType {
        
        BinACustomer(1),
        DoublePoints(2),
        InstaCook(3),
        SpeedBoost(4),
        TimeFreeze(5);
        

        private int value;

        PowerUpType(int id){
            this.value = id;
        }


        public int getValue(){
            return value;
        }
    
        private static final Map<Integer, PowerUpType> _map  = new HashMap<Integer, PowerUpType>();
        static {
            for (PowerUpType powerupType : PowerUpType.values()){
                _map .put(powerupType.value, powerupType);
            }
        }
    
        public static PowerUpType from(int value){
            return _map .get(value);
        }


    }

    

}

