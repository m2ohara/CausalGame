package com.causal.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.causal.game.interact.IInteractionType;
import com.causal.game.state.PlayerState;

public class AutoInteractSprite extends Image {
	
	private static String framesPath = "sprites/Meep/Effects/Effects.pack";
	public boolean isInteracting = false;
	protected float interactionScaleFactor;
	protected float currentScaleFactor;
	protected float interactionStateLength;
	protected float finalScale;
	protected ScaleToAction scaleAction;
	private IInteractionType interactionType;
	
	public AutoInteractSprite(float interactionStateLength, int interactionStages, GameSprite interactor, IInteractionType interactionType) {
		super(new TextureAtlas(Gdx.files.internal(framesPath)).getRegions().get(1));
		
		this.interactionType = interactionType;
		//Set interaction length based on level - faster for higher difficulty
		this.interactionStateLength = (float)(interactionStateLength - (PlayerState.get().getLevel()/2));
		if(this.interactionStateLength < 1) { this.interactionStateLength = 1; }
		
		//Scale sprite based on level - smaller for higher difficulty
		finalScale = WorldSystem.get().getLevelScaleFactor();
		
		this.interactionScaleFactor = finalScale/(float)(interactionStages);
		this.currentScaleFactor = interactionScaleFactor;
		
		
		setSprite(interactor.getStartingX()+35, interactor.getStartingY()+35);		
		this.setDrawable(new TextureRegionDrawable(new TextureRegion(new TextureAtlas(Gdx.files.internal(framesPath)).getRegions().get(2))));
	}
	
	private void setSprite(float xCoord, float yCoord) {

		this.setPosition(xCoord, yCoord);
		this.setTouchable(Touchable.disabled);
		//Set scale to 0
		this.scaleBy(-1);

		GameProperties.get().addActorToStage(this);
	}
	
	public void setAction() {
		scaleAction = Actions.scaleTo(currentScaleFactor, currentScaleFactor, interactionStateLength);
		this.addAction(scaleAction);
		this.isInteracting = true;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	
		if(this.scaleAction != null && this.getActions().size == 0 && currentScaleFactor >= finalScale) {
//			System.out.println("Finished interaction at scale "+currentScaleFactor);
			scaleAction.finish();
			isComplete = true;
			interactionType.complete();
			this.remove();
		}
		else if(this.scaleAction != null && this.getActions().size == 0 && currentScaleFactor < finalScale) {
//			System.out.println("Finished interaction stage at scale "+currentScaleFactor);
			currentScaleFactor += interactionScaleFactor;
			scaleAction = Actions.scaleTo(currentScaleFactor, currentScaleFactor, interactionStateLength);
			this.addAction(scaleAction);
//			System.out.println("Starting interaction stage at scale "+currentScaleFactor);
		}
	}
	
	protected boolean isComplete = false;
	public boolean isComplete() {
		return isComplete;
	}
	
}

