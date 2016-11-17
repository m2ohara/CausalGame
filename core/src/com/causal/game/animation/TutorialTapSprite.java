package com.causal.game.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.causal.game.act.IOnActing;
import com.causal.game.act.OnAct;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.tutorial.TutorialDisplayMessage;
import com.causal.game.tutorial.TutorialGameSprite;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public class TutorialTapSprite extends Image {
	
	private IOnActing onActing = new OnAct(0.15f, "sprites/Meep/TapSprite/TapSprite.pack", "Tap");
	private TutorialGameSprite tapGameSprite;
	private Orientation tapOrientation;
	private Vector2 tapGameSpriteCoords = null;
	public boolean isFired = false;
	private TutorialDisplayMessage displayMessage;
	
	public TutorialTapSprite(Vector2 tapGameSprite, Orientation tapOrientation) {
		super(new TextureAtlas(Gdx.files.internal("sprites/Meep/TapSprite/TapSprite.pack")).getRegions().get(0));
		
		this.tapGameSpriteCoords = tapGameSprite;
		this.tapOrientation = tapOrientation;
	}
	
	public TutorialTapSprite(Vector2 tapGameSprite, Orientation tapOrientation, TutorialDisplayMessage displayMessage) {
		super(new TextureAtlas(Gdx.files.internal("sprites/Meep/TapSprite/TapSprite.pack")).getRegions().get(0));
		
		this.tapGameSpriteCoords = tapGameSprite;
		this.tapOrientation = tapOrientation;
		this.displayMessage = displayMessage;
	}
	
	public void setSprite() {
		this.tapGameSprite = (TutorialGameSprite)WorldSystem.get().getMemberFromCoords((int)tapGameSpriteCoords.x, (int)tapGameSpriteCoords.y);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
			this.setDrawable(new TextureRegionDrawable(new TextureRegion(onActing.getCurrentFrame())));
	}

	@Override
	public void act(float delta) {
		super.act(delta);
			onActing.performActing(delta);
			
			checkChangeEvent();
	}
	
	public void checkChangeEvent() {
		if(tapGameSprite.getOrientation() == tapOrientation && !isFired) {
			this.fire(new ChangeEvent());
			isFired = true;
			this.remove();
			
			//Display on tap finished
			if(displayMessage.getDisplayStage() == 1) {
				displayMessage.setVisible(true);
			}
		}
	}
	
	

}
