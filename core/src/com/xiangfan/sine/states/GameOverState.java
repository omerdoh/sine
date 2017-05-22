package com.xiangfan.sine.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xiangfan.sine.SineGame;

/**
 * Created by Xiang on 5/22/2017.
 */

public class GameOverState extends State {
    private BitmapFont font;
    private Texture background;
    private Texture playButton;
    private int score;
    private static Preferences prefs;

    public GameOverState(GameStateManager gsm, int score) {
        super(gsm);
        prefs = Gdx.app.getPreferences("ZombieBird");
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }
        cam.setToOrtho(false, SineGame.WIDTH / 2, SineGame.HEIGHT / 2);
        this.score = score;
        font = new BitmapFont();
        font.setColor(com.badlogic.gdx.graphics.Color.BLACK);
        font.getData().setScale(2);

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
        sb.draw(playButton, cam.position.x - playButton.getWidth() / 2, cam.position.y - 65);
        font.draw(sb, "GAME OVER", cam.position.x - 89, cam.position.y + 100 );
        font.draw(sb, Integer.toString(score), cam.position.x - 5, cam.position.y + 170);
        font.draw(sb, "Toppest " + Integer.toString(getHighScore()) , cam.position.x - 65, cam.position.y + 57);

        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playButton.dispose();
        font.dispose();
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
