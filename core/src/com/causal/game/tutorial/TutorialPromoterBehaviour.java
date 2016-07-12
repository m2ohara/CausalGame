package com.causal.game.tutorial;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.causal.game.behaviour.PromoterBehaviour;
import com.causal.game.main.WorldSystem.Orientation;

public class TutorialPromoterBehaviour extends PromoterBehaviour {
	
	private Orientation presetOrientation;
	private List<Vector2> tapableSpriteCoords;
	
	public TutorialPromoterBehaviour(Orientation presetOrientation, List<Vector2> list) {
		super();
		this.presetOrientation = presetOrientation;
		this.tapableSpriteCoords = list;
	}
	
	@Override
	protected void setSpriteOrientation(int xGameCoord, int yGameCoord) {
		spriteOrientation = new TutorialSpriteOrientation(xGameCoord, yGameCoord, presetOrientation, tapableSpriteCoords);
	}

}
