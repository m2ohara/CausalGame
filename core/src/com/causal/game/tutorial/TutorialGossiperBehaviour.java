package com.causal.game.tutorial;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.causal.game.behaviour.GossiperBehaviour;
import com.causal.game.interact.DeceiverAutonomousBehaviour;
import com.causal.game.interact.GossiperAutonomousBehaviour;
import com.causal.game.interact.IInteraction;
import com.causal.game.interact.PromoterAutonomousBehaviour;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.sprite.GameSprite;
import com.causal.game.sprite.GameSprite.InfluenceType;

public class TutorialGossiperBehaviour extends GossiperBehaviour {
	
	private Orientation presetOrientation;
	private List<Vector2> tapableSpriteCoords;
	private Orientation autoInteractOrientation;
	private InfluenceType influenceType;
	private TutorialAnimationProperties animations;
	
	public TutorialGossiperBehaviour(Orientation presetOrientation, List<Vector2> list, Orientation autoInteractOrientation, InfluenceType influenceType) {
		super();
		this.presetOrientation = presetOrientation;
		this.tapableSpriteCoords = list;
		this.autoInteractOrientation = autoInteractOrientation;
		this.influenceType = influenceType;
	}
	
	@Override
	protected void setSpriteOrientation(int xGameCoord, int yGameCoord) {
		spriteOrientation = new TutorialSpriteOrientation(xGameCoord, yGameCoord, presetOrientation, tapableSpriteCoords, autoInteractOrientation);
	}
	
	@Override
	protected void setAutoInteraction(GameSprite gameSprite) {
		IInteraction interaction = influenceType == InfluenceType.SUPPORT ? new PromoterAutonomousBehaviour() : new DeceiverAutonomousBehaviour(); 
		autoInteraction = new TutorialAutonomousInteraction(gameSprite, interaction);
	}	
	
	@Override
	protected void setBehaviourProperties() {
		properties = new TutorialGossiperProperties(influenceType);
	}

}
