package com.causal.game.setup;

import com.causal.game.main.GameSprite;

public interface ISetGameSprites {
	
	GameSprite createGameSprite(float probability, int x, int y);
	
	int getSupportCount();
	
	int getOpposeCount();

}
