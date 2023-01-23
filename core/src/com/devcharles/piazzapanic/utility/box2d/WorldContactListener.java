package com.devcharles.piazzapanic.utility.box2d;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.StationComponent;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.Pair;

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

    private Pair<StationComponent, Entity> stationInteractResolver(Contact contact) {
        Object objA = contact.getFixtureA().getUserData();
        Object objB = contact.getFixtureB().getUserData();

        if (objA == null || objB == null) {
            return null;
        }

        boolean objAStation = (StationComponent.class.isAssignableFrom(objA.getClass()));
        boolean objBStation = (StationComponent.class.isAssignableFrom(objB.getClass()));

        if (objAStation || objBStation) {
            Object station = objAStation ? objA : objB;
            Entity cook = station == objA ? ((Entity) objB) : ((Entity) objA);

            PlayerComponent player = Mappers.player.get(cook);

            if (cook != null && player != null) {
                player.interacting = false;
                return new Pair<StationComponent, Entity>((StationComponent) station, cook);
            }
        }
        return null;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}
