package com.devcharles.piazzapanic.scene2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.utils.SimpleNonBlockingSemaphore.Factory;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.devcharles.piazzapanic.GameScreen;
import com.devcharles.piazzapanic.MainMenuScreen;
import com.devcharles.piazzapanic.PiazzaPanic;
import com.devcharles.piazzapanic.SaveLoad;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.utility.EntityFactory;
import com.devcharles.piazzapanic.utility.GdxTimer;
import com.devcharles.piazzapanic.utility.Difficulty;

/**
 * HUD user interface rendering for the game, also includes the win screen.
 */
public class Hud extends ApplicationAdapter {
    public Stage stage;
    private Viewport viewport;
    private Integer[] customerTimer;
    private float timeCounter = 0;
    private Integer[] reputation;
    private Float[] tillBalance;
    private Integer[] customersServed;
    private Skin skin;
    private SaveLoad saveLoad;

    private final float fontScale = 0.6f;

    // A label is basically a widget
    LabelStyle hudLabelStyle;
    LabelStyle hudRedLabelStyle;
    LabelStyle hudGreenLabelStyle;
    LabelStyle titleLabelStyle;

    Label timerLabel;
    Label timeNameLabel;
    Label reputationLabel;
    Label reputationNameLabel;
    Label difficultyNameLabel;
    Label tillBalanceNameLabel;
    Label tillBalanceLabel;

    Label customersServedLabel;
    Label customersServedNameLabel;

    Label difficultyLabel;
    Label pausedNameLabel;
    Label infoMsgLabel;
    BitmapFont uiFont, uiTitleFont;

    Label powerInv;
    Label speedBoostTimer;
    Label InstaCookTimer;
    Label DoublePointsTimer;
    Label TimeFreezeTimer;

    Integer SpeedCounter = 30;
    Integer InstaCounter = 30;
    Integer DoubleCounter = 30;
    Integer FreezeCounter = 30;

    Boolean SpeedActive = false;
    Boolean InstaActive = false;
    Boolean DoubleActive = false;
    Boolean FreezeActive = false;

    public EntityFactory factory;
    private GameScreen powerUps;
    // an image used as the background of recipe book and tutorial
    private Image photo;

    private Game game;
    private Difficulty difficulty;
    private Table tableBottom, tableRight, tableTop, tableLeft, tablePause, tableBottomLabel, infoTable;

    private boolean pauseToggled = false;
    public boolean paused = false;

    private GameScreen gameScreen;
    private GdxTimer infoTimer = new GdxTimer(2000, false, false);

