package com.causal.game.tutorial;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.causal.game.animation.TutorialTapSprite;
import com.causal.game.main.GameProperties;
import com.causal.game.main.WorldSystem;

public class TutorialAnimationProperties {
	
	public Vector2 activateSpriteCoords;
	public ArrayList<Integer> animationTypes;
	private boolean isActive = false;
	public boolean isSet = false;
	
	public TutorialAnimationProperties(Vector2 activateSprite, ArrayList<Integer> arrayList) {
		this.activateSpriteCoords = activateSprite;
		this.animationTypes = arrayList;
	}
	
	public TutorialAnimationProperties(boolean activeFromStart, ArrayList<Integer> arrayList) {
		this.animationTypes = arrayList;
		this.isSet = activeFromStart;
	}
	
	public void prepareAnimation() {
		if(!isSet) {
			Gdx.app.log("TutorialAnimationProperties", "GameSprite animations set" );
			isSet = true;
			GameProperties.get().getActorGroup().clear();
		}
	}
	
	public void activateAnimations(boolean autoInteract, final TutorialGameSprite dependentActor) {
		if(isSet && !autoInteract && !isActive) {
			Gdx.app.log("TutorialAnimationProperties", "GameSprite "+dependentActor.getX()+", "+dependentActor.getY()+ " animations active" );
			
			if (animationTypes.contains(0)) {
				TutorialTapSprite tapSprite = new TutorialTapSprite();
				tapSprite.setPosition(dependentActor.getX(), dependentActor.getY());
				tapSprite.setTouchable(Touchable.disabled);
				GameProperties.get().addToActorGroup(tapSprite);
				
				updateOnChange(dependentActor, tapSprite);
				
				tapSprite.addListener(new ChangeListener() {

					@Override
					public void changed(ChangeEvent event, Actor actor) {
						if(dependentActor.getOrientation() == dependentActor.getSwipeOrientation()) {
							actor.setVisible(false);
						}
					}
					
				});
			}
			
			
			if(animationTypes.contains(1)) {
				
			}
			
			isActive = true;
		}
	}
	
	public void updateOnChange(TutorialGameSprite dependentActor, Actor updateActor) {
		
		dependentActor.event.setListenerActor(updateActor);
		
	};
	
}
