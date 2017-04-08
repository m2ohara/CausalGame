package com.causal.game.interact;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.causal.game.sprite.AutoInteractFrameSprite;
import com.causal.game.sprite.GameSprite;
import com.causal.game.sprite.GameSprite.Status;

public class DeceiverAutonomousBehaviour implements IInteraction {
	
	private AutoInteractFrameSprite interactSprite;
	private IInteractionType interactionType;

	@Override
	public void interact(GameSprite interactor, GameSprite interactee) {
		
		//Influence if interactee is neutral and interactor isn't already interacting
		if(!interactor.isInteracting && interactee.interactStatus == Status.NEUTRAL ) {
			setInteractionResult(interactor, interactee);
			
			interactor.isInteracting = true;
			interactee.interactStatus = Status.INFLUENCED;
			interactee.isActive = false;
			interactSprite = new AutoInteractFrameSprite(interactor, interactee, interactionType, new TextureAtlas(Gdx.files.internal(interactor.getFramesPath()+"SpriteMove.pack")), interactor.getFramesPath());
			interactSprite.setAction();

		}
		
	}
	
	private void setInteractionResult(GameSprite interactor, GameSprite interactee) {
		
		if(interactee.interactStatus == Status.NEUTRAL && interactee.isActive == true) {
			//Oppose
			interactionType = new OpposerInteractionType(interactor, interactee);
		}
	}
	
}
