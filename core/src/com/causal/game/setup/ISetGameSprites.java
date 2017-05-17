package com.causal.game.setup;

import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.sprite.GameSprite;

public interface ISetGameSprites {
	
	GameSprite createGameSprite(float probability, int x, int y);
	
	GameSprite getGameSprite(ISpriteBehaviour behaviour, float x, float y, String framesPath, boolean isActive);
	
	int getSupportCount();
	
	int getOpposeCount();
	
	void resetIndex();
	
	void setIndex(int index);

}
