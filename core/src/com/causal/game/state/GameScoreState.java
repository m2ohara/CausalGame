package com.causal.game.state;

import com.causal.game.setup.IGameRules;
import com.causal.game.setup.VoteGameRules;

public class GameScoreState {
	
	public enum State {PLAYING, WIN, LOSE, DRAW, NOTPLAYING, FINISHED}
	public enum VoteState {SUPPORT, OPPOSED}
	
	static int totalPoints = 0;
	private static int touchActionPoints = 4;//TODO: Add to DB
	private static int userPoints = touchActionPoints;
	private IGameRules scoreSystem = null;
	private VoteState voteState;

	public GameScoreState(int winVotes, VoteState voteState, int totalVotes) {
		scoreSystem = new VoteGameRules(voteState, winVotes, totalVotes);
		userPoints = touchActionPoints;
		this.voteState = voteState;
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
		return scoreSystem.getWinState();
	}
	
	public VoteState getVoteState() {
		return this.voteState;
	}

}
