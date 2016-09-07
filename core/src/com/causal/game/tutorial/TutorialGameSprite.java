package com.causal.game.tutorial;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.main.GameSprite;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;

public class TutorialGameSprite extends GameSprite{
	
	private Orientation autoInteractOrientation;
	private Orientation swipeOrientation;
	private List<Vector2> autoInteractOnSelectedSprite;
	
	public TutorialGameSprite(ISpriteBehaviour behaviour, float x, float y, String framesPath, boolean isActive, Orientation swipeOrientation, List<Vector2> autoInteractOnSelectedSprite, Orientation autoInteractOrientation) {
		super(behaviour, x, y, framesPath, isActive);
		
		this.swipeOrientation = swipeOrientation;
		this.autoInteractOnSelectedSprite = autoInteractOnSelectedSprite;
		this.autoInteractOrientation = autoInteractOrientation;
	}
	
	public Orientation getSwipeOrientation() {
		Gdx.app.debug("TutorialGameSprite","Getting swipe orientation"+ swipeOrientation);
		return this.swipeOrientation;
	}
	
	public boolean isValidAutoInteractSelectedSprite(Orientation orientation) {
		for(Vector2 coords : autoInteractOnSelectedSprite) {
			if(WorldSystem.get().getMemberFromCoords((int)coords.x, (int)coords.y).interactorType == InteractorType.FIRST && orientation == autoInteractOrientation) {
				Gdx.app.debug("TutorialGameSprite", "GameSprite "+this.getXGameCoord()+", "+this.getYGameCoord()+ " able to interact" );
				return true;
			}
		}
		return false;
	}

}
