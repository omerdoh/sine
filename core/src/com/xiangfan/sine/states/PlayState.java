package com.xiangfan.sine.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.xiangfan.sine.SineGame;
import com.xiangfan.sine.sprites.Bird;
import com.xiangfan.sine.sprites.Tube;

import java.awt.Color;

/**
 * Created by Xiang on 5/21/2017.
 */

public class PlayState extends State {
    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;

    private Bird bird;
    private Texture bg;
    private int score;
    private BitmapFont font;

    private Array<Tube> tubes;

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, SineGame.WIDTH / 2, SineGame.HEIGHT / 2);
        bird = new Bird(50, 300);
        bg = new Texture("bg.png");
        score = 0;
        font = new BitmapFont();
        font.setColor(com.badlogic.gdx.graphics.Color.BLACK);

        tubes = new Array<Tube>();

        for (int i = 1; i <= TUBE_COUNT; i++){
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        bird.update(dt);
        cam.position.x = bird.getPosition().x + 80;

        if (bird.getPosition().y <= 0  || bird.getPosition().y >= cam.viewportHeight) {
            gsm.set(new PlayState(gsm));
        }

        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);
            if (cam.position.x - cam.viewportWidth / 2 > tube.getPosTopTube().x + tube.getTopTube().getWidth()){
                tube.reposition(tube.getPosTopTube().x + (Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT);
                tube.setChecked(false);
            }

            if (bird.getPosition().x >= tube.getPosBottomTube().x && !tube.hasChecked()) {
                tube.setChecked(true);
                score++;
            }

            if (tube.collides(bird.getBounds())){
                gsm.set(new PlayState(gsm));
            }
        }

        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        sb.draw(bird.getBird(), bird.getPosition().x, bird.getPosition().y);

        System.out.println("Score " + score);

        for (Tube tube : tubes){
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBottomTube().x, tube.getPosBottomTube().y);

        }
        font.draw(sb, Integer.toString(score), cam.position.x - 7, cam.position.y + 190);
        sb.end();



    }

    @Override
    public void dispose() {
        font.dispose();
        bg.dispose();
        bird.dispose();

        for (Tube tube : tubes) {
            tube.dispose();
        }

        System.out.println("Play state disposed");
    }



}
