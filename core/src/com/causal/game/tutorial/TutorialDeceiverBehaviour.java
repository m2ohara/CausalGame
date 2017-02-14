package com.causal.game.tutorial;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.causal.game.behaviour.DeceiverBehaviour;
import com.causal.game.interact.DeceiverAutonomousBehaviour;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.sprite.GameSprite;

public class TutorialDeceiverBehaviour extends DeceiverBehaviour {
	
	private Orientation presetOrientation;
	private List<Vector2> tapableSpriteCoords;
	private Orientation autoInteractOrientation;
	
	public TutorialDeceiverBehaviour(Orientation presetOrientation, List<Vector2> list, Orientation autoInteractOrientation) {
		super();
		this.presetOrientation = presetOrientation;
		this.tapableSpriteCoords = list;
		this.autoInteractOrientation = autoInteractOrientation;
	}
	
	@Override
	protected void setSpriteOrientation(int xGameCoord, int yGameCoord) {
		spriteOrientation = new TutorialSpriteOrientation(xGameCoord, yGameCoord, presetOrientation, tapableSpriteCoords, autoInteractOrientation);
	}
	
	@Override
	protected void setAutoInteraction(GameSprite gameSprite) {
		autoInteraction = new TutorialAutonomousInteraction(gameSprite, new DeceiverAutonomousBehaviour());
	}	
}
