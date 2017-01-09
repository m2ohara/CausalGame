package com.causal.game.interact.swipe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.causal.game.interact.IInteractionType;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.GameSprite.InteractorType;
import com.causal.game.state.PlayerState;

public class SwipeInteractScaleSprite extends Image{
	
	private static String framesPath = "sprites/Meep/Effects/Effects.pack";
	public boolean isInteracting = false;
	protected float interactionScaleFactor;
	protected float currentScaleFactor;
	protected float interactionStateLength;
	protected ScaleToAction scaleAction;
	protected float finalScale;
	
	private GameSprite interactor;
	private GameSprite interactee;
	private IInteractionType interactionType;

	public SwipeInteractScaleSprite(float interactionStateLength, int interactionStages, GameSprite interactor, GameSprite interactee, IInteractionType interactionType) {
		super(new TextureAtlas(Gdx.files.internal(framesPath)).getRegions().get(1));
		
		//Set interaction length based on level - faster for higher difficulty
		this.interactionStateLength = (float)(interactionStateLength - (PlayerState.get().getLevel()/2));
		if(this.interactionStateLength < 1) { this.interactionStateLength = 1; }
		
		//Scale sprite based on level - smaller for higher difficulty
		finalScale = WorldSystem.get().getLevelScaleFactor();		
		this.interactionScaleFactor = finalScale/(float)(interactionStages);
		this.currentScaleFactor = interactionScaleFactor;
		
		setSprite(interactor.getStartingX()+interactor.getWidth()/2, interactor.getStartingY()+interactor.getHeight()/2);	
		this.setDrawable(new TextureRegionDrawable(new TextureRegion(new TextureAtlas(Gdx.files.internal(framesPath)).getRegions().get(0))));
		
		this.interactor = interactor;
		this.interactee = interactee;
		
		this.interactionType = interactionType;
		this.interactionType.setInteracts(interactor, interactee);
	}
	
	private void setSprite(float xCoord, float yCoord) {

		this.setPosition(xCoord, yCoord);
		this.setTouchable(Touchable.disabled);
		this.scaleBy(-1);

		GameProperties.get().addActorToStage(this);
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
		
		scaleAction = Actions.scaleTo(currentScaleFactor, currentScaleFactor, interactionStateLength);
		this.addAction(scaleAction);
		this.isInteracting = true;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		//Start interaction
		if(interactor.isActive == true && scaleAction == null) {
			setAction();
		}
		
		//Interaction finished
		if(interactor.isActive == true && this.scaleAction != null && this.getActions().size == 0 && currentScaleFactor >= finalScale) {
			scaleAction.finish();
			this.remove();
			completeInteraction();
		}
		
		//Interaction action finished
		else if(interactor.isActive == true && this.scaleAction != null && this.getActions().size == 0 && currentScaleFactor < finalScale) {
			currentScaleFactor += interactionScaleFactor;
			scaleAction = Actions.scaleTo(currentScaleFactor, currentScaleFactor, interactionStateLength);
			this.addAction(scaleAction);
		}
	}
	
	private void startFirstInteraction() {
		interactionType.setStatus();
		interactor.setColor(Color.WHITE);
	}
	
	
	private void completeInteraction() {
		interactionType.setStatus();
		interactionType.setInfluencedSprite();
		setInteractor();
		setInteractee();
	}
	
	private void setInteractor() {
		interactor.behaviour.changeOrientation();
		interactor.interactorType = InteractorType.NONE;
		interactor.isInteracting = false;
	}
	
	private void setInteractee() {
		interactee.isActive = true;
	}

}
