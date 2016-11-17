package com.causal.game.main;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.causal.game.gestures.ISwipeInteraction;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.state.PlayerState;

public class GameProperties {

	private static GameProperties instance;

	private float universalTimeRatio = 0.7f;
	public boolean isAutoInteractionAllowed = false;
	public ISwipeInteraction swipeInteraction = null;
	private ArrayList<GameSprite> gameSprites = new ArrayList<GameSprite>();
	private int tapLimit;
	private Stage stage = null;
	private int swipeCount;
	private int swipeLimit;
	private Group gameSpriteGroup = new Group();
	private Group actorGroup = new Group();
	private int[] playerGoals = new int[2];

	private GameProperties() {
		tapLimit = PlayerState.get().getTapLimit();
		swipeLimit = PlayerState.get().getInfluenceLimit();
		
	}

	public static GameProperties get() {
		if(instance == null) {
			instance = new GameProperties();
		}

		return instance;
	}
	
	public void setSwipeInteraction(ISwipeInteraction swipeInteraction) {
		this.swipeInteraction = swipeInteraction;
	}
	
	public ISwipeInteraction getSwipeInteraction() {
		return this.swipeInteraction;
	}

	public float getUniversalTimeRatio() {
		return universalTimeRatio;
	}

	public Group getGameSpriteGroup() {
		return gameSpriteGroup;
	}

	public ArrayList<GameSprite> getGameSprites() {
		gameSprites.clear();
		Actor[] actors = ((Actor[])gameSpriteGroup.getChildren().toArray());
		for(Actor actor : actors) {
			gameSprites.add((GameSprite) actor);
		}
		return gameSprites;	
	}

	public void addToGameSpriteGroup(Actor actor) {
		this.gameSpriteGroup.addActor(actor);
	}
	
	public Group getActorGroup() {
		return actorGroup;
	}
	
	public void addToActorGroup(Actor actor) {
		this.actorGroup.addActor(actor);
	}

	public List<MoveableSprite> actorsToReplace = new ArrayList<MoveableSprite>();

	public void replaceActorInGroup(MoveableSprite actor) {
		
		//Ensure HeadSprite actor gets hit
		setActorGroupOriginToZero();
		actor.getSourceSprite().setTouchable(Touchable.disabled);
		Gdx.app.debug("GameProperties", "Replacing actor at "+actor.getCurrentX()+", "+actor.getCurrentY());
		Actor actorToRemove = stage.hit(actor.getCurrentX(), actor.getCurrentY(), true);
		try {
			//Remove current actor at coordinates
			gameSpriteGroup.removeActor(actorToRemove);
			actorToRemove.remove();

			GameSprite actorToAdd = new GameSprite(actor.getBehaviour(), actor.getCurrentX(), actor.getCurrentY(), actor.getFramesPath(), false);
			Gdx.app.debug(this.toString().substring(this.toString().lastIndexOf(".")), 
					"Replaced actor "+((GameSprite)actorToRemove).hashCode()+" with actor "+actorToAdd.hashCode());
//			actorToAdd.setValidOrientations();
			if(((GameSprite)actorToRemove).interactStatus == Status.SELECTED) {
				actorToAdd.interactStatus = Status.SELECTED;
				actorToAdd.setColor(Color.YELLOW);
				SwipeSprite.get().setStartSprite(actorToAdd);
			}
			gameSpriteGroup.addActor(actorToAdd);
			//Remove placeholder
			actor.getSourceSprite().remove();
			actor.getTargetImage().remove();
			
		}
		catch(Exception ex) {
			Gdx.app.error("GameProperties", "Exception replacing actor on stage "+ex);
		}
		setActorGroupOriginToCentre();
	}
	
	//Hack for hitting scaled actors. NB always reset to centre when finished hit
	private void setActorGroupOriginToZero() {
		for(Actor actor : gameSpriteGroup.getChildren()) {
			actor.setOrigin(0f, 0f);
		}
	}
	
	private void setActorGroupOriginToCentre() {
		for(Actor actor : gameSpriteGroup.getChildren()) {
			actor.setOrigin(actor.getWidth()/2, actor.getHeight()/2);
		}
	}
	
	private int tapCount = 0;
	private ArrayList<Integer> tappedObjects = new ArrayList<Integer>();
	public void updateTapCount(int tappedObj) {
		if(!isAutoInteractionAllowed && tapCount < tapLimit && !(tappedObjects.contains(tappedObj))) {
			Gdx.app.debug("GameProperties", "Updating tap count from "+tapCount+". Can interact "+isAutoInteractionAllowed);
			tapCount++;
			tappedObjects.add(tappedObj);
		}
	}
	
	public void resetTapCount() {
		tapCount = 0;
		tappedObjects.clear();
	}
	
	public boolean isTapAllowed(int tappedObj) {
		if(tapCount < tapLimit) {
			return true;
		}
		else if(tappedObjects.contains(tappedObj)) {
			return true;
		}
		
		return false;
	}
	
	public int getTapCount() {
		return tapLimit - tapCount;
	}
	
	public void updateSwipeCount(int swipe) {
		swipeCount += swipe;
	}
	
	public void resetSwipeCount() {
		swipeCount = 0;;
	}
	
	public int getSwipeCount() {
		return swipeLimit - swipeCount;
	}

	public void dispose() {
		stage.clear();
		gameSpriteGroup = new Group();
		actorsToReplace = new ArrayList<MoveableSprite>();
		gameSprites.clear();
		isAutoInteractionAllowed = false;
	}
	
	//Encapsulated stage logic
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public Actor getGameScreenActor(String name) {
		for(Actor actor : stage.getActors()) {
			if(actor.getName() == name) {
				return actor;
			}
		}
		return null;
	}

	public void addActorToStage(Actor actor) {
		this.stage.addActor(actor);
	}
	
	public void removeAllActorsFromStage(Array<Actor> actors) {
		stage.getActors().removeAll(actors, false);
	}
	
	public void resizeStage(int height, int width) {
		stage.getViewport().update(width, height, true);
	}
	
	public void renderStage() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.draw();
		stage.act(Gdx.graphics.getDeltaTime());
	}
	
	public void activateActors() {
		stage.addActor(actorGroup);
	}
	
	public void setPlayerGoals(int[] playerGoals) {
		this.playerGoals = playerGoals;
	}
	
	public int[] getPlayerGoals() {
		return this.playerGoals;
	}



}
