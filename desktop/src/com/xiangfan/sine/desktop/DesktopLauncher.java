package com.xiangfan.sine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.xiangfan.sine.SineGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = SineGame.WIDTH;
		config.height = SineGame.HEIGHT;
		config.title = SineGame.TITLE;
		new LwjglApplication(new SineGame(), config);
	}
}