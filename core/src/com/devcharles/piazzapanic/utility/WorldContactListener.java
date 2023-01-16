package com.devcharles.piazzapanic.utility;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.devcharles.piazzapanic.gameobjects.Cook;
import com.devcharles.piazzapanic.gameobjects.Station;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Pair<Cook, Station> pair = stationInteractResolver(contact);

        if (pair == null) {
            return;
        }

        pair.first.currentStation = pair.second;
    }

    @Override
    public void endContact(Contact contact) {
        Pair<Cook, Station> pair = stationInteractResolver(contact);

        if (pair == null) {
            return;
        }

        pair.first.currentStation = null;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub

    }

    private Pair<Cook, Station> stationInteractResolver(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        Object objA = fixA.getUserData();
        Object objB = fixB.getUserData();

        if (objA == null || objB == null) {
            return null;
        }

        boolean objAvalid = Station.class.isAssignableFrom(objA.getClass());
        boolean objBvalid = Station.class.isAssignableFrom(objB.getClass());


        if (objAvalid || objBvalid) {
            Fixture station = objAvalid ? fixA : fixB;
            Fixture cook = station == fixA ? fixB : fixA;

            if (cook.getUserData() != null && Cook.class.isAssignableFrom(cook.getUserData().getClass())) {
                return (new Pair<Cook, Station>((Cook) cook.getUserData(), (Station) station.getUserData()));
            }
        }
        return null;
    }

}
