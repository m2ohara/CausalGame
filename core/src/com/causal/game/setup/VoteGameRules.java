package com.causal.game.setup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.InfluenceType;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.state.GameScoreState.State;

public class VoteGameRules implements IGameRules {
	
	private State currentState = null;
	private Group actors = null;
	private int winVotes;
	private State winState;
	private int totalVotes = 0;
	private int remainingVotes = 0;
	private int scoreWinMultiplier = 100;
	private int scoreLoseMultiplier = 50;
	private int waitTime = 300;
	
	public VoteGameRules(State winState, int winVotes, int totalVotes) {
		this.winVotes = winVotes;
		this.winState = winState;
		this.totalVotes = totalVotes;
		this.remainingVotes = winVotes;
		setup();
	}

	@Override
	public void setup() {
		this.currentState = State.PLAYING;
		this.actors = GameProperties.get().getActorGroup();
	}

	@Override
	public void update() {
		
		if(currentState != State.FINISHED) {
			calculateVotes();
		}
		else if(currentState == State.SUPPORT || currentState == State.OPPOSE || currentState == State.DRAW) {
			currentState = State.NOTPLAYING;
		}
		
	}
	
	private void calculateVotes() {

		int forVotes = 0;
		int againstVotes = 0;
		
		for(Actor a : actors.getChildren()) {
			GameSprite actor = (GameSprite) a;
			if((actor.interactStatus == Status.INFLUENCED || actor.interactStatus == Status.SELECTED) && actor.influenceType == InfluenceType.SUPPORT) {
				forVotes+=1;
			}
			else if((actor.interactStatus == Status.INFLUENCED || actor.interactStatus == Status.SELECTED) && actor.influenceType == InfluenceType.OPPOSE) {
				againstVotes+=1;
			}
		}
		
		setGameState(forVotes, againstVotes);
		
		updateRemainingVotes(forVotes, againstVotes);
		
		checkIsFinished(forVotes, againstVotes);
	}
	
	private void checkIsFinished(int forVotes, int againstVotes) {
		Gdx.app.debug("", "F "+forVotes+", A "+againstVotes+", T "+totalVotes);	
		if(forVotes + againstVotes == totalVotes) {
			setEndScore(forVotes, againstVotes);
			
			if(waitTime == 0) {
				currentState = State.FINISHED;
			}
			else {
				waitTime--;
			}
			
		}
		
	}
	
	private void setGameState(int forVotes, int againstVotes) {
		
		if(currentState == State.PLAYING) {
			if(winState == State.SUPPORT) {
	
				if(forVotes >= winVotes) {
					currentState =  State.SUPPORT;
				}
				else if((totalVotes - (forVotes + againstVotes)) < (winVotes - forVotes)) {
					currentState =  State.OPPOSE;
				}
				endState = currentState;
			}
			else if(winState == State.OPPOSE) {
				if(againstVotes >= winVotes) {
					currentState =  State.SUPPORT;
				}
				else if((totalVotes - (forVotes + againstVotes)) < (winVotes - againstVotes)) {
					currentState =  State.OPPOSE;
				}
				endState = currentState;
			}
		}
		else {
			currentState = State.NOTPLAYING;
		}
		
	}
	
	private void updateRemainingVotes(int forVotes, int againstVotes) {
		if(winState == State.SUPPORT) {
			remainingVotes = winVotes - forVotes;
		}
		else if(winState == State.OPPOSE) {
			remainingVotes = winVotes - againstVotes;
		}
	}
	
	@Override
	public int getRemainingPoints() {
		return remainingVotes;
	}

	@Override
	public State getCurrentState() {
		return currentState;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	private State endState = null;
	private int endScore = 0;
	private void setEndScore(int forVotes, int againstVotes) {
		if(winState == State.SUPPORT) {
			if(endState == State.SUPPORT) {
				endScore = scoreWinMultiplier * forVotes;
			}
			else {
				endScore = scoreLoseMultiplier * forVotes;
			}
		}
		else if(winState == State.OPPOSE) {
			if(endState == State.SUPPORT) {
				endScore = scoreWinMultiplier * againstVotes;
			}
			else {
				endScore = scoreLoseMultiplier * againstVotes;
			}
		}
	}

	@Override
	public int getEndScore() {
		return endScore;
	}
	

}
