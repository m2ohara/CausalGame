package com.causal.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.causal.game.data.SqlConnection;
import com.causal.game.main.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 600;
		config.width = 480;
		new LwjglApplication(new Game(), config);
		
		//Obtain db connection from jdbc driver
		new SqlConnection(new DesktopActionResolver());
	}
}
