package com.devcharles.piazzapanic.scene2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CookCarryHud extends ApplicationAdapter {
    public Stage gameStage;
    private Viewport gameViewport;
    private Integer customerTimer;
    private Skin skin;
    private Image photo, photo2;


    public CookCarryHud(SpriteBatch spriteBatch) {

        gameViewport = new FitViewport(1280, 720, new OrthographicCamera());
        gameStage = new Stage(gameViewport, spriteBatch);


        Table gameTable = new Table();
        gameTable.right();
        gameTable.setFillParent(true);
        Gdx.input.setInputProcessor(gameStage);

        photo = new Image(new Texture(Gdx.files.internal("droplet.png")));
        photo2 = new Image(new Texture(Gdx.files.internal("droplet.png")));
        gameTable.add(photo).expandX().right();
        gameTable.row();
        gameTable.add(photo2).expandX().right();
        gameStage.addActor(gameTable);


    }

    public void updateStack(){

    }
    public void update(float deltaTime){
        gameStage.act();
        gameStage.draw();

    }
    public void dispose(){
        gameStage.dispose();
    }


}

