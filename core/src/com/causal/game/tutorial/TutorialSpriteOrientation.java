package com.causal.game.tutorial;

import com.badlogic.gdx.Gdx;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.touch.SpriteOrientation;

public class TutorialSpriteOrientation extends SpriteOrientation {
	
	private Orientation presetOrientation;
	
	public TutorialSpriteOrientation(int xGameCoord, int yGameCoord, Orientation presetOrientation) 
	{
		super(xGameCoord, yGameCoord);
		this.presetOrientation = presetOrientation;
	}
	
	@Override
	public Orientation onRandomChange() {
		Gdx.app.log("TutorialSpriteOrientation", "Setting sprite "+xGameCoord+", "+yGameCoord+" to orientation index "+presetOrientation);
		orientation = presetOrientation;
		return orientation;
	}

}
