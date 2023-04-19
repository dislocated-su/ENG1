package com.devcharles.piazzapanic;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.devcharles.piazzapanic.components.ControllableComponent;
import com.devcharles.piazzapanic.components.ItemComponent;
import com.devcharles.piazzapanic.components.PlayerComponent;
import com.devcharles.piazzapanic.components.TransformComponent;
import com.devcharles.piazzapanic.componentsystems.CarryItemsSystem;
import com.devcharles.piazzapanic.componentsystems.CustomerAISystem;
import com.devcharles.piazzapanic.componentsystems.InventoryUpdateSystem;
import com.devcharles.piazzapanic.componentsystems.LightingSystem;
import com.devcharles.piazzapanic.componentsystems.PhysicsSystem;
import com.devcharles.piazzapanic.componentsystems.PlayerControlSystem;
import com.devcharles.piazzapanic.componentsystems.PowerUpSystem;
import com.devcharles.piazzapanic.componentsystems.RenderingSystem;
import com.devcharles.piazzapanic.componentsystems.StationSystem;
import com.devcharles.piazzapanic.utility.Mappers;
import com.devcharles.piazzapanic.utility.saving.GameState;
import com.devcharles.piazzapanic.utility.saving.SavableCook;
import com.devcharles.piazzapanic.utility.saving.SavableFood;

public class EndlessGameScreen extends BaseGameScreen {

  public EndlessGameScreen(PiazzaPanic game, String mapPath, boolean loadSave) {
    super(game, mapPath);
    hud.setEndless(true);
    engine.addSystem(new PhysicsSystem(world));
    engine.addSystem(new RenderingSystem(mapLoader.map, game.batch, camera));
    engine.addSystem(new LightingSystem(rayhandler, camera));
    // This can be commented in during debugging.
    // engine.addSystem(new DebugRendererSystem(world, camera));
    engine.addSystem(new PlayerControlSystem(kbInput));
    engine.addSystem(new StationSystem(kbInput, factory));
    CustomerAISystem aiSystem =
        new CustomerAISystem(mapLoader.getObjectives(), world, factory, hud,
            reputationPointsAndMoney,
            true, 3);
    engine.addSystem(aiSystem);
    engine.addSystem(new CarryItemsSystem());
    engine.addSystem(new InventoryUpdateSystem(hud));
    PowerUpSystem powerUpSystem = new PowerUpSystem();
    engine.addSystem(powerUpSystem);
    hud.initShop(powerUpSystem);

    if (loadSave) {
      FileHandle saveFile = Gdx.files.local(GameState.SAVE_LOCATION);
      Json json = new Json();
      GameState gameSave = json.fromJson(GameState.class, saveFile.readString());

      // Load stations
      for (String key : gameSave.getStations().keySet()) {
        Mappers.station.get(stationsMap.get(Integer.valueOf(key)))
            .copyValues(gameSave.getStations().get(key).toStationComponent(factory), engine);
      }

      // Load cooks
      engine.removeAllEntities(
          Family.all(TransformComponent.class, ControllableComponent.class).get());
      for (int i = 0; i < gameSave.getCooks().size(); i++) {
        SavableCook savedCook = gameSave.getCooks().get(i);
        Entity cook = factory.createCook((int) savedCook.transformComponent.position.x,
            (int) savedCook.transformComponent.position.y);

        ControllableComponent controllableComponent = Mappers.controllable.get(cook);
        for (SavableFood savableFood : gameSave.getCooks().get(i).foodStack) {
          Entity foodEntity = savableFood.toEntity(factory);
          ItemComponent itemComponent = engine.createComponent(ItemComponent.class);
          itemComponent.holderTransform = Mappers.transform.get(cook);
          foodEntity.add(itemComponent);
          controllableComponent.currentFood.push(foodEntity);
        }
        controllableComponent.speedModifier = savedCook.speedModifier;
        if (i == 0) {
          cook.add(engine.createComponent(PlayerComponent.class));
        }
      }

      reputationPointsAndMoney[0] = gameSave.getReputation();
      reputationPointsAndMoney[1] = gameSave.getMoney();

      // Load customerAISystem
      aiSystem.loadFromSave(gameSave.getCustomerAISystem());

      // Load powerUpSystem
      powerUpSystem.loadFromSave(gameSave.getPowerUpSystem());

      // Load hud save details
      hud.loadFromSave(gameSave);
    }
  }
}
