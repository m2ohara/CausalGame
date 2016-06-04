package com.causal.game.setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.causal.game.behaviour.DeceiverBehaviour;
import com.causal.game.behaviour.GossiperBehaviour;
import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.behaviour.PromoterBehaviour;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.main.WorldSystem;
import com.causal.game.state.FollowerType;
import com.causal.game.state.GameScoreState.State;
import com.causal.game.state.PlayerState;

public class GameGenerator {
	
	public float removalProb;
	private Vector2 starterCoords;
	public Random rand;
	private int supportCount = 0;
	private int opposeCount = 0;
	private int levelWinAmount = 0;
	private int setWinAmount = 0;
	protected float followerTypeProb;
	protected int starterX = -1;
	
	public GameGenerator() {
		rand = new Random();
		setRemovalProb();
	}
	
	public void populateFullCrowdScreen() {
		
		List<FollowerType> types = PlayerState.get().getFollowerTypes();
		starterCoords = new Vector2(starterX, WorldSystem.get().getSystemHeight()-1);
		for(int x = 0; x < WorldSystem.get().getSystemWidth(); x++) {
			for(int y = 0; y < WorldSystem.get().getSystemHeight(); y++) {
				GameSprite current = null;
				setCrowdProperties();
				if(followerTypeProb < 0.33) {
					current = getGameSprite(new GossiperBehaviour(), WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), types.get(0).imagePath, true);
					incrementVoteType(2);
				}
				else if(followerTypeProb >= 0.33 && followerTypeProb < 0.66) {
					current = getGameSprite(new PromoterBehaviour(), WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), types.get(1).imagePath, true);
					incrementVoteType(0);
				}
				else {
					current = getGameSprite(new DeceiverBehaviour(), WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), types.get(2).imagePath, true);
					incrementVoteType(1);
				}
				if(y == WorldSystem.get().getSystemHeight()-1 && x == starterX) {
					current.interactStatus = Status.SELECTED;
					current.setName("startingGameSprite");
					current.setColor(Color.YELLOW);
					GameProperties.get().swipeSprite.setStartSprite(current);
				}
				GameProperties.get().addToActorGroup(current);
				
				if(y == WorldSystem.get().getSystemHeight()-1) { 
					Gdx.app.debug("GameGenerator", "Setting actor at coords "+current.getX()+", "+current.getY()); }
			}
		}
		
//		populateLevelCrowdScreen();
		setCrowdValidDirections();
	}
	
	public void setCrowdProperties() {
		starterX = starterX == -1 ? rand.nextInt(WorldSystem.get().getSystemWidth()-1) : starterX;
		followerTypeProb = rand.nextFloat();
	}
	
	public GameSprite getGameSprite(ISpriteBehaviour type, float x, float y, String framesPath, boolean isActive) {
		return new GameSprite(type, x, y, framesPath, isActive);
	}
	
	public void populateLevelCrowdScreen() {
		
		SnapshotArray<Actor> spritesArray = GameProperties.get().getActorGroup().getChildren();
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
			sprite.setValidOrientations();
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
	
	public boolean isValidMemberRemoval() {
		
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
	
	public void setLevelWinAmount(State winState) {
		
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
				setLevelWinAmount(winState);
			}
			if(winState == State.OPPOSE && levelWinAmount > opposeCount) {
				setLevelWinAmount(winState);
			}
			else return;
		}
	}
	
	public int getLevelWinAmount() {
		return levelWinAmount;
	}

}
