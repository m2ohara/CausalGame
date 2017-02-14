package com.causal.game.interact;

import com.causal.game.sprite.GameSprite;

public interface IInteractionType {
	
	void setStatus();
	
	void setInfluencedSprite();
	
	void complete();
	
	void setInteracts(GameSprite interactor, GameSprite interactee);

}
