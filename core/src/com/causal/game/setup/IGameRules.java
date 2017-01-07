package com.causal.game.setup;

import com.causal.game.state.GameScoreState.State;

public interface IGameRules {
	
	//Setup game rules
	void setup();
	
	//Update game score
	void update();
	
	State getCurrentState();
	
	int getRemainingPoints();
	
	int getEndScore();
	
	State getWinState();

}
