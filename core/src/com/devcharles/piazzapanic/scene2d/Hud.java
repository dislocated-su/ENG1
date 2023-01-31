package com.devcharles.piazzapanic.scene2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.devcharles.piazzapanic.GameScreen;
import com.devcharles.piazzapanic.MainMenuScreen;
import com.devcharles.piazzapanic.PiazzaPanic;
import com.devcharles.piazzapanic.components.FoodComponent.FoodType;
import com.devcharles.piazzapanic.utility.EntityFactory;

public class Hud extends ApplicationAdapter {
    public Stage stage;
    private Viewport viewport;
    private Integer customerTimer = 000;
    private float timeCounter = 0;
    private Integer[] reputation;
    private Skin skin;

    private final float fontScale = 0.6f;

    // A label is basically a widget
    LabelStyle hudLabelStyle;
    LabelStyle titleLabelStyle;
    Label timerLabel;
    Label timeNameLabel;
    Label reputationLabel;
    Label reputationNameLabel;
    Label pausedNameLabel;
    BitmapFont uiFont, uiTitleFont;

    private Image photo;

    private Game game;
    private Table tableBottom, tableRight, tableTop, tablePause;

    private boolean pauseToggled = false;
    public boolean paused = false;
    
    private GameScreen gameScreen;

    public Hud(SpriteBatch spriteBatch, final GameScreen savedGame, final Game game, Integer[] reputationPoints) {
        this.game = game;
        this.reputation = reputationPoints;
        this.gameScreen = savedGame;

        // Setup the viewport
        viewport = new ScreenViewport(new OrthographicCamera(1280, 720));
        stage = new Stage(viewport, spriteBatch);
        viewport.apply();

        // Import the custom skin
        skin = new Skin(Gdx.files.internal("craftacular/skin/craftacular-ui.json"));
        uiFont = new BitmapFont(Gdx.files.internal("craftacular/raw/font-export.fnt"));
        uiTitleFont = new BitmapFont(Gdx.files.internal("craftacular/raw/font-title-export.fnt"));

        // Create generic style for labels
        hudLabelStyle = new Label.LabelStyle();
        hudLabelStyle.font = uiFont;
        titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = uiTitleFont;

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                // TODO Auto-generated method stub
                if (keycode == Keys.ESCAPE) {
                    pauseToggled = true;
                }
                return true;
            }
        });

        createTables();
    }

    private void createTables() {

        timerLabel = new Label(String.format("%03d", customerTimer), hudLabelStyle);
        reputationLabel = new Label(String.format("%01d", reputation[0]), hudLabelStyle);
        timeNameLabel = new Label("Time", hudLabelStyle);
        reputationNameLabel = new Label("Reputation", hudLabelStyle);

        reputationLabel.setFontScale(fontScale + 0.1f);
        timerLabel.setFontScale(fontScale + 0.1f);
        timeNameLabel.setFontScale(fontScale + 0.1f);
        reputationNameLabel.setFontScale(fontScale + 0.1f);

        
        tableTop = new Table();
        tableTop.top();
        tableTop.setFillParent(true);


        tableTop.add(timeNameLabel).expandX().padTop(10);
        tableTop.add(reputationNameLabel).expandX().padTop(10);

        tableTop.row();
        tableTop.add(timerLabel).expandX();
        tableTop.add(reputationLabel).expandX();

        Table tableBottomLabel = new Table();
        tableBottomLabel.bottom();
        tableBottomLabel.setFillParent(true);

        Label inv = new Label("Inventory", hudLabelStyle);
        inv.setFontScale(fontScale);
        tableBottomLabel.add(inv).padBottom(60);

        tablePause = new Table();
        tablePause.setFillParent(true);
        tablePause.setVisible(false);

        pausedNameLabel = new Label("Game paused", titleLabelStyle);
        tablePause.add(pausedNameLabel).padBottom(30);

        tablePause.row();

        TextButton unpauseButton = new TextButton("Unpause", skin);
        TextButton recipeBookButton = new TextButton("Recipe Book", skin);
        TextButton tutorialButton = new TextButton("Tutorial", skin);
        
        unpauseButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                pauseToggled = true;
            }
        });
        recipeBookButton.addListener(createListener(new Slideshow(game, Slideshow.Type.recipe, gameScreen)));
        tutorialButton.addListener(createListener(new Slideshow(game, Slideshow.Type.tutorial, gameScreen)));
        
        tablePause.add(unpauseButton).width(240).height(70).padBottom(30);

        tablePause.row();

        tablePause.add(recipeBookButton).width(240).height(70).padBottom(30);
        tablePause.row();
        tablePause.add(tutorialButton).width(240).height(70);

    

        this.tableRight = new Table();
        this.tableBottom = new Table();

        stage.addActor(tablePause);
        stage.addActor(tableTop);
        stage.addActor(tableRight);
        stage.addActor(tableBottom);
        stage.addActor(tableBottomLabel);
    }

    public void updateInventory(FoodType[] inventory) {
        tableBottom.clear();
        tableBottom.bottom();
        tableBottom.setFillParent(true);

        tableBottom.row();
        for (int i = 0; i < inventory.length; i++) {
            TextureRegion region = EntityFactory.getFoodTexture(inventory[i]);
            if (region == null) {
                Gdx.app.log("Texture is null", "");
            } else {
                photo = new Image(region);
                tableBottom.add(photo).width(64).height(64).center();
            }
        }
    }

    public void updateOrders(FoodType[] orders) {
        tableRight.clear();
        tableRight.right();
        tableRight.setFillParent(true);

        for (int i = 0; i < orders.length; i++) {
            TextureRegion region = EntityFactory.getFoodTexture(orders[i]);
            if (region == null) {
                Gdx.app.log("Texture is null", "");
            } else {

                Label orderNumberLabel = new Label(String.format("%01d", i + 1), hudLabelStyle);
                tableRight.add(orderNumberLabel).padRight(10);
                photo = new Image(region);
                tableRight.add(photo).width(64).height(64);
                tableRight.row().height(0);
            }
        }
    }

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
        // Staggered to once per second using timeCounter.
        if (timeCounter >= 1) {
            customerTimer++;
            timerLabel.setText(String.format("%03d", customerTimer));
            reputationLabel.setText(reputation[0]);
            if (triggerWin) {
                triggerWin = false;
                Win();
            } 
            if (pauseToggled) {
                pauseToggled = false;
                this.pause();
            }
            timeCounter -= 1;
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

        // Show the pause hud
        tablePause.setVisible(true);

        //super.pause();
    }

    @Override
    public void resume() {
        paused = false;
        gameScreen.resume();
        
        
        // Show the normal hud
        tableBottom.setVisible(true);
        tableRight.setVisible(true);
        tableTop.setVisible(true);
        
        // Hide the pause hud
        tablePause.setVisible(false);

        super.resume();
    }

    public boolean won;
    public boolean triggerWin = false;


    public void Win() {
        won = true;

        stage.clear();
        Table centerTable = new Table();
        centerTable.setFillParent(true);

        Label congrats = new Label("Congratulations!", titleLabelStyle);
        Label congratsSubtitle = new Label("You won!", hudLabelStyle);

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

        TextButton returnToMenuButton = new TextButton("Main menu", skin);
        centerTable.add(returnToMenuButton).width(240).height(70).padTop(50).colspan(2);

        returnToMenuButton.addListener(createListener(new MainMenuScreen((PiazzaPanic) game)));

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
        // no! can u hear us? no
        // maybe restart discord i have i sent you a photo on messages

    }

    private ClickListener createListener(final Screen screen) {
        return new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(screen);
            }
        };
    }
}
