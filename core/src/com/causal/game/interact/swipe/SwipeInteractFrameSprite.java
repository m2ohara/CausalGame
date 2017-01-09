package com.causal.game.interact.swipe;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.causal.game.interact.IInteractionType;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.GameSprite.InteractorType;
import com.causal.game.state.PlayerState;

public class SwipeInteractFrameSprite extends Image {

	private static String framesPath = "";
	
	protected float interactionStateLength;
	protected float finalScale;
	
	protected float interactionScaleFactor;
	protected float currentScaleFactor;
	
	private float frameLength = 0.2f * GameProperties.get().getUniversalTimeRatio();
	private float frameTime = frameLength;
	private int frameCount = 0;
	private AtlasRegion currentFrame = null;
	
	private HashMap<String, Array<AtlasRegion>> animationFrames = new HashMap<String, Array<AtlasRegion>>();
	private Array<AtlasRegion> frames;
	
	
	private SwipeInteract swipeInteract;
	
	public SwipeInteractFrameSprite(float interactionStateLength, int interactionStages, GameSprite interactor, GameSprite interactee, IInteractionType interactionType) {
		super(new TextureAtlas(Gdx.files.internal(framesPath)).getRegions().get(0));
		
		set(interactor.getX(), interactor.getY());
	}
	
	private void set(float xCoord, float yCoord) {

		setInteractSpeed();
		
		this.setPosition(xCoord, yCoord);
		this.setTouchable(Touchable.disabled);
		this.scaleBy(-1);

		GameProperties.get().addActorToStage(this);
	}
	
	private void setInteractSpeed() {
		//Set interaction length based on level - faster for higher difficulty
		this.interactionStateLength = (float)(interactionStateLength - (PlayerState.get().getLevel()/2));
		if(this.interactionStateLength < 1) { this.interactionStateLength = 1; }
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		//Start interaction
		if(swipeInteract.interactor.isActive == true ) {
			swipeInteract.setAction();
		}
		
		//Interaction finished
		if(swipeInteract.interactor.isActive == true) {
			this.remove();
			swipeInteract.completeInteraction();
		}
		
		//Interaction action finished
		else if(swipeInteract.interactor.isActive == true) {
		}
	}
	
	private void updateSprite(float delta) {
		
		if(frameTime >= frameLength) {
			frameTime = 0.0f;
			
			if(frameCount > frames.size -1) {
				frameCount = 0;
			}
			
			currentFrame = frames.get(frameCount++);
			
		}
		
		frameTime += delta;
	}
}
