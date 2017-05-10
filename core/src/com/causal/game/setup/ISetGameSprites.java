package com.causal.game.setup;

import com.causal.game.sprite.GameSprite;

public interface ISetGameSprites {
	
	GameSprite createGameSprite(float probability, int x, int y);
	
	int getSupportCount();
	
	int getOpposeCount();
	
	void resetIndex();

}
