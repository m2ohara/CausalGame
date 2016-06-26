package com.causal.game.tutorial;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.main.GameSprite;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.setup.SetGameSprite;
import com.causal.game.state.PlayerState;

public class SetTutorialGameSprite extends SetGameSprite {
	
	private int orientationIdx = 0;
	
	private List<Orientation> startingOrientations = Arrays.asList(
			Orientation.N, Orientation.S, Orientation.N, 
			Orientation.E, Orientation.S, Orientation.N, 
			Orientation.N, Orientation.S, Orientation.N);
	
	private List<Orientation> swipeOrientations = Arrays.asList(
			Orientation.E, Orientation.N, Orientation.N, 
			Orientation.E, Orientation.E, Orientation.N, 
			Orientation.N, Orientation.S, Orientation.N);

	@Override
	public GameSprite createGameSprite(float probability, int x, int y) {
		GameSprite current;
		if(probability < 0.33) {
			Gdx.app.debug("SetTutorialSprite", "Creating gossiper "+x+", "+y+" to orientation index "+orientationIdx+" value "+startingOrientations.get(orientationIdx));
			current = getGameSprite(new TutorialGossiperBehaviour(startingOrientations.get(orientationIdx)), WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), PlayerState.get().getFollowerTypes().get(0).imagePath, true);
			incrementVoteType(2);
		}
		else if(probability >= 0.33 && probability < 0.66) {
			Gdx.app.debug("SetTutorialSprite", "Creating promoter "+x+", "+y+" to orientation index "+orientationIdx+" value "+startingOrientations.get(orientationIdx));
			current = getGameSprite(new TutorialPromoterBehaviour(startingOrientations.get(orientationIdx)), WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), PlayerState.get().getFollowerTypes().get(1).imagePath, true);
			incrementVoteType(0);
		}
		else {
			Gdx.app.debug("SetTutorialSprite", "Creating deceiver "+x+", "+y+" to orientation index "+orientationIdx+" value "+startingOrientations.get(orientationIdx));
			current = getGameSprite(new TutorialDeceiverBehaviour(startingOrientations.get(orientationIdx)), WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), PlayerState.get().getFollowerTypes().get(2).imagePath, true);
			incrementVoteType(1);
		}
		orientationIdx+=1;
		return current;
	}

	@Override
	public GameSprite getGameSprite(ISpriteBehaviour behaviour, float x, float y, String framesPath, boolean isActive) {
		Gdx.app.debug("SetTutorialSprite", "Getting tutorial sprite "+x+", "+y);
		return new TutorialGameSprite(behaviour, x, y, framesPath, isActive, swipeOrientations.get(orientationIdx));
	}

}
