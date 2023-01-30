package com.devcharles.piazzapanic.scene2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.devcharles.piazzapanic.GameScreen;
import com.devcharles.piazzapanic.PiazzaPanic;

public class Tutorial extends ApplicationAdapter implements Screen {

    OrthographicCamera camera;
    private Stage stage;
    private Skin skin;
    private Batch batch;
    private Sprite sprite;
    private String[] completeTutorial = {"Tutorial/page0.png", "Tutorial/page1.png", "Tutorial/page2.png", "Tutorial/page3.png", "Tutorial/page4.png",
            "Tutorial/page5.png", "Tutorial/page6.png", "Tutorial/page7.png", "Tutorial/page8.png"};
    private Integer newPageNumber;
    private Integer rightButtonOn = 1, leftButtonOn = 1;
    private TextButton leftRecipeButton, rightRecipeButton;




    public Tutorial( final Integer currentPage, final GameScreen savedGame, final Game game, final Integer launchNewGame) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal(completeTutorial[currentPage])));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        skin = new Skin(Gdx.files.internal("craftacular/skin/craftacular-ui.json"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.top();
        root.setFillParent(true);
        stage.addActor(root);


        TextButton exitButtonToGame = new TextButton("Exit", skin);
        root.add(exitButtonToGame).width(180).height(80).expandX().left();
        exitButtonToGame.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (launchNewGame == 1){
                    game.setScreen(new GameScreen((PiazzaPanic) game,2));
                    dispose();
                } else {
                    game.setScreen(savedGame);
                    dispose();
                }
            }
        });

        root.row();

        //Begin layout
        if(currentPage == 0){
            leftButtonOn = 0;
        }
        else if(completeTutorial.length - 1 == currentPage){
            rightButtonOn = 0;
        }

        if(leftButtonOn == 1){
            TextButton leftRecipeButton = new TextButton("Page Left", skin);
            root.add(leftRecipeButton).width(200).height(50).expandX().left();
            //Checks if button is clicked
            leftRecipeButton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    newPageNumber = currentPage - 1;
                    game.setScreen(new Tutorial(newPageNumber, savedGame, game, launchNewGame));
                    dispose();
                }
            });
        }
        if(rightButtonOn == 1){
            TextButton rightRecipeButton = new TextButton("Page Right", skin);
            root.add(rightRecipeButton).width(200).height(50).expandX().right();
            //Checks if button is clicked
            rightRecipeButton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    newPageNumber = currentPage + 1;

                    game.setScreen(new Tutorial(newPageNumber, savedGame, game, launchNewGame));
                    dispose();
                }
            });
        }
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        sprite.draw(batch);
        batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void dispose () {
        skin.dispose();
        stage.dispose();
    }
}

