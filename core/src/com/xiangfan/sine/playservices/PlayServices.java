package com.xiangfan.sine.playservices;

/**
 * Created by Jason on 6/22/17.
 */

public interface PlayServices {
    public void signIn();
    public void signOut();
    public void rateGame();
    public void unlockAchievement();
    public void submitScore(int highScore);
    public void showAchievement();
    public void showScore();
    public boolean isSignedIn();
}
