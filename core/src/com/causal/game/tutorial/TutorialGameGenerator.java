package com.causal.game.tutorial;

import java.util.Arrays;
import java.util.List;

import com.causal.game.behaviour.ISpriteBehaviour;
import com.causal.game.main.GameSprite;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.setup.GameGenerator;

public class TutorialGameGenerator extends GameGenerator {
	
	private static final double YELLOW = 0.1;
	private static final double BLUE = 0.34;
	private static final double RED = 1;
	
	private int idx = 0;
	private List<Float> followerTypeProbList = Arrays.asList(
			new Float(YELLOW), new Float(RED), new Float(BLUE), 
			new Float(YELLOW), new Float(BLUE), new Float(RED), 
			new Float(YELLOW), new Float(BLUE), new Float(RED));
	
	public TutorialGameGenerator() {
		super();
		
		setGameSprite = new SetTutorialGameSprite();
	}

	public void setCrowdProperties() {
		starterX = 1;
		followerTypeProb = followerTypeProbList.get(idx);
		idx++;
	}

}
