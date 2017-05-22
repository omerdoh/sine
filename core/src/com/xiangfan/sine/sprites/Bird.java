package com.xiangfan.sine.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Xiang on 5/21/2017.
 */

public class Bird {
    public Vector3 getPosition() {
        return position;
    }

    public Texture getBird() {
        return bird;
    }

    private static final int MOVEMENT = 150;
    private int GRAVITY = -10;

    private Vector3 position;
    private Vector3 velocity;
    private Texture bird;
    private Rectangle bounds;
    private Sound flap;

    public Bird(int x, int y) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        bird = new Texture("bird.png");
        bounds = new Rectangle(x - bird.getWidth() / 2, y - bird.getHeight() / 2, bird.getWidth(), bird.getHeight());
        flap = Gdx.audio.newSound(Gdx.files.internal("flap.wav"));
    }

    public void update(float dt) {
        if (position.y >0){
            velocity.add(0, GRAVITY, 0);
        }

        velocity.scl(dt);
        position.add(MOVEMENT * dt, velocity.y, 0);
        if (position.y < 0) {
            position.y = 0;
        }

        velocity.scl(1 / dt);
        bounds.setPosition(position.x, position.y);
    }

    public void jump(){
        GRAVITY *= -1;
        flap.play();
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public float getDistance() {
        return position.x;
    }

    public void dispose(){
        bird.dispose();
        flap.dispose();
    }
}

