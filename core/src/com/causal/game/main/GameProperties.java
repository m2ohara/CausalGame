package com.causal.game.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.causal.game.interact.SwipeInteraction;
import com.causal.game.main.Game.Head;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.state.PlayerState;

public class GameProperties {

	private static GameProperties instance;

	private ArrayList<GameSprite> gameSprites = new ArrayList<GameSprite>();
	public boolean isAutoInteractionAllowed = false;
	public SwipeInteraction swipeInteraction = null;
	public SwipeSprite swipeSprite = null;
	private int tapLimit;

	private GameProperties() {
		tapLimit = PlayerState.get().getTapLimit();
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
		System.out.println("replacing actor at "+actor.getCurrentX()+", "+actor.getCurrentY());
		Actor actorToRemove = stage.hit(actor.getCurrentX(), actor.getCurrentY(), true);
		try {
			//Remove current actor at coordinates
			actorGroup.removeActor(actorToRemove);
			actorToRemove.remove();

			GameSprite actorToAdd = new GameSprite(actor.getType(), actor.getCurrentX(), actor.getCurrentY(), actor.getFramesPath(), false);
			actorToAdd.setValidOrientations();
			if(((GameSprite)actorToRemove).interactStatus == Status.SELECTED) {
				actorToAdd.interactStatus = Status.SELECTED;
				actorToAdd.setColor(Color.YELLOW);
			}
			actorGroup.addActor(actorToAdd);
			//Remove placeholder
			actor.getSourceSprite().remove();
			actor.getTargetImage().remove();
		}
		catch(Exception ex) {
			System.out.println("Exception replacing actor on stage "+ex);
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

	private Stage stage = null;
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void addActorToStage(Actor actor) {
		this.stage.addActor(actor);
	}
	
	private int tapCount = 0;
	private ArrayList<Integer> tappedObjects = new ArrayList<Integer>();
	public void updateTapCount(int tappedObj) {
		if(!isAutoInteractionAllowed && tapCount < tapLimit) {
			System.out.println("Updating tap count from "+tapCount+". Can interact "+isAutoInteractionAllowed);
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

	public void dispose() {
		stage.clear();
		actorGroup = new Group();
		actorsToReplace = Arrays.asList();
		gameSprites.clear();
		isAutoInteractionAllowed = false;
	}



}
