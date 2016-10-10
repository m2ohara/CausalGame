package com.causal.game.tutorial;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.causal.game.animation.TutorialSwipeSprite;
import com.causal.game.animation.TutorialTapSprite;
import com.causal.game.main.GameProperties;
import com.causal.game.main.WorldSystem;

public class TutorialAnimationProperties {
	
	public Vector2 activateSpriteCoords;
	public ArrayList<Integer> animationTypes;
	public List<Object> animations;
	public boolean isSet = false;
	private Vector2 nextAnimationCoords;
	
	public TutorialAnimationProperties(Vector2 activateSprite, ArrayList<Integer> arrayList) {
		this.activateSpriteCoords = activateSprite;
		this.animationTypes = arrayList;
	}
	
	public TutorialAnimationProperties(boolean activeFromStart, ArrayList<Object> arrayList, Vector2 nextAnimation) {
		this.animations = arrayList;
		this.isSet = activeFromStart;
		this.nextAnimationCoords = nextAnimation;
	}
	
	public TutorialAnimationProperties(ArrayList<Object> arrayList) {
		this.animations = arrayList;
		
	}
	
	public void createAnimations(final TutorialGameSprite parent, boolean isSet) {
		this.isSet = true;
		this.createAnimations(parent);
	}
	
	public void createAnimations(final TutorialGameSprite parent) {
		
		Actor previous = null;
		int idx = 1;
		
		for(Object actor : animations) {
			
			final Actor animation = (Actor)actor;
			
			if(animation.getClass() == TutorialTapSprite.class) {
				
				((TutorialTapSprite) animation).setSprite();
				
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
				
				((TutorialSwipeSprite) animation).setSprite();
				
				if(previous != null) {
					previous.addCaptureListener(new ChangeListener() {

						@Override
						public void changed(ChangeEvent event, Actor actor) {
							
							GameProperties.get().addToActorGroup(animation);	
						}});
				}
				
			}
			
			if(previous == null && isSet) {
				animation.setPosition(parent.getX(), parent.getY());
				animation.setTouchable(Touchable.disabled);
				GameProperties.get().addToActorGroup(animation);	
				isSet = false;
			}
			
			if(idx++ == animations.size()) {
				animation.addCaptureListener(new ChangeListener() {

						@Override
						public void changed(ChangeEvent event, Actor actor) {
							TutorialGameSprite nextSprite = (TutorialGameSprite)WorldSystem.get().getMemberFromCoords((int)nextAnimationCoords.x, (int)nextAnimationCoords.y);
							nextSprite.createAnimations();

						}});
			}
			
			previous = animation;
			
		}
	}

	
	public void prepareAnimation() {
		if(!isSet) {
			Gdx.app.log("TutorialAnimationProperties", "GameSprite animations set" );
			isSet = true;
			GameProperties.get().getActorGroup().clear();
		}
	}
	
//	public void activateAnimations(boolean autoInteract, TutorialGameSprite dependentActor) {
//		if(isSet && !autoInteract && !isActive) {
//			Gdx.app.log("TutorialAnimationProperties", "GameSprite "+dependentActor.getX()+", "+dependentActor.getY()+ " animations active" );
//			
//			if (animationTypes.contains(0)) {
//				tapSprite.setPosition(dependentActor.getX(), dependentActor.getY());
//				tapSprite.setTouchable(Touchable.disabled);
//				GameProperties.get().addToActorGroup(tapSprite);
//				
//				tapSprite.addCaptureListener(new ChangeListener() {
//					
//
//					@Override
//					public void changed(ChangeEvent event, Actor actor) {
//						actor.setColor(Color.BLUE);
//					}
//					
//				});
//			}
//			
//			
//			if(animationTypes.contains(1)) {
//				List<Orientation> orientations = Arrays.asList(Orientation.N);
//				swipingSprite = new TutorialSwipeSprite(orientations, dependentActor.getXGameCoord(), dependentActor.getYGameCoord());
//				swipingSprite.setPosition(dependentActor.getX(), dependentActor.getY());
//				swipingSprite.setTouchable(Touchable.disabled);
//				GameProperties.get().addToActorGroup(swipingSprite);
//			}
//			
//			isActive = true;
//		}
//	}
	
	//TODO: Instantiate animation sprites.
//	public void instantiateAnimations() {
//		for(Actor animation : animations) {
//			if(animation.getClass() == TutorialTapSprite.class) {
//				
//				animation = new TutorialTapSprite();
//			}
//			
//			if(animation.getClass() == TutorialSwipeSprite.class) {
//				animation = new TutorialSwipeSprite();
//			}
//		}
//	}
	
	
}
