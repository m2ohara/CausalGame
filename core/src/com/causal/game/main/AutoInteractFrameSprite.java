package com.causal.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.causal.game.interact.IInteractionType;
import com.causal.game.interact.individual.IndividualInteraction;
import com.causal.game.state.PlayerState;

public class AutoInteractFrameSprite  extends Image {
	
	private static String framesPath = "sprites/PlanetRelease/Deceiver/SpaceShipRedMove.pack";
	public boolean isInteracting = false;
	protected float interactionStateLength = 400f;
	protected ScaleToAction scaleAction;
	private IInteractionType interactionType;
	
	protected MoveToAction moveAction;
	private float moveDuration = 5f;
	
	private float frameLength = 0.2f * GameProperties.get().getUniversalTimeRatio();
	private float frameTime = frameLength;
	private int frameCount = 0;
	private AtlasRegion currentFrame = null;	
	private Array<AtlasRegion> frames;	
	
	private GameSprite interactor;
	
	public AutoInteractFrameSprite(GameSprite interactor, GameSprite interactee, IInteractionType interactionType) {
		super(new TextureAtlas(Gdx.files.internal(framesPath)).getRegions().get(1));
		
		this.interactionType = interactionType;
		frames = new TextureAtlas(Gdx.files.internal(framesPath)).getRegions();
		currentFrame = frames.get(0);
		
		this.interactor = interactor;
		
		set(interactor, interactee);		
		this.setDrawable(new TextureRegionDrawable(new TextureRegion(new TextureAtlas(Gdx.files.internal(framesPath)).getRegions().get(2))));
	}
	
	private void set(GameSprite interactor, GameSprite interactee) {

		setInteractSpeed();
		
		int degrees = 0;

		switch (interactor.getOrientation().ordinal()) {
			case 2 :  { degrees = 270; break; }
			case 4 : { degrees = 180; break; }
			case 6 : { degrees = 90; }
		}
			
		moveAction = Actions.moveTo(interactee.getX(), interactee.getY(), moveDuration);
		
		Gdx.app.debug("AutoInteractFramesSprite", "Set orientation to degrees "+degrees+ " movement to "+interactee.getX()+", "+interactee.getY()+ " for "+moveDuration+" duration");
		
		this.rotateBy(degrees);
		this.setOrigin(currentFrame.getRegionWidth()/2, currentFrame.getRegionHeight()/2);
		this.setPosition(interactor.getX()+35, interactor.getY()+35);
		this.setTouchable(Touchable.disabled);
		this.setVisible(false);
		GameProperties.get().addActorToStage(this);
	}
	
	private void setInteractSpeed() {
		//Set interaction length based on level - faster for higher difficulty
		this.interactionStateLength = (float)(interactionStateLength - (PlayerState.get().getLevel()/2));
		if(this.interactionStateLength < 1) { this.interactionStateLength = 1; }
	}
	
	public void setAction() {
		this.isInteracting = true;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		this.setDrawable(new TextureRegionDrawable(new TextureRegion(currentFrame)));
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	
		if(interactor.isActive == true) {
			//Start interaction
			if(!isInteracting) {
				this.setVisible(true);
				setAction();
			}
			
			if(isInteracting ) {
				
				if(!this.hasActions()) {
					Gdx.app.log("AutoInteractFramesSprite", "Added action to "+this.hashCode()+"");
					this.addAction(moveAction);
				}
			
				//Interaction finished
				if(interactionStateLength < 0) {
					moveAction.finish();
					this.remove();
					isComplete = true;
					interactionType.complete();
				}
			
				updateSprite(delta);
				
				interactionStateLength--;
			
			}
		
		}
	}
	
	private void updateSprite(float delta) {
		
		if(frameTime >= frameLength) {
			frameTime = 0.0f;
			
			if(frameCount > frames.size -1) {
				frameCount = 0;
			}
			
			this.currentFrame = frames.get(frameCount++);
			
		}
		
		frameTime += delta;
	}
	
	protected boolean isComplete = false;
	public boolean isComplete() {
		return isComplete;
	}
}
