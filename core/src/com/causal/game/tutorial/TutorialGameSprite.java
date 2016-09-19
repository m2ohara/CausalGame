package com.causal.game.tutorial;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;

public class TutorialGameSprite extends GameSprite{
	
	private Orientation autoInteractOrientation;
	private Orientation swipeOrientation;
	private List<Vector2> autoInteractOnSelectedSprite;
	private TutorialAnimationProperties animations;
	public ChangeEvent event;
	
	
	public TutorialGameSprite(ISpriteBehaviour behaviour, float x, float y, String framesPath, boolean isActive, Orientation swipeOrientation, List<Vector2> autoInteractOnSelectedSprite, Orientation autoInteractOrientation, TutorialAnimationProperties animations) {
		super(behaviour, x, y, framesPath, isActive);
		
		this.swipeOrientation = swipeOrientation;
		this.autoInteractOnSelectedSprite = autoInteractOnSelectedSprite;
		this.autoInteractOrientation = autoInteractOrientation;
		this.animations = animations;
		
		this.event = new ChangeEvent();

	}
	
	public Orientation getSwipeOrientation() {
		Gdx.app.debug("TutorialGameSprite","Getting swipe orientation"+ swipeOrientation);
		return this.swipeOrientation;
	}
	
	public boolean isValidAutoInteractSelectedSprite(Orientation orientation) {
		for(Vector2 coords : autoInteractOnSelectedSprite) {
			if(WorldSystem.get().getMemberFromCoords((int)coords.x, (int)coords.y).interactorType == InteractorType.FIRST && orientation == autoInteractOrientation) {
				Gdx.app.debug("TutorialGameSprite", "GameSprite "+this.getXGameCoord()+", "+this.getYGameCoord()+ " able to interact" );
				return true;
			}
		}
		
		checkAnimations();
		
		return false;
	}
	
	@Override
	public void act(float delta) {
		checkAnimations();
		super.act(delta);
	}
	
	public void checkAnimations() {		
		if(animations.activateSpriteCoords != null && WorldSystem.get().getMemberFromCoords((int)animations.activateSpriteCoords.x, (int)animations.activateSpriteCoords.y).interactorType == InteractorType.FIRST) {
			animations.prepareAnimation();
		}
		else if(animations.isSet) {
			animations.activateAnimations(GameProperties.get().isAutoInteractionAllowed, this);
		}
	}

}
