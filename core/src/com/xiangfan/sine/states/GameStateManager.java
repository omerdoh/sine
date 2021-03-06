package com.xiangfan.sine.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xiangfan.sine.playservices.PlayServices;

import java.util.Stack;

/**
 * Created by Xiang on 5/21/2017.
 */

public class GameStateManager {

    private Stack<State> states;

    public GameStateManager(PlayServices playServices) {
        states = new Stack<State>();
    }

    public void push(State state) {
        states.push(state);
    }

    public void pop(){
        states.pop().dispose();
    }

    public void set(State state){
        states.pop().dispose();
        states.push(state);
    }

    public void update(float dt){
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        states.peek().render(sb);
    }

}
