package com.causal.game.tutorial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.causal.game.animation.TutorialTapSprite;
import com.causal.game.main.GameProperties;
import com.causal.game.main.WorldSystem;
import com.causal.game.setup.GameGenerator;
import com.causal.game.sprite.DropSprite;
import com.causal.game.sprite.GameSprite;
import com.causal.game.state.Follower;
import com.causal.game.state.FollowerType;
import com.causal.game.state.GameScoreState.State;
import com.causal.game.state.GameScoreState.VoteState;
import com.causal.game.state.PlayerState;

public class TutorialGameGenerator extends GameGenerator {
	
	private static final double YELLOW = 0.1;
	private static final double BLUE = 0.34;
	private static final double RED = 1;
	public static int Round = -1;
	private static TutorialGameGenerator instance;
	
	private static int idx = 0;
	private List<Float> followerTypeProbList1 = Arrays.asList(
			new Float(RED), new Float(RED), new Float(BLUE), 
			new Float(BLUE), new Float(BLUE), new Float(RED), 
			new Float(BLUE), new Float(BLUE), new Float(RED));
	
	private List<Float> followerTypeProbList2 = Arrays.asList(
			new Float(YELLOW), new Float(RED), new Float(BLUE),
			new Float(BLUE), new Float(YELLOW), new Float(YELLOW), 
			new Float(RED), new Float(BLUE), new Float(RED), 
			new Float(BLUE), new Float(YELLOW), new Float(YELLOW)
			);
	
	@SuppressWarnings("unchecked")
	private List<List<Float>> followerTypeProbListRounds = Arrays.asList(followerTypeProbList1, followerTypeProbList2);
	
	private List<Integer> followerType = Arrays.asList(0);
	private static List<Integer> winAmount = Arrays.asList(4, 7);
	
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
		setGameSprite.resetIndex();
		
	}
	
	private TutorialGameGenerator() {
		super();
		setGameSprite = new SetTutorialGameSprite();
	}

	protected void setCrowdProperties() {
		Gdx.app.debug("TutorialGameGenerator", "Setting crowd properties for index "+idx);
		starterX = 1;
		followerTypeProb = followerTypeProbListRounds.get(Round).get(idx);
		idx++;
	}
	
	protected void generateVoteType(int voteType) {
		if(voteType == 0) {
			voteTypeString = "WHITE";
			voteState = VoteState.SUPPORT;
		}
		else {
			voteTypeString = "RED";
			voteState = VoteState.OPPOSED;
		}
	}
	
	public void setLevelWinAmount() {
		levelWinAmount = winAmount.get(Round);
	}
	
	public void createDropSprites(ArrayList<DropSprite> followers, ArrayList<Image> placeHolders) {
		final List<Follower> plFollowers = PlayerState.get().getFollowers();
		List<FollowerType> types = PlayerState.get().getFollowerTypes();
		
		for(int i = 0; i < types.size(); i++) {
			Image placeHolder = (Image)createTargetImage("icons/iconsPack",WorldSystem.get().getHudXCoords().get(i), WorldSystem.get().getHudYCoords().get(i));
			placeHolders.add(placeHolder);
			for(Follower follower : plFollowers) {
				if(follower.type.head == types.get(i).head) {
					DropSprite followerInstance = new TutorialDropSprite(follower, WorldSystem.get().getHudXCoords().get(i), WorldSystem.get().getHudYCoords().get(i), placeHolder);
					followers.add(followerInstance);
				}
			}
		}
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
