package com.devcharles.piazzapanic.components;

import java.util.Stack;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class ControllableComponent implements Component {
    public Stack<Entity> currentFood = new Stack<>();
}
