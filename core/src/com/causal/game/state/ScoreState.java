package com.causal.game.state;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.causal.game.main.GameProperties;
import com.causal.game.main.WorldSystem;

public class ScoreState {
	
	private PlayerState plState = PlayerState.get();
	private static ScoreState instance = null;
	
	public static ScoreState get() {
		if(instance == null) {
			instance = new ScoreState();
		}	
		return instance;
	}
	
	public void setLevel() {
		//If level points are reached
		if(plState.getReputationPoints() > plState.getLevelUpThreshold()) {
			if(plState.getLevel() < plState.getMaxLevel()) {
				plState.setLevel(plState.getLevel() + 1);
			}
		}
	}
	
//	private void setFollowerRewards() {
//		int rewardPoints = GameProperties.get().getRewardScore();
//		int points = plState.getReputationPoints();
//		if(points >= rewardPoints) {
//			generateRewardFollowers(points / rewardPoints);
//			plState.setReputationPoints(points % rewardPoints);
//		}
//	}
	
//	private void generateRewardFollowers(int amount) {	
//		
//		List<Follower> rewardedFollowers = new ArrayList<Follower>();
//		List<FollowerType> types = plState.getFollowerTypes();
//		
//		int count = amount > 3 ? 3 : amount;
//		
//		Random rand = new Random();
//		for(int i =0; i < count; i++) {
//			FollowerType type = types.get(rand.nextInt(types.size()));
//			rewardedFollowers.add(new Follower(type.head, 0, type.imagePath+"Default.pack"));
//		}
//		
//		plState.addFollowers(rewardedFollowers);
//		
//		setRewardFollowers(rewardedFollowers);
//	}
	
	private void setRewardFollowers(List<Follower> rewardedFollowers) {
		
		for(int count = 0; count < rewardedFollowers.size(); count++) {
			setRewardImage(rewardedFollowers.get(count).type.imagePath, WorldSystem.get().getHudXCoords().get(count), WorldSystem.get().getHudYCoords().get(count));
		}
	}
	
	private void setRewardImage(String framesPath, float origX, float origY) {
		Image targetImage = new Image(new TextureAtlas(Gdx.files.internal(framesPath)).getRegions().get(0));
		targetImage.setPosition(origX, origY);
		GameProperties.get().addActorToStage(targetImage);
		targetImage.setTouchable(Touchable.disabled);
	}

}
