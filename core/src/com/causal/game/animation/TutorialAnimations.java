package com.causal.game.animation;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.causal.game.main.Assets;

public class TutorialAnimations {
	
	private HashMap<String, Array<AtlasRegion>> animationFrames = new HashMap<String, Array<AtlasRegion>>();
	private Array<AtlasRegion> frames;
	private Animation TapSprite;
	
	public TutorialAnimations() {
		TapSprite = new Animation(0.3f, animationFrames.get("TapSprite"), PlayMode.LOOP);
	}

	private void setFramePacks() {
		Array<AtlasRegion> tapSpriteFrames = Assets.get().getAssetManager().get("sprites/Meep/TapSprite/TapSprite.pack", TextureAtlas.class).getRegions();
		
		animationFrames.put("TapSprite", tapSpriteFrames);
	}
	
	public Animation getTapAnimation() {
		return TapSprite;
	}
	
	

}
