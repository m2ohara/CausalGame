package com.causal.game.interact.swipe;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.causal.game.interact.IInteractionType;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.GameSprite.InteractorType;
import com.causal.game.state.PlayerState;

public class SwipeInteractFrameSprite extends Image {

	private static String framesPath = "sprites/Meep/TapSprite/TapSprite.pack";
	private float interactionStateLength = 400f;
	
	private float frameLength = 0.2f * GameProperties.get().getUniversalTimeRatio();
	private float frameTime = frameLength;
	private int frameCount = 0;
	private AtlasRegion currentFrame = null;
	
	private Array<AtlasRegion> frames;	
	
	private SwipeInteract swipeInteract;
	
	public SwipeInteractFrameSprite(GameSprite interactor, GameSprite interactee) {
		super(new TextureAtlas(Gdx.files.internal(framesPath)).getRegions().get(0));
		
		set(interactor.getX(), interactor.getY());
		
		frames = new TextureAtlas(Gdx.files.internal(framesPath)).getRegions();
		
		currentFrame = frames.get(0);
		
		swipeInteract = new SwipeInteract(interactor, interactee);
	}
	
	private void set(float xCoord, float yCoord) {

		setInteractSpeed();
		
		this.setPosition(xCoord, yCoord);
		this.setTouchable(Touchable.disabled);
		this.setVisible(false);
		GameProperties.get().addActorToStage(this);
	}
	
	private void setInteractSpeed() {
		//Set interaction length based on level - faster for higher difficulty
		this.interactionStateLength = (float)(interactionStateLength - (PlayerState.get().getLevel()/2));
		if(this.interactionStateLength < 1) { this.interactionStateLength = 1; }
	}
	
	public void startInteraction() {
		swipeInteract.startInteraction();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(swipeInteract.interactor.isActive == true) {
			//Start interaction
			if(!swipeInteract.isInteracting) {
				this.setVisible(true);
				swipeInteract.setAction();
			}
			
			if(swipeInteract.isInteracting ) {
			
			//Interaction finished
			if(interactionStateLength < 0) {
				this.remove();
				swipeInteract.completeInteraction();
			}
		
			updateSprite(delta);
			
			interactionStateLength--;
			
			}
		
		}
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
			this.setDrawable(new TextureRegionDrawable(new TextureRegion(currentFrame)));
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
}
