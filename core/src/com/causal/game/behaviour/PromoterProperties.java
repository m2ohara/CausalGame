package com.causal.game.behaviour;

import com.causal.game.main.GameSprite.InfluenceType;
import com.causal.game.main.GameSprite.Status;

public class PromoterProperties implements IBehaviourProperties {

	private float rotateP = 0.8f;
	private float interactP = 0.4f;
	private int influenceAmount = 3;
	private InfluenceType influenceType = InfluenceType.SUPPORT;

	@Override
	public float getRotateProbability() {
		return rotateP;
	}

	@Override
	public float getInteractProbability() {
		return interactP;
	}

	@Override
	public int getInfluenceAmount() {
		return influenceAmount;
	}
	
	@Override
	public InfluenceType getInfluenceType() {
		return influenceType;
	}

}
