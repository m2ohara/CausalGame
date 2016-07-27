package com.causal.game.tutorial;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.causal.game.behaviour.PromoterBehaviour;
import com.causal.game.interact.PromoterAutonomousBehaviour;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.InfluenceType;
import com.causal.game.main.WorldSystem.Orientation;

public class TutorialPromoterBehaviour extends PromoterBehaviour {
	
	private Orientation presetOrientation;
	private List<Vector2> tapableSpriteCoords;
	private Orientation autoInteractOrientation;
	
	public TutorialPromoterBehaviour(Orientation presetOrientation, List<Vector2> list, Orientation autoInteractOrientation) {
		super();
		this.presetOrientation = presetOrientation;
		this.tapableSpriteCoords = list;
		this.autoInteractOrientation = autoInteractOrientation;
	}
	
	@Override
	protected void setSpriteOrientation(int xGameCoord, int yGameCoord) {
		spriteOrientation = new TutorialSpriteOrientation(xGameCoord, yGameCoord, presetOrientation, tapableSpriteCoords, autoInteractOrientation);
	}
	
	@Override
	protected void setAutoInteraction(GameSprite gameSprite) {
		autoInteraction = new TutorialAutonomousInteraction(gameSprite, new PromoterAutonomousBehaviour());
	}	
	
	@Override
	protected void setBehaviourProperties() {
		properties = new TutorialPromoterProperties();
	}

}
