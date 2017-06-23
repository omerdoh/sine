package com.xiangfan.sine.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.Timer;
import com.xiangfan.sine.SineGame;
import com.xiangfan.sine.playservices.PlayServices;
import com.xiangfan.sine.sprites.Bird;
import com.xiangfan.sine.sprites.Tube;


/**
 * Created by Xiang on 5/21/2017.
 */

public class PlayState extends State {
    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;

    private Bird bird;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private int score;
    private BitmapFont font;
    private boolean collision;

    private Array<Tube> tubes;
    private Queue<Vector2> path;

    private ShapeRenderer sr;
    private Sound dead;


    protected PlayState(GameStateManager gsm, PlayServices playServices) {
        super(gsm, playServices);
        cam.setToOrtho(false, SineGame.WIDTH / 2, SineGame.HEIGHT / 2);
        bird = new Bird(50, 300);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth, GROUND_Y_OFFSET);
        groundPos2 = new Vector2(cam.position.x - cam.viewportWidth + ground.getWidth(), GROUND_Y_OFFSET);
        score = 0;
        collision = false;
        dead = Gdx.audio.newSound(Gdx.files.internal("collide.wav"));

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setColor(com.badlogic.gdx.graphics.Color.BLACK);
        font.setUseIntegerPositions(false);

        tubes = new Array<Tube>();
        path = new Queue<Vector2>();
        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);

        for (int i = 2; i <= TUBE_COUNT + 1; i++){
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
        if (!collision) {
            handleInput();
            updateGround();
            bird.update(dt);

            if (path.size == 0 || !bird.getPosition().equals(path.last())) {
                path.addLast(new Vector2(bird.getPosition().x, bird.getPosition().y));
            }
            if (path.size > 100) {
                path.removeFirst();
            }

            cam.position.x = bird.getPosition().x;

            if (bird.getPosition().y - bird.getBird().getHeight() / 2 <= ground.getHeight() + GROUND_Y_OFFSET ||
                    bird.getPosition().y + bird.getBird().getHeight() / 2 >= cam.viewportHeight) {
                collision = true;
                dead.play();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        gsm.set(new GameOverState(gsm, score, playServices));
                    }
                }, 1.0f);
                return;
            }

            for (int i = 0; i < tubes.size; i++) {
                Tube tube = tubes.get(i);
                if (cam.position.x - cam.viewportWidth / 2 > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                    tube.reposition(tube.getPosTopTube().x + (Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT);
                    tube.setChecked(false);
                }

                if (bird.getPosition().x >= tube.getPosBottomTube().x && !tube.hasChecked()) {
                    tube.setChecked(true);
                    score++;
                }

                if (tube.collides(bird.getBounds())) {
                    collision = true;
                    dead.play();
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            gsm.set(new GameOverState(gsm, score, playServices));
                        }
                    }, 1.0f);
                    return;
                }
            }

            cam.update();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);

        sb.draw(bird.getBird(),
                bird.getPosition().x - bird.getBird().getWidth() / 2,
                bird.getPosition().y - bird.getBird().getHeight() / 2);

        for (Tube tube : tubes){
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBottomTube().x, tube.getPosBottomTube().y);
        }

        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);

        font.draw(sb, Integer.toString(score), cam.position.x - 7, cam.position.y + 190);
        sb.end();

        if (path.size > 0) {
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setProjectionMatrix(cam.combined);
            sr.setColor(Color.BLACK);
            for (int i = 1; i < path.size; i++) {
                sr.rectLine(path.get(i - 1).x, path.get(i - 1).y, path.get(i).x, path.get(i).y, 2f);
            }
            sr.end();
        }
    }

    @Override
    public void dispose() {
        font.dispose();
        bg.dispose();
        bird.dispose();
        dead.dispose();
        ground.dispose();
        sr.dispose();

        for (Tube tube : tubes) {
            tube.dispose();
        }

        System.out.println("Play state disposed");
    }

    private void updateGround() {
        if (cam.position.x - cam.viewportWidth / 2 > groundPos1.x + ground.getWidth()) {
            groundPos1.add(ground.getWidth() * 2, 0);
        }
        if (cam.position.x - cam.viewportWidth / 2 > groundPos2.x + ground.getWidth()) {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
    }
}
