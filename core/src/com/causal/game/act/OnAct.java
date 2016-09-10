package com.causal.game.act;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class OnAct implements IOnActing {

	private float animateStateTime = 0;

	private TextureRegion currentFrame = null;

	private HashMap<String, Array<AtlasRegion>> animationFrames = new HashMap<String, Array<AtlasRegion>>();
	private Animation animation;
	
	public OnAct(float duration, String spriteName) {
		animation = new Animation(duration, animationFrames.get(spriteName), PlayMode.LOOP);
	}

	@Override
	public void performActing(float delta) {
		animateStateTime += delta;
		currentFrame = animation.getKeyFrame(animateStateTime, true);
	}
	
	@Override
	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

}
