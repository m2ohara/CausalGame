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
	private Label bottomLabel;
	private int voteTypeInt;
	protected Random voteTypeGen = new Random();
	protected static ISetGameSprites setGameSprite;
	protected float followerTypeProb;
	protected int starterX = -1;
	protected State winState;
	protected static int levelWinAmount = 0;
	protected String voteTypeString;
	
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
		font.getData().scale(1f);
		skin.add("default", new LabelStyle(font, Color.BLACK));
		topLabel = new Label("GET "+levelWinAmount+" "+voteTypeString+" VOTES", skin);
		bottomLabel = new Label("TO WIN THE CROWD", skin);
	}
	
	protected void generateVoteType() {
		
		voteTypeInt = voteTypeGen.nextInt(2);
		if(voteTypeInt == 0) {
			voteTypeString = "WHITE";
			winState = State.SUPPORT;
		}
		else {
			voteTypeString = "RED";
			winState = State.OPPOSE;
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
		
//		populateLevelCrowdScreen();
//		setCrowdValidDirections();
	}
	
	protected void setCrowdProperties() {
		starterX = starterX == -1 ? rand.nextInt(WorldSystem.get().getSystemWidth()-1) : starterX;
		followerTypeProb = rand.nextFloat();
	}
	
//	private GameSprite getGameSprite(ISpriteBehaviour type, float x, float y, String framesPath, boolean isActive) {
//		return new GameSprite(type, x, y, framesPath, isActive);
//	}
	
	private void populateLevelCrowdScreen() {
		
		SnapshotArray<Actor> spritesArray = GameProperties.get().getGameSpriteGroup().getChildren();
		spritesArray.shuffle();
		Actor[] sprites = spritesArray.toArray();
		int currentAmount = sprites.length;
		for(int i = 0; i < sprites.length; i++) {
			if (rand.nextFloat() < removalProb && currentAmount > levelWinAmount) {
				if (sprites[i].getName() != "startingGameSprite") {
					sprites[i].setVisible(false);
					if (isValidMemberRemoval()) {
						sprites[i].remove();
						currentAmount--;
					} else {
						sprites[i].setVisible(true);
					}
				}
			}
		}
		
	}
	
	private void setCrowdValidDirections() {
		
		for(GameSprite sprite : GameProperties.get().getGameSprites()) {
//			sprite.setValidOrientations();
		}
		
	}
	
	ArrayList<GameMember> gameMembers = new ArrayList<GameMember>();
	private void setGameMembers() {
		for(int y = 0; y < WorldSystem.get().getSystemHeight(); y++) {
			for(int x = 0; x < WorldSystem.get().getSystemWidth(); x++) {
				if(WorldSystem.get().getMemberFromCoords(x, y) != null && WorldSystem.get().getMemberFromCoords(x, y).isVisible() == true) {
					GameMember member = new GameMember(new Vector2(x , y));
					if(starterCoords.x == x && starterCoords.y == y) {
						member.isFirst = true;
					}
					gameMembers.add(member);
				}
			}
		}
		
		for(GameMember member : gameMembers) {
			member.neighbours = getNeighbouringMembers(member);
		}
	}
	
	private boolean isValidMemberRemoval() {
		
		boolean isValid = false;
		setGameMembers();
//		Set first sprite to current sprite, set as found & get neighbours
		GameMember startMember = getMemberByCoords(starterCoords);
		startMember.isFound = true;

		
		
		GameMember current = startMember;
		Gdx.app.debug("GameGenerator", "Start "+current.coords.x+" "+current.coords.y);
		int foundMembers = 1;
		boolean startingPlacement = true;
		int neighbourIdx = 0;

		while(current.isFirst == false || startingPlacement == true) {
			startingPlacement = false;
			if(current.isFound == false) {
//				Get neighbours, set current as found
				current.isFound = true;
				Gdx.app.debug("GameGenerator", "Found "+current.coords.x+" "+current.coords.y);
				foundMembers++;
				neighbourIdx = 0;
			}
//			Else if current is found && neighbours are searched
			else if(current.isFound && neighbourIdx == current.neighbours.size()) {
//				Get parent & set parent to current
				if(current.coords.x == startMember.coords.x && current.coords.y == startMember.coords.y) {
					break;
				}
				current = current.parentMember;
				Gdx.app.debug("GameGenerator", "Backtracking to "+current.coords.x+" "+current.coords.y);
				neighbourIdx = 0;
			}
			else {
//				Set next neighbour as current
				if(current.neighbours.get(neighbourIdx).isFound == false) {
					GameMember parent = current;
					current = current.neighbours.get(neighbourIdx);
					Gdx.app.debug("GameGenerator", "Checking "+current.coords.x+" "+current.coords.y);
					current.parentMember = parent;
				}
				neighbourIdx++;
			}
		}
		if(foundMembers == gameMembers.size()) {
			isValid = true;
		}
		else {
			isValid = false;
		}
		
		gameMembers.clear();
		
		Gdx.app.debug("GameGenerator", "Is valid "+isValid);
		return isValid;
	}
	
	private ArrayList<GameMember> getNeighbouringMembers(GameMember member) {
		ArrayList<GameMember> neighbourMembers = new ArrayList<GameMember>();
		if(WorldSystem.get().getMemberFromCoords((int)member.coords.x, (int)member.coords.y + 1)!= null) {
			GameMember foundMember = getMemberByCoords(new Vector2(member.coords.x, member.coords.y + 1));
			if(foundMember != null)  {
				neighbourMembers.add(foundMember);
			}
		}
		if(WorldSystem.get().getMemberFromCoords((int)member.coords.x + 1, (int)member.coords.y)!= null ) {
			GameMember foundMember = getMemberByCoords(new Vector2(member.coords.x+1, member.coords.y));
			if(foundMember != null)  {
				neighbourMembers.add(foundMember);
			}
		}
		if(WorldSystem.get().getMemberFromCoords((int)member.coords.x, (int)member.coords.y - 1)!= null ) {
			GameMember foundMember = getMemberByCoords(new Vector2(member.coords.x, member.coords.y - 1));
			if(foundMember != null)  {
				neighbourMembers.add(foundMember);
			}
		}
		if(WorldSystem.get().getMemberFromCoords((int)member.coords.x - 1, (int)member.coords.y)!= null ) {
			GameMember foundMember = getMemberByCoords(new Vector2(member.coords.x - 1, member.coords.y));
			if(foundMember != null)  {
				neighbourMembers.add(foundMember);
			}
		}
		
		return neighbourMembers;
	}
	

	private GameMember getMemberByCoords(Vector2 coords) {
		for(GameMember member : gameMembers) {
			if(member.coords.x == coords.x && member.coords.y == coords.y) {
				return member;
			}
		}
		return null;
	}
	
	class GameMember { 
		public boolean isFound = false;
		public boolean isFirst = false;
		public Vector2 coords =  null;
		public GameMember parentMember = null;
		public ArrayList<GameMember> neighbours = null;
		
		public GameMember(Vector2 coords) {
			this.coords = coords;
		}
	}
	
	private void setRemovalProb() {
		removalProb = ((float)((PlayerState.get().getLevel() / 2)*2)/10); 
	}
	
	private void incrementVoteType(int type) {
		if(type == 0 || type == 2) {
			supportCount += 1;
		}
		if(type == 1 || type == 2) {
			opposeCount += 1;
		}
	}
	
	protected void setLevelWinAmount() {
		
		rand = new Random();
		rand.setSeed(rand.hashCode());
		setWinAmount++;
		int amount = WorldSystem.get().getSystemWidth() * WorldSystem.get().getSystemHeight();
		levelWinAmount = rand.nextInt(amount+PlayerState.get().getLevel())+amount/2;
		
		Gdx.app.debug("GameGenerator", "Win state "+winState.toString()+ " Limit "+amount+ "+"+PlayerState.get().getLevel());
		Gdx.app.debug("GameGenerator", "Support count "+supportCount+ " OpposeCount "+opposeCount);
		Gdx.app.debug("GameGenerator", "Level win amount"+levelWinAmount);
		
		if(setWinAmount < 100) {
			if(winState == State.SUPPORT && levelWinAmount > supportCount) {
				setLevelWinAmount();
			}
			if(winState == State.OPPOSE && levelWinAmount > opposeCount) {
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
	
	public Label getBottomLabel() {
		return bottomLabel;
	}
	
	public State getWinState() {
		return winState;
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
