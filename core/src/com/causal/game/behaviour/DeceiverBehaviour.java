package com.causal.game.behaviour;

import com.causal.game.interact.DeceiverAutonomousBehaviour;
import com.causal.game.interact.GenericInteraction;
import com.causal.game.interact.IInteractionType;
import com.causal.game.main.GameSprite;
import com.causal.game.touch.DeceiverTouchAction;
import com.causal.game.touch.SpriteOrientation;

public class DeceiverBehaviour implements ISpriteBehaviour {
	
	public Behaviour create(IInteractionType interactionType, int xGameCoord, int yGameCoord, boolean isActive, GameSprite gameSprite) {
	
		return new Behaviour(
				isActive, 
				new GenericInteraction(gameSprite, new DeceiverAutonomousBehaviour()),
				new DeceiverTouchAction(interactionType, xGameCoord, yGameCoord), 
				new DeceiverProperties(),
				new SpriteOrientation(xGameCoord, yGameCoord));
	}
}
