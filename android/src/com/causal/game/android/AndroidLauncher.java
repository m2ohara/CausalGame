package com.causal.game.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.causal.game.data.SqlConnection;
import com.causal.game.main.Game;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		Game game = new Game();
		((Game) game).setAndroid(true);
		initialize(game, config);
		
		//Obtain db connection from jdbc driver
		new SqlConnection(new AndroidActionResolver(getContext()));  
	}
}
