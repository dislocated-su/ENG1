package com.devcharles.piazzapanic;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.devcharles.piazzapanic.scene2d.Slideshow;

/**
 * Endless menu screen for selecting easy, normal or hard difficulty, transitions the player to the Tutorial
 * {@link Slideshow} on button press
 */
public class EndlessMenuScreen extends ApplicationAdapter implements Screen {

    final PiazzaPanic game;
    OrthographicCamera camera;
    private Stage stage;
    private Skin skin;
    private Batch batch;
    private Sprite sprite;
    private BitmapFont gamesFont;
    private BitmapFont subtitleFont;
    private Label title;
    private Label subtitle;
    private TextButton toggleDifficulty;

    public EndlessMenuScreen(final PiazzaPanic game) {

        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        batch = new SpriteBatch();

        sprite = new Sprite(new Texture(Gdx.files.internal("mainMenuImage.png")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        skin = new Skin(Gdx.files.internal("craftacular/skin/craftacular-ui.json"));
        stage = new Stage(new ScreenViewport());

        Label.LabelStyle menuLabelStyle = new Label.LabelStyle();
        Label.LabelStyle subtitleLabelStyle = new Label.LabelStyle();
        gamesFont = new BitmapFont(Gdx.files.internal("craftacular/raw/font-title-export.fnt"));
        subtitleFont = new BitmapFont(Gdx.files.internal("craftacular/raw/font-title-export.fnt"));
        subtitleFont.getData().setScale((float) 0.7,(float) 0.7);
        menuLabelStyle.font = gamesFont;
        subtitleLabelStyle.font=subtitleFont;

        title = new Label("Piazza Panic", menuLabelStyle);
        subtitle = new Label("Endless mode - Toggle Difficulty", subtitleLabelStyle);
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        root.add(title).expandX().padBottom(120);
        root.row();
        root.add(subtitle).expandX().padBottom(50);
        root.row();
        toggleDifficulty = new TextButton("Easy", skin);
        TextButton backButton = new TextButton("Back", skin);
        TextButton startButton = new TextButton("Start", skin);
        root.add(toggleDifficulty).padBottom(50);
        root.row();
        root.add(backButton);
        root.row();
        root.add(startButton);

        // Checks if button is clicked and if clicked goes onto the tutorial
        startButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // TODO Start endless mode with specified difficulty.
                GameScreen.Difficulty difficulty = GameScreen.Difficulty.ENDLESS_NORMAL;
                switch(toggleDifficulty.getText().toString()){
                    case "Easy":
                        difficulty=GameScreen.Difficulty.ENDLESS_EASY;
                        break;
                    case "Normal":
                        difficulty=GameScreen.Difficulty.ENDLESS_NORMAL;
                        break;
                    case "Hard":
                        difficulty=GameScreen.Difficulty.ENDLESS_HARD;
                        break;
                }
                game.setScreen(new GameScreen(game,999,difficulty));
            }
        });

        // Toggles difficulty of endless game mode
        toggleDifficulty.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

                if(toggleDifficulty.getText().toString().equals("Easy")){
                    toggleDifficulty.setText("Normal");
                }
                else if(toggleDifficulty.getText().toString().equals("Normal")){
                    toggleDifficulty.setText("Hard");
                }
                else if(toggleDifficulty.getText().toString().equals("Hard")) {
                    toggleDifficulty.setText("Easy");
                }
            }
        });

        // Checks if the back button is clicked and then goes back to main menu screen.
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // draws everything (dont change this order unless you know what youre doing)
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
    public void hide() {

    }
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}
