package com.causal.game.interact;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.InfluenceType;
import com.causal.game.main.GameSprite.Status;

public class OpposerInteractionType implements IInteractionType {
	private GameSprite interactor;
	private GameSprite interactee;
	
	public OpposerInteractionType() {};
	
	@Override
	public void setInteracts(GameSprite interactor, GameSprite interactee) {
		this.interactor = interactor;
		this.interactee = interactee;
		
	}
	
	public OpposerInteractionType(GameSprite interactor, GameSprite interactee) {
		this.interactor = interactor;
		this.interactee = interactee;
	}
	
	//Swipe interaction
	public void setStatus() {
		interactor.interactStatus = Status.INFLUENCED;
		interactor.influenceType = InfluenceType.OPPOSE;
		Gdx.app.debug("OpposerInteraction", "Setting intermediate opposer");
	}
	
	//On autonomous interaction complete
	public void complete() {
		interactee.interactStatus = Status.INFLUENCED;
		interactee.influenceType = InfluenceType.OPPOSE;
		interactor.changeOrientationOnInvalid();
		interactor.isInteracting = false;
		interactee.isActive = true;
		setInfluencedSprite();
		Gdx.app.debug("OpposerInteraction", "Autonomous opposer interaction complete");
	}
	
	public void setInfluencedSprite() {
		
		Actor handSign = new Image(new TextureAtlas(Gdx.files.internal("sprites/Meep/Gestures/HandSigns.pack")).getRegions().get(1));

		handSign.setOrigin(handSign.getWidth()/2, handSign.getHeight()/2);
		handSign.setPosition(interactee.getStartingX(), interactee.getStartingY());
		
		GameProperties.get().addActorToStage(handSign);
	}

}
