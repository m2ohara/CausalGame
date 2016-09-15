package com.causal.game.tutorial;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.causal.game.animation.TutorialTapSprite;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite.InfluenceType;

public class TutorialAnimationProperties {
	
	public Vector2 activateSprite;
	public ArrayList<Integer> animationTypes;
	private boolean isActive = false;
	public boolean isSet = false;
	
	public TutorialAnimationProperties(Vector2 activateSprite, ArrayList<Integer> arrayList) {
		this.activateSprite = activateSprite;
		this.animationTypes = arrayList;
	}
	
	public void countdownAnimation() {
		if(!isSet) {
		Gdx.app.log("TutorialAnimationProperties", "GameSprite animations set" );
		isSet = true;
		GameProperties.get().getActorGroup().clear();
		}
	}
	
	public void activateAnimations(boolean notReady, float x, float y) {
		if(isSet && !isActive && !notReady) {
			Gdx.app.log("TutorialAnimationProperties", "GameSprite "+x+", "+y+ " animations active" );
			
			if (animationTypes.contains(0)) {
				TutorialTapSprite sprite = new TutorialTapSprite();
				sprite.setPosition(x, y);
				GameProperties.get().addToActorGroup(sprite);
			}
			
			isActive = true;
		}
	}
	
}
