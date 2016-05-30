package com.causal.game.act;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.causal.game.main.GameSprite.Status;


public interface IOnAct {
	
	public void performActing(float delta, Status actorStatus, boolean isInteracting);
	 
	public void changeSpriteOrientation();
	
	public AtlasRegion getCurrentFrame();

}
