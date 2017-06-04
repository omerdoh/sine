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
 * Created by Xiang on 5/21/2017.
 */

public class MenuState extends State{
    private static Preferences prefs;
    private Texture background;
    private Texture highscore;
    private Texture playButton;
    private Texture title;
    private BitmapFont font;
    private GlyphLayout layout;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        prefs = Gdx.app.getPreferences("sine");
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }
        cam.setToOrtho(false, SineGame.WIDTH / 2, SineGame.HEIGHT / 2);
        background = new Texture("bg.png");
        highscore = new Texture("highscore.png");
        playButton = new Texture("playbtn.png");
        title = new Texture("title.png");
        layout = new GlyphLayout();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 16;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
    }

    @Override
    public void handleInput() {
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
        sb.draw(title, cam.position.x - title.getWidth() / 2, cam.position.y + 110);
        sb.draw(playButton, cam.position.x - playButton.getWidth() / 2, cam.position.y - playButton.getHeight() / 2);
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
        title.dispose();
        font.dispose();
        System.out.println("Play state disposed");
    }

    private static int getHighScore() {
        return prefs.getInteger("highscore");
    }
}
