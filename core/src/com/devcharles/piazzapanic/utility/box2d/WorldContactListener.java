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

/**
 * Handles collision events, allows interactivity between the player and other objects.
 */
public class WorldContactListener implements ContactListener {

  @Override
  public void beginContact(Contact contact) {
    Pair<StationComponent, Entity> stationCook = stationInteractResolver(contact);
    if (stationCook != null) {
      stationCook.first.interactingCook = stationCook.second;
      return;
    }

    Pair<Entity, Entity> customerCook = customerInteractResolver(contact);
    if (customerCook != null) {
      // Gdx.app.log("Begin contact", "Cook+Customer");
      Mappers.customer.get(customerCook.first).interactingCook = customerCook.second;
    }
  }

  @Override
  public void endContact(Contact contact) {
    Pair<StationComponent, Entity> stationCook = stationInteractResolver(contact);
    if (stationCook != null) {
      stationCook.first.interactingCook = null;
      return;
    }

    Pair<Entity, Entity> customerCook = customerInteractResolver(contact);
    if (customerCook != null) {
      // Gdx.app.log("End contact", "Cook+Customer");
      Mappers.customer.get(customerCook.first).interactingCook = null;
    }

  }

  protected Pair<StationComponent, Entity> stationInteractResolver(Contact contact) {
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

      if (player != null) {
        player.putDown = false;
        player.pickUp = false;
        return new Pair<>((StationComponent) station, cook);
      }
    }
    return null;
  }

  protected Pair<Entity, Entity> customerInteractResolver(Contact contact) {
    Object objA = contact.getFixtureA().getUserData();
    Object objB = contact.getFixtureB().getUserData();

    if (objA == null || objB == null) {
      return null;
    }

    boolean objAEntity = (Entity.class.isAssignableFrom(objA.getClass()));
    boolean objBEntity = (Entity.class.isAssignableFrom(objB.getClass()));

    if (!objAEntity || !objBEntity) {
      return null;
    }

    Entity a = (Entity) objA;
    Entity b = (Entity) objB;

    if (Mappers.customer.has(a) || Mappers.customer.has(b)) {
      Entity customer = Mappers.customer.has(a) ? a : b;
      Entity cook = (customer == a) ? b : a;

      PlayerComponent player = Mappers.player.get(cook);

      if (player != null) {
        player.putDown = false;
        return new Pair<>(customer, cook);
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
