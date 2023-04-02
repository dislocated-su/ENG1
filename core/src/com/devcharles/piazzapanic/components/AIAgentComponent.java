package com.devcharles.piazzapanic.components;

import com.badlogic.ashley.core.Component;
import com.devcharles.piazzapanic.utility.box2d.Box2dSteeringBody;

public class AIAgentComponent implements Component {
    public Box2dSteeringBody steeringBody;
    public int currentObjective = 0;
    public int slot = 0;
}