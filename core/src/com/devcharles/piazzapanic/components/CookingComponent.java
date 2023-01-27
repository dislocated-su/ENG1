package com.devcharles.piazzapanic.components;

import com.badlogic.ashley.core.Component;
import com.devcharles.piazzapanic.utility.GdxTimer;

public class CookingComponent implements Component {
    public GdxTimer timer = new GdxTimer(5000, false);
}
