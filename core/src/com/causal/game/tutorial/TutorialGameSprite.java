package com.causal.game.tutorial;

import com.causal.game.main.Game.Head;
import com.causal.game.main.GameSprite;
import com.causal.game.main.WorldSystem.Orientation;

public class TutorialGameSprite extends GameSprite{
	
	private Orientation orientation;
	
	public TutorialGameSprite(Head type, float x, float y, String framesPath, boolean isActive, Orientation orientation) {
		super(type, x, y, framesPath, isActive);
		
		this.orientation = orientation;
	}
	
	public void setValidOrientations() {
		spriteOrientation = new TutorialSpriteOrientation(getXGameCoord(), getYGameCoord(), orientation);
	}

}
