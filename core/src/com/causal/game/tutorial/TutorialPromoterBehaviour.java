package com.causal.game.tutorial;

import com.causal.game.behaviour.PromoterBehaviour;
import com.causal.game.main.WorldSystem.Orientation;

public class TutorialPromoterBehaviour extends PromoterBehaviour {
	
	private Orientation presetOrientation;
	
	public TutorialPromoterBehaviour(Orientation presetOrientation) {
		super();
		this.presetOrientation = presetOrientation;
	}
	
	@Override
	protected void setSpriteOrientation(int xGameCoord, int yGameCoord) {
		spriteOrientation = new TutorialSpriteOrientation(xGameCoord, yGameCoord, presetOrientation);
	}

}
