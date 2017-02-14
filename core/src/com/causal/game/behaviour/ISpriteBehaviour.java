package com.causal.game.behaviour;

import com.causal.game.interact.IInteractionType;
import com.causal.game.sprite.GameSprite;

public interface ISpriteBehaviour {
	
	public Behaviour create(IInteractionType interactionType, int xGameCoord, int yGameCoord, boolean isActive, GameSprite gameSprite);

	
}
