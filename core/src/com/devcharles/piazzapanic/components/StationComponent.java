package com.devcharles.piazzapanic.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.devcharles.piazzapanic.utility.Station.StationType;

public class StationComponent implements Component {
    public Entity interactingCook = null;
    public boolean interactable = false;
    public StationType type;
    public Entity food;
}
