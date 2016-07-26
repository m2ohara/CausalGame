package com.causal.game.interact;

import com.badlogic.gdx.Gdx;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;

public class AutonomousInteraction {
	
	public IInteraction interactionBehaviour;
	public GameSprite interactor;
	
	public AutonomousInteraction() {}//TODO: Remove on full refactor
	
	public AutonomousInteraction(GameSprite interactor, IInteraction interactionBehaviour) {
		this.interactor = interactor;
		this.interactionBehaviour = interactionBehaviour;
	}

	public void interact(Orientation orientation) {
		Gdx.app.debug("AutonomousInteraction","Interactor with status "+interactor.interactStatus);
		// As long as interactor isn't neutral
		if (interactor.interactStatus != Status.NEUTRAL) {
			GameSprite interactee = null;

			// If facing towards the right
			if (orientation == Orientation.E
					&& (interactor.getXGameCoord() + 1) < WorldSystem
							.get().getGameXCoords().size()) {
				// Get interactee by coordinates
				interactee = WorldSystem.get().getMemberFromCoords(
						interactor.getXGameCoord() + 1,
						(interactor.getYGameCoord()));
				Gdx.app.debug("AutonomousInteraction","Member type " + interactor.interactStatus
						+ "  influencing to the right at "
						+ (interactor.getXGameCoord() + 1) + ", "
						+ interactor.getYGameCoord());

			}
			if (orientation == Orientation.N
					&& (interactor.getYGameCoord() - 1) > -1) {
				interactee = WorldSystem.get().getMemberFromCoords(
						interactor.getXGameCoord(),
						(interactor.getYGameCoord() - 1));
				Gdx.app.debug("AutonomousInteraction","Member type " + interactor.interactStatus
						+ "  influencing above at "
						+ interactor.getXGameCoord() + ", "
						+ (interactor.getYGameCoord() - 1));

			}
			if (orientation == Orientation.S
					&& (interactor.getYGameCoord() + 1) < WorldSystem
							.get().getGameYCoords().size()) {
				interactee = WorldSystem.get().getMemberFromCoords(
						interactor.getXGameCoord(),
						(interactor.getYGameCoord() + 1));
				Gdx.app.debug("AutonomousInteraction","Member type " + interactor.interactStatus
						+ "  influencing below at "
						+ interactor.getXGameCoord() + ", "
						+ (interactor.getYGameCoord() + 1));
			}
			if (orientation == Orientation.W
					&& (interactor.getXGameCoord() - 1) > -1) {
				interactee = WorldSystem.get().getMemberFromCoords(
						interactor.getXGameCoord() - 1,
						(interactor.getYGameCoord()));
				Gdx.app.debug("AutonomousInteraction","Member type " + interactor.interactStatus
						+ " influencing to the left at "
						+ (interactor.getXGameCoord() - 1) + ", "
						+ interactor.getYGameCoord());
			}

			// Perform interaction
			if (interactee != null) {
				interact(interactee);
			}

		}

	}

	protected void interact(GameSprite interactee) {

		if (this.interactionBehaviour != null) {
			this.interactionBehaviour.interact(interactor, interactee);
		}
	}
}
