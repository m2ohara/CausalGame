package com.causal.game.setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.SnapshotArray;
import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.gestures.DefaultGestures;
import com.causal.game.gestures.SwipeInteraction;
import com.causal.game.interact.IInteractionType;
import com.causal.game.interact.IndividualInteractionType;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.main.SwipeSprite;
import com.causal.game.main.WorldSystem;
import com.causal.game.state.GameScoreState.State;
import com.causal.game.state.Follower;
import com.causal.game.state.FollowerType;
import com.causal.game.state.GameScoreState.VoteState;
import com.causal.game.state.PlayerState;
import com.causal.game.tutorial.TutorialGameGenerator;
import com.causal.game.tutorial.TutorialSwipeInteraction;

public class GameGenerator {
	
	private float removalProb;
	private Vector2 starterCoords;
	private Random rand = new Random();
	private int supportCount = 0;
	private int opposeCount = 0;
	private int setWinAmount = 0;
	private IInteractionType interactionType;
	private Label topLabel;
	private Label middleLabel;
	private Label bottomLabel;
	private int voteTypeInt;
	protected Random voteTypeGen = new Random();
	protected static ISetGameSprites setGameSprite;
	protected float followerTypeProb;
	protected int starterX = -1;
	protected VoteState voteState;
	protected static int levelWinAmount = 0;
	protected String voteTypeString;
	protected String voteResultString;
	
	public GameGenerator() {
		setRemovalProb();
		setGameSprite = new SetGameSprite();
	}
	
	public void setGameVoteRules() {
		
		generateVoteType();
		
		interactionType = new IndividualInteractionType();
		
		GameProperties.get().setSwipeInteraction(PlayerState.get().isFirstGame() ? new TutorialSwipeInteraction(interactionType, voteTypeInt) : new SwipeInteraction(interactionType, voteTypeInt));
		
		populateFullCrowdScreen();
		
		setLevelWinAmount();
		
		final Skin skin = new Skin();
		BitmapFont font = new BitmapFont();
		font.getData().scale(0.9f);
		skin.add("default", new LabelStyle(font, Color.BLACK));
		topLabel = new Label("THE LOCAL LEADER", skin);
		middleLabel = new Label("NEEDS "+levelWinAmount+" "+voteTypeString+" VOTES", skin);
		bottomLabel = new Label("TO "+voteResultString+" THE BILL", skin);
	}
	
	protected void generateVoteType() {
		
		voteTypeInt = voteTypeGen.nextInt(2);
		if(voteTypeInt == 0) {
			voteTypeString = "WHITE";
			voteResultString = "PASS";
			voteState = VoteState.SUPPORT;
		}
		else {
			voteTypeString = "RED";
			voteResultString = "DEFEAT";
			voteState = VoteState.OPPOSED;
		}
	}
	
	private void populateFullCrowdScreen() {
		
		starterCoords = new Vector2(starterX, WorldSystem.get().getSystemHeight()-1);
		for(int x = 0; x < WorldSystem.get().getSystemWidth(); x++) {
			for(int y = 0; y < WorldSystem.get().getSystemHeight(); y++) {
				GameSprite current = null;
				setCrowdProperties();
				current = setGameSprite.createGameSprite(followerTypeProb, x, y);
				if(y == WorldSystem.get().getSystemHeight()-1 && x == starterX) {
					current.interactStatus = Status.SELECTED;
					current.setName("startingGameSprite");
					current.setColor(Color.YELLOW);
					SwipeSprite.get().setStartSprite(current);
				}
				GameProperties.get().addToGameSpriteGroup(current);
				
				if(y == WorldSystem.get().getSystemHeight()-1) { 
					Gdx.app.debug("GameGenerator", "Setting actor at coords "+current.getX()+", "+current.getY()); }
			}
		}
		
		supportCount = setGameSprite.getSupportCount();
		opposeCount = setGameSprite.getOpposeCount();
		
	}
	
	protected void setCrowdProperties() {
		starterX = starterX == -1 ? rand.nextInt(WorldSystem.get().getSystemWidth()-1) : starterX;
		followerTypeProb = rand.nextFloat();
	}
	
	private void setRemovalProb() {
		removalProb = ((float)((PlayerState.get().getLevel() / 2)*2)/10); 
	}
	
	//Bug loop reaches over 100 before meeting condition TEST
	protected void setLevelWinAmount() {
		
		rand = new Random();
		setWinAmount++;
		int amount = (WorldSystem.get().getSystemWidth() * WorldSystem.get().getSystemHeight())-1;
		levelWinAmount = levelWinAmount == 0 ? rand.nextInt(amount+PlayerState.get().getLevel()) + (amount/2) : levelWinAmount-1;
		
		Gdx.app.log("GameGenerator", "Win state "+voteState.toString()+ " Limit "+amount+ "+"+PlayerState.get().getLevel());
		Gdx.app.log("GameGenerator", "Support count "+supportCount+ " OpposeCount "+opposeCount);
		Gdx.app.log("GameGenerator", "Level win amount "+levelWinAmount);
		
		if(setWinAmount < 100) {
			if(voteState == VoteState.SUPPORT && levelWinAmount > supportCount) {
				setLevelWinAmount();
			}
			if(voteState == VoteState.OPPOSED && levelWinAmount > opposeCount) {
				setLevelWinAmount();
			}
			else return;
		}
	}
	
	public int getLevelWinAmount() {
		return levelWinAmount;
	}
	
	public IInteractionType getInteractionType() {
		return interactionType;
	}
	
	public Label getTopLabel() {
		return topLabel;
	}
	
	public Label getMiddleLabel() {
		return middleLabel;
	}
	
	public Label getBottomLabel() {
		return bottomLabel;
	}
	
	public VoteState getVoteState() {
		return voteState;
	}
	
	public List<Follower> generateRewardFollowers(int amount) {	
		
		List<Follower> rewardedFollowers = new ArrayList<Follower>();
		List<FollowerType> types = PlayerState.get().getFollowerTypes();
		
		int count = amount > 3 ? 3 : amount;
		
		Random rand = new Random();
		for(int i =0; i < count; i++) {
			FollowerType type = types.get(rand.nextInt(types.size()));
			rewardedFollowers.add(new Follower(type, 0));
		}
		
		PlayerState.get().addFollowers(rewardedFollowers);
		
		return rewardedFollowers;

	}

}
