package com.causal.game.tutorial;

import com.badlogic.gdx.Gdx;
import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.main.GameSprite;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;

public class TutorialGameSprite extends GameSprite{
	
	private Orientation swipeOrientation;
	
	public TutorialGameSprite(ISpriteBehaviour behaviour, float x, float y, String framesPath, boolean isActive, Orientation swipeOrientation) {
		super(behaviour, x, y, framesPath, isActive);
		
		this.swipeOrientation = swipeOrientation;
	}
	
	public Orientation getSwipeOrientation() {
		Gdx.app.debug("TutorialGameSprite","Getting swipe orientation"+ swipeOrientation);
		return this.swipeOrientation;
	}

}
