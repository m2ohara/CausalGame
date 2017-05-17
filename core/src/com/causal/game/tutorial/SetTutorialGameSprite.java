package com.causal.game.tutorial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.causal.game.animation.TutorialSwipeSprite;
import com.causal.game.animation.TutorialTapSprite;
import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.setup.SetGameSprite;
import com.causal.game.sprite.GameSprite;
import com.causal.game.sprite.GameSprite.InfluenceType;
import com.causal.game.state.PlayerState;

public class SetTutorialGameSprite extends SetGameSprite {
	
	public int orientationIdx = 0;
	private int indexCount = 9;
	
	public void resetIndex() {
		orientationIdx = 0;
		indexCount = TutorialGameGenerator.Round == 0 ? 9 : 12;
	}
	
	public void setIndex(int index) {
		orientationIdx = index;
	}
		
	private List<Orientation> startingOrientations1 = Arrays.asList(
			Orientation.E, Orientation.S, Orientation.N, 
			Orientation.W, Orientation.S, Orientation.N, 
			Orientation.S, Orientation.S, Orientation.N);
	
	private List<Orientation> startingOrientations2 = Arrays.asList(
			Orientation.E, Orientation.N, Orientation.N, 
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
	private List<Orientation> autoInteractOrientations2 = Arrays.asList(	
			Orientation.S, Orientation.N, Orientation.E,      				 
			Orientation.W, Orientation.W, Orientation.W, 					
			Orientation.S, Orientation.S, Orientation.E,					
			Orientation.N, Orientation.N, Orientation.E						
			);
	
	@SuppressWarnings("unchecked")
	private List<List<Orientation>> autoInteractOrientationsRound = Arrays.asList(autoInteractOrientations1, autoInteractOrientations2);
	
	private List<Orientation> swipeOrientations1 = Arrays.asList(
			Orientation.S, Orientation.S, Orientation.N, 
			Orientation.W, Orientation.E, Orientation.N, 
			Orientation.W, Orientation.N, Orientation.N);
	
	//Orietation.W indicates unused coordinates
	private List<Orientation> swipeOrientations2 = Arrays.asList(
			Orientation.W, Orientation.W, Orientation.W,
			Orientation.E, Orientation.N, Orientation.N, 
			Orientation.E, Orientation.W, Orientation.N, 
			Orientation.S, Orientation.S, Orientation.W
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
			Arrays.asList( new Vector2(2,3)), Arrays.asList( new Vector2(3,0)), Arrays.asList( new Vector2(3,1))
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
			InfluenceType.OPPOSE, InfluenceType.OPPOSE, InfluenceType.SUPPORT,
			InfluenceType.OPPOSE, InfluenceType.SUPPORT, InfluenceType.OPPOSE,
			InfluenceType.OPPOSE, InfluenceType.SUPPORT, InfluenceType.SUPPORT,
			InfluenceType.SUPPORT, InfluenceType.SUPPORT, InfluenceType.SUPPORT
			);
	
	@SuppressWarnings("unchecked")
	private List<List<InfluenceType>> autoInfluenceTypesRound = Arrays.asList(influenceTypes1, influenceTypes2);
	
	private List<TutorialAnimationProperties> animations1 = Arrays.asList(
			new TutorialAnimationProperties(
					new ArrayList<Object>(Arrays.asList(
							new TutorialTapSprite(new Vector2(0,0), Orientation.S),
							new TutorialSwipeSprite(Arrays.asList(Orientation.W, Orientation.S), new Vector2(1, 0), new Vector2(0, 1))
							))), 
			new TutorialAnimationProperties(new ArrayList<Object>(Arrays.asList(new TutorialTapSprite(new Vector2(0,0), Orientation.N))), new Vector2(0,0)), 
			new TutorialAnimationProperties(new ArrayList<Object>(Arrays.asList(new TutorialTapSprite(new Vector2(0,0), Orientation.N))), new Vector2(0,0)), 
			
			new TutorialAnimationProperties(new ArrayList<Object>(Arrays.asList(new TutorialTapSprite(new Vector2(2,0), Orientation.N))), new Vector2(0,0)), 
			new TutorialAnimationProperties(
					true, 
					new ArrayList<Object>(Arrays.asList(
							new TutorialTapSprite(new Vector2(1,1), Orientation.E), 
							new TutorialSwipeSprite(Arrays.asList(Orientation.N), new Vector2(1, 2), new Vector2(1, 1))
							)),
					new Vector2(2, 1)
					), 
			new TutorialAnimationProperties(new ArrayList<Object>(Arrays.asList(new TutorialTapSprite(new Vector2(0,0), Orientation.N))), new Vector2(0,0)), 
			
			new TutorialAnimationProperties(
					new ArrayList<Object>(Arrays.asList(
							new TutorialTapSprite(new Vector2(2,0), Orientation.W),
							new TutorialSwipeSprite(Arrays.asList(Orientation.N, Orientation.W), new Vector2(2, 1), new Vector2(1,0))
							)), 
					new Vector2(0,0)), 
			new TutorialAnimationProperties(
					new ArrayList<Object>(Arrays.asList(
							new TutorialTapSprite(new Vector2(2,1), Orientation.N), 
							new TutorialSwipeSprite(Arrays.asList(Orientation.E), new Vector2(1, 1), new Vector2(2,1))
							)),
					new Vector2(2,0)
							), 
			new TutorialAnimationProperties(new ArrayList<Object>(Arrays.asList(new TutorialTapSprite(new Vector2(0,0), Orientation.N))), new Vector2(0,0)) 
			);
	
	private List<TutorialAnimationProperties> animations2 = Arrays.asList(
			
			new TutorialAnimationProperties(new ArrayList<Object>(Arrays.asList(new TutorialTapSprite(new Vector2(0,0), Orientation.N))), new Vector2(0,0)),
			new TutorialAnimationProperties(new ArrayList<Object>(Arrays.asList(new TutorialTapSprite(new Vector2(0,0), Orientation.N))), new Vector2(0,0)), 			
			new TutorialAnimationProperties(new ArrayList<Object>(Arrays.asList(new TutorialTapSprite(new Vector2(0,0), Orientation.N))), new Vector2(0,0)),
					
			new TutorialAnimationProperties( 
					new ArrayList<Object>(Arrays.asList(
							new TutorialTapSprite(new Vector2(1,0), Orientation.E), 
							new TutorialSwipeSprite(Arrays.asList(Orientation.N, Orientation.E), new Vector2(1, 1), new Vector2(2, 0))
							)),
					new Vector2(3, 0)
					), 	
			new TutorialAnimationProperties(
					true, 
					new ArrayList<Object>(Arrays.asList(
							new TutorialTapSprite(new Vector2(1,1), Orientation.N), 
							new TutorialSwipeSprite(Arrays.asList(Orientation.N), new Vector2(1, 2), new Vector2(1, 1))
							)),
					new Vector2(1, 0)
					), 
			new TutorialAnimationProperties(new ArrayList<Object>(Arrays.asList(new TutorialTapSprite(new Vector2(0,0), Orientation.N))), new Vector2(0,0)), 

			new TutorialAnimationProperties(new ArrayList<Object>(Arrays.asList(new TutorialTapSprite(new Vector2(0,0), Orientation.N))), new Vector2(0,0)), 
			new TutorialAnimationProperties(new ArrayList<Object>(Arrays.asList(new TutorialTapSprite(new Vector2(0,0), Orientation.N))), new Vector2(0,0)), 
			new TutorialAnimationProperties(new ArrayList<Object>(Arrays.asList(new TutorialTapSprite(new Vector2(0,0), Orientation.N))), new Vector2(0,0)), 
			
			new TutorialAnimationProperties(
					new ArrayList<Object>(Arrays.asList(
							new TutorialTapSprite(new Vector2(3,0), Orientation.S), 
							new TutorialSwipeSprite(Arrays.asList(Orientation.E), new Vector2(2, 0), new Vector2(3, 0))
							)),
					new Vector2(3, 1)
					),
			new TutorialAnimationProperties(
					new ArrayList<Object>(Arrays.asList(
							new TutorialTapSprite(new Vector2(3,1), Orientation.S), 
							new TutorialSwipeSprite(Arrays.asList(Orientation.S), new Vector2(3, 0), new Vector2(3, 1))
							)),
					new Vector2(3, 2)
					),
			new TutorialAnimationProperties(
					new ArrayList<Object>(Arrays.asList(
							new TutorialTapSprite(new Vector2(3,2), Orientation.W), 
							new TutorialSwipeSprite(Arrays.asList(Orientation.S, Orientation.W), new Vector2(3, 1), new Vector2(2, 3))
							)),
					new Vector2(0, 0)		
					)
			
			);
	
	@SuppressWarnings("unchecked")
	private List<List<TutorialAnimationProperties>> animationProperties = Arrays.asList(animations1, animations2);

	@Override
	public GameSprite createGameSprite(float probability, int x, int y) {
		if(orientationIdx == indexCount) { orientationIdx = 0;}
		
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
