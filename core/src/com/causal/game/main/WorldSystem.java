package com.causal.game.main;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.causal.game.state.PlayerState;

public class WorldSystem {
	
	private static WorldSystem instance;
	private int baseWidth = 3;
	private int baseHeight = 3;
	private int systemWidth;
	private int systemHeight;
	public static enum Orientation {N, NE, E, SE, S, SW, W, NW}
	
	//Properties related to setting sprites on game screen
	public float headSpriteH;
	public int headSpriteW;
	private float hudYCoord;
	
	private ArrayList<Float> gameXCoords = new ArrayList<Float>();
	private ArrayList<Float>  gameYCoords = new ArrayList<Float>();
	
	private ArrayList<Float> hudXCoords = new ArrayList<Float>();
	private ArrayList<Float> hudYCoords = new ArrayList<Float>();
	
	private Float rewardYCoord;
	
	public static WorldSystem get() {
		if(instance == null) {
			instance = new WorldSystem();
		}
		return instance;
	}
	
	private WorldSystem() {
		
		headSpriteH = Math.round(72f * getLevelScaleFactor());
		headSpriteW = Math.round(72f * getLevelScaleFactor());
		
		setWorldDimensions();
		
		setGameCoords();
		
		setHudCoords();
		
		setYRewardCoord();
	}
	
	private void setGameCoords() {
		float centreX = (Gdx.graphics.getWidth()) / 2; 
		float xSpan = headSpriteW*systemWidth;
		float startX = centreX - (xSpan/2);
		float spanLengthX = startX + xSpan;
		for(float x = startX; x < spanLengthX; x+=headSpriteW) {
			gameXCoords.add(x);
		}
		
		float centreY = ((Gdx.graphics.getHeight()-300) / 2); 
		float ySpan = headSpriteH*systemHeight;
		float startY = centreY + (ySpan/2);
		float spanLengthY = startY - ySpan;
		for(float y = startY; y > spanLengthY; y-=headSpriteH) {
			gameYCoords.add(y);
		}
	}
	
	public ArrayList<Float> getGameXCoords() {
		return gameXCoords;
	}
	
	public ArrayList<Float> getGameYCoords() {
		return gameYCoords;
	}
	
	private void setHudCoords() {
		
		setYHudCoord();
		//Set coordinates for followers in HUD
		int spriteTypeCount = PlayerState.get().getFollowerTypes().size();
		
		float centreX = (Gdx.graphics.getWidth()) / 2; 
		float xSpan = headSpriteW*spriteTypeCount;
		float startX = centreX - (xSpan/2);
		float spanLengthX = startX + xSpan;
		for(float x = startX; x < spanLengthX; x+=headSpriteW) {
			hudXCoords.add(x);
			hudYCoords.add(hudYCoord); 
		}
		
		
	}
	
	private void setYHudCoord() {
		//Start from centre
		float centreY = (Gdx.graphics.getHeight() / 2) - 50; 
		float ySpan = headSpriteH*5;
		float startY = centreY + (ySpan/2);
		float spanLengthY = startY - 370;
		hudYCoord = (float) (spanLengthY - (headSpriteH*0.5));
	}
	
	public ArrayList<Float> getHudXCoords() {
		return hudXCoords;
	}
	
	public ArrayList<Float> getHudYCoords() {
		return hudYCoords;
	}
	
	private void setYRewardCoord() {
		//Start from centre
		float centreY = (Gdx.graphics.getHeight() / 2) - 10; 
		float ySpan = headSpriteH*5;
		float startY = centreY + (ySpan/2);
		float spanLengthY = startY - 370;
		rewardYCoord = (float) (spanLengthY - (headSpriteH*0.5));
	}
	
	public Float getRewardYCoord() {
		return rewardYCoord;
	}


	public void setDirection(GameSprite actor) {
		//TODO: Implement
	}
	
	public static Orientation getCoordDirection(int direction, int angle ) {
		
		if(direction == 1) { //Right
			if(angle == 0) {
				return Orientation.E;
			}
			else if(angle == 90) {
				return Orientation.N;
			}
			else if(angle == 270) {
				return Orientation.S;
			}
			
		}
		else if(direction == 0) { //Left
			if(angle == 0) {
				return Orientation.W;
			}
			else if(angle == 90) {
				return Orientation.S;
			}
			else if(angle == 270) {
				return Orientation.N;
			}
		}
		return null;
	}
	
	public GameSprite getMemberFromCoords(int gameXPos, int gameYPos) {
		
		if(GameProperties.get().getActorGroup() != null) {
			if(gameXPos != -1 && gameXPos < systemWidth && gameYPos != -1 && gameYPos < systemHeight){
				Array<Actor> actors = GameProperties.get().getActorGroup().getChildren();
				for(Actor actor : actors) {
						GameSprite headSprite = (GameSprite)actor;
						if(gameXCoords.indexOf(headSprite.getStartingX()) == gameXPos && gameYCoords.indexOf(headSprite.getStartingY()) == gameYPos)
							return headSprite;
				}
			}
		}
		return null;
	}
	
	public int getSystemWidth() {
		return systemWidth;
	}

	public int getSystemHeight() {
		return systemHeight;
	}
	
	public boolean isValidYCoordinate(int coordinate) {
		if(coordinate >= 0 &&  coordinate < systemHeight)
		{	
			return true;
		}
		return false;
	}
		
	
	public boolean isValidXCoordinate(int coordinate) {
		if(coordinate >= 0 && coordinate < systemWidth)
		{	
			return true;
		}
		return false;
	}
	
	protected void setPositionFromCentre(Actor actorToSet, float _xCentreOffset, float _yCentreOffset) {
		
		//Centre actor
		float x = (Gdx.graphics.getWidth() - actorToSet.getWidth()) /2 ;
		float y = (Gdx.graphics.getHeight()  - actorToSet.getHeight()) / 2;
		
		x += _xCentreOffset;
		y += _yCentreOffset;
		
		actorToSet.setPosition(x, y);
		
		_xCentreOffset = actorToSet.getX();
		_yCentreOffset = actorToSet.getY();
	}
	
	//Determine scaleFactor based on game level for every factor of 3
	public float getLevelScaleFactor() {
		
		float scaleFactor = 1f;
		int numerator = 0;
		int denominator = PlayerState.get().getMaxLevel();
		int level = PlayerState.get().getLevel();
		numerator = level / 3;
		
		scaleFactor = (float)(denominator - numerator) / (float)denominator;
		
		return scaleFactor;
	}
	
	public void setWorldDimensions() {
		
		int level = PlayerState.get().getLevel();
		systemWidth = baseWidth + ((level+1)/2);
		systemHeight = baseHeight + (level/2);
	}
	
	public void dispose() {
		instance = null;
		try {
			this.finalize();
		}
		catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
