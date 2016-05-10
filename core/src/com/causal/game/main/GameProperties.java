package com.causal.game.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.causal.game.interact.SwipeInteraction;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.state.PlayerState;

public class GameProperties {

	private static GameProperties instance;

	public boolean isAutoInteractionAllowed = false;
	public SwipeInteraction swipeInteraction = null;
	public SwipeSprite swipeSprite = null;
	private ArrayList<GameSprite> gameSprites = new ArrayList<GameSprite>();
	private int tapLimit;
	private Stage stage = null;
	private int swipeCount;
	private int swipeLimit;

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
	
	public void setSwipeInteraction(SwipeInteraction swipeInteraction) {
		this.swipeInteraction = swipeInteraction;
	}
	
	public SwipeInteraction getSwipeInteraction() {
		return this.swipeInteraction;
	}

	private float universalTimeRatio = 0.7f;

	public float getUniversalTimeRatio() {
		return universalTimeRatio;
	}

	private Group actorGroup = new Group();
	public Group getActorGroup() {
		return actorGroup;
	}

	public ArrayList<GameSprite> getGameSprites() {
		gameSprites.clear();
		Actor[] actors = ((Actor[])actorGroup.getChildren().toArray());
		for(Actor actor : actors) {
			gameSprites.add((GameSprite) actor);
		}
		return gameSprites;	
	}

	public void addToActorGroup(Actor actor) {
		this.actorGroup.addActor(actor);
	}

	public List<MoveableSprite> actorsToReplace = Arrays.asList();

	public void replaceActorInGroup(MoveableSprite actor) {
		
		//Ensure HeadSprite actor gets hit
		setActorGroupOriginToZero();
		actor.getSourceSprite().setTouchable(Touchable.disabled);
		Gdx.app.debug("GameProperties", "Replacing actor at "+actor.getCurrentX()+", "+actor.getCurrentY());
		Actor actorToRemove = stage.hit(actor.getCurrentX(), actor.getCurrentY(), true);
		try {
			//Remove current actor at coordinates
			actorGroup.removeActor(actorToRemove);
			actorToRemove.remove();

			GameSprite actorToAdd = new GameSprite(actor.getType(), actor.getCurrentX(), actor.getCurrentY(), actor.getFramesPath(), false);
			Gdx.app.debug(this.toString().substring(this.toString().lastIndexOf(".")), 
					"Replaced actor "+((GameSprite)actorToRemove).hashCode()+" with actor "+actorToAdd.hashCode());
			actorToAdd.setValidOrientations();
			if(((GameSprite)actorToRemove).interactStatus == Status.SELECTED) {
				actorToAdd.interactStatus = Status.SELECTED;
				actorToAdd.setColor(Color.YELLOW);
				GameProperties.get().swipeSprite.setStartSprite(actorToAdd);
			}
			actorGroup.addActor(actorToAdd);
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
		for(Actor actor : actorGroup.getChildren()) {
			actor.setOrigin(0f, 0f);
		}
	}
	
	private void setActorGroupOriginToCentre() {
		for(Actor actor : actorGroup.getChildren()) {
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
		actorGroup = new Group();
		actorsToReplace = Arrays.asList();
		gameSprites.clear();
		isAutoInteractionAllowed = false;
	}
	
	//Encapsulated stage logic
	public void setStage(Stage stage) {
		this.stage = stage;
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
		stage.draw();
		stage.act(Gdx.graphics.getDeltaTime());
	}



}
