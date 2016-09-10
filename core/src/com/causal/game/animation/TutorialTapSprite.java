package com.causal.game.animation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.causal.game.act.IOnActing;
import com.causal.game.act.OnAct;

public class TutorialTapSprite extends Image {
	
	private IOnActing onActing = new OnAct(03f, "TapSprite");
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
			this.setDrawable(new TextureRegionDrawable(new TextureRegion(onActing.getCurrentFrame())));
	}

	@Override
	public void act(float delta) {
		super.act(delta);
			onActing.performActing(delta);
	}

}
