package com.causal.game.tutorial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.InfluenceType;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.setup.SetGameSprite;
import com.causal.game.state.PlayerState;

public class SetTutorialGameSprite extends SetGameSprite {
	
	public int orientationIdx = 0;
	
	public void resetIndex() {
		orientationIdx = 0;
	}
		
	private List<Orientation> startingOrientations1 = Arrays.asList(
			Orientation.E, Orientation.S, Orientation.N, 
			Orientation.W, Orientation.S, Orientation.N, 
			Orientation.S, Orientation.S, Orientation.N);
	
	private List<Orientation> startingOrientations2 = Arrays.asList(
			Orientation.W, Orientation.N, Orientation.N, 
			Orientation.S, Orientation.E, Orientation.N,
			Orientation.E, Orientation.S, Orientation.N, 
			Orientation.S, Orientation.N, Orientation.N
			);
	
	@SuppressWarnings("unchecked")
	private List<List<Orientation>> startingOrientationsRound = Arrays.asList(startingOrientations1, startingOrientations2);
	
	private List<Orientation> autoInteractOrientations1 = Arrays.asList(
			Orientation.S, Orientation.S, Orientation.N, 
			Orientation.S, Orientation.W, Orientation.W, 
			Orientation.S, Orientation.S, Orientation.N);
	
	//Orientation.N indicates unused coordinates
	private List<Orientation> autoInteractOrientations2 = Arrays.asList(	//End result
			Orientation.S, Orientation.N, Orientation.S,      				// Y, Y, N 
			Orientation.W, Orientation.W, Orientation.W, 					// N, Y, Y
			Orientation.S, Orientation.S, Orientation.E,					// Y, N, Y
			Orientation.N, Orientation.N, Orientation.E						// N, Y, N
			);
	
	@SuppressWarnings("unchecked")
	private List<List<Orientation>> autoInteractOrientationsRound = Arrays.asList(autoInteractOrientations1, autoInteractOrientations2);
	
	private List<Orientation> swipeOrientations1 = Arrays.asList(
			Orientation.S, Orientation.S, Orientation.N, 
			Orientation.W, Orientation.E, Orientation.N, 
			Orientation.W, Orientation.N, Orientation.N);
	
	//Orietation.W indicates unused coordinates
	private List<Orientation> swipeOrientations2 = Arrays.asList(
			Orientation.S, Orientation.S, Orientation.W,
			Orientation.E, Orientation.N, Orientation.N, 
			Orientation.E, Orientation.W, Orientation.N, 
			Orientation.S, Orientation.W, Orientation.W
			);
	
	@SuppressWarnings("unchecked")
	private List<List<Orientation>> swipeOrientationsRound = Arrays.asList(swipeOrientations1, swipeOrientations2);
	
	@SuppressWarnings("unchecked")
	private List<List<Vector2>> tapableOnSelectedSprite1 = Arrays.asList(
			Arrays.asList( new Vector2(1, 0)), Arrays.asList( new Vector2(0,1)), Arrays.asList( new Vector2(0,2)),
			Arrays.asList( new Vector2(2, 1)), Arrays.asList( new Vector2(1,2)), Arrays.asList( new Vector2(1,2)),
			Arrays.asList( new Vector2(2, 1)), Arrays.asList( new Vector2(1,1)), Arrays.asList( new Vector2(2,2))
			);
	
	@SuppressWarnings("unchecked")
	private List<List<Vector2>> tapableOnSelectedSprite2= Arrays.asList(
			Arrays.asList( new Vector2(2,0)), Arrays.asList( new Vector2(3,0)), Arrays.asList( new Vector2(3,1)), 
			Arrays.asList( new Vector2(1,1)), Arrays.asList( new Vector2(1,2)), Arrays.asList( new Vector2(3,2)), 
			Arrays.asList( new Vector2(2,3)), Arrays.asList( new Vector2(3,2)), Arrays.asList( new Vector2(3,2)), 
			Arrays.asList( new Vector2(2,3)), Arrays.asList( new Vector2(3,0)), Arrays.asList( new Vector2(3,2))
			);
	
