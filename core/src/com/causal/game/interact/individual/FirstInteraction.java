package com.causal.game.interact.individual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.causal.game.interact.InfluenceTypeSetter;
import com.causal.game.main.GameProperties;
import com.causal.game.sprite.GameSprite;
import com.causal.game.sprite.GameSprite.InfluenceType;
import com.causal.game.sprite.GameSprite.InteractorType;
import com.causal.game.sprite.GameSprite.Status;

public class FirstInteraction implements IIndividualInteraction {
	
	private GameSprite interactor;
	private boolean isStatusSet = false;
	
	public FirstInteraction(GameSprite interactor) {
		this.interactor = interactor;
	}
	
	public void setStatus() {
		//If first
		if(interactor.interactorType == InteractorType.FIRST && !isStatusSet) {
			if(interactor.influenceType == InfluenceType.NONE){
				Gdx.app.log("FirstInteraction", "Setting first interactee with influenceType "+interactor.influenceType+" to influence: "+interactor.behaviour.getInfluenceType());
				interactor.influenceType = (interactor.behaviour.getInfluenceType());
				setFirstInfluencedSprite();
				isStatusSet = true;
			}
			if(interactor.interactStatus == Status.SELECTED) {
				interactor.interactStatus = Status.INFLUENCED;
				Gdx.app.log("FirstInteraction", "Setting first interactor to  "+interactor.interactStatus);
			}
		}
	}
	
	public void setInfluencedSprite() {

	}
	
	public void setFirstInfluencedSprite() {
		InfluenceTypeSetter.setBlock(interactor, new Image(new TextureAtlas(Gdx.files.internal("sprites/Meep/Gestures/HandSigns.pack")).getRegions().get(interactor.influenceType.ordinal())));
	}

}
