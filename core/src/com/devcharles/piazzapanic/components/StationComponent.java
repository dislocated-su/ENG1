package com.devcharles.piazzapanic.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class StationComponent implements Component {
    public Entity interactingCook = null;
    public boolean interactable = false;
}
