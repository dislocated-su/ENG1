package com.devcharles.piazzapanic.utility;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.StationComponent;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Pair<StationComponent, Entity> pair = stationInteractResolver(contact);
        // Gdx.app.log("End Contact", "");

        if (pair == null) {
            return;
        }

        pair.first.interactable = true;
        pair.first.interactingCook = pair.second;
    }

    @Override
    public void endContact(Contact contact) {
        Pair<StationComponent, Entity> pair = stationInteractResolver(contact);
        // Gdx.app.log("End Contact", "");

        if (pair == null) {
            return;
        }

        pair.first.interactable = false;
        pair.first.interactingCook = null;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub

    }

    private Pair<StationComponent, Entity> stationInteractResolver(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        Object objA = fixA.getUserData();
        Object objB = fixB.getUserData();

        if (objA == null || objB == null) {
            return null;
        }

        boolean objAvalid = (StationComponent.class.isAssignableFrom(objA.getClass()));
        boolean objBvalid = (StationComponent.class.isAssignableFrom(objB.getClass()));

        if (objAvalid || objBvalid) {
            Object station = objAvalid ? objA : objB;
            Entity cook = station == objA ? ((Entity) objB) : ((Entity) objA);

            if (cook != null && cook.getComponent(PlayerComponent.class) != null) {
                // Gdx.app.log("cook-station contact", "");
                return new Pair<StationComponent, Entity>((StationComponent) station, cook);
            }
        }
        return null;
    }

}
