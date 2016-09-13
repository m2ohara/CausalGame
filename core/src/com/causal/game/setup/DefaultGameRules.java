package com.causal.game.setup;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.InfluenceType;
import com.causal.game.state.GameScoreState.State;

public class DefaultGameRules implements IGameRules {
	
	public State currentState = State.PLAYING;
	
	private Group actors = null;
	private int totalPoints = 0;
	
	public DefaultGameRules(int totalPoints) {
		this.totalPoints = totalPoints;
		setup();
	}

	@Override
	public void setup() {
		this.actors = GameProperties.get().getGameSpriteGroup();
	}

	@Override
	public void update() {

		int forPoints = 0;
		int againstPoints = 0;
		
		for(Actor a : actors.getChildren()) {
			GameSprite actor = (GameSprite) a;
			if(actor.influenceType == InfluenceType.SUPPORT) {
				forPoints+=1;
			}
			else if(actor.influenceType == InfluenceType.OPPOSE) {
				againstPoints+=1;
			}
		}
		
		setZeroSumGameState(forPoints, againstPoints); 
		
	}
	
	private void setZeroSumGameState(int forPoints, int againstPoints) {
		if(forPoints > (againstPoints + (totalPoints - (forPoints + againstPoints)))) {
			currentState =  State.SUPPORT;
		}
		else if(againstPoints > (forPoints + (totalPoints - (forPoints + againstPoints)))) {
			//Loss
			currentState =  State.OPPOSE;
		}
		else if((forPoints + againstPoints == totalPoints) && forPoints == againstPoints) {
			//Draw
			currentState =  State.DRAW;
		}
		else {
			currentState =  State.PLAYING;
		}
		
	}

	@Override
	public State getCurrentState() {
		return currentState;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRemainingPoints() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEndScore() {
		// TODO Auto-generated method stub
		return 0;
	}

}
