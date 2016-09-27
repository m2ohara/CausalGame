package com.causal.game.tutorial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.causal.game.animation.TutorialSwipeSprite;
import com.causal.game.animation.TutorialTapSprite;
import com.causal.game.main.GameProperties;
import com.causal.game.main.WorldSystem.Orientation;

public class TutorialAnimationProperties {
	
	public Vector2 activateSpriteCoords;
	public ArrayList<Integer> animationTypes;
	public List<Actor> animations;
	private boolean isActive = false;
	public boolean isSet = false;
	public TutorialTapSprite tapSprite = new TutorialTapSprite();
	public TutorialSwipeSprite swipingSprite = null;
	
	public TutorialAnimationProperties(Vector2 activateSprite, ArrayList<Integer> arrayList) {
		this.activateSpriteCoords = activateSprite;
		this.animationTypes = arrayList;
		
	}
	
	public TutorialAnimationProperties(boolean activeFromStart, ArrayList<Integer> arrayList) {
		this.animationTypes = arrayList;
		this.isSet = activeFromStart;
	}
	
	public TutorialAnimationProperties(ArrayList<Actor> animations) {
		this.animations = animations;
	}
	
	public void prepareAnimation() {
		if(!isSet) {
			Gdx.app.log("TutorialAnimationProperties", "GameSprite animations set" );
			isSet = true;
			GameProperties.get().getActorGroup().clear();
		}
	}
	
	public void activateAnimations(boolean autoInteract, TutorialGameSprite dependentActor) {
		if(isSet && !autoInteract && !isActive) {
			Gdx.app.log("TutorialAnimationProperties", "GameSprite "+dependentActor.getX()+", "+dependentActor.getY()+ " animations active" );
			
			if (animationTypes.contains(-1)) {
				tapSprite.setPosition(dependentActor.getX(), dependentActor.getY());
				tapSprite.setTouchable(Touchable.disabled);
//				dependentActor.getParent().addActor(tapSprite);
				
//				tapSprite.addCaptureListener(new ChangeListener() {
//					
//
//					@Override
//					public void changed(ChangeEvent event, Actor actor) {
//						actor.setColor(Color.BLUE);
//					}
//					
//				});
			}
			
			
			if(animationTypes.contains(1)) {
				List<Orientation> orientations = Arrays.asList(Orientation.N);
				swipingSprite = new TutorialSwipeSprite(orientations, dependentActor.getXGameCoord(), dependentActor.getYGameCoord());
				swipingSprite.setPosition(dependentActor.getX(), dependentActor.getY());
				swipingSprite.setTouchable(Touchable.disabled);
				GameProperties.get().addToActorGroup(swipingSprite);
			}
			
			isActive = true;
		}
	}
	
	public void createAnimations(final TutorialGameSprite parent) {
		
		Actor previous = null;
		
		for(final Actor animation : animations) {
			
			if(animation.getClass() == TutorialTapSprite.class) {
				
				if(previous != null) {
					previous.addCaptureListener(new ChangeListener() {

						@Override
						public void changed(ChangeEvent event, Actor actor) {
							animation.setPosition(parent.getX(), parent.getY());
							animation.setTouchable(Touchable.disabled);
							GameProperties.get().addToActorGroup(animation);	
						}});
				}
				
			}
			
			if(animation.getClass() == TutorialSwipeSprite.class) {
				
				if(previous != null) {
					previous.addCaptureListener(new ChangeListener() {

						@Override
						public void changed(ChangeEvent event, Actor actor) {
							animation.setPosition(parent.getX(), parent.getY());
							animation.setTouchable(Touchable.disabled);
							GameProperties.get().addToActorGroup(animation);	
						}});
				}
				
			}
			previous = animation;
			
		}
	}
	
	public void startAnimationChain() {
		
	}
	
}
