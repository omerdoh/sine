package com.xiangfan.sine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.xiangfan.sine.SineGame;
import com.xiangfan.sine.playservices.PlayServices;

public class AndroidLauncher extends AndroidApplication implements PlayServices {
	private GameHelper gameHelper;
	private final static int requestCode = 1;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);

		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInFailed(){ }

			@Override
			public void onSignInSucceeded(){ }
		};

		gameHelper.setup(gameHelperListener);

		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new SineGame(this), config);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}

	// Google Play Services methods
    @Override
    public void signIn()
    {
        try
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void signOut()
    {
        try
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    gameHelper.signOut();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void rateGame()
    {
        String str = "Your PlayStore Link";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }

    @Override
    public void unlockAchievement()
    {
        //Games.Achievements.unlock(gameHelper.getApiClient(),
        //        getString(R.string.achievement_dum_dum));
    }

    @Override
    public void submitScore(int highScore)
    {
        if (isSignedIn() == true)
        {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_highscore), highScore);
        }
    }

    @Override
    public void showAchievement()
    {
        if (isSignedIn() == true)
        {
            //startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient(),
            //        getString(R.string.achievement_dum_dum)), requestCode);
        }
        else
        {
            signIn();
        }
    }

    @Override
    public void showScore()
    {
        if (isSignedIn() == true)
        {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_highscore)), requestCode);
        }
        else
        {
            signIn();
        }
    }

    @Override
    public boolean isSignedIn()
    {
        return gameHelper.isSignedIn();
    }
}
