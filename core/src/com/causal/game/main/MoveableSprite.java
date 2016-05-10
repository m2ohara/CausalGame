package com.causal.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Array;
import com.causal.game.main.Game.Head;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.state.Follower;
import com.causal.game.state.PlayerState;

public class MoveableSprite 
{
	
	private Head type = null;
	private String framesPath = null;
	private Array<AtlasRegion> frames;
	private TextureRegion currentFrame;
	private Actor dragSprite;
	private Actor sourceSprite;
	private float origX;
	private float origY;
	private boolean isPlaceholderActive = false;
	private Image placeholderImage;
	
	public MoveableSprite(Follower follower, float x, float y, Image sourceTargetImage) {
		this.type = follower.type.head;
		this.origX = x;
		this.origY = y;
		this.framesPath = follower.type.imagePath;
		this.frames = new TextureAtlas(Gdx.files.internal(framesPath)).getRegions();
		currentFrame = frames.get(0);
		
		this.placeholderImage = sourceTargetImage;
		this.placeholderImage.setScale(WorldSystem.get().getLevelScaleFactor());
		this.placeholderImage.setOrigin(placeholderImage.getWidth()/2, placeholderImage.getWidth()/2);
		
		setSourceSprite(x, y);
		
		setDragAndDrop(false);
	}
	
	private void setSourceSprite(float x, float y) {
		sourceSprite = new Image(currentFrame);
		sourceSprite.setOrigin(sourceSprite.getWidth()/2, sourceSprite.getWidth()/2);
		sourceSprite.setPosition(x, y);
		sourceSprite.setScale(WorldSystem.get().getLevelScaleFactor());
		
		Gdx.app.debug("MoveableSprite","Setting source sprite coords "+sourceSprite.getX()+", "+sourceSprite.getY());
		
		GameProperties.get().addActorToStage(sourceSprite);
	}
	
	private DragAndDrop dragAndDrop;
	private void setDragAndDrop(boolean addOrigSource) {	
		
		dragSprite = new Image(currentFrame);	
		dragAndDrop = new DragAndDrop();
		dragAndDrop.setDragActorPosition(-dragSprite.getWidth()/2, dragSprite.getHeight()/2+(dragSprite.getHeight()/8));	
		
		setDragSource();
		
		Group group = GameProperties.get().getActorGroup();
		for(Actor actor : group.getChildren()) {
			addDropTarget(actor);
		}
		
		if(addOrigSource) {
			this.isPlaceholderActive = true;
			addPlaceholderTarget();
		}
	}
	
	public void resetLocation(float x, float y, boolean isPlaceholderActive) {
		
		dragAndDrop.clear();
		sourceSprite.remove();
		dragSprite.remove();
		
		setSourceSprite(x, y);
		
		setDragAndDrop(isPlaceholderActive);
		
		this.isPlaceholderActive = isPlaceholderActive;
	}
	
	public void setDragSource() {
		dragAndDrop.addSource(new Source(sourceSprite) {
			public Payload dragStart (InputEvent event, float x, float y, int pointer) {
				Payload payload = new Payload();
				payload.setObject(dragSprite);
				payload.setDragActor(dragSprite);
				
				//Set placeholder visible if dragging and is active
				if(isPlaceholderActive) {
					placeholderImage.toFront();
					placeholderImage.setTouchable(Touchable.enabled);
				}

				return payload;
			}
		});
	}
	
	private void addDropTarget(final Actor target) {
		dragAndDrop.addTarget(new Target(target) {			
			public boolean drag (Source source, Payload payload, float x, float y, int pointer) {			
				getActor().setColor(Color.RED);
				return true;
			}

			public void reset (Source source, Payload payload) {
				if(((GameSprite)getActor()).interactStatus == Status.SELECTED)
					getActor().setColor(Color.ORANGE);
				else
					getActor().setColor(Color.WHITE);
			}

			public void drop (Source source, Payload payload, float x, float y, int pointer) {
				
				Gdx.app.debug("MoveableSprite","Dropped at " + x + ", " + y);

				//Hide original drop target if displayed
				hidePlaceholderTarget();
				
				//Set dragActor new source coordinates
				resetLocation(getActor().getX(), getActor().getY(), true);
			}
		});
	}
	
	private void addPlaceholderTarget() {

		
		dragAndDrop.addTarget(new Target(placeholderImage) {
			
			public boolean drag (Source source, Payload payload, float x, float y, int pointer) {
				return true;
			}

			public void reset (Source source, Payload payload) {
			}

			public void drop (Source source, Payload payload, float x, float y, int pointer) {
				
				Gdx.app.debug("MoveableSprite","Accepted: " + payload.getObject() + " " + x + ", " + y);
				
				hidePlaceholderTarget();
				
				resetLocation(origX, origY, false);
			}
		});
	}
	
	private void hidePlaceholderTarget() {
		if(isPlaceholderActive) {
			placeholderImage.toBack();
			placeholderImage.setTouchable(Touchable.disabled);
		}
	}
	
	public void setAsActive() {
		this.isPlaceholderActive = true;
	}
	
	public boolean isActive() {
		return this.isPlaceholderActive;
	}
	
	public Actor getSourceSprite() {
		return this.sourceSprite;
	}
	
	public Head getType() {
		return this.type;
	}
	
	public float getCurrentX() {
		return sourceSprite.getX();
	}
	
	public float getCurrentY() {
		return sourceSprite.getY();
	}
	
	public String getFramesPath() {
		return (String)PlayerState.get().getFollowerTypeByHead(this.type).imagePath;
	}
	
	public Actor getTargetImage() {
		return this.placeholderImage;
	}
	

}
