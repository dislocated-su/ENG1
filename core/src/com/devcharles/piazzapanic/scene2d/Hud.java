package com.devcharles.piazzapanic.scene2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.devcharles.piazzapanic.BaseGameScreen;
import com.devcharles.piazzapanic.MainMenuScreen;
import com.devcharles.piazzapanic.PiazzaPanic;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.componentsystems.PowerUpSystem;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.saving.GameState;
import java.util.ArrayList;

/**
 * HUD user interface rendering for the game, also includes the win screen.
 */
public class Hud extends ApplicationAdapter {

  public Stage stage;
  private final Viewport viewport;
  private Integer customerTimer = 0;
  private float timeCounter = 0;
  private final Integer[] reputationAndMoney;

  private boolean isEndless = false;

  // A label is basically a widget
  LabelStyle hudLabelStyle;
  LabelStyle titleLabelStyle;
  Label timerLabel;
  Label timeNameLabel;
  Label moneyLabel;
  Label moneyNameLabel;
  Label reputationLabel;
  Label reputationNameLabel;
  Label pausedNameLabel;
  TextButton saveButton, shopButton, pauseButton;
  final TextButton movementSpeed, prepSpeed, chopSpeed, customerPatience, salePrice;
  BitmapFont uiFont, uiTitleFont;
  // an image used as the background of recipe book and tutorial
  private Image photo;

  private final PiazzaPanic game;
  private Table tableBottom, tableRight, shopTable, tableTop, tablePause, tableBottomLabel;

  private boolean pauseToggled = false;
  public boolean paused = false;
  public boolean isShopOpen = false;

  private final BaseGameScreen gameScreen;
  private int numCustomersServed = 0;
  private PowerUpSystem powerUpSystem;

  /**
   * Create the hud.
   *
   * @param spriteBatch      the batch to draw the HUD with
   * @param savedGame        reference to the screen drawing the hud to switch back to in case of
   *                         screen transitions.
   * @param game             {@link PiazzaPanic} instance for switching screens.
   * @param reputationPoints Must be an object to pass by reference, see <a
   *                         href="https://stackoverflow.com/questions/3326112/java-best-way-to-pass-int-by-reference">...</a>
   */
  public Hud(SpriteBatch spriteBatch, final BaseGameScreen savedGame, final PiazzaPanic game,
      Integer[] reputationPoints) {
    this.game = game;
    this.reputationAndMoney = reputationPoints;
    this.gameScreen = savedGame;

    // Setup the viewport
    viewport = new ScreenViewport(new OrthographicCamera(1280, 720));
    stage = new Stage(viewport, spriteBatch);
    viewport.apply();

    // Import the custom skin with different fonts
    uiFont = game.assetManager.get("craftacular/raw/font-export.fnt", BitmapFont.class);
    uiTitleFont = game.assetManager.get("craftacular/raw/font-title-export.fnt");

    // Create generic style for labels with the different fonts
    hudLabelStyle = new Label.LabelStyle();
    hudLabelStyle.font = uiFont;
    titleLabelStyle = new Label.LabelStyle();
    titleLabelStyle.font = uiTitleFont;

    movementSpeed = new TextButton("$20 - Movement Speed", game.skin);
    prepSpeed = new TextButton("$20 - Preparation Speed", game.skin);
    chopSpeed = new TextButton("$20 - Chopping Speed", game.skin);
    salePrice = new TextButton("$20 - Order Income", game.skin);
    customerPatience = new TextButton("$20 - Customer Patience", game.skin);

    stage.addListener(new InputListener() {
      @Override
      public boolean keyDown(InputEvent event, int keycode) {
        if (keycode == Keys.ESCAPE) {
          if (isShopOpen) {
            hideShop();
          } else {
            pauseToggled = true;
          }
        } else if (keycode == Keys.TAB) {
          if (isShopOpen) {
            hideShop();
          } else {
            showShop();
          }
          // sets game to go bigscreen if F11 is pressed or sets it to go small screen
        } else if (keycode == Keys.F11) {
          boolean fullScreen = Gdx.graphics.isFullscreen();
          Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
          if (fullScreen) {
            Gdx.graphics.setWindowedMode(1280, 720);
          } else {
            Gdx.graphics.setFullscreenMode(currentMode);
          }
        }
        return true;
      }
    });

    // Create the UI layout.
    createTables();
  }

  private void saveGame() {
    GameState state = new GameState();
    state.setFromEngine(gameScreen.getEngine());
    state.setCustomerTimer(customerTimer);
    state.setNumCustomersServed(numCustomersServed);

    state.setReputation(reputationAndMoney[0]);
    state.setMoney(reputationAndMoney[1]);

    FileHandle saveFile = Gdx.files.local(GameState.SAVE_LOCATION);

    Json json = new Json();
    saveFile.writeString(json.toJson(state, GameState.class), false);
  }