	@SuppressWarnings("unchecked")
	private List<List<List<Vector2>>> tapableOnSelectedSpriteRound = Arrays.asList(tapableOnSelectedSprite1, tapableOnSelectedSprite2);
	
	@SuppressWarnings("unchecked")
	private List<List<Vector2>> autoInteractOnSelectedSprite1 = Arrays.asList(
			Arrays.asList( new Vector2(2,2)), Arrays.asList( new Vector2(2,2)), Arrays.asList( new Vector2(2,2)),
			Arrays.asList( new Vector2(2,2)), Arrays.asList( new Vector2(2,2)), Arrays.asList( new Vector2(2,1)),
			Arrays.asList( new Vector2(2,2)), Arrays.asList( new Vector2(0,0), new Vector2(1,0)), Arrays.asList( new Vector2(2,2))
			);
	
	@SuppressWarnings("unchecked")
	private List<List<Vector2>> autoInteractOnSelectedSprite2 = Arrays.asList(
			Arrays.asList( new Vector2(3,2)), Arrays.asList( new Vector2(3,2)), Arrays.asList( new Vector2(3,2)), 
			Arrays.asList( new Vector2(2,0)), Arrays.asList( new Vector2(1,0), new Vector2(2,0)), Arrays.asList( new Vector2(1,1)), 
			Arrays.asList( new Vector2(3,0), new Vector2(3,1)), Arrays.asList( new Vector2(3,1)), Arrays.asList( new Vector2(3,2)), 
			Arrays.asList( new Vector2(3,2)), Arrays.asList( new Vector2(3,2)), Arrays.asList( new Vector2(3,2))
			);
	
	@SuppressWarnings("unchecked")
	List<List<List<Vector2>>> autoInteractOnSelectedSpriteRound = Arrays.asList(autoInteractOnSelectedSprite1, autoInteractOnSelectedSprite2);
	
	private List<InfluenceType> influenceTypes1 = Arrays.asList(
			InfluenceType.OPPOSE, InfluenceType.SUPPORT, InfluenceType.SUPPORT,
			InfluenceType.OPPOSE, InfluenceType.SUPPORT, InfluenceType.SUPPORT,
			InfluenceType.SUPPORT, InfluenceType.SUPPORT, InfluenceType.SUPPORT
			);
	
	private List<InfluenceType> influenceTypes2 = Arrays.asList(
			InfluenceType.NONE, InfluenceType.OPPOSE, InfluenceType.OPPOSE, //Rightmost column 4?
			InfluenceType.OPPOSE, InfluenceType.SUPPORT, InfluenceType.OPPOSE, //2nd column ?
			InfluenceType.NONE, InfluenceType.NONE, InfluenceType.NONE,
			InfluenceType.NONE, InfluenceType.NONE, InfluenceType.NONE
			);
	
	@SuppressWarnings("unchecked")
	private List<List<InfluenceType>> autoInfluenceTypesRound = Arrays.asList(influenceTypes1, influenceTypes2);
	
	private List<TutorialAnimationProperties> animations1 = Arrays.asList(
			new TutorialAnimationProperties(new Vector2(0,0), new ArrayList<Integer>(Arrays.asList(0))), new TutorialAnimationProperties(new Vector2(0,0), new ArrayList<Integer>(Arrays.asList(0))), new TutorialAnimationProperties(new Vector2(0,0), new ArrayList<Integer>(Arrays.asList(0))),
			new TutorialAnimationProperties(new Vector2(0,0), new ArrayList<Integer>(Arrays.asList(0))), new TutorialAnimationProperties(new Vector2(0,0), new ArrayList<Integer>(Arrays.asList(0))), new TutorialAnimationProperties(new Vector2(0,0), new ArrayList<Integer>(Arrays.asList(0))),
			new TutorialAnimationProperties(new Vector2(1,1), new ArrayList<Integer>(Arrays.asList(0))), new TutorialAnimationProperties(new Vector2(1,2), new ArrayList<Integer>(Arrays.asList(0))), new TutorialAnimationProperties(new Vector2(0,0), new ArrayList<Integer>(Arrays.asList(0)))
			);
	
