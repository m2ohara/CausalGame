package com.causal.game.tutorial;

import java.util.Arrays;
import java.util.List;

import com.causal.game.main.GameSprite;
import com.causal.game.main.Game.Head;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.setup.GameGenerator;

public class TutorialGenerator extends GameGenerator {
	
	private int idx = 0;
	private List<Float> followerTypeProbList = Arrays.asList(new Float(0.1), new Float(1), new Float(0.34), new Float(0.1), new Float(0.34), new Float(1), new Float(0.1), new Float(0.34), new Float(1));
	private int orientationIdx = -1;
	private List<Orientation> orientations = Arrays.asList(
			Orientation.N, Orientation.S, Orientation.N, 
			Orientation.E, Orientation.S, Orientation.E, 
			Orientation.N, Orientation.W, Orientation.N);
	
	public TutorialGenerator() {
		super();
	}

	public void setCrowdProperties() {
		starterX = 1;
		followerTypeProb = followerTypeProbList.get(idx);
		idx++;
	}
	
	public GameSprite setGameSprite(Head type, float x, float y, String framesPath, boolean isActive) {
		orientationIdx+=1;
		if(orientationIdx == 9) {
			orientationIdx = 0;
		}
		return new TutorialGameSprite(type, x, y, framesPath, isActive, orientations.get(orientationIdx));
	}

}
