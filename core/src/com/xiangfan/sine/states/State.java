package com.xiangfan.sine.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.xiangfan.sine.playservices.PlayServices;


/**
 * Created by Xiang on 5/21/2017.
 */

public abstract class State {
    protected static final int BUTTON_WIDTH = 90;
    protected static final int BUTTON_HEIGHT = 55;


    protected OrthographicCamera cam;
    protected Vector3 mouse;
    protected GameStateManager gsm;

    protected PlayServices playServices;

    protected State(GameStateManager gsm, PlayServices playServices){
        this.gsm = gsm;
        this.playServices = playServices;
        cam = new OrthographicCamera();
        mouse = new Vector3();
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}
