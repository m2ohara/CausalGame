package com.causal.game.tutorial;

import com.causal.game.behaviour.Behaviour;
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
			super.interact(orientation);
		}
		
	}

}
