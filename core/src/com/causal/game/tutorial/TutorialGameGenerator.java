package com.causal.game.tutorial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.causal.game.setup.GameGenerator;
import com.causal.game.state.GameScoreState.State;
import com.causal.game.state.Follower;
import com.causal.game.state.FollowerType;
import com.causal.game.state.PlayerState;

public class TutorialGameGenerator extends GameGenerator {
	
	private static final double YELLOW = 0.1;
	private static final double BLUE = 0.34;
	private static final double RED = 1;
	public static int Round = -1;
	private static TutorialGameGenerator instance;
	
	private static int idx = 0;
	private List<Float> followerTypeProbList = Arrays.asList(
			new Float(YELLOW), new Float(RED), new Float(BLUE), 
			new Float(YELLOW), new Float(BLUE), new Float(RED), 
			new Float(YELLOW), new Float(BLUE), new Float(RED));
	
	private List<Integer> followerType = Arrays.asList(0);
	private static List<Integer> winAmount = Arrays.asList(4, 6);
	
	public static TutorialGameGenerator get() {
		if(instance == null) {
			instance = new TutorialGameGenerator();
		}
		
		SetRound();
		
		return instance;
	}
	
	private static void SetRound() {
		Round+=1;
		idx = 0;
		levelWinAmount = winAmount.get(Round);
		
	}
	
	private TutorialGameGenerator() {
		super();
		setGameSprite = new SetTutorialGameSprite();
	}

	protected void setCrowdProperties() {
		Gdx.app.log("TutorialGameGenerator", "Setting crowd properties for index "+idx);
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
		levelWinAmount = winAmount.get(Round);
	}
	
	public List<Follower> generateRewardFollowers(int amount) {	
		
		List<Follower> rewardedFollowers = new ArrayList<Follower>();
		List<FollowerType> types = PlayerState.get().getFollowerTypes();
		
		int count = amount > 3 ? 3 : amount;
		
		for(int i =0; i < count; i++) {
			FollowerType type = types.get(followerType.get(0));
			rewardedFollowers.add(new Follower(type, 0));
		}
		
		PlayerState.get().addFollowers(rewardedFollowers);
		
		return rewardedFollowers;

	}

}
