package com.causal.game.sprite;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.causal.game.main.GameProperties;
import com.causal.game.main.WorldSystem;
import com.causal.game.sprite.GameSprite.Status;

public class SwipeSprite {
	
	private static SwipeSprite instance;
	private DragAndDrop dragAndDrop;
	private static Actor sourceSprite;
	private Actor dragSprite;
	private TextureRegion sourceTexture = new TextureAtlas(Gdx.files.internal("sprites/Meep/Effects/Effects.pack")).getRegions().get(1);
	private ArrayList<Target> targets = new ArrayList<Target>();
	private boolean validSwipe = true;
	private Target lastValidTarget;
	private Actor lastValidActor;
	private GameSprite startSprite = null;
	
	public static SwipeSprite get() {
		if(instance == null) {
			instance = new SwipeSprite();
		}
		return instance;
	}
	
	private SwipeSprite() {
	}
	
	public void setStartSprite(GameSprite startSprite) {
		this.startSprite = startSprite;
	}
	
	public void activate() {
		setSourceSprite(startSprite.getX(), startSprite.getY());
		setDragAndDrop();
	}
	
	private void setSourceSprite(float x, float y) {
		sourceSprite = new Image(sourceTexture);
		sourceSprite.setOrigin(sourceSprite.getWidth()/2, sourceSprite.getWidth()/2);
		sourceSprite.setPosition(x+sourceSprite.getWidth()/8, y);
		sourceSprite.setScale(WorldSystem.get().getLevelScaleFactor());
		
		Gdx.app.debug("SwipeSprite",  "Setting source sprite coords "+sourceSprite.getX()+", "+sourceSprite.getY());
		
		GameProperties.get().addActorToStage(sourceSprite);
	}
	
	private void setDragAndDrop() {	
		
		dragSprite = new Image(sourceTexture);	
		dragAndDrop = new DragAndDrop();
		dragAndDrop.setDragActorPosition(0, dragSprite.getHeight()/2);
		
		setDragSource();
		
		Group group = GameProperties.get().getGameSpriteGroup();
		for(Actor actor : group.getChildren()) {
			addDropTarget(actor);
		}
	}
	
	private void setDragSource() {
		dragAndDrop.addSource(new Source(sourceSprite) {
			public Payload dragStart (InputEvent event, float x, float y, int pointer) {
				Payload payload = new Payload();
				payload.setObject(dragSprite);
				payload.setDragActor(dragSprite);
				GameProperties.get().getSwipeInteraction().interactHit(startSprite, true);
				sourceSprite.setVisible(false);
				Gdx.app.debug("SwipeSprite", "Starting drag. Is valid "+validSwipe);

				return payload;
			}
			
			public void dragStop (InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
				if(!validSwipe) {
					Gdx.app.debug("SwipeSprite", "Stopped invalid drag");
					if(lastValidTarget != null) {
						validSwipe = true;
						Gdx.app.debug("SwipeSprite", "Dropped source at last valid target");
						lastValidTarget.drop(this, payload, x, y, pointer);
					}
					else {
						Gdx.app.debug("SwipeSprite", "Enabled sprite");
						sourceSprite.setVisible(true);
					}
					
				}
				else {
					if(lastValidTarget != null) {
						validSwipe = false;
						lastValidTarget.drop(this, payload, x, y, pointer);
					}
					else {
						sourceSprite.setVisible(true);
					}
					Gdx.app.debug("SwipeSprite",  "Stopped valid drag");
				}
			}
			
			
		});
	}
	
	private void addDropTarget(final Actor target) {
		
		Target targetToAdd = new Target(target) {
			boolean isHit = false;
			public boolean drag (Source source, Payload payload, float x, float y, int pointer) {
				
				//On hit
				if(!isHit) {
					isHit = true;
					if(GameProperties.get().getSwipeInteraction().interactHit((GameSprite)target, false)) {
						Gdx.app.debug("SwipeSprite",  "Valid drag at " + x + ", " + y);
						lastValidActor = target;
						lastValidTarget = this;
					}
					else if(((GameSprite)target).interactStatus == Status.NEUTRAL) {
						Gdx.app.debug("SwipeSprite",  "Invalid hit at " + x + ", " + y);
						validSwipe = false;
					}
				}
				
				return true;
			}
			
			public void reset (Source source, Payload payload) {
				isHit = false;
			}

			public void drop (Source source, Payload payload, float x, float y, int pointer) {
				if(!validSwipe) {
					Gdx.app.debug("SwipeSprite",  "Invalid drop at " + x + ", " + y);
					if(lastValidTarget != null) {
						validSwipe = true;
						Gdx.app.debug("SwipeSprite",  "Dropped source at last valid target at "+lastValidActor.getX()+" "+lastValidActor.getY());
						lastValidTarget.drop(source, payload, lastValidActor.getX(), lastValidActor.getY(), pointer);
					}
					else {
						sourceSprite.setVisible(true);
					}
					
				}
				else {
					Gdx.app.debug("SwipeSprite",  "Valid drop. Dropped at " + x + ", " + y);
					
					//Set dragActor new source coordinates
					onComplete(lastValidActor);
				}
			}
		};
		
		targets.add(targetToAdd);
		dragAndDrop.addTarget(targetToAdd);
	}
	
	private void onComplete(Actor lastActor) {
		
		if(validSwipe) {
			Gdx.app.debug("SwipeSprite",  "Resetting swipe sprite");
			validSwipe = true;
			dragAndDrop.clear();
			sourceSprite.remove();
			dragSprite.remove();
			lastValidActor = null;
			lastValidTarget = null;
			
			startSprite = (GameSprite)lastActor;
			GameProperties.get().getSwipeInteraction().reset();
		}
	
	}
	
	public GameSprite getStartSprite()  {
		return startSprite;
	}
	

}
