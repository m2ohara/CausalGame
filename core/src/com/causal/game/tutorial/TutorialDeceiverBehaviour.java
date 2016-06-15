package com.causal.game.tutorial;

import com.causal.game.behaviour.DeceiverBehaviour;
import com.causal.game.main.WorldSystem.Orientation;

public class TutorialDeceiverBehaviour extends DeceiverBehaviour {
	
	private Orientation presetOrientation;
	
	public TutorialDeceiverBehaviour(Orientation presetOrientation) {
		super();
		this.presetOrientation = presetOrientation;
	}
	
	@Override
	protected void setSpriteOrientation(int xGameCoord, int yGameCoord) {
		spriteOrientation = new TutorialSpriteOrientation(xGameCoord, yGameCoord, presetOrientation);
	}
}