    /**
     * Create the hud.
     * 
     * @param spriteBatch      the batch to draw the HUD with
     * @param savedGame        reference to the screen drawing the hud to switch
     *                         back to in case of screen transitions.
     * @param game             {@link PiazzaPanic} instance for switching screens.
     * @param reputationPoints Must be an object to pass by reference, see
     *                         https://stackoverflow.com/questions/3326112/java-best-way-to-pass-int-by-reference
     */
    public Hud(SpriteBatch spriteBatch, final GameScreen savedGame, final Game game, Integer[] reputationPoints,
            Difficulty difficulty, Float[] tillBalance, Integer[] customersServed, Integer[] customerTimer,
            SaveLoad saveLoad, EntityFactory factory) {
        this.game = game;
        this.reputation = reputationPoints;
        this.gameScreen = savedGame;
        this.difficulty = difficulty;
        this.tillBalance = tillBalance;
        this.customerTimer = customerTimer;
        this.customersServed = customersServed;
        this.powerUps = savedGame;
        this.saveLoad = saveLoad;
        this.factory = factory;

        // Setup the viewport
        viewport = new ScreenViewport(new OrthographicCamera(1280, 720));
        stage = new Stage(viewport, spriteBatch);
        viewport.apply();

        // Import the custom skin with different fonts
        skin = new Skin(Gdx.files.internal("craftacular/skin/craftacular-ui.json"));
        uiFont = new BitmapFont(Gdx.files.internal("craftacular/raw/font-export.fnt"));
        uiTitleFont = new BitmapFont(Gdx.files.internal("craftacular/raw/font-title-export.fnt"));

        // Create generic style for labels with the different fonts
        hudLabelStyle = new Label.LabelStyle();
        hudRedLabelStyle = new Label.LabelStyle();
        hudGreenLabelStyle = new Label.LabelStyle();
        hudRedLabelStyle.font = uiFont;
        hudGreenLabelStyle.font = uiFont;
        hudRedLabelStyle.fontColor = Color.RED;
        hudGreenLabelStyle.fontColor = Color.GREEN;
        hudLabelStyle.font = uiFont;
        titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = uiTitleFont;

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ESCAPE) {
                    pauseToggled = true;
                    // sets game to go bigscreen if F11 is pressed or sets it to go small screen
                } else if (keycode == Keys.F11) {
                    Boolean fullScreen = Gdx.graphics.isFullscreen();
                    Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
                    if (fullScreen == true) {
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

    private void createTables() {

        timerLabel = new Label(String.format("%03d", customerTimer[0]), hudLabelStyle);
        reputationLabel = new Label(String.format("%01d", reputation[0]), hudLabelStyle);
        difficultyLabel = new Label(difficulty.getDisplayName(), hudLabelStyle);
        timeNameLabel = new Label("Time", hudLabelStyle);
        reputationNameLabel = new Label("Reputation", hudLabelStyle);
        difficultyNameLabel = new Label("Game Mode", hudLabelStyle);
        tillBalanceNameLabel = new Label("Till Balance", hudLabelStyle);
        tillBalanceLabel = new Label("0", hudRedLabelStyle);
        infoMsgLabel = new Label("", titleLabelStyle);

        // Creates a bunch of labels and sets the fontsize
        reputationLabel.setFontScale(fontScale + 0.1f);
        timerLabel.setFontScale(fontScale + 0.1f);
        timeNameLabel.setFontScale(fontScale + 0.1f);
        reputationNameLabel.setFontScale(fontScale + 0.1f);
        difficultyNameLabel.setFontScale(fontScale + 0.1f);
        difficultyLabel.setFontScale(fontScale + 0.1f);
        tillBalanceNameLabel.setFontScale(fontScale + 0.1f);
        tillBalanceLabel.setFontScale(fontScale + 0.1f);
        infoMsgLabel.setFontScale(fontScale);

        // lays out timer and reputation
        tableTop = new Table();
        tableTop.top();
        tableTop.setFillParent(true);

        tableTop.add(timeNameLabel).expandX().padTop(10);
        tableTop.add(reputationNameLabel).expandX().padTop(10);
        tableTop.add(tillBalanceNameLabel).expandX().padTop(10);
        tableTop.add(difficultyNameLabel).expandX().padTop(10);

        tableTop.row();
        tableTop.add(timerLabel).expandX();
        tableTop.add(reputationLabel).expandX();
        tableTop.add(tillBalanceLabel).expandX();
        tableTop.add(difficultyLabel).expandX();

        TextButton muteButton = new TextButton("Mute", skin);
        muteButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.audio.toggleMute();
            }
        });
        tableTop.add(muteButton).width(80);

