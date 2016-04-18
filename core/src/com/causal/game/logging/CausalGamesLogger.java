package com.causal.game.logging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class CausalGamesLogger {
	
	private FileHandle logFile;
	
	private static CausalGamesLogger instance;
	
	public static CausalGamesLogger get() {
		if(instance == null) {
			instance = new CausalGamesLogger();
		}
		
		return instance;
	}
	
	private CausalGamesLogger() {
		logFile = Gdx.files.local("LogFile.txt");
	}
	
	public void write(String tag, String message) {
		logFile.writeString(tag + message, true);
		logFile.writeString("\n", true);
		
		writeToGdx(message);
	}
	
	public void write(String message) {
		logFile.writeString(message, true);
		logFile.writeString("\n", true);
		
		writeToGdx(message);
	}
	
	private void writeToGdx(String message) {
		Gdx.app.log("", message);
	}

}
