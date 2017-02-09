package com.causal.game.interact.swipe;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.causal.game.interact.IInteractionType;
import com.causal.game.interact.individual.IndividualInteraction;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.InteractorType;
import com.causal.game.state.PlayerState;

public class SwipeInteract {
	
	public GameSprite interactor;
	public GameSprite interactee;
	public boolean isInteracting;
	public IInteractionType interactionType = new IndividualInteraction();
	
	public SwipeInteract(GameSprite interactor, GameSprite interactee) {
		this.interactee = interactee;
		this.interactor = interactor;
		
		this.interactionType.setInteracts(interactor, interactee);

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
	
	private void startFirstInteraction() {
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
