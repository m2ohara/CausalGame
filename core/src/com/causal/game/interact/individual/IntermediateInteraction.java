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
		Actor handSign = new Image(new TextureAtlas(Gdx.files.internal("sprites/Meep/Gestures/HandSigns.pack")).getRegions().get(influenceType.ordinal()));

		handSign.setOrigin(handSign.getWidth()/2, handSign.getHeight()/2);
		handSign.setPosition(interactor.getStartingX(), interactor.getStartingY());
		
		GameProperties.get().addActorToStage(handSign);
	}
	

}
