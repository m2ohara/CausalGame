package com.causal.game.main;

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
import com.causal.game.interact.IInteractionType;
import com.causal.game.interact.SwipeInteraction;
import com.causal.game.main.GameSprite.Status;

public class SwipeSprite {
	
	private DragAndDrop dragAndDrop;
	private Actor sourceSprite;
	private Actor dragSprite;
	private TextureRegion sourceTexture = new TextureAtlas(Gdx.files.internal("sprites/Meep/Effects/Effects.pack")).getRegions().get(1);
	private ArrayList<Target> targets = new ArrayList<Target>();
	private boolean validSwipe = true;
	private Target lastValidTarget;
	private Actor lastValidActor;
	
	private static SwipeInteraction interaction = null;
	private GameSprite startSprite = null;
	
	public SwipeSprite(IInteractionType interactionType, int influenceType) {
		
		interaction = new SwipeInteraction(interactionType, influenceType);
		GameProperties.get().setSwipeInteraction(interaction);
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
		sourceSprite.setPosition(x+sourceSprite.getWidth()/2, y+sourceSprite.getWidth()/2);
		sourceSprite.setScale(WorldSystem.get().getLevelScaleFactor());
		
		Gdx.app.debug(this.toString(),  "Setting source sprite coords "+sourceSprite.getX()+", "+sourceSprite.getY());
		
		GameProperties.get().getStage().addActor(sourceSprite);
	}
	
	private void setDragAndDrop() {	
		
		dragSprite = new Image(sourceTexture);	
		dragAndDrop = new DragAndDrop();
		dragAndDrop.setDragActorPosition(0, dragSprite.getHeight()/2);
		
		setDragSource();
		
		Group group = GameProperties.get().getActorGroup();
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
				interaction.interactHit(startSprite, true);
				sourceSprite.setVisible(false);
				Gdx.app.debug(this.toString(), "Starting drag");

				return payload;
			}
			
			public void dragStop (InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
				if(!validSwipe) {
					Gdx.app.debug(this.toString(), "Stopped invalid drag");
					if(lastValidTarget != null) {
						validSwipe = true;
						Gdx.app.debug(this.toString(), "Dropped source at last valid target");
						lastValidTarget.drop(this, payload, x, y, pointer);
					}
					else {
						Gdx.app.debug(this.toString(), "Enabled sprite");
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
					Gdx.app.debug(this.toString(),  "Stopped valid drag");
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
					if(interaction.interactHit((GameSprite)target, false)) {
						Gdx.app.debug(this.toString(),  "Valid drag at " + x + ", " + y);
						lastValidActor = target;
						lastValidTarget = this;
					}
					else if(((GameSprite)target).interactStatus == Status.NEUTRAL) {
						Gdx.app.debug(this.toString(),  "Invalid drag at " + x + ", " + y);
						validSwipe = false;
					}
				}
				
				return true;
			}

			public void drop (Source source, Payload payload, float x, float y, int pointer) {
				if(!validSwipe) {
					Gdx.app.debug(this.toString(),  "Invalid drop at " + x + ", " + y);
					if(lastValidTarget != null) {
						validSwipe = true;
						Gdx.app.debug(this.toString(),  "Dropped source at last valid target at "+lastValidActor.getX()+" "+lastValidActor.getY());
						lastValidTarget.drop(source, payload, lastValidActor.getX(), lastValidActor.getY(), pointer);
					}
					else {
						sourceSprite.setVisible(true);
					}
					
				}
				else {
					Gdx.app.debug(this.toString(),  "Valid drop. Dropped at " + x + ", " + y);
					
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
			Gdx.app.debug(this.toString(),  "Resetting swipe sprite");
			validSwipe = true;
			dragAndDrop.clear();
			sourceSprite.remove();
			dragSprite.remove();
			lastValidActor = null;
			lastValidTarget = null;
			
			startSprite = (GameSprite)lastActor;
			interaction.reset();
			
			activate();
		}
	
	}

}
