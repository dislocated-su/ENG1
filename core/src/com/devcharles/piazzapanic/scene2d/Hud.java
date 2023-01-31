package com.devcharles.piazzapanic.scene2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
    public Stage gameStage;
    private Viewport gameViewport;
    private Integer customerTimer = 000;
    private float timeCounter = 0;
    private Integer[] reputation;
    private Skin skin;

    private final float fontScale = 0.6f;

    // A label is basically a widget
    LabelStyle hudLabelStyle;
    Label timerLabel;
    Label timeNameLabel;
    Label reputationLabel;
    Label reputationNameLabel;
    LabelStyle titleLabelStyle;
    BitmapFont uiFont, uiTitleFont;

    private Image photo;

    private Game game;
    private Table tableBottom, tableRight;

    public Hud(SpriteBatch spriteBatch, final GameScreen savedGame, final Game game, Integer[] reputationPoints) {
        this.game = game;
        this.reputation = reputationPoints;

        // Setup the viewport
        gameViewport = new ScreenViewport(new OrthographicCamera(1280, 720));
        gameStage = new Stage(gameViewport, spriteBatch);
        gameViewport.apply();

        // Import the custom skin
        skin = new Skin(Gdx.files.internal("craftacular/skin/craftacular-ui.json"));
        uiFont = new BitmapFont(Gdx.files.internal("craftacular/raw/font-export.fnt"));
        uiTitleFont = new BitmapFont(Gdx.files.internal("craftacular/raw/font-title-export.fnt"));

        // Create generic style for labels
        hudLabelStyle = new Label.LabelStyle();
        hudLabelStyle.font = uiFont;
        titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = uiTitleFont;

        createTables(savedGame);
    }

    private void createTables(final GameScreen savedGame) {

        timerLabel = new Label(String.format("%03d", customerTimer), hudLabelStyle);
        reputationLabel = new Label(String.format("%01d", reputation[0]), hudLabelStyle);
        timeNameLabel = new Label("Time", hudLabelStyle);
        reputationNameLabel = new Label("Reputation", hudLabelStyle);

        reputationLabel.setFontScale(fontScale + 0.1f);
        timerLabel.setFontScale(fontScale + 0.1f);
        timeNameLabel.setFontScale(fontScale + 0.1f);
        reputationNameLabel.setFontScale(fontScale + 0.1f);

        TextButton recipeBookButton = new TextButton("Recipe Book", skin);
        TextButton tutorialButton = new TextButton("Tutorial", skin);

        recipeBookButton.getLabel().setFontScale(fontScale);
        tutorialButton.getLabel().setFontScale(fontScale);

        recipeBookButton.addListener(createListener(new Slideshow(game, Slideshow.Type.recipe, savedGame)));
        tutorialButton.addListener(createListener(new Slideshow(game, Slideshow.Type.tutorial, savedGame)));

        Table tableTop = new Table();
        tableTop.top();
        tableTop.setFillParent(true);

        tableTop.add(recipeBookButton).width(180).height(50).left().top();
        tableTop.add(timeNameLabel).expandX().padTop(10);
        tableTop.add(reputationNameLabel).expandX().padTop(10);

        tableTop.row();
        tableTop.add(tutorialButton).width(180).height(50).left().top();
        tableTop.add(timerLabel).expandX();
        tableTop.add(reputationLabel).expandX();

        Table tableBottomLabel = new Table();
        tableBottomLabel.bottom();
        tableBottomLabel.setFillParent(true);

        Label inv = new Label("Inventory", hudLabelStyle);
        inv.setFontScale(fontScale);
        tableBottomLabel.add(inv).padBottom(60);

        this.tableRight = new Table();
        this.tableBottom = new Table();

        gameStage.addActor(tableTop);
        gameStage.addActor(tableRight);
        gameStage.addActor(tableBottom);
        gameStage.addActor(tableBottomLabel);
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
        timeCounter += won ? 0 : deltaTime;
        if (timeCounter >= 1) {
            customerTimer++;
            timerLabel.setText(String.format("%03d", customerTimer));
            reputationLabel.setText(reputation[0]);
            if (triggerWin) {
                triggerWin = false;
                Win();
            }
            timeCounter = 0;
        }

        gameStage.act();
        gameStage.draw();

    }

    public boolean won;
    public boolean triggerWin = false;

    public void Win() {
        won = true;

        gameStage.clear();
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

        gameStage.addActor(centerTable);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gameViewport.update(width, height, true);
        gameViewport.apply();
    }

    public void dispose() {
        gameStage.dispose();
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
