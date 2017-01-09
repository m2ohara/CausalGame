package com.causal.game.interact.swipe;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.causal.game.interact.IInteractionType;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.InteractorType;
import com.causal.game.state.PlayerState;

public class SwipeInteract {
	
	public GameSprite interactor;
	public GameSprite interactee;
	public IInteractionType interactionType;
	public boolean isInteracting;
	public float interactionStateLength;
	
	public void setInteractionSpeed(Image image) {
		
		//Set interaction length based on level - faster for higher difficulty
		this.interactionStateLength = (float)(interactionStateLength - (PlayerState.get().getLevel()/2));
		if(this.interactionStateLength < 1) { this.interactionStateLength = 1; }
	}
	
	public void startInteraction() {
		interactor.isActive = true;
		GameProperties.get().isAutoInteractionAllowed = true;
	}
	
	public void setAction() {
		//If first interaction
		if(interactor.interactorType == InteractorType.FIRST) {
			startFirstInteraction();
		}
		
		this.isInteracting = true;
	}
	
	public void startFirstInteraction() {
		interactionType.setStatus();
		interactor.setColor(Color.WHITE);
	}
	
	public void completeInteraction() {
		interactionType.setStatus();
		interactionType.setInfluencedSprite();
		setInteractor();
		setInteractee();
	}
	
	public void setInteractor() {
		interactor.behaviour.changeOrientation();
		interactor.interactorType = InteractorType.NONE;
		interactor.isInteracting = false;
	}
	
	public void setInteractee() {
		interactee.isActive = true;
	}

}
