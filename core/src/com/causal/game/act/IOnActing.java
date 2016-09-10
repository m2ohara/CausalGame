package com.causal.game.act;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public interface IOnActing {
	
	public void performActing(float delta);
	
	public TextureRegion getCurrentFrame();

}
