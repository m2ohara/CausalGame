package com.causal.game.tutorial;

import com.causal.game.behaviour.GossiperBehaviour;
import com.causal.game.main.WorldSystem.Orientation;

public class TutorialGossiperBehaviour extends GossiperBehaviour {
	
	private Orientation presetOrientation;
	
	public TutorialGossiperBehaviour(Orientation presetOrientation) {
		super();
		this.presetOrientation = presetOrientation;
	}
	
	@Override
	protected void setSpriteOrientation(int xGameCoord, int yGameCoord) {
		spriteOrientation = new TutorialSpriteOrientation(xGameCoord, yGameCoord, presetOrientation);
	}

}