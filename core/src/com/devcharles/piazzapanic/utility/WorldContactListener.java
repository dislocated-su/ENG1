package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.devcharles.piazzapanic.gameobjects.Cook;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "station" || fixB.getUserData() == "station") {
            Fixture station = fixA.getUserData() == "station" ? fixA : fixB;
            Fixture object = station == fixA ? fixB : fixA;

            if (object.getUserData() != null && Cook.class.isAssignableFrom(object.getUserData().getClass())) {
                ((Cook) object.getUserData()).interactStation();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("End Contact:", "");

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub

    }

}
