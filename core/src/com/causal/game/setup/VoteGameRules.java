package com.causal.game.setup;

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
		else if(currentState == State.WIN || currentState == State.LOSE || currentState == State.DRAW) {
			currentState = State.NOTPLAYING;
		}
		
	}
	
	private void calculateVotes() {

		int forVotes = 0;
		int againstVotes = 0;
		
		for(Actor a : actors.getChildren()) {
			GameSprite actor = (GameSprite) a;
//			if(actor.interactStatus == Status.SELECTED) {
//				if(winState == State.WIN) {
//					forVotes+=1;
//				}
//				if(winState == State.LOSE) {
//					againstVotes+=1;
//				}			
//			}
			if(actor.influenceType == InfluenceType.SUPPORT) {
				forVotes+=1;
			}
			else if(actor.influenceType == InfluenceType.OPPOSE) {
				againstVotes+=1;
			}
		}
		
		setGameState(forVotes, againstVotes);
		
		updateRemainingVotes(forVotes, againstVotes);
		
		checkIsFinished(forVotes, againstVotes);
	}
	
	private void checkIsFinished(int forVotes, int againstVotes) {
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
			if(winState == State.WIN) {
	
				if(forVotes >= winVotes) {
					currentState =  State.WIN;
				}
				else if((totalVotes - (forVotes + againstVotes)) < (winVotes - forVotes)) {
					currentState =  State.LOSE;
				}
				endState = currentState;
			}
			else if(winState == State.LOSE) {
				if(againstVotes >= winVotes) {
					currentState =  State.WIN;
				}
				else if((totalVotes - (forVotes + againstVotes)) < (winVotes - againstVotes)) {
					currentState =  State.LOSE;
				}
				endState = currentState;
			}
		}
		else {
			currentState = State.NOTPLAYING;
		}
		
	}
	
	private void updateRemainingVotes(int forVotes, int againstVotes) {
		if(winState == State.WIN) {
			remainingVotes = winVotes - forVotes;
		}
		else if(winState == State.LOSE) {
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
		if(winState == State.WIN) {
			if(endState == State.WIN) {
				endScore = scoreWinMultiplier * forVotes;
			}
			else {
				endScore = scoreLoseMultiplier * forVotes;
			}
		}
		else if(winState == State.LOSE) {
			if(endState == State.WIN) {
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
