package com.causal.game.behaviour;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.causal.game.act.IOnAct;
import com.causal.game.act.OnAnimateSpaceShip;
import com.causal.game.interact.AutonomousInteraction;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite.InfluenceType;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.touch.ISpriteOrientation;
import com.causal.game.touch.TouchAction;

public class Behaviour {

	//Members
	private boolean isActive = true;
	private TouchAction onTouch;
	public IOnAct actType;
	private ISpriteOrientation spriteOrientation;
	private IBehaviourProperties properties;
	
	public Behaviour(boolean isActive, AutonomousInteraction interaction, TouchAction touchAction, IBehaviourProperties properties, ISpriteOrientation spriteOrientation) {
		
		this.isActive = isActive;
		this.actType = new OnAnimateSpaceShip(properties.getRotateProbability(), properties.getInteractProbability(), interaction, spriteOrientation, properties.getFramesPath());
		this.onTouch = touchAction;
		
		Gdx.app.debug("Behaviour", "Created animation for "+interaction.interactor.getXGameCoord()+", "+interaction.interactor.getYGameCoord());		
		this.spriteOrientation = spriteOrientation;
		this.properties = properties;
	}
	

	public void onTouch() {
		
//		if(isActive) {
////			onTouch.onAction();
//		}
		if(GameProperties.get().isTapAllowed(this.hashCode()) && spriteOrientation.cyclicChange()) {
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
		return spriteOrientation.getOrientation();
	}
	
	public boolean changeOrientationOnInvalid() {
		if(spriteOrientation.cyclicChangeOnInvalidInteractee()) {
			actType.changeSpriteOrientation();	
			return true;
		}
		return false;
	}
	
	public void changeOrientation() {
		spriteOrientation.onCyclicChange();
		actType.changeSpriteOrientation();	
	}
	
	public InfluenceType getInfluenceType() {
		return properties.getInfluenceType();
	}
	
	public AtlasRegion getCurrentFrame() {
		return actType.getCurrentFrame();
	}
}
