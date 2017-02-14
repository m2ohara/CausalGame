package com.causal.game.tutorial;

import com.causal.game.behaviour.GossiperProperties;
import com.causal.game.sprite.GameSprite.InfluenceType;

public class TutorialGossiperProperties extends GossiperProperties {
	
	private InfluenceType influenceType;
	
	public TutorialGossiperProperties(InfluenceType influenceType) {
		super();
		this.influenceType = influenceType;
	}

	@Override
	public InfluenceType getInfluenceType() {
		return influenceType;
	}
	
	@Override
	public float getInteractProbability() {
		return 1;
	}

}
