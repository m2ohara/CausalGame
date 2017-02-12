package com.causal.game.interact;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.InfluenceType;
import com.causal.game.main.GameSprite.Status;

public class SupporterInteractionType implements IInteractionType {
	
	private GameSprite interactor;
	private GameSprite interactee;
	
	public SupporterInteractionType() {}
	
	@Override
	public void setInteracts(GameSprite interactor, GameSprite interactee) {
		this.interactor = interactor;
		this.interactee = interactee;
		
	}
	
	public SupporterInteractionType(GameSprite interactor, GameSprite interactee) {
		this.interactor = interactor;
		this.interactee = interactee;
	}
	
	//Swipe interaction
	public void setStatus() {
		interactor.interactStatus = Status.INFLUENCED;
		interactor.influenceType = InfluenceType.SUPPORT;
		Gdx.app.debug("SupporterInteraction", "Setting supporter status to influenced");
	}
	
	//On autonomous interaction complete
	public void complete() {
		interactee.interactStatus = Status.INFLUENCED;
		interactee.influenceType = InfluenceType.SUPPORT;
		interactor.changeOrientationOnInvalid();
		interactor.isInteracting = false;
		interactee.isActive = true;
		setInfluencedSprite();
		Gdx.app.debug("SupporterInteraction", "Autonomous supporter interaction complete");
	}
	
	public void setInfluencedSprite() {
		InfluenceTypeSetter.setHandSign(interactee, new Image(new TextureAtlas(Gdx.files.internal("sprites/Meep/Gestures/HandSigns.pack")).getRegions().get(0)));
	}
//	
//	private void setBlockSprite() {
//		
//		Actor influenceBlock = new Image(new TextureAtlas(Gdx.files.internal("sprites/Meep/Gestures/HandSigns.pack")).getRegions().get(0));
//
//		influenceBlock.setOrigin(influenceBlock.getWidth()/2, influenceBlock.getHeight()/2);
//		influenceBlock.setPosition(interactee.getStartingX()-2, interactee.getStartingY()+2, 0);
//		
//		GameProperties.get().addActorToStage(influenceBlock);
//		
//		influenceBlock.setZIndex(GameProperties.get().getStageActor("GameScreen").getZIndex()+1);
//	}
//	
//	private void setHandSignSprite() {
		
//		Actor handSign = new Image(new TextureAtlas(Gdx.files.internal("sprites/Meep/Gestures/HandSigns.pack")).getRegions().get(0));
//
//		handSign.setOrigin(handSign.getWidth()/2, handSign.getHeight()/2);
//		handSign.setPosition(interactee.getStartingX(), interactee.getStartingY(), 0);
//		
//		GameProperties.get().addActorToStage(handSign);
//	}

}
