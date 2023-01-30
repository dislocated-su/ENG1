package com.devcharles.piazzapanic.scene2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.devcharles.piazzapanic.GameScreen;

public class Hud extends ApplicationAdapter {
    public Stage gameStage;
    private Viewport gameViewport;
    private Integer customerTimer = 000;
    private float timeCounter = 0;
    private Integer reputation = 3;
    private Skin skin;

    // A label is basically a widget
    LabelStyle hudLabelStyle;
    Label timerLabel;
    Label timeNameLabel;
    Label reputationLabel;
    Label reputationNameLabel;
    BitmapFont uiFont;

    private Image photo, photo2;

    private Game game;

    public Hud(SpriteBatch spriteBatch, final GameScreen savedGame, final Game game) {
        this.game = game;

        gameViewport = new ScreenViewport(new OrthographicCamera(1280, 720));
        gameStage = new Stage(gameViewport, spriteBatch);
        gameViewport.apply();

        skin = new Skin(Gdx.files.internal("craftacular/skin/craftacular-ui.json"));

        hudLabelStyle = new Label.LabelStyle();
        uiFont = new BitmapFont(Gdx.files.internal("craftacular/raw/font-export.fnt"));
        hudLabelStyle.font = uiFont;

        createTables(savedGame);
    }

    private void createTables(final GameScreen savedGame) {

        timerLabel = new Label(String.format("%03d", customerTimer), hudLabelStyle);
        reputationLabel = new Label(String.format("%01d", reputation), hudLabelStyle);
        timeNameLabel = new Label("Time", hudLabelStyle);
        reputationNameLabel = new Label("Reputation", hudLabelStyle);

        TextButton recipeBookButton = new TextButton("Recipe Book", skin);
        TextButton tutorialButton = new TextButton("Tutorial", skin);

        recipeBookButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new recipeBook(0, savedGame, game));
            }
        });
        tutorialButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Tutorial(0, savedGame, game, 0));
            }
        });

        // We will need to update these to food textures.
        photo = new Image(new Texture(Gdx.files.internal("droplet.png")));
        photo2 = new Image(new Texture(Gdx.files.internal("droplet.png")));

        Table tableLeft = new Table();
        tableLeft.top();
        tableLeft.setFillParent(true);

        tableLeft.add(timeNameLabel).expandX().padTop(10);
        tableLeft.add(reputationNameLabel).expandX().padTop(10);
        tableLeft.row();
        tableLeft.add(timerLabel).expandX();
        tableLeft.add(reputationLabel).expandX();
        tableLeft.row();

        tableLeft.add(recipeBookButton).width(220).height(50).left().top();
        tableLeft.row();
        tableLeft.add(tutorialButton).width(220).height(50).left().top();

        Table tableRight = new Table();
        tableRight.right();
        tableRight.setFillParent(true);

        tableRight.add(photo).expandX().right();
        tableRight.row();
        tableRight.add(photo2).expandX().right();

        gameStage.addActor(tableLeft);
        gameStage.addActor(tableRight);

    }

    public void update(float deltaTime) {
        timeCounter += deltaTime;
        if (timeCounter >= 1) {
            customerTimer++;
            timerLabel.setText(String.format("%03d", customerTimer));
            timeCounter = 0;
        }

        gameStage.act();
        gameStage.draw();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gameViewport.update(width, height, true);
        gameViewport.apply();
    }

    public void dispose() {
        gameStage.dispose();
    }

}
