package com.causal.game.behaviour;

import com.causal.game.act.OnAnimateTalkingAct;
import com.causal.game.interact.AutonomousInteraction;
import com.causal.game.interact.GenericInteraction;
import com.causal.game.interact.GossiperAutonomousBehaviour;
import com.causal.game.interact.IInteractionType;
import com.causal.game.touch.GossiperTouchAction;
import com.causal.game.touch.SpriteOrientation;

public class GossiperBehaviour {
	
	public AutonomousInteraction interaction;
	public Behaviour behaviour;
	
	public void create(IInteractionType interactionType, SpriteOrientation changeOrientation) {
//		IBehaviourProperties properties = new GossiperProperties();
//		//Review
//		OnAnimateTalkingAct actType = new OnAnimateTalkingAct(properties.getRotateProbability(), properties.getInteractProbability(), this, changeOrientation);
//		behaviour = new Behaviour(
//				isActive, 
//				actType,
//				new GossiperTouchAction(interactionType, getXGameCoord(), getYGameCoord()), 
//				properties,
//				changeOrientation);
//		interaction = new GenericInteraction(new GossiperAutonomousBehaviour());
	}

}
