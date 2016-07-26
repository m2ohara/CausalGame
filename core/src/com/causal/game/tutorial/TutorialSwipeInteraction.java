package com.causal.game.tutorial;

import com.badlogic.gdx.Gdx;
import com.causal.game.gestures.ISwipeInteraction;
import com.causal.game.gestures.SwipeInteraction;
import com.causal.game.interact.IInteractionType;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;

public class TutorialSwipeInteraction extends SwipeInteraction implements ISwipeInteraction {

	public TutorialSwipeInteraction(IInteractionType interactionType,
			int connectorSprite) {
		super(interactionType, connectorSprite);
	}
	
	@Override
	public boolean isValidInteraction(GameSprite hitActor) {

		boolean isValid = false;

		if(lastHitActor != null && hitActor.interactStatus == Status.NEUTRAL 
				&& ((TutorialGameSprite)hitActor).getSwipeOrientation() == hitActor.getOrientation()) { //Is facing the correct swipe direction

			if(((TutorialGameSprite)lastHitActor).getSwipeOrientation() == Orientation.E && lastHitActor.getOrientation() == Orientation.E) {
				if(WorldSystem.get().getGameXCoords().indexOf(lastHitActor.startingX) == (WorldSystem.get().getGameXCoords().indexOf(hitActor.startingX)-1) 
						&& WorldSystem.get().getGameYCoords().indexOf(lastHitActor.startingY) ==  WorldSystem.get().getGameYCoords().indexOf(hitActor.startingY)) {
					isValid = true;
					Gdx.app.debug("TutorialSwipeInteraction","Follower hit to the right. Last Hit x : "+WorldSystem.get().getGameXCoords().indexOf(lastHitActor.startingX)+", Hit X "+WorldSystem.get().getGameXCoords().indexOf(hitActor.startingX));
				}
			}
			else if(((TutorialGameSprite)lastHitActor).getSwipeOrientation() == Orientation.N && lastHitActor.getOrientation() == Orientation.N) {
				if(WorldSystem.get().getGameXCoords().indexOf(lastHitActor.startingX) == WorldSystem.get().getGameXCoords().indexOf(hitActor.startingX) 
						&& WorldSystem.get().getGameYCoords().indexOf(lastHitActor.startingY) ==  (WorldSystem.get().getGameYCoords().indexOf(hitActor.startingY)+1)) {
					isValid = true;
					Gdx.app.debug("TutorialSwipeInteraction","Follower hit above");
				}

			}
			else if(((TutorialGameSprite)lastHitActor).getSwipeOrientation() == Orientation.S && lastHitActor.getOrientation() == Orientation.S) {
				if(WorldSystem.get().getGameXCoords().indexOf(lastHitActor.startingX) == WorldSystem.get().getGameXCoords().indexOf(hitActor.startingX) 
						&& WorldSystem.get().getGameYCoords().indexOf(lastHitActor.startingY) ==  (WorldSystem.get().getGameYCoords().indexOf(hitActor.startingY)-1)) {
					isValid = true;
					Gdx.app.log("TutorialSwipeInteraction","Follower hit below");
				}
			}
			else if(((TutorialGameSprite)lastHitActor).getSwipeOrientation() == Orientation.W && lastHitActor.getOrientation() == Orientation.W) {
				if(WorldSystem.get().getGameXCoords().indexOf(lastHitActor.startingX) == (WorldSystem.get().getGameXCoords().indexOf(hitActor.startingX)+1) 
						&& WorldSystem.get().getGameYCoords().indexOf(lastHitActor.startingY) ==  WorldSystem.get().getGameYCoords().indexOf(hitActor.startingY)) {
					isValid = true;
					Gdx.app.log("TutorialSwipeInteraction","Follower hit to the left");
				}
			}
		}

		return isValid;

	}

}
