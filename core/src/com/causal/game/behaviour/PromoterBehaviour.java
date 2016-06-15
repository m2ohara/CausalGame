package com.causal.game.behaviour;

import com.causal.game.interact.GenericInteraction;
import com.causal.game.interact.IInteractionType;
import com.causal.game.interact.PromoterAutonomousBehaviour;
import com.causal.game.main.GameSprite;
import com.causal.game.touch.ISpriteOrientation;
import com.causal.game.touch.PromoterTouchAction;
import com.causal.game.touch.SpriteOrientation;

public class PromoterBehaviour implements ISpriteBehaviour {
	
	protected ISpriteOrientation spriteOrientation;
	
	public Behaviour create(IInteractionType interactionType, int xGameCoord, int yGameCoord, boolean isActive, GameSprite gameSprite) {
		
		setSpriteOrientation(xGameCoord, yGameCoord);
	
		return new Behaviour(
				isActive, 
				new GenericInteraction(gameSprite, new PromoterAutonomousBehaviour()),
				new PromoterTouchAction(interactionType, xGameCoord, yGameCoord), 
				new PromoterProperties(),
				spriteOrientation);
	}
	
	protected void setSpriteOrientation(int xGameCoord, int yGameCoord) {
		spriteOrientation = new SpriteOrientation(xGameCoord, yGameCoord);
	}
}
