package com.causal.game.state;

import com.causal.game.setup.IGameRules;
import com.causal.game.setup.VoteGameRules;

public class GameScoreState {
	
	public enum State {PLAYING, WIN, LOSE, DRAW, NOTPLAYING, FINISHED}
	
	static int totalPoints = 0;
	private static int touchActionPoints = 4;//TODO: Add to DB
	private static int userPoints = touchActionPoints;
	private IGameRules scoreSystem = null;
	private State winState = null;

	public GameScoreState(int winVotes, State winState, int totalVotes) {
		scoreSystem = new VoteGameRules(winState, winVotes, totalVotes);
		userPoints = touchActionPoints;
		winState = this.winState;
	}
	
	public static int getTouchActionPoints() {
		return touchActionPoints;
	}
	
	public static boolean validTouchAction() {
		if(userPoints >= touchActionPoints) {
			return true;
		}
		return false;
	}

	public static void addUserPoints(int _userPoints) {
		userPoints += _userPoints;
	}
	
	public static int getUserPoints() {
		return userPoints;
	}
	
	public static void resetUserPoints() {
		userPoints = 0;
	}
	
	public void update() {
		scoreSystem.update();
	}
	
	public State getCurrentState() {
		return scoreSystem.getCurrentState();
	}
	
	public int getRemaingVotes() {
		return scoreSystem.getRemainingPoints();
	}
	
	public int getEndScore() {
		return scoreSystem.getEndScore();
	}
	
	public State getWinState() {
		return this.winState;
	}

}
