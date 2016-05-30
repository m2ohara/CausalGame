package com.causal.game.interact;

import com.causal.game.main.GameSprite;

public class GenericInteraction extends AutonomousInteraction {
	
	public GenericInteraction(GameSprite interactor, IInteraction interactionBehaviour) {
		this.interactor = interactor;
		this.interactionBehaviour = interactionBehaviour;
	}

}
