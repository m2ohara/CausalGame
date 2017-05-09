package com.causal.game.animation;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.causal.game.act.IOnActing;
import com.causal.game.act.OnAct;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.sprite.GameSprite;
import com.causal.game.sprite.SwipeSprite;
import com.causal.game.sprite.GameSprite.InfluenceType;
import com.causal.game.tutorial.TutorialDisplayMessage;
import com.causal.game.tutorial.TutorialGameSprite;

public class TutorialSwipeSprite extends Image {
	
	private RepeatAction repeatAction = new RepeatAction();
	private IOnActing onActing = new OnAct(0.15f, "sprites/Meep/SwipingSprite/SwipingSprite.pack", "Swipe");
	private int parentX;
	private int parentY;
	private List<Orientation> swipeDestinations = null;
	private Vector2 endSwipeCoords;
	private boolean isFired = false;
	private boolean isRemoved = false;
	private TutorialDisplayMessage displayMessage;
	
	public TutorialSwipeSprite(List<Orientation> swipeDestinations, Vector2 startSwipeCoords, Vector2 endSwipeCoords) {
		super(new TextureAtlas(Gdx.files.internal("sprites/Meep/SwipingSprite/SwipingSprite.pack")).getRegions().get(0));
		
		this.parentX = (int)startSwipeCoords.x;
		this.parentY = (int)startSwipeCoords.y;
		this.swipeDestinations = swipeDestinations;
		this.endSwipeCoords = endSwipeCoords;

	}
	
	public TutorialSwipeSprite(List<Orientation> swipeDestinations, Vector2 startSwipeCoords, Vector2 endSwipeCoords, TutorialDisplayMessage displayMessage) {
		super(new TextureAtlas(Gdx.files.internal("sprites/Meep/SwipingSprite/SwipingSprite.pack")).getRegions().get(0));
		
		this.parentX = (int)startSwipeCoords.x;
		this.parentY = (int)startSwipeCoords.y;
		this.swipeDestinations = swipeDestinations;
		this.endSwipeCoords = endSwipeCoords;
		this.displayMessage = displayMessage;

	}
	
	public void setSprite() {
		
		Vector2 originalVectors = WorldSystem.get().getVector2Coords(parentX, parentY);
		
		this.setPosition(originalVectors.x, originalVectors.y);
		this.setTouchable(Touchable.disabled);
		
		SequenceAction sequence = new SequenceAction();
		
		for(Orientation destination : swipeDestinations) {

			Vector2 destinationVectors = null;
			
			if(destination == Orientation.N) {
				destinationVectors = WorldSystem.get().getVector2Coords(parentX, parentY - 1);
				parentY = parentY - 1;
			}
			else if(destination == Orientation.E) {
				destinationVectors = WorldSystem.get().getVector2Coords(parentX + 1, parentY);
				parentX = parentX + 1;
			}
			else if(destination == Orientation.S) {
				destinationVectors = WorldSystem.get().getVector2Coords(parentX, parentY + 1);
				parentY = parentY + 1;
			}
			else if(destination == Orientation.W) {
				destinationVectors = WorldSystem.get().getVector2Coords(parentX - 1, parentY);
				parentX = parentX - 1;
			}
			
			if(destinationVectors != null) {
				Action action = Actions.moveTo(destinationVectors.x, destinationVectors.y, 0.5f);
				sequence.addAction(action);
			}

		}
		
		sequence.addAction(Actions.moveTo(originalVectors.x, originalVectors.y, 0f));
		
		repeatAction.setAction(sequence);
		repeatAction.setCount(RepeatAction.FOREVER);

		this.addAction(repeatAction);
		
		Gdx.app.log("TutorialSwipeSprite", "Swipe animation set for "+parentX + ", "+parentY);

	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
			this.setDrawable(new TextureRegionDrawable(new TextureRegion(onActing.getCurrentFrame())));
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		checkChangeEvent();
	}
	
	public void checkChangeEvent() {
		
		GameSprite startSprite = WorldSystem.get().getMemberFromCoords((int)endSwipeCoords.x, (int)endSwipeCoords.y);
		
		if(SwipeSprite.get().getStartSprite().getXGameCoord() == (int)endSwipeCoords.x && SwipeSprite.get().getStartSprite().getYGameCoord() == (int)endSwipeCoords.y 
				&& !isFired && startSprite.influenceType != InfluenceType.NONE) {
			this.fire(new ChangeEvent());
			isFired = true;
			this.remove();
		}
		
		if(SwipeSprite.get().getStartSprite().getXGameCoord() == (int)endSwipeCoords.x && SwipeSprite.get().getStartSprite().getYGameCoord() == (int)endSwipeCoords.y && !isRemoved) 
		{			
			this.setVisible(false);
			isRemoved = true;
			
			//Display on swipe finished
//			if(displayMessage.getDisplayStage() == 2) {
//				displayMessage.setVisible(true);
//			}
		}
	}
	
	

}
