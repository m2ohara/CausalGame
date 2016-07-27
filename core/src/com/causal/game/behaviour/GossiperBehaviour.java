package com.causal.game.behaviour;

import com.causal.game.interact.AutonomousInteraction;
import com.causal.game.interact.GossiperAutonomousBehaviour;
import com.causal.game.interact.IInteractionType;
import com.causal.game.main.GameSprite;
import com.causal.game.touch.GossiperTouchAction;
import com.causal.game.touch.ISpriteOrientation;
import com.causal.game.touch.SpriteOrientation;

public class GossiperBehaviour implements ISpriteBehaviour {
	
	protected ISpriteOrientation spriteOrientation;
	protected AutonomousInteraction autoInteraction;
	protected IBehaviourProperties properties;
	
	public Behaviour create(IInteractionType interactionType, int xGameCoord, int yGameCoord, boolean isActive, GameSprite gameSprite) {
		
		setSpriteOrientation(xGameCoord, yGameCoord);
		setAutoInteraction(gameSprite);
		setBehaviourProperties();
	
		return new Behaviour(
				isActive, 
				autoInteraction,
				new GossiperTouchAction(interactionType, xGameCoord, yGameCoord), 
				properties,
				spriteOrientation);
	}
	
	protected void setSpriteOrientation(int xGameCoord, int yGameCoord) {
		spriteOrientation = new SpriteOrientation(xGameCoord, yGameCoord);
	}
	
	protected void setAutoInteraction(GameSprite gameSprite) {
		autoInteraction = new AutonomousInteraction(gameSprite, new GossiperAutonomousBehaviour());
	}
	
	protected void setBehaviourProperties() {
		properties = new GossiperProperties();
	}


}
