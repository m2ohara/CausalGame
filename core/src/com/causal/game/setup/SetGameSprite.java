package com.causal.game.setup;

import com.badlogic.gdx.Gdx;
import com.causal.game.behaviour.DeceiverBehaviour;
import com.causal.game.behaviour.GossiperBehaviour;
import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.behaviour.PromoterBehaviour;
import com.causal.game.main.WorldSystem;
import com.causal.game.sprite.GameSprite;
import com.causal.game.state.PlayerState;

public class SetGameSprite implements ISetGameSprites {
	
	private int supportCount = 0;
	private int opposeCount = 0;

	public GameSprite createGameSprite(float probability, int x, int y) {
		GameSprite current;
		if(probability < 0.33) {
			current = getGameSprite(new GossiperBehaviour(), WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), PlayerState.get().getFollowerTypes().get(0).imagePath, true);
			incrementVoteType(2);
		}
		else if(probability >= 0.33 && probability < 0.66) {
			current = getGameSprite(new PromoterBehaviour(), WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), PlayerState.get().getFollowerTypes().get(1).imagePath, true);
			incrementVoteType(0);
		}
		else {
			current = getGameSprite(new DeceiverBehaviour(), WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), PlayerState.get().getFollowerTypes().get(2).imagePath, true);
			incrementVoteType(1);
		}
		return current;
	}
	
	public int getSupportCount() {
		return supportCount;
	}
	
	public int getOpposeCount() {
		return opposeCount;
	}
	
	public GameSprite getGameSprite(ISpriteBehaviour type, float x, float y, String framesPath, boolean isActive) {
		Gdx.app.debug("SetGameSprite", "Getting sprite "+x+", "+y);
		return new GameSprite(type, x, y, framesPath, isActive);
	}
	
	protected void incrementVoteType(int type) {
		if(type == 0 || type == 2) {
			supportCount += 1;
		}
		if(type == 1 || type == 2) {
			opposeCount += 1;
		}
	}

}
