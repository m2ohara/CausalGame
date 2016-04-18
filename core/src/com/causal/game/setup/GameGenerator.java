package com.causal.game.setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.causal.game.main.Game.Head;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.main.WorldSystem;
import com.causal.game.state.FollowerType;
import com.causal.game.state.PlayerState;

public class GameGenerator {
	
	public float removalProb;
	private Vector2 starterCoords;
	public Random rand;
	
	public GameGenerator() {
		rand = new Random();
		setRemovalProb();
	}
	
	public void populateFullCrowdScreen() {
		
		List<FollowerType> types = PlayerState.get().getFollowerTypes();
		Random crowdSetter = new Random();
		int starterX = crowdSetter.nextInt(WorldSystem.get().getSystemWidth()-1);
		starterCoords = new Vector2(starterX, WorldSystem.get().getSystemHeight()-1);
		for(int x = 0; x < WorldSystem.get().getSystemWidth(); x++) {
			for(int y = 0; y < WorldSystem.get().getSystemHeight(); y++) {
				GameSprite current = null;
				float rand = crowdSetter.nextFloat();
				if(rand < 0.33) {
					current = new GameSprite(Head.GOSSIPER, WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), types.get(0).imagePath, true);
				}
				else if(rand >= 0.33 && rand < 0.66) {
					current = new GameSprite(Head.INFLUENCER, WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), types.get(1).imagePath, true);
				}
				else {
					current = new GameSprite(Head.DECEIVER, WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), types.get(2).imagePath, true);
				}
				if(y == WorldSystem.get().getSystemHeight()-1 && x == starterX) {
					current.interactStatus = Status.SELECTED;
					current.setName("startingGameSprite");
					current.setColor(Color.YELLOW);
					GameProperties.get().swipeSprite.setStartSprite(current);
				}
				GameProperties.get().addToActorGroup(current);
				
				if(y == WorldSystem.get().getSystemHeight()-1) { 
					System.out.println("Setting actor at coords "+current.getX()+", "+current.getY()); }
			}
		}
		
		populateLevelCrowdScreen();
		setCrowdValidDirections();
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
//		System.out.println("Start "+current.coords.x+" "+current.coords.y);
		int foundMembers = 1;
		boolean startingPlacement = true;
		int neighbourIdx = 0;

		while(current.isFirst == false || startingPlacement == true) {
			startingPlacement = false;
			if(current.isFound == false) {
//				Get neighbours, set current as found
				current.isFound = true;
//				System.out.println("Found "+current.coords.x+" "+current.coords.y);
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
//				System.out.println("Backtracking to "+current.coords.x+" "+current.coords.y);
				neighbourIdx = 0;
			}
			else {
//				Set next neighbour as current
				if(current.neighbours.get(neighbourIdx).isFound == false) {
					GameMember parent = current;
					current = current.neighbours.get(neighbourIdx);
//					System.out.println("Checking "+current.coords.x+" "+current.coords.y);
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
		
//		System.out.println("Is valid "+isValid);
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
	
	private int levelWinAmount;
	public void setLevelWinAmount(int levelWinAmount) {
		this.levelWinAmount = levelWinAmount;
	}
	
	private void setRemovalProb() {
		removalProb = ((float)((PlayerState.get().getLevel() / 2)*2)/10); 
	}

}
