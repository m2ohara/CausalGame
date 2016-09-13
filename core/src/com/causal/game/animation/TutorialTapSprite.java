package com.causal.game.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.causal.game.act.IOnActing;
import com.causal.game.act.OnAct;

public class TutorialTapSprite extends Image {
	
	private IOnActing onActing = new OnAct(0.15f, "sprites/Meep/TapSprite/TapSprite.pack", "Tap");
	
	public TutorialTapSprite() {
		super(new TextureAtlas(Gdx.files.internal("sprites/Meep/TapSprite/TapSprite.pack")).getRegions().get(0));
	}
	
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
