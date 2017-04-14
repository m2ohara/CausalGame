package com.causal.game.setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.causal.game.gestures.SwipeInteraction;
import com.causal.game.interact.IInteractionType;
import com.causal.game.interact.IndividualInteractionType;
import com.causal.game.main.GameProperties;
import com.causal.game.main.WorldSystem;
import com.causal.game.sprite.GameSprite;
import com.causal.game.sprite.GameSprite.Status;
import com.causal.game.sprite.SwipeSprite;
import com.causal.game.state.Follower;
import com.causal.game.state.FollowerType;
import com.causal.game.state.GameScoreState.VoteState;
import com.causal.game.state.PlayerState;
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
	protected String bottomLabelString;
	
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
		font.getData().scale(0.6f);
		skin.add("default", new LabelStyle(font, Color.BLACK));
		topLabel = new Label("STAR COMMAND NEEDS", skin);
		middleLabel = new Label(""+levelWinAmount+" "+voteResultString, skin);
		bottomLabel = new Label(""+bottomLabelString, skin);
	}
	
	protected void generateVoteType() {
		
		voteTypeInt = voteTypeGen.nextInt(2);
		if(voteTypeInt == 0) {
			voteTypeString = "BLUE";
			voteResultString = "BOOST COMMUNICATORS";
			bottomLabelString = " TO ENABLE THE MOTION";
			voteState = VoteState.SUPPORT;
		}
		else {
			voteTypeString = "RED";
			voteResultString = "PLANETARY JAMMERS TO";
			bottomLabelString = "PREVENT THE MOTION";
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
		
		int voteCount = voteState.toString() == "SUPPORT" ? supportCount : opposeCount;
		int offset = PlayerState.get().getLevel() + ((voteCount/4)*3);
		
		Gdx.app.log("GameGenerator", "Win state "+voteState.toString()+ " Limit "+voteCount+ " Offset "+offset);
		Gdx.app.log("GameGenerator", "Support count "+supportCount+ " OpposeCount "+opposeCount);
		
		levelWinAmount = rand.nextInt(offset >= voteCount ? voteCount : voteCount - offset) + (offset > voteCount ? 0 : offset);
		
		Gdx.app.log("GameGenerator", "Level win amount "+levelWinAmount);

	
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
