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

public class IntermediateInteraction implements IIndividualInteraction {
	
	private GameSprite interactor;
	private InfluenceType influenceType;
	
	public IntermediateInteraction(GameSprite interactor, InfluenceType influenceType) {
		this.interactor = interactor;
		this.influenceType = influenceType;
	}
	
	public void setStatus() {
		if(interactor.interactorType == InteractorType.INTERMEDIATE){
			//Set interactee to interactor's influence type
			Gdx.app.log("IntermediateInteraction","Setting intermediate interactee influence: "+interactor.influenceType);
		}
	}
	
	public void setInfluencedSprite(){
		interactor.influenceType = influenceType;
		InfluenceTypeSetter.setHandSign(interactor, new Image(new TextureAtlas(Gdx.files.internal("sprites/Meep/Gestures/HandSigns.pack")).getRegions().get(influenceType.ordinal())));
	}
	

}
