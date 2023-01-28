package com.devcharles.piazzapanic.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.devcharles.piazzapanic.utility.GdxTimer;

public class CookingComponent implements Component, Poolable {
    public GdxTimer timer = new GdxTimer(5000, false);
    /**
     * If patty is flipped, onion is chopped, etc.
     */
    public boolean processed = false;

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        processed = false;
    }
}
