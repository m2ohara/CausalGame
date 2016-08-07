package com.causal.game.tutorial;

import java.util.Arrays;
import java.util.List;

import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.main.GameSprite;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.setup.GameGenerator;
import com.causal.game.state.GameScoreState.State;

public class TutorialGameGenerator extends GameGenerator {
	
	private static final double YELLOW = 0.1;
	private static final double BLUE = 0.34;
	private static final double RED = 1;
	public static int Round = -1;
	private static TutorialGameGenerator instance;
	
	private int idx = 0;
	private List<Float> followerTypeProbList = Arrays.asList(
			new Float(YELLOW), new Float(RED), new Float(BLUE), 
			new Float(YELLOW), new Float(BLUE), new Float(RED), 
			new Float(YELLOW), new Float(BLUE), new Float(RED));
	
	public static TutorialGameGenerator get() {
		if(instance == null) {
			instance = new TutorialGameGenerator();
		}
		Round+=1;
		return instance;
	}
	
	private TutorialGameGenerator() {
		super();
		setGameSprite = new SetTutorialGameSprite();
	}

	protected void setCrowdProperties() {
		starterX = 1;
		followerTypeProb = followerTypeProbList.get(idx);
		idx++;
	}
	
	protected void generateVoteType(int voteType) {
		if(voteType == 0) {
			voteTypeString = "WHITE";
			winState = State.SUPPORT;
		}
		else {
			voteTypeString = "RED";
			winState = State.OPPOSE;
		}
	}
	
	public void setLevelWinAmount() {
		levelWinAmount = 4;
	}

}
