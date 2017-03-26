package com.causal.game.interact.individual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.causal.game.interact.InfluenceTypeSetter;
import com.causal.game.main.GameProperties;
import com.causal.game.sprite.GameSprite;
import com.causal.game.sprite.SwipeSprite;
import com.causal.game.sprite.GameSprite.InfluenceType;
import com.causal.game.sprite.GameSprite.InteractorType;
import com.causal.game.sprite.GameSprite.Status;
import com.causal.game.state.GameScoreState;

public class LastInteraction implements IIndividualInteraction {
	
	private GameSprite interactor;
	private InfluenceType influenceType;
	
	public LastInteraction(GameSprite interactor, InfluenceType influenceType) {
		this.interactor = interactor;
		this.influenceType = influenceType;
	}
	
	public void setStatus() {
		if(interactor.interactorType == InteractorType.LAST && interactor.influenceType == InfluenceType.NONE){
			interactor.interactStatus = Status.SELECTED;
			interactor.changeOrientationOnInvalid();
			setSelectedInteractee();
			GameProperties.get().resetTapCount();
			Gdx.app.debug("LastInteraction", "Setting last interactee influence: "+interactor.influenceType);
		}
	}
	
	public void setInfluencedSprite() {
		
		interactor.influenceType = influenceType;

		InfluenceTypeSetter.setHandSign(interactor, new Image(new TextureAtlas(Gdx.files.internal("sprites/Meep/Gestures/HandSigns.pack")).getRegions().get(influenceType.ordinal())));
		
		SwipeSprite.get().activate();
	}
	
	private void setSelectedInteractee() {
		if(GameScoreState.validTouchAction()) {
//			interactor.setColor(Color.ORANGE);
		}
		else {
//			interactor.setColor(Color.YELLOW);
		}
		
		//Can last interactee interact on next swipe
		if(interactor.changeOrientationOnInvalid()) {
			GameProperties.get().isAutoInteractionAllowed = false;
		}		
		interactor.isActive = true;
	}

}
