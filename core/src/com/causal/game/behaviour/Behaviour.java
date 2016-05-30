package com.causal.game.behaviour;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.causal.game.act.IOnAct;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite.InfluenceType;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.touch.SpriteOrientation;
import com.causal.game.touch.TouchAction;

public class Behaviour {

	//Members
	private boolean isActive = true;
	private TouchAction onTouch;
	public IOnAct actType;
	private SpriteOrientation changeOrientation;
	private IBehaviourProperties properties;
	
	public Behaviour(boolean isActive, IOnAct onAct, TouchAction touchAction, IBehaviourProperties properties, SpriteOrientation changeOrientation) {
		
		this.isActive = isActive;
		this.actType = onAct;
//		this.actType = new OnAnimateTalkingAct(properties.getRotateProbability(), properties.getInteractProbability(), this, changeOrientation);
		this.onTouch = touchAction;
		
		this.changeOrientation = changeOrientation;
		this.properties = properties;
	}
	

	public void onTouch() {
		
//		if(isActive) {
////			onTouch.onAction();
//		}
		if(GameProperties.get().isTapAllowed(this.hashCode()) && changeOrientation.cyclicChange()) {
			actType.changeSpriteOrientation();
			GameProperties.get().updateTapCount(this.hashCode());
		}
		
	}

	public void onAct(float delta, Status actorStatus, boolean isInteracting) {
		
		if(isActive) {
			actType.performActing(delta, actorStatus, isInteracting);		
		}
		
	}
	
	//Orientation logic
	public Orientation getOrientation() {
		return changeOrientation.getOrientation();
	}
	
	public boolean changeOrientationOnInvalid() {
		if(changeOrientation.cyclicChangeOnInvalidInteractee()) {
			actType.changeSpriteOrientation();	
			return true;
		}
		return false;
	}
	
	public void changeOrientation() {
		changeOrientation.onCyclicChange();
		actType.changeSpriteOrientation();	
	}
	
	public InfluenceType getInfluenceType() {
		return properties.getInfluenceType();
	}
	
	public AtlasRegion getCurrentFrame() {
		return actType.getCurrentFrame();
	}
}
