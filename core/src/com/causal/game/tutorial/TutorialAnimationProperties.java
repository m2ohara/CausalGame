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
import com.causal.game.sprite.GameSprite;

public class TutorialAnimationProperties {
	
	private List<Object> animations;
	private boolean active = false;
	private Vector2 nextAnimationCoords;
	
	
	public TutorialAnimationProperties(boolean activeFromStart, ArrayList<Object> arrayList, Vector2 nextAnimation) {
		this.animations = arrayList;
		this.active = activeFromStart;
		this.nextAnimationCoords = nextAnimation;
	}
	
	public TutorialAnimationProperties(ArrayList<Object> animations, Vector2 nextAnimation) {
		this.animations = animations;
		this.nextAnimationCoords = nextAnimation;
	}
	
	public TutorialAnimationProperties(ArrayList<Object> arrayList) {
		this.animations = arrayList;
	}
	
	public void showFirstAnimation(final TutorialGameSprite parent, boolean isSet) {
		Actor firstAnimation = (Actor)animations.toArray()[0];
		
		firstAnimation.setPosition(parent.getX(), parent.getY());
		firstAnimation.setTouchable(Touchable.disabled);
		GameProperties.get().addToActorGroup(firstAnimation);	
		
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
							
							Gdx.app.debug("TutorialAnimations", "Tap animation fired");
							
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
							Gdx.app.debug("TutorialAnimations", "Swipe animation fired");
							GameProperties.get().addToActorGroup(animation);	
						}});
				}				
			}
			
			if(previous == null && active) {
				animation.setPosition(parent.getX(), parent.getY());
				animation.setTouchable(Touchable.disabled);
				GameProperties.get().addToActorGroup(animation);	
				active = false;
			}
			
			if(idx++ == animations.size() && nextAnimationCoords != null) {
				animation.addCaptureListener(new ChangeListener() {

						@Override
						public void changed(ChangeEvent event, Actor actor) {
							
							Gdx.app.debug("TutorialAnimations", "Next animation fired");
							
							TutorialGameSprite nextSprite = (TutorialGameSprite)WorldSystem.get().getMemberFromCoords((int)nextAnimationCoords.x, (int)nextAnimationCoords.y);
							nextSprite.createAnimations();

						}});
				
				
				Gdx.app.debug("TutorialAnimations", "Next animation set for "+(int)nextAnimationCoords.x + ", "+(int)nextAnimationCoords.y);
			}
			
			previous = animation;
			
		}
	}
	
}
