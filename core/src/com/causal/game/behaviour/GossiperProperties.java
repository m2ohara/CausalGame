package com.causal.game.behaviour;

import java.util.Random;

import com.causal.game.main.GameSprite.InfluenceType;
import com.causal.game.state.PlayerState;


public class GossiperProperties implements IBehaviourProperties {
	
	private float rotateProbability = 0.8f;
	private float interactProbability = 0.8f;
	private int influenceAmount = 2;
	private Random rand = new Random();
	
	public float getRotateProbability() {
		return rotateProbability;
	}
	public float getInteractProbability() {
		return interactProbability;
	}
	public int getInfluenceAmount() {
		return influenceAmount;
	}
	public InfluenceType getInfluenceType() {
		return InfluenceType.values()[rand.nextInt(2)];
	}
	public String getFramesPath() {
		return PlayerState.get().getFollowerTypes().get(0).getImagePath();
	}

}