        // add the powerUps on the hud
        if (difficulty != Difficulty.SCENARIO) {
            tableLeft = new Table();
            tableLeft.left();
            tableLeft.setFillParent(true);

            powerInv = new Label("PowerUps", hudLabelStyle);
            powerInv.setFontScale(fontScale);

            Texture speedTexture = new Texture(Gdx.files.internal("speed_boost32.png"));
            Texture speedPressedTexture = new Texture(Gdx.files.internal("speed_boost32_pressed.png"));
            // Texture speedRejectTexutre = new
            // Texture(Gdx.files.internal("speed_boost32_x.png"));
            ImageButton speedButton = new ImageButton(new TextureRegionDrawable(speedTexture),
                    new TextureRegionDrawable(speedPressedTexture));
            speedBoostTimer = new Label("", hudLabelStyle);
            Label speedCost = new Label("Cost:15", hudGreenLabelStyle);
            speedCost.setFontScale(0.5f);
            speedButton.setSize(64, 64);
            speedBoostTimer.setFontScale(0.5f);

            Texture instaTexture = new Texture(Gdx.files.internal("instantCook32.png"));
            Texture instaPressedTexture = new Texture(Gdx.files.internal("instantCook32_pressed.png"));
            ImageButton instaCook = new ImageButton(new TextureRegionDrawable(instaTexture),
                    new TextureRegionDrawable(instaPressedTexture));
            InstaCookTimer = new Label("", hudLabelStyle);
            Label instaCost = new Label("Cost:20", hudGreenLabelStyle);
            instaCost.setFontScale(0.5f);
            InstaCookTimer.setFontScale(0.5f);

            Texture binCustomerTexture = new Texture(Gdx.files.internal("binOrder32.png"));
            Texture binCustomerPressed = new Texture(Gdx.files.internal("binOrder32_pressed.png"));
            ImageButton binCustomer = new ImageButton(new TextureRegionDrawable(binCustomerTexture),
                    new TextureRegionDrawable(binCustomerPressed));
            Label binCost = new Label("Cost:50", hudGreenLabelStyle);
            binCost.setFontScale(0.5f);
            binCustomer.setSize(64, 64);

            Texture doubleRepTexture = new Texture(Gdx.files.internal("doubleRep32.png"));
            Texture doubleRepPressed = new Texture(Gdx.files.internal("doubleRep32_pressed.png"));
            ImageButton doubleRep = new ImageButton(new TextureRegionDrawable(doubleRepTexture),
                    new TextureRegionDrawable(doubleRepPressed));
            DoublePointsTimer = new Label("", hudLabelStyle);
            Label doubleRepCost = new Label("Cost:30", hudGreenLabelStyle);
            doubleRepCost.setFontScale(0.5f);
            doubleRep.setSize(64, 64);
            DoublePointsTimer.setFontScale(0.5f);

            Texture timeFreezeTexture = new Texture(Gdx.files.internal("timeFreeze32.png"));
            Texture timeFreezePressed = new Texture(Gdx.files.internal("timeFreeze32_pressed.png"));
            ImageButton timeFreeze = new ImageButton(new TextureRegionDrawable(timeFreezeTexture),
                    new TextureRegionDrawable(timeFreezePressed));
            TimeFreezeTimer = new Label("", hudLabelStyle);
            Label timeCost = new Label("Cost:100", hudGreenLabelStyle);
            timeCost.setFontScale(0.5f);
            timeFreeze.setSize(64, 64);
            TimeFreezeTimer.setFontScale(0.5f);

            Texture addChef = new Texture(Gdx.files.internal("chef.png"));
            Texture addChefClicked = new Texture(Gdx.files.internal("chef_clicked.png"));
            ImageButton chefButton = new ImageButton(new TextureRegionDrawable(addChef),
                    new TextureRegionDrawable(addChefClicked));
            Label chefCost = new Label("Cost 300", hudGreenLabelStyle);
            chefCost.setFontScale(0.5f);
            chefButton.setSize(64, 64);

            // add functionality to the powerup buttons by reducing the till balance and
            // setting the timers
            speedButton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if (tillBalance[0] - 15 < 0 || tillBalance[0] <= 14) {
                        displayInfoMessage("Insufficient Till Balance!!");
                    }
                    if (tillBalance[0] - 15 > 0) {
                        powerUps.SpeedActive();
                        tillBalance[0] -= 15;
                        SpeedActive = true;
                        SpeedCounter = 30;
                    }

                }
            });

            instaCook.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if (tillBalance[0] - 20 < 0 || tillBalance[0] <= 19) {
                        displayInfoMessage("Insufficient Till Balance!!");
                    }
                    if (tillBalance[0] - 20 > 0) {
                        powerUps.InstaActive();
                        tillBalance[0] -= 20;
                        System.out.print("InstaCook is Active");
                        InstaActive = true;
                        InstaCounter = 30;
                    }
                }

            });

            binCustomer.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if (tillBalance[0] - 50 < 0 || tillBalance[0] <= 49) {
                        displayInfoMessage("Insufficient Till Balance!!");
                    }
                    if (tillBalance[0] - 50 > 0) {
                        powerUps.BinActive();
                        tillBalance[0] -= 50;
                        System.out.print("BinACustomer is active");
                    }
                }
            });

            doubleRep.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if (tillBalance[0] - 30 < 0 || tillBalance[0] <= 29) {
                        displayInfoMessage("Insufficient Till Balance!!");
                    }
                    if (tillBalance[0] - 30 > 0) {
                        powerUps.DoubleActive();
                        tillBalance[0] -= 30;
                        System.out.println("DoubleRep active");
                        DoubleActive = true;
                        DoubleCounter = 30;
                    }
                }
            });

            timeFreeze.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if (tillBalance[0] - 100 < 0 || tillBalance[0] <= 99) {
                        displayInfoMessage("Insufficient Till Balance!!");
                    }
                    if (tillBalance[0] - 100 > 0) {
                        powerUps.TimeActive();
                        tillBalance[0] -= 100;
                        System.out.print("TimeFreeze active");
                        FreezeActive = true;
                        FreezeCounter = 30;
                    }
                }
            });

            chefButton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if (tillBalance[0] - 300 < 0 || tillBalance[0] <= 259) {
                        displayInfoMessage("Insufficient Till Balance!!");
                    }
                    if (tillBalance[0] - 300 > 0) {
                        factory.createCook((int) 60.00, (int) 28.00);
                        tillBalance[0] -= 300;
                    }
                }
            });

            tableLeft.add(powerInv);
            tableLeft.row();
            tableLeft.add(speedButton);
            tableLeft.add(speedBoostTimer);
            tableLeft.row();
            tableLeft.add(speedCost);
            tableLeft.row();
            tableLeft.add(instaCook);
            tableLeft.add(InstaCookTimer);
            tableLeft.row();
            tableLeft.add(instaCost);
            tableLeft.row();
            tableLeft.add(binCustomer);
            tableLeft.row();
            tableLeft.add(binCost);
            tableLeft.row();
            tableLeft.add(doubleRep);
            tableLeft.add(DoublePointsTimer);
            tableLeft.row();
            tableLeft.add(doubleRepCost);
            tableLeft.row();
            tableLeft.add(timeFreeze);
            tableLeft.add(TimeFreezeTimer);
            tableLeft.row();
            tableLeft.add(timeCost);
            tableLeft.add().row();
            tableLeft.add(chefButton);
            tableLeft.add().row();
            tableLeft.add(chefCost);
        }

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
        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton recipeBookButton = new TextButton("Recipe Book", skin);
        TextButton tutorialButton = new TextButton("Tutorial", skin);

        resumeButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                pauseToggled = true;
            }
        });
        recipeBookButton.addListener(createListener(new Slideshow(game, Slideshow.Type.recipe, gameScreen)));
        tutorialButton.addListener(createListener(new Slideshow(game, Slideshow.Type.tutorial, gameScreen)));

        tablePause.add(resumeButton).width(240).height(70).padBottom(30);
        tablePause.row();
        tablePause.add(recipeBookButton).width(240).height(70).padBottom(30);
        tablePause.row();
        tablePause.add(tutorialButton).width(240).height(70).padBottom(30);
        tablePause.row();

        if (difficulty != Difficulty.SCENARIO) {
            TextButton saveButton = new TextButton("Save Game", skin);

            saveButton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    saveLoad.save();
                    System.out.println("Saved game data");

                    pauseToggled = true;
                }
            });

            tablePause.add(saveButton).width(240).height(70);
            tablePause.row();
        }

        this.tableRight = new Table();
        this.tableBottom = new Table();
        this.infoTable = new Table();
        infoTable.setFillParent(true);
        infoTable.add(infoMsgLabel);

        stage.addActor(tablePause);
        stage.addActor(tableTop);
        stage.addActor(tableRight);
        stage.addActor(tableBottom);
        stage.addActor(tableBottomLabel);
        stage.addActor(infoTable);
        if (difficulty != Difficulty.SCENARIO) {
            stage.addActor(tableLeft);
        }
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
        for (int i = 0; i < inventory.length; i++) {
            TextureRegion region = EntityFactory.getFoodTexture(inventory[i]);
            if (region == null) {
                Gdx.app.log("Texture is null", ""); // debugger can be ignored/removed idk do what you want
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
    public void updateOrders(FoodType[] orders, int[] orderTimes) {
        tableRight.clear();
        tableRight.right();
        tableRight.setFillParent(true);

        for (int i = 0; i < orders.length; i++) {
            TextureRegion region = EntityFactory.getFoodTexture(orders[i]);
            if (region == null) {
                Gdx.app.log("Texture is null", "");
            } else {
                // adds all the orders onto the right of the screen with a little number
                Label orderNumberLabel = new Label(String.format("%01d", i + 1), hudLabelStyle);
                Label orderDueLabel = new Label(orderTimes[i] > 0 ? Integer.toString(orderTimes[i]) : "0",
                        orderTimes[i] > 60 ? hudGreenLabelStyle : hudRedLabelStyle);
                tableRight.add(orderNumberLabel).padRight(10);
                photo = new Image(region);
                tableRight.add(photo).width(64).height(64).padRight(15);
                tableRight.add(orderDueLabel).padRight(10);
                tableRight.row().height(0);
            }
        }
    }

    /**
     * Render the hud. If {@code triggerWin} is true when this runs, the Win screen
     * will be shown.
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

        if (infoTimer.tick(deltaTime)) {
            infoMsgLabel.setText("");
            infoTimer.stop();
            infoTimer.reset();
        }
        timeCounter += gameOver ? 0 : deltaTime;
        // Staggered once per second using timeCounter makes it way faster
        if (timeCounter >= 1) {
            if (!FreezeActive) {
                customerTimer[0]++;
                timerLabel.setText(String.format("%03d", customerTimer[0]));
                reputationLabel.setText(reputation[0]);
                tillBalanceLabel.setText(Float.toString(tillBalance[0]));
                tillBalanceLabel.setStyle(tillBalance[0] > 0 ? hudGreenLabelStyle : hudRedLabelStyle);
            }
            timerLabel.setText(String.format("%03d", customerTimer[0]));
            reputationLabel.setText(reputation[0]);
            tillBalanceLabel.setText(Float.toString(tillBalance[0]));
            tillBalanceLabel.setStyle(tillBalance[0] > 0 ? hudGreenLabelStyle : hudRedLabelStyle);

            if (triggerGameOver) {
                triggerGameOver = false;
                gameOver();
            }
            if (pauseToggled) {
                pauseToggled = false;
                this.pause();
            }

            timeCounter -= 1;

            // adjust the timers for the powerups
            if (difficulty != Difficulty.SCENARIO) {
                if (SpeedCounter == 0) {
                    SpeedActive = false;
                    speedBoostTimer.setText("");
                    SpeedCounter = 30;
                }

                if (InstaCounter == 0) {
                    InstaActive = false;
                    InstaCookTimer.setText("");
                    InstaCounter = 20;
                }

                if (DoubleCounter == 0) {
                    DoubleActive = false;
                    DoublePointsTimer.setText("");
                    DoubleCounter = 30;
                }

                if (FreezeCounter == 0) {
                    FreezeActive = false;
                    TimeFreezeTimer.setText("");
                    FreezeCounter = 30;
                }

                if (SpeedActive) {
                    speedBoostTimer.setText(String.format("%01d", SpeedCounter));
                }

                if (InstaActive) {
                    InstaCookTimer.setText(String.format("%01d", InstaCounter));
                }

                if (DoubleActive) {
                    DoublePointsTimer.setText(String.format("%01d", DoubleCounter));
                }

                if (FreezeActive) {
                    TimeFreezeTimer.setText(String.format("%01d", FreezeCounter));
                }

                SpeedCounter--;
                InstaCounter--;
                DoubleCounter--;
                FreezeCounter--;
            }
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
        tableTop.setVisible(false);
        tableBottomLabel.setVisible(false);
        if (difficulty != Difficulty.SCENARIO) {
            tableLeft.setVisible(false);
        }

        // Show the pause hud
        tablePause.setVisible(true);

        // super.pause();
    }

    @Override
    public void resume() {
        paused = false;
        gameScreen.resume();

        // Show the normal hud
        tableBottom.setVisible(true);
        tableRight.setVisible(true);
        tableTop.setVisible(true);
        tableBottomLabel.setVisible(true);
        if (difficulty != Difficulty.SCENARIO) {
            tableLeft.setVisible(true);
        }

        // Hide the pause hud
        tablePause.setVisible(false);

        super.resume();
    }

    public boolean gameOver;
    public boolean triggerGameOver = false;

    /**
     * Win screen Refactored into game over screen to account for both winning and
     * losing the game.
     */
    private void gameOver() {
        gameOver = true;
        // winscreen table made
        stage.clear();
        Table centerTable = new Table();
        centerTable.setFillParent(true);
        // labels given different fonts so it looks nicer
        Label congrats = new Label("Congratulations!", titleLabelStyle);
        Label congratsSubtitle = new Label("You won!", hudLabelStyle);
        customersServedNameLabel = new Label("Customers served", hudLabelStyle);
        customersServedLabel = new Label(customersServed[0].toString(), hudLabelStyle);
        customersServedLabel.setFontScale(fontScale + 0.1f);
        customersServedNameLabel.setFontScale(fontScale + 0.1f);

        if (reputation[0] == 0) {
            congrats.setText("Game Over");
            congratsSubtitle.setText("You lost!");
        }

        // colspan2 important! do some googling if you dont know what it does (scene2d)
        centerTable.add(congrats).padBottom(40).colspan(3);
        centerTable.row();
        centerTable.add(congratsSubtitle).padBottom(30).colspan(3);

        centerTable.row();

        centerTable.add(timeNameLabel);
        centerTable.add(customersServedNameLabel);
        centerTable.add(reputationNameLabel);

        centerTable.row();

        centerTable.add(timerLabel);
        centerTable.add(customersServedLabel);
        centerTable.add(reputationLabel);

        centerTable.row();

        TextButton returnToMenuButton = new TextButton("Main menu", skin);
        centerTable.add(returnToMenuButton).width(240).height(70).padTop(50).colspan(3);

        returnToMenuButton.addListener(createListener(new MainMenuScreen((PiazzaPanic) game)));

        stage.addActor(centerTable);
    }

    public void displayInfoMessage(String msg) {
        infoMsgLabel.setText(msg);
        infoTimer.start();
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

    private ClickListener createListener(final Screen screen) {
        return new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(screen);
            }
        };
    }
}
