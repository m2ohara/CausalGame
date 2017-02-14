package com.causal.game.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.causal.game.interact.IInteractionType;
import com.causal.game.main.GameProperties;
import com.causal.game.state.PlayerState;

public class AutoInteractFrameSprite  extends Image {
	
	private static String framesPath = "sprites/PlanetRelease/Deceiver/SpaceShipRedMove.pack";
	public boolean isInteracting = false;
	protected float interactionStateLength;
	protected ScaleToAction scaleAction;
	private IInteractionType interactionType;
	
	private float xDistance = 0f;
	private float yDistance = 0f;
	
	private float frameLength = 0.2f * GameProperties.get().getUniversalTimeRatio();
	private float frameTime = frameLength;
	private int frameCount = 0;
	private AtlasRegion currentFrame = null;	
	private Array<AtlasRegion> frames;	
	
	private boolean actionAdded = false;
	
	private GameSprite interactor;
	
	public AutoInteractFrameSprite(GameSprite interactor, GameSprite interactee, IInteractionType interactionType) {
		super(new TextureAtlas(Gdx.files.internal(framesPath)).getRegions().get(1));
		
		this.interactionType = interactionType;
		frames = new TextureAtlas(Gdx.files.internal(framesPath)).getRegions();
		currentFrame = frames.get(0);
		this.interactionStateLength = interactor.getInteractLength();
		
		this.interactor = interactor;
		
		set(interactor, interactee);		
		this.setDrawable(new TextureRegionDrawable(new TextureRegion(new TextureAtlas(Gdx.files.internal(framesPath)).getRegions().get(2))));
	}
	
	private void set(GameSprite interactor, GameSprite interactee) {

		setInteractSpeed();
		
		setMovement(interactor, interactee);
		
		GameProperties.get().addActorToStage(this);

	}
	
	private void setInteractSpeed() {
		//Set interaction length based on level - faster for higher difficulty
		this.interactionStateLength = (float)(interactionStateLength - (PlayerState.get().getLevel()/2));
		if(this.interactionStateLength < 1) { this.interactionStateLength = 1; }
	}
	
	private void setMovement(GameSprite interactor, GameSprite interactee) {
		
		int degrees = 0;
		float x = 0;
		float y = -(interactor.getY() - interactee.getY());		

		switch (interactor.getOrientation().ordinal()) {
			case 2 :  { degrees = 270; x = interactee.getX() - interactor.getX(); y = 0; break;  }
			case 4 : { degrees = 180; y = (interactee.getY() - interactor.getY()); break; }
			case 6 : { degrees = 90; x = -(interactor.getX() - interactee.getX()); y = 0; }
		}
			
		Gdx.app.debug("AutoInteractFramesSprite", "Set movement from "+interactor.getX()+", "+interactor.getY()+"");

		
		this.rotateBy(degrees);
		this.setOrigin(currentFrame.getRegionWidth()/2, currentFrame.getRegionHeight()/2);
		this.setPosition(interactor.getX(), interactor.getY());
		this.setTouchable(Touchable.disabled);
		
		xDistance = x / interactionStateLength;
		yDistance = y / interactionStateLength;
		
		Gdx.app.debug("AutoInteractFramesSprite", "Set orientation to degrees "+degrees+ " movement to "+interactee.getX()+", "+interactee.getY());

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
				setAction();
			}
			
			if(isInteracting ) {
				
				if(!actionAdded) {
					Gdx.app.debug("AutoInteractFramesSprite", "Added action to "+this.hashCode()+"");
					actionAdded = true;
				}
			
				//Interaction finished
				if(interactionStateLength < 0) {
					Gdx.app.debug("AutoInteractFramesSprite", "Interaction complete "+this.hashCode()+"");
					this.remove();
					isComplete = true;
					interactionType.complete();
				}
				
				this.moveBy(xDistance, yDistance);
			
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
