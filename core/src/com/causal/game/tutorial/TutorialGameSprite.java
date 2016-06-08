package com.causal.game.tutorial;

import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.main.GameSprite;
import com.causal.game.main.WorldSystem.Orientation;

public class TutorialGameSprite extends GameSprite{
	
	private Orientation presetOrientation;
	private Orientation swipeOrientation;
	
	public TutorialGameSprite(ISpriteBehaviour behaviour, float x, float y, String framesPath, boolean isActive, Orientation orientation, Orientation swipeOrientation) {
		super(behaviour, x, y, framesPath, isActive);
		
		this.presetOrientation = orientation;
		this.swipeOrientation = swipeOrientation;
	}
	
//	public void setValidOrientations() {
//		spriteOrientation = new TutorialSpriteOrientation(getXGameCoord(), getYGameCoord(), presetOrientation);
//	}
	
	public Orientation getSwipeOrientation() {
		return this.swipeOrientation;
	}

}