	@SuppressWarnings("unchecked")
	private List<List<TutorialAnimationProperties>> animationProperties = Arrays.asList(animations1);

	@Override
	public GameSprite createGameSprite(float probability, int x, int y) {
		if(orientationIdx == 9) { orientationIdx = 0;}
		GameSprite current;
		if(probability < 0.33) {
			Gdx.app.log("SetTutorialSprite", "Round "+TutorialGameGenerator.Round+" Creating gossiper "+x+", "+y+" to orientation index "+orientationIdx+" value "+startingOrientationsRound.get(TutorialGameGenerator.Round).get(orientationIdx));
			current = getGameSprite(
					new TutorialGossiperBehaviour(
							startingOrientationsRound.get(TutorialGameGenerator.Round).get(orientationIdx), 
							tapableOnSelectedSpriteRound.get(TutorialGameGenerator.Round).get(orientationIdx), 
							autoInteractOrientationsRound.get(TutorialGameGenerator.Round).get(orientationIdx), 
							autoInfluenceTypesRound.get(TutorialGameGenerator.Round).get(orientationIdx)), 
							WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), PlayerState.get().getFollowerTypes().get(0).imagePath, true
							);
			incrementVoteType(2);
		}
		else if(probability >= 0.33 && probability < 0.66) {
			Gdx.app.log("SetTutorialSprite", "Round "+TutorialGameGenerator.Round+" Creating promoter "+x+", "+y+" to orientation index "+orientationIdx+" value "+startingOrientationsRound.get(TutorialGameGenerator.Round).get(orientationIdx));
			current = getGameSprite(new TutorialPromoterBehaviour(startingOrientationsRound.get(TutorialGameGenerator.Round).get(orientationIdx), tapableOnSelectedSpriteRound.get(TutorialGameGenerator.Round).get(orientationIdx), autoInteractOrientationsRound.get(TutorialGameGenerator.Round).get(orientationIdx)), WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), PlayerState.get().getFollowerTypes().get(1).imagePath, true);
			incrementVoteType(0);
		}
		else {
			Gdx.app.log("SetTutorialSprite", "Round "+TutorialGameGenerator.Round+" Creating deceiver "+x+", "+y+" to orientation index "+orientationIdx+" value "+startingOrientationsRound.get(TutorialGameGenerator.Round).get(orientationIdx));
			current = getGameSprite(new TutorialDeceiverBehaviour(startingOrientationsRound.get(TutorialGameGenerator.Round).get(orientationIdx), tapableOnSelectedSpriteRound.get(TutorialGameGenerator.Round).get(orientationIdx), autoInteractOrientationsRound.get(TutorialGameGenerator.Round).get(orientationIdx)), WorldSystem.get().getGameXCoords().get(x), WorldSystem.get().getGameYCoords().get(y), PlayerState.get().getFollowerTypes().get(2).imagePath, true);
			incrementVoteType(1);
		}
		orientationIdx += 1;
		return current;
	}

	@Override
	public GameSprite getGameSprite(ISpriteBehaviour behaviour, float x, float y, String framesPath, boolean isActive) {
		Gdx.app.debug("SetTutorialSprite", "Getting tutorial sprite "+x+", "+y);
		return new TutorialGameSprite(behaviour, x, y, framesPath, isActive, swipeOrientationsRound.get(TutorialGameGenerator.Round).get(orientationIdx), autoInteractOnSelectedSpriteRound.get(TutorialGameGenerator.Round).get(orientationIdx), autoInteractOrientationsRound.get(TutorialGameGenerator.Round).get(orientationIdx), animationProperties.get(TutorialGameGenerator.Round).get(orientationIdx));
	}

}