  public void loadFromSave(GameState savedGame) {
    customerTimer = savedGame.getCustomerTimer();
    timerLabel.setText(String.format("%03d", customerTimer));
    numCustomersServed = savedGame.getNumCustomersServed();
    moneyLabel.setText(String.format("$%d", reputationAndMoney[1]));
    reputationLabel.setText(reputationAndMoney[0]);
  }

  private void createTables() {
    pauseButton = new TextButton("Pause", game.skin);
    shopButton = new TextButton("Shop", game.skin);
    shopButton.setVisible(false);

    pauseButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        pauseToggled = true;
      }
    });

    shopButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        showShop();
      }
    });

    timerLabel = new Label(String.format("%03d", customerTimer), hudLabelStyle);
    moneyNameLabel = new Label("Money", hudLabelStyle);
    reputationLabel = new Label(String.format("%01d", reputationAndMoney[0]), hudLabelStyle);
    timeNameLabel = new Label("Time", hudLabelStyle);
    moneyLabel = new Label(String.format("$%d", reputationAndMoney[1]), hudLabelStyle);
    reputationNameLabel = new Label("Reputation", hudLabelStyle);
    // Creates a bunch of labels and sets the fontsize
    float fontScale = 0.6f;
    reputationLabel.setFontScale(fontScale + 0.1f);
    moneyLabel.setFontScale(fontScale + 0.1f);
    timerLabel.setFontScale(fontScale + 0.1f);
    timeNameLabel.setFontScale(fontScale + 0.1f);
    moneyNameLabel.setFontScale(fontScale + 0.1f);
    reputationNameLabel.setFontScale(fontScale + 0.1f);

    moneyLabel.setVisible(false);
    moneyNameLabel.setVisible(false);

    // lays out timer and reputation
    tableTop = new Table();
    tableTop.top();
    tableTop.setFillParent(true);

    tableTop.add(shopButton).width(120).padTop(10).padLeft(10);
    tableTop.add(timeNameLabel).expandX().padTop(10);
    tableTop.add(moneyNameLabel).expandX().padTop(10);
    tableTop.add(reputationNameLabel).expandX().padTop(10);
    tableTop.add(pauseButton).width(120).padTop(10).padRight(10);

    tableTop.row();
    tableTop.add().width(120).padLeft(10);
    tableTop.add(timerLabel).expandX();
    tableTop.add(moneyLabel).expandX();
    tableTop.add(reputationLabel).expandX();
    tableTop.add().width(120).padRight(10);

    tableBottomLabel = new Table();
    tableBottomLabel.bottom();
    tableBottomLabel.setFillParent(true);

    Label inv = new Label("Inventory", hudLabelStyle);
    inv.setFontScale(fontScale);
    tableBottomLabel.add(inv).padBottom(60);
    // the pause screen
    tablePause = new Table();
    tablePause.setFillParent(true);
    tablePause.setVisible(false); // not visible by default

    pausedNameLabel = new Label("Game paused", titleLabelStyle);
    tablePause.add(pausedNameLabel).padBottom(30);

    tablePause.row();
    // checks if resume button is clicked
    TextButton resumeButton = new TextButton("Resume", game.skin);
    TextButton recipeBookButton = new TextButton("Recipe Book", game.skin);
    TextButton tutorialButton = new TextButton("Tutorial", game.skin);
    saveButton = new TextButton("Exit", game.skin);

    resumeButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y) {
        pauseToggled = true;
      }
    });
    recipeBookButton.addListener(
        createListener(new Slideshow(game, Slideshow.Type.recipe, gameScreen)));
    tutorialButton.addListener(
        createListener(new Slideshow(game, Slideshow.Type.tutorial, gameScreen)));
    saveButton.addListener(new ClickListener() {
      public void clicked(InputEvent event, float x, float y) {
        if (isEndless) {
          saveGame();
        }
        Gdx.app.log("save", "Game is saved!");
        game.setScreen(new MainMenuScreen(game));
        dispose();
        gameScreen.dispose();
      }
    });

    tablePause.add(resumeButton).width(260).height(70).padBottom(30);
    tablePause.row();
    tablePause.add(recipeBookButton).width(260).height(70).padBottom(30);
    tablePause.row();
    tablePause.add(tutorialButton).width(260).height(70).padBottom(30);
    tablePause.row();
    tablePause.add(saveButton).width(260).height(70);

    this.tableRight = new Table();
    this.shopTable = new Table();
    this.tableBottom = new Table();

    stage.addActor(tablePause);
    stage.addActor(shopTable);
    stage.addActor(tableTop);
    stage.addActor(tableRight);
    stage.addActor(tableBottom);
    stage.addActor(tableBottomLabel);
  }

  /**
   * Create table with items to buy
   *
   * @param powerUpSystem The system that deals with the purchase of powerups
   */
  public void initShop(final PowerUpSystem powerUpSystem) {
    this.powerUpSystem = powerUpSystem;
    shopTable.setVisible(false);
    shopTable.clear();
    shopTable.center();
    shopTable.setFillParent(true);

    TextButton resumeButton = new TextButton("Resume", game.skin);

    movementSpeed.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (reputationAndMoney[1] < 20) {
          updateShopButtons();
          return;
        }
        reputationAndMoney[1] -= 20;
        moneyLabel.setText(String.format("$%d", reputationAndMoney[1]));
        powerUpSystem.addSpeedUp();
        updateShopButtons();
      }
    });

    prepSpeed.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (reputationAndMoney[1] < 20) {
          updateShopButtons();
          return;
        }
        reputationAndMoney[1] -= 20;
        moneyLabel.setText(String.format("$%d", reputationAndMoney[1]));
        powerUpSystem.addPrepSpeed();
        updateShopButtons();
      }
    });

    chopSpeed.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (reputationAndMoney[1] < 20) {
          updateShopButtons();
          return;
        }
        reputationAndMoney[1] -= 20;
        moneyLabel.setText(String.format("$%d", reputationAndMoney[1]));
        powerUpSystem.addChopSpeed();
        updateShopButtons();
      }
    });

    salePrice.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (reputationAndMoney[1] < 20) {
          updateShopButtons();
          return;
        }
        reputationAndMoney[1] -= 20;
        moneyLabel.setText(String.format("$%d", reputationAndMoney[1]));
        powerUpSystem.addSalePrice();
        updateShopButtons();
      }
    });

    customerPatience.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (reputationAndMoney[1] < 20) {
          updateShopButtons();
          return;
        }
        reputationAndMoney[1] -= 20;
        moneyLabel.setText(String.format("$%d", reputationAndMoney[1]));
        powerUpSystem.addPatience();
        updateShopButtons();
      }
    });

    resumeButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        hideShop();
      }
    });

    shopTable.add(movementSpeed).width(500).padBottom(30);
    shopTable.row();
    shopTable.add(prepSpeed).width(500).padBottom(30);
    shopTable.row();
    shopTable.add(chopSpeed).width(500).padBottom(30);
    shopTable.row();
    shopTable.add(salePrice).width(500).padBottom(30);
    shopTable.row();
    shopTable.add(customerPatience).width(500).padBottom(30);
    shopTable.row();
    shopTable.add(resumeButton).width(500);
  }

  private void updateShopButtons() {
    boolean hasInsufficientFunds = reputationAndMoney[1] < 20;
    movementSpeed.setDisabled(hasInsufficientFunds);
    prepSpeed.setDisabled(hasInsufficientFunds);
    chopSpeed.setDisabled(hasInsufficientFunds);
    salePrice.setDisabled(hasInsufficientFunds);
    customerPatience.setDisabled(hasInsufficientFunds);
    if (hasInsufficientFunds) {
      return;
    }
    if (powerUpSystem == null) {
      return;
    }
    movementSpeed.setDisabled(powerUpSystem.isMaxSpeedUp());
    prepSpeed.setDisabled(powerUpSystem.isMaxPrepSpeed());
    chopSpeed.setDisabled(powerUpSystem.isMaxChopSpeed());
    salePrice.setDisabled(powerUpSystem.isMaxSalePrice());
    customerPatience.setDisabled(powerUpSystem.isMaxPatience());
  }

  /**
   * Update HUD inventory section.
   *
   * @param inventory array of {@link FoodType} to display.
   */
  public void updateInventory(FoodType[] inventory) {
    tableBottom.clear();
    tableBottom.bottom();
    tableBottom.setFillParent(true);

    tableBottom.row();
    for (FoodType foodType : inventory) {
      TextureRegion region = EntityFactory.getFoodTexture(foodType);
      if (region == null) {
        Gdx.app.log("Texture is null", "");  // debugger can be ignored/removed idk do what you want
      } else {
        // adds images of the items the controlled cook is holding into the inventory
        photo = new Image(region);
        tableBottom.add(photo).width(64).height(64).center();
      }
    }
  }

  /**
   * Update the current orders HUD section.
   *
   * @param orders array of {@link FoodType} to display.
   */
  public void updateOrders(ArrayList<FoodType> orders) {
    tableRight.clear();
    tableRight.right();
    tableRight.setFillParent(true);

    for (int i = 0; i < orders.size(); i++) {
      TextureRegion region = EntityFactory.getFoodTexture(orders.get(i));
      if (region == null) {
        Gdx.app.log("Texture is null", "");
      } else {
        // adds all the orders onto the right of the screen with a little number
        Label orderNumberLabel = new Label(String.format("%01d", i + 1), hudLabelStyle);
        tableRight.add(orderNumberLabel).padRight(10);
        photo = new Image(region);
        tableRight.add(photo).width(64).height(64);
        tableRight.row().height(0);
      }
    }
  }

  /**
   * When an order is fulfilled, update the UI to know that it has happened.
   */
  public void incrementCompletedOrders() {
    numCustomersServed++;
    moneyLabel.setText(String.format("$%d", reputationAndMoney[1]));
  }

  /**
   * Render the hud. If {@code triggerWin} is true when this runs, the Win screen will be shown.
   *
   * @param deltaTime the time elapsed since last frame.
   */
  public void update(float deltaTime) {
    if (paused) {
      if (pauseToggled) {
        pauseToggled = false;
        this.resume();
      }
      stage.act();
      stage.draw();
      return;
    }
    timeCounter += won ? 0 : deltaTime;
    // Staggered once per second using timeCounter makes it way faster
    if (timeCounter >= 1) {
      customerTimer++;
      timerLabel.setText(String.format("%03d", customerTimer));
      reputationLabel.setText(reputationAndMoney[0]);
      timeCounter -= 1;
    }

    if (triggerWin && !won) {
      triggerWin = false;
      win();
    }
    if (pauseToggled) {
      pauseToggled = false;
      this.pause();
    }

    stage.act();
    stage.draw();

  }

  @Override
  public void pause() {
    paused = true;
    gameScreen.pause();

    // Hide the normal hud
    tableBottom.setVisible(false);
    tableRight.setVisible(false);
    pauseButton.setVisible(false);
    shopButton.setVisible(false);
    tableBottomLabel.setVisible(false);

    // Show the pause hud
    tablePause.setVisible(true);

    // super.pause();
  }

  public void showShop() {
    if (isShopOpen) {
      return;
    }
    isShopOpen = true;
    pause();
    updateShopButtons();
    tablePause.setVisible(false);
    shopTable.setVisible(true);
  }

  public void hideShop() {
    if (!isShopOpen) {
      return;
    }
    isShopOpen = false;
    shopTable.setVisible(false);
    resume();
  }

  @Override
  public void resume() {
    paused = false;
    gameScreen.resume();

    // Show the normal hud
    tableBottom.setVisible(true);
    tableRight.setVisible(true);
    pauseButton.setVisible(true);
    shopButton.setVisible(true);
    tableBottomLabel.setVisible(true);

    // Hide the pause hud
    tablePause.setVisible(false);

    super.resume();
  }

  public boolean won;
  public boolean triggerWin = false;

  /**
   * Win screen
   */
  private void win() {
    gameScreen.pause();
    won = true;
    // winscreen table made
    stage.clear();
    Table centerTable = new Table();
    centerTable.setFillParent(true);

    // labels given different fonts so it looks nicer
    Label congrats;
    Label congratsSubtitle;
    if (isEndless) {
      congrats = new Label("The End!", titleLabelStyle);
      congratsSubtitle = new Label(
          String.format("You served %d customers and lasted %03d seconds!", numCustomersServed,
              customerTimer),
          hudLabelStyle);
    } else {
      congrats = new Label("Congratulations!", titleLabelStyle);
      congratsSubtitle = new Label("You won!", hudLabelStyle);
    }

    //colspan2 important! do some googling if you dont know what it does (scene2d)
    centerTable.add(congrats).padBottom(40).colspan(2);
    centerTable.row();
    centerTable.add(congratsSubtitle).padBottom(30).colspan(2);

    centerTable.row();

    centerTable.add(timeNameLabel);
    centerTable.add(reputationNameLabel);

    centerTable.row();

    centerTable.add(timerLabel);
    centerTable.add(reputationLabel);

    centerTable.row();

    TextButton returnToMenuButton = new TextButton("Main menu", game.skin);
    centerTable.add(returnToMenuButton).width(240).height(70).padTop(50).colspan(2);

    returnToMenuButton.addListener(createListener(new MainMenuScreen(game)));

    stage.addActor(centerTable);
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    viewport.update(width, height, true);
    viewport.apply();
  }

  public void dispose() {
    stage.dispose();
  }

  private ClickListener createListener(final com.badlogic.gdx.Screen screen) {
    return new ClickListener() {
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(screen);
        gameScreen.dispose();
      }
    };
  }

  public void setEndless(boolean endless) {
    isEndless = endless;
    moneyNameLabel.setVisible(isEndless);
    moneyLabel.setVisible(isEndless);
    saveButton.setText(isEndless ? "Save and Exit" : "Exit");
    shopButton.setVisible(isEndless);
  }
}
