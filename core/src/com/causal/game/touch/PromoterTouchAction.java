package com.causal.game.touch;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.causal.game.interact.IInteractionType;
import com.causal.game.interact.SupporterInteractionType;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.state.GameScoreState;

public class PromoterTouchAction extends TouchAction {

	private IInteractionType manInteraction = null;
	
	public PromoterTouchAction(IInteractionType manInteraction, int x, int y) {
		super(x, y);
		this.manInteraction = new SupporterInteractionType();
		this.setInteractorDir(WorldSystem.getCoordDirection(0, 0));
	}
	
	@Override
	protected void generateValidInteractees() {
		
		if(this.getInteractorDir() != null) {
			int origX = this.getInteractorX();
			int origY = this.getInteractorY();
			
			interactor = WorldSystem.get().getMemberFromCoords(origX, origY);
			
			Random rand = new Random();
			
			//Determine direction
			if(this.getInteractorDir() == Orientation.N) {
				//Set related coordinates for valid interactees
				if(WorldSystem.get().isValidYCoordinate(origY-1)) {
					validYCoords.add(origY-1);
					validYCoords.add(origY-1);
					
					float randVal = rand.nextFloat();
					setValidXCoords(randVal, origX);

				}
			}
			
			if(this.getInteractorDir() == Orientation.E) {
				//Set related coordinates for valid interactees
				if(WorldSystem.get().isValidXCoordinate(origX+1)) {
					validXCoords.add(origX+1);
					validXCoords.add(origX+1);
					
					float randVal = rand.nextFloat();
					setValidYCoords(randVal, origY);
				}
			}
			
			if(this.getInteractorDir() == Orientation.S) {
				//Set related coordinates for valid interactees
				if(WorldSystem.get().isValidYCoordinate(origY+1)) {
					validYCoords.add(origY+1);
					validYCoords.add(origY+1);
					
					float randVal = rand.nextFloat();
					setValidXCoords(randVal, origX);
				}
			}
			
			if(this.getInteractorDir() == Orientation.W) {
				//Set related coordinates for valid interactees
				if(WorldSystem.get().isValidXCoordinate(origX-1)) {
					validXCoords.add(origX-1);
					validXCoords.add(origX-1);
					
					float randVal = rand.nextFloat();
					setValidYCoords(randVal, origY);
				} 
			}
		}
		
		removeEmptyCoordinates();
		
	}
	
	private void setValidXCoords(float randVal, int origX) {
		if(randVal < 0.33) {
			if(WorldSystem.get().isValidXCoordinate(origX-1)) {
				validXCoords.add(origX-1);
				validXCoords.add(origX);
			}
			else {
				validXCoords.add(origX+1);
				validXCoords.add(origX);
			}
		}
		else if(randVal >= 0.33 && randVal <= 0.66) {
			validXCoords.add(origX);
			if(WorldSystem.get().isValidXCoordinate(origX-1)) {
				validXCoords.add(origX-1);
			}
			else {
				validXCoords.add(origX+1);
			}
		}
		else {
			if(WorldSystem.get().isValidXCoordinate(origX+1)) {
				validXCoords.add(origX+1);
				validXCoords.add(origX);
			}
			else {
				validXCoords.add(origX-1);
				validXCoords.add(origX);
			}
		}
	}
	
	private void setValidYCoords(float randVal, int origY) {
		if(randVal < 0.33) {
			if(WorldSystem.get().isValidYCoordinate(origY-1)) {
				validYCoords.add(origY-1);
				validYCoords.add(origY);
			}
			else {
				validYCoords.add(origY+1);
				validYCoords.add(origY);
			}
		}
		else if(randVal >= 0.33 && randVal <= 0.66) {
			validYCoords.add(origY);
			if(WorldSystem.get().isValidYCoordinate(origY-1)) {
				validYCoords.add(origY-1);
			}
			else {
				validYCoords.add(origY+1);
			}
		}
		else {
			if(WorldSystem.get().isValidXCoordinate(origY+1)) {
				validYCoords.add(origY+1);
				validYCoords.add(origY);
			}
			else {
				validYCoords.add(origY-1);
				validYCoords.add(origY);
			}
		}
	}
	
	@Override
	public void onAction() {
		
		if(isSelectedInteractor()) {
			//Generate current crowd members that can be influenced
			generateValidInteractees();
			
			if(validXCoords.size() > 0) {
				manInteraction.setStatus();
				setConnectorSprite(interactor);
				for(int i = 0; i < validXCoords.size(); i++) {
					GameSprite actor = WorldSystem.get().getMemberFromCoords(validXCoords.get(i), validYCoords.get(i));
					if(i == validXCoords.size()-1) {
						setToLastFollower(actor);
					}
					else {
						manInteraction.setStatus();
					}
					
				}
			}
			
			GameScoreState.resetUserPoints();
		}
		
	}
	
	private void setToLastFollower(GameSprite actor) {
		actor.setColor(Color.YELLOW);
		actor.interactStatus = Status.SELECTED;
	}
	
	private void setConnectorSprite(GameSprite lastHitActor) {
		
		Actor connector = new Image(new TextureAtlas(Gdx.files.internal("sprites//connectorPack.pack")).getRegions().get(1));

		connector.setOrigin(connector.getWidth()/2, connector.getHeight()/2);
		connector.setPosition(lastHitActor.getStartingX() - 20, lastHitActor.getStartingY() -22);
		
		if(this.getInteractorDir() == Orientation.E) {
			connector.rotateBy(270);
		}
		else if(this.getInteractorDir() == Orientation.S) {
			connector.rotateBy(180);
		}
		else if(this.getInteractorDir() == Orientation.W) {
			connector.rotateBy(90);
		}
		
		
		GameProperties.get().addActorToStage(connector);
	}


}
