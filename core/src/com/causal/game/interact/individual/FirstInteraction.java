package com.causal.game.interact.individual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.InfluenceType;
import com.causal.game.main.GameSprite.InteractorType;
import com.causal.game.main.GameSprite.Status;

public class FirstInteraction implements IIndividualInteraction {
	
	private GameSprite interactor;
	private boolean isStatusSet = false;
	
	public FirstInteraction(GameSprite interactor) {
		this.interactor = interactor;
	}
	
	public void setStatus() {
		//If first
		if(interactor.interactorType == InteractorType.First && !isStatusSet) {
			if(interactor.influenceType == InfluenceType.NONE){
				System.out.println("Setting first interactee with influenceType "+interactor.influenceType+" to influence: "+interactor.behaviour.getInfluenceType());
				interactor.influenceType = (interactor.behaviour.getInfluenceType());
				setFirstInfluencedSprite();
				isStatusSet = true;
			}
//			interactor.isInteracting = true;
			if(interactor.interactStatus == Status.SELECTED) {
				interactor.interactStatus = Status.INFLUENCED;
			}
		}
	}
	
	public void setInfluencedSprite() {

	}
	
	public void setFirstInfluencedSprite() {
			System.out.println("Setting first interactor handsign for status: "+interactor.interactStatus);
			Actor handSign = new Image(new TextureAtlas(Gdx.files.internal("sprites/Meep/Gestures/HandSigns.pack")).getRegions().get(interactor.influenceType.ordinal()));
	
			handSign.setOrigin(handSign.getWidth()/2, handSign.getHeight()/2);
			handSign.setPosition(interactor.getStartingX(), interactor.getStartingY());
			
			GameProperties.get().addActorToStage(handSign);
	}

}
