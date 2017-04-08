package com.causal.game.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.causal.game.interact.individual.SwipeInteract;
import com.causal.game.main.GameProperties;
import com.causal.game.state.PlayerState;

public class SwipeInteractFrameSprite extends Image {

	private String framesPath;
	private float interactionStateLength;
	
	private float xDistance = 0f;
	private float yDistance = 0f;
	
	private float frameLength = 0.2f * GameProperties.get().getUniversalTimeRatio();
	private float frameTime = frameLength;
	private int frameCount = 0;
	private AtlasRegion currentFrame = null;
	private Vector2 interacteeCoords;
	
	private Array<AtlasRegion> frames;	
	
	private SwipeInteract swipeInteract;
	private SwipeInteractFinishSprite finishSprite;
	
	public SwipeInteractFrameSprite(GameSprite interactor, GameSprite interactee, TextureAtlas texture, String framesPath) {
		super(texture.getRegions().get(0));
		
		this.framesPath = framesPath;
		frames = texture.getRegions();
		currentFrame = frames.get(0);
		this.interactionStateLength = interactor.getInteractLength();
		
		interacteeCoords = new Vector2(interactee.getX(),interactee.getY());
		
		set(interactor, interactee);
		
		swipeInteract = new SwipeInteract(interactor, interactee);
	}
	
	private void set(GameSprite interactor, GameSprite interactee) {

		setInteractSpeed();
		
		setMovement(interactor, interactee);
		
		this.setVisible(false);
		
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
			
		Gdx.app.debug("SwipeInteractFramesSprite", "Set movement from "+interactor.getX()+", "+interactor.getY()+"");

		
		this.rotateBy(degrees);
		this.setOrigin(currentFrame.getRegionWidth()/2, currentFrame.getRegionHeight()/2);
		this.setPosition(interactor.getX(), interactor.getY());
		this.setTouchable(Touchable.disabled);
		
		xDistance = x / interactionStateLength;
		yDistance = y / interactionStateLength;
		
		Gdx.app.debug("SwipeInteractFramesSprite", "Set orientation to degrees "+degrees+ " movement to "+interactee.getX()+", "+interactee.getY());

	}
	
	public void startInteraction() {
		swipeInteract.startInteraction();
		
		Gdx.app.debug("SwipeInteractFramesSprite", "Started interaction");
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
				
				if(interactionStateLength == 0 && finishSprite == null) {
					this.setVisible(false);
					finishSprite = new SwipeInteractFinishSprite(interacteeCoords, new TextureAtlas(Gdx.files.internal(framesPath+"InteractFinishSprite.pack")));
				}
				else {
					updateSprite(delta);
					moveBy(xDistance, yDistance);
					interactionStateLength--;
				}
				
				//Interaction finished
				if(interactionStateLength < 1 && finishSprite != null && finishSprite.isFinished()) {
					swipeInteract.completeInteraction();
					finishSprite.remove();
					this.remove();
				}
			
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
