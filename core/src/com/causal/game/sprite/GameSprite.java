package com.causal.game.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.causal.game.behaviour.Behaviour;
import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.interact.IInteractionType;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.Game.Head;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.state.GameScoreState;
import com.causal.game.touch.ISpriteOrientation;
import com.causal.game.touch.SpriteOrientation;

public class GameSprite  extends Image {
	
	public Behaviour behaviour;
	public ISpriteBehaviour spriteBehaviour;
	
	public float startingX;
	public float startingY;
	public boolean isActive = true;
	public Status interactStatus = Status.NEUTRAL;
	public InfluenceType influenceType = InfluenceType.NONE;
	public InteractorType interactorType = InteractorType.NONE;
	public int scoreStatus = 0;
	
	public boolean isInteracting = false;
	private boolean isActing = false;
	private String framesPath = null;
	private static String defaultPack = "Default.pack";
	
	public GameSprite(ISpriteBehaviour spriteBehaviour, float x, float y, String framesPath, boolean isActive) {
		super(new TextureAtlas(Gdx.files.internal(framesPath+defaultPack)).getRegions().get(0));

		//Centre origin in frame for rotation;
		TextureRegion currentFrame  = new TextureAtlas(Gdx.files.internal(framesPath+defaultPack)).getRegions().get(0);
		this.setOrigin(currentFrame.getRegionWidth()/2, currentFrame.getRegionHeight()/2);
		this.setPosition(x, y);
		
		float scaleFactor = WorldSystem.get().getLevelScaleFactor();
		this.setScale(scaleFactor);
		
		this.startingX = x;
		this.startingY = y;
		this.framesPath = framesPath;
		this.spriteBehaviour = spriteBehaviour;
	}
	
	public void create(IInteractionType interactionType) {
		
		behaviour = spriteBehaviour.create(interactionType, getXGameCoord(), getYGameCoord(), isActive, this);
		
		//Refactor into Behaviour
		setTouchAction();

		this.isActing = true;
	}
	
	//Implement onTouch action
	private void setTouchAction() {
		
		this.addListener(new ClickListener() {
			
			public void clicked(InputEvent event, float x, float y) 
		    {
				if(GameScoreState.validTouchAction()) {
					Gdx.app.debug("GameSprite", "Pressed at: x: "+x+", y: "+y+"");
					behaviour.onTouch();
				}
		    }
			
		});
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(isActive && isActing){
			this.setDrawable(new TextureRegionDrawable(new TextureRegion(behaviour.getCurrentFrame())));
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if(isActive && isActing){
			behaviour.onAct(delta, interactStatus, isInteracting == false ? interactorType == InteractorType.NONE ? false : true : true);
		}
	}
	
	public int getXGameCoord() {
		return WorldSystem.get().getGameXCoords().indexOf(this.startingX);
	}
	
	public int getYGameCoord() {
		return WorldSystem.get().getGameYCoords().indexOf(this.startingY);
	}

	public float getStartingX() {
		return startingX;
	}

	public float getStartingY() {
		return startingY;
	}
	
	public boolean isActing() {
		return isActing;
	}
	
	public String getFramesPath() {
		return framesPath;
	}

	public Orientation getOrientation() {
		return behaviour.getOrientation();
	}
	
	public boolean changeOrientationOnInvalid() {
		return behaviour.changeOrientationOnInvalid();
	}
	
	public float getInteractLength() {
		return behaviour.getInteractLength();
	}

	public enum Status { NEUTRAL, SELECTED, INFLUENCED }
	
	public enum InfluenceType { SUPPORT, OPPOSE, NONE }
	
	public enum InteractorType { FIRST, INTERMEDIATE, LAST, NONE}
	
}
