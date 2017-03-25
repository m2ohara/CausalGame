package com.causal.game.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.causal.game.main.GameProperties;
import com.causal.game.state.PlayerState;

public class SwipeInteractFinishSprite extends Image {
	
	private static String framesPath = "sprites/PlanetRelease/InteractFinishSprite/InteractFinishSprite.pack";
	
	private float frameLength = 0.2f * GameProperties.get().getUniversalTimeRatio();
	private float frameTime = frameLength;
	private int frameCount = 0;
	private AtlasRegion currentFrame = null;
	private Array<AtlasRegion> frames;	
	private boolean isFinished = false;
	
	public SwipeInteractFinishSprite(Vector2 interacteeCoords) {
		super(new TextureAtlas(Gdx.files.internal(framesPath)).getRegions().get(0));
		
		frames = new TextureAtlas(Gdx.files.internal(framesPath)).getRegions();
		currentFrame = frames.get(0);
		
		set(interacteeCoords);
	}
	
	private void set(Vector2 interacteeCoords) {

		
		setMovement(interacteeCoords);
		
		GameProperties.get().addActorToStage(this);
	}
	
	private void setMovement(Vector2 interacteeCoords) {	

		this.setOrigin(currentFrame.getRegionWidth()/2, currentFrame.getRegionHeight()/2);
		this.setPosition(interacteeCoords.x - currentFrame.getRegionWidth()/4, interacteeCoords.y - currentFrame.getRegionHeight()/4);
		this.setTouchable(Touchable.disabled);
		
		
		Gdx.app.debug("SwipeInteractFinishSprite", "Set finish sprite at "+interacteeCoords.x+", "+interacteeCoords.y);

	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		//Interaction finished
		if(currentFrame == frames.get(frames.size-1)) {
			isFinished = true;
		}
		else {	
			updateSprite(delta);
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
			
			this.currentFrame = frames.get(frameCount++);
			
		}
		
		frameTime += delta;
	}
	
	public boolean isFinished() {
		return isFinished;
	}

}
