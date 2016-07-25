package com.causal.game.tutorial;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.touch.SpriteOrientation;

public class TutorialSpriteOrientation extends SpriteOrientation {
	
	private Orientation presetOrientation;
	private List<Vector2> tapableSpriteCoords;
	private boolean isTapped = false;
	private Orientation autoInteractOrientation;
	
	public TutorialSpriteOrientation(int xGameCoord, int yGameCoord, Orientation presetOrientation, List<Vector2> tapableSpriteCoords, Orientation autoInteractOrientation) 
	{
		super(xGameCoord, yGameCoord);
		this.presetOrientation = presetOrientation;
		this.tapableSpriteCoords = tapableSpriteCoords;
		this.autoInteractOrientation = autoInteractOrientation;
		
	}
	
	@Override
	public Orientation onRandomChange() {
		Gdx.app.debug("TutorialSpriteOrientation", "Setting sprite "+xGameCoord+", "+yGameCoord+" to orientation "+presetOrientation);
		orientation = presetOrientation;
		return orientation;
	}
	
	@Override
	public boolean cyclicChange() {
		if(!isTapable()) {
			return false;
		}
		else {
			isTapped = true;
			return super.cyclicChange();
		}
	}
	
	@Override
	public void onCyclicChange() {
		if(!isTapped)
			this.orientation = autoInteractOrientation;
		else {
			super.onCyclicChange();
		}
		isTapped = false;
	}
	
	private boolean isTapable() {
		for(Vector2 coords : tapableSpriteCoords) {
			if(WorldSystem.get().getMemberFromCoords((int)coords.x, (int)coords.y).interactStatus == Status.SELECTED) {
				return true;
			}
		}
		
		return false;
	}

}
