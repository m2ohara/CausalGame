package com.causal.game.interact;

import com.causal.game.main.GameSprite;

public interface IInteractionType {
	
	void setStatus();
	
	void setInfluencedSprite();
	
	void complete();
	
	void setInteracts(GameSprite interactor, GameSprite interactee);

}
