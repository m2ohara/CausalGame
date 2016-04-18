package com.causal.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.causal.game.logging.CausalGamesLogger;
import com.causal.game.main.Game;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(400, 600);
        }

        @Override
        public ApplicationListener getApplicationListener () {
            	Gdx.app.setLogLevel(LOG_INFO);
            	CausalGamesLogger.get().write("Initiating gwt application");
                return new Game();
        }

		@Override
		public ApplicationListener createApplicationListener() {
			return new Game();
		}
}