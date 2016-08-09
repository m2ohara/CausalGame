package com.causal.game.tutorial;

import com.badlogic.gdx.Gdx;
import com.causal.game.interact.AutonomousInteraction;
import com.causal.game.interact.IInteraction;
import com.causal.game.main.GameSprite;
import com.causal.game.main.WorldSystem.Orientation;

public class TutorialAutonomousInteraction extends AutonomousInteraction {
	
	public TutorialAutonomousInteraction(GameSprite interactor, IInteraction interactionBehaviour) {
		super(interactor, interactionBehaviour);
	}
	
	@Override
	public void interact(Orientation orientation) {
		if(((TutorialGameSprite)interactor).isValidAutoInteractSelectedSprite()) {
			Gdx.app.debug("TutorialAutonomousInteraction", "GameSprite at "+interactor.getXGameCoord()+", "+interactor.getYGameCoord()+" attempting to interact");
			super.interact(orientation);
		}
		
	}

}
