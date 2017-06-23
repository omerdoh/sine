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
import com.badlogic.gdx.math.Vector3;
import com.xiangfan.sine.SineGame;
import com.xiangfan.sine.playservices.PlayServices;

/**
 * Created by Xiang on 5/21/2017.
 */

public class MenuState extends State {
    private static Preferences prefs;

    private Texture background;
    private Texture highscore;
    private Texture playButton;
    private Texture leaderboardButton;
    private Texture title;
    private BitmapFont font;
    private GlyphLayout layout;
    private Vector3 mousePos;

    public MenuState(GameStateManager gsm, PlayServices playServices) {
        super(gsm, playServices);
        prefs = Gdx.app.getPreferences("sine");
        if (!prefs.contains("highScore2")) {
            prefs.putInteger("highScore2", 0);
        }
        cam.setToOrtho(false, SineGame.WIDTH / 2, SineGame.HEIGHT / 2);
        background = new Texture("bg.png");
        highscore = new Texture("highscore.png");
        playButton = new Texture("playbtn.png");
        leaderboardButton = new Texture("leaderboardbtn.png");
        title = new Texture("title.png");
        layout = new GlyphLayout();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 16;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        mousePos = new Vector3();
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(mousePos);
            if (mousePos.y > cam.position.y - playButton.getHeight() / 2 &&
                    mousePos.y < cam.position.y + playButton.getHeight() / 2) {
                // Play
                if (mousePos.x > cam.position.x - 39 * playButton.getWidth() / 40 &&
                        mousePos.x < cam.position.x + playButton.getWidth() / 40) {
                    gsm.set(new PlayState(gsm, playServices));
                // Leaderboard
                } else if (mousePos.x > cam.position.x + playButton.getWidth() / 10 &&
                        mousePos.x < cam.position.x + 11 * playButton.getWidth() / 10) {
                    playServices.showScore();
                }
            }
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
        sb.draw(title, cam.position.x - title.getWidth() / 2, cam.position.y + 110);
        sb.draw(playButton, cam.position.x - 39 * playButton.getWidth() / 40,
                cam.position.y - playButton.getHeight() / 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        sb.draw(leaderboardButton, cam.position.x + playButton.getWidth() / 10,
                cam.position.y - playButton.getHeight() / 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        sb.draw(highscore, cam.position.x - highscore.getWidth() / 2, highscore.getHeight() * 2);
        layout.setText(font, Integer.toString(getHighScore()));
        font.draw(sb, layout, SineGame.WIDTH / 4 - layout.width / 2, highscore.getHeight() * 2);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        highscore.dispose();
        playButton.dispose();
        leaderboardButton.dispose();
        title.dispose();
        font.dispose();
        System.out.println("Play state disposed");
    }

    private static int getHighScore() {
        return prefs.getInteger("highscore2");
    }
}
