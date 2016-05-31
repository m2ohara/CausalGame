package com.causal.game.behaviour;

import com.causal.game.interact.GenericInteraction;
import com.causal.game.interact.GossiperAutonomousBehaviour;
import com.causal.game.interact.IInteractionType;
import com.causal.game.main.GameSprite;
import com.causal.game.touch.GossiperTouchAction;
import com.causal.game.touch.SpriteOrientation;

public class GossiperBehaviour implements ISpriteBehaviour {
	
	public Behaviour create(IInteractionType interactionType, int xGameCoord, int yGameCoord, boolean isActive, GameSprite gameSprite) {
	
		return new Behaviour(
				isActive, 
				new GenericInteraction(gameSprite, new GossiperAutonomousBehaviour()),
				new GossiperTouchAction(interactionType, xGameCoord, yGameCoord), 
				new GossiperProperties(),
				new SpriteOrientation(xGameCoord, yGameCoord));
	}


}
