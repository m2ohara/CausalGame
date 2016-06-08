package com.causal.game.tutorial;

import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.touch.ISpriteOrientation;
import com.causal.game.touch.SpriteOrientation;

public class TutorialSpriteOrientation extends SpriteOrientation implements ISpriteOrientation {
	
	private Orientation presetOrientation;
	
	public TutorialSpriteOrientation(int xGameCoord, int yGameCoord, Orientation presetOrientation) 
	{
		super(xGameCoord, yGameCoord);
		this.presetOrientation = presetOrientation;
	}
	
	@Override
	public Orientation onRandomChange() {
		orientation = presetOrientation;
		return orientation;
	}

}
