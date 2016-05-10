package com.causal.game.interact.individual;

import com.badlogic.gdx.Gdx;
import com.causal.game.interact.IInteractionType;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.InfluenceType;
import com.causal.game.main.GameSprite.InteractorType;
import com.causal.game.main.GameSprite.Status;

public class IndividualInteraction implements IInteractionType {
	private GameSprite interactor;
	private GameSprite interactee;
	private IIndividualInteraction interactorInteraction;
	private IIndividualInteraction interacteeInteraction;
	
	public IndividualInteraction() {}
	
	@Override
	public void setInteracts(GameSprite interactor, GameSprite interactee) {
		this.interactor = interactor;
		this.interactee = interactee;
	}
	
	private void setInteraction() {
		if(interactor.interactorType == InteractorType.First && interactor.influenceType == InfluenceType.NONE) {
			Gdx.app.debug("IndividualInteraction", "Setting first interaction for interactor "+interactor.hashCode());
			interactorInteraction = new FirstInteraction(interactor);
		}
		
		if(interactor.interactorType == InteractorType.Intermediate && interactor.influenceType == InfluenceType.NONE) {
			Gdx.app.debug("IndividualInteraction", "Setting intermediate interaction for interactor "+interactor.hashCode());
			interactorInteraction = new IntermediateInteraction(interactor, interactor.behaviour.getInfluenceType());
		}
		
		if(interactee.interactorType == InteractorType.Intermediate && interactee.influenceType == InfluenceType.NONE) {
			Gdx.app.debug("IndividualInteraction", "Setting intermediate interaction for interactee "+interactee .hashCode());
			interacteeInteraction = new IntermediateInteraction(interactee, interactor.behaviour.getInfluenceType());
		}
		
		if(interactee.interactorType == InteractorType.Last && interactee.influenceType == InfluenceType.NONE) {
			Gdx.app.debug("IndividualInteraction", "Setting last interaction for interactee "+interactee.hashCode());
			interacteeInteraction = new LastInteraction(interactee, interactor.behaviour.getInfluenceType());
		}
	}


	@Override
	public void setStatus() {
		if(interactorInteraction == null) {
			setInteraction();
		}
		
		if(interactorInteraction != null) {
			interactorInteraction.setStatus();
		}
		
		if(interacteeInteraction != null) {
			interacteeInteraction.setStatus();
		}
		
	}


	@Override
	public void setInfluencedSprite() {
		if(interactorInteraction == null) {
			setInteraction();
		}
		
		if(interactorInteraction != null) {
			interactorInteraction.setInfluencedSprite();
		}
		
		if(interacteeInteraction != null) {
			interacteeInteraction.setInfluencedSprite();
		}
		
	}


	@Override
	public void complete() {
		interactee.influenceType = interactor.behaviour.getInfluenceType();
		interactor.changeOrientationOnInvalid();
		interactor.isInteracting = false;
		interactee.isActive = true;
		setInfluencedSprite();
	}

}
