package com.causal.game.gestures;

import com.causal.game.sprite.GameSprite;

public interface ISwipeInteraction {
	
	boolean interactHit(GameSprite hitActor, boolean isFirst);
	
	void reset();
	
	boolean isValidInteraction(GameSprite hitActor);

}
