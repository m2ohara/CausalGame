package com.causal.game.behaviour;

import com.causal.game.interact.AutonomousInteraction;
import com.causal.game.interact.IInteractionType;
import com.causal.game.interact.PromoterAutonomousBehaviour;
import com.causal.game.main.GameSprite;
import com.causal.game.touch.ISpriteOrientation;
import com.causal.game.touch.PromoterTouchAction;
import com.causal.game.touch.SpriteOrientation;

public class PromoterBehaviour implements ISpriteBehaviour {
	
	protected ISpriteOrientation spriteOrientation;
	protected AutonomousInteraction autoInteraction;
	
	public Behaviour create(IInteractionType interactionType, int xGameCoord, int yGameCoord, boolean isActive, GameSprite gameSprite) {
		
		setSpriteOrientation(xGameCoord, yGameCoord);
		setAutoInteraction(gameSprite);
	
		return new Behaviour(
				isActive, 
				autoInteraction,
				new PromoterTouchAction(interactionType, xGameCoord, yGameCoord), 
				new PromoterProperties(),
				spriteOrientation);
	}
	
	protected void setSpriteOrientation(int xGameCoord, int yGameCoord) {
		spriteOrientation = new SpriteOrientation(xGameCoord, yGameCoord);
	}
	
	protected void setAutoInteraction(GameSprite gameSprite) {
		autoInteraction = new AutonomousInteraction(gameSprite, new PromoterAutonomousBehaviour());
	}
}
