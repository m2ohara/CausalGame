package com.causal.game.behaviour;

import com.causal.game.main.GameSprite.InfluenceType;


public interface IBehaviourProperties {
	
	public float getRotateProbability();
	
	public float getInteractProbability();
	
	public int getInfluenceAmount(); //TODO: Remoe. Not in use
	
	public InfluenceType getInfluenceType();
	
	public String getFramesPath();

}
