package com.devcharles.piazzapanic.scene2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.devcharles.piazzapanic.GameScreen;

public class Hud extends ApplicationAdapter {
    public Stage gameStage;
    private Viewport gameViewport;
    private Integer customerTimer;
    private float timeCounter;
    private Integer reputation;
    private Skin skin;

    // A label is basically a widget
    Label timerLabel;
    Label timeNameLabel;
    Label reputationLabel;
    Label reputationNameLabel;

    public Hud (SpriteBatch spriteBatch, final GameScreen savedGame,final Game game ) {
        customerTimer = 000;
        timeCounter = 0;
        reputation = 3;

        gameViewport = new FitViewport(1280, 720, new OrthographicCamera());
        gameStage = new Stage(gameViewport, spriteBatch);
        skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));

        Table gameTable = new Table();
        gameTable.top();
        gameTable.setFillParent(true);
        Gdx.input.setInputProcessor(gameStage);

        timerLabel = new Label(String.format("%03d", customerTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        reputationLabel = new Label(String.format("%01d", reputation), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeNameLabel = new Label("Time", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        reputationNameLabel = new Label("Reputation", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        gameTable.add(timeNameLabel).expandX().padTop(10);
        gameTable.add(reputationNameLabel).expandX().padTop(10);
        gameTable.row();
        gameTable.add(timerLabel).expandX();
        gameTable.add(reputationLabel).expandX();
        gameTable.row();
        TextButton recipeBookButton = new TextButton("Recipe Book", skin);
        gameTable.add(recipeBookButton).width(80).height(40).left().top();
        TextButton tutorialButton = new TextButton("Tutorial", skin);
        gameTable.add(tutorialButton).width(80).height(40).right().top();
        gameStage.addActor(gameTable);

        recipeBookButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                        game.setScreen(new recipeBook(0, savedGame, game));
            }
        });
        tutorialButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Tutorial(0, savedGame, game, 0));
            }
        });

    }

    public void update(float deltaTime){
        timeCounter += deltaTime;
        if (timeCounter >= 1){
            customerTimer ++;
            timerLabel.setText(String.format("%03d", customerTimer));
            timeCounter = 0;
        }
        gameStage.act();
        gameStage.draw();

    }
    public void dispose(){
        gameStage.dispose();
    }


}