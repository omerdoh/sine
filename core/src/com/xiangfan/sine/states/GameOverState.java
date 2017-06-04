package com.xiangfan.sine.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.xiangfan.sine.SineGame;

/**
 * Created by Xiang on 5/22/2017.
 */

public class GameOverState extends State {
    private BitmapFont font1;
    private BitmapFont font2;
    private Texture background;
    private Texture playButton;
    private GlyphLayout layout;
    private FreeTypeFontGenerator generator;
    private int score;
    private static Preferences prefs;

    public GameOverState(GameStateManager gsm, int score) {
        super(gsm);
        prefs = Gdx.app.getPreferences("sine");
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }
        cam.setToOrtho(false, SineGame.WIDTH / 2, SineGame.HEIGHT / 2);
        this.score = score;

        generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        layout = new GlyphLayout();
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 30;
        parameter.color = Color.BLACK;
        font1 = generator.generateFont(parameter);
        parameter.size = 20;
        font2 = generator.generateFont(parameter);

        if (score > getHighScore()) {
            setHighScore(score);
        }

        background = new Texture("bg.png");
        playButton = new Texture("playbtn.png");
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        sb.draw(background, 0, 0);
        sb.draw(playButton, cam.position.x - playButton.getWidth() / 2, cam.position.y - playButton.getHeight() / 2);

        layout.setText(font1, "GAME OVER");
        font1.draw(sb, layout, SineGame.WIDTH / 4 - layout.width / 2, cam.position.y + 130);

        layout.setText(font2, "Best");
        font2.draw(sb, layout, SineGame.WIDTH / 8 - layout.width / 2, cam.position.y - 100);
        layout.setText(font2, "Score");
        font2.draw(sb, layout, 3 * SineGame.WIDTH / 8 - layout.width / 2, cam.position.y - 100);
        layout.setText(font2, Integer.toString(getHighScore()));
        font2.draw(sb, layout, SineGame.WIDTH / 8 - layout.width / 2, cam.position.y - 125);
        layout.setText(font2, Integer.toString(score));
        font2.draw(sb, layout, 3 * SineGame.WIDTH / 8 - layout.width / 2, cam.position.y - 125);

        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playButton.dispose();
        font1.dispose();
        font2.dispose();
        generator.dispose();
        System.out.println("Game state disposed");
    }

    private static void setHighScore(int val) {
        prefs.putInteger("highscore", val);
        prefs.flush();
    }

    private static int getHighScore() {
        return prefs.getInteger("highscore");
    }
}
