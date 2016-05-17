package com.causal.game.tutorial;

import java.util.Arrays;
import java.util.List;

import com.causal.game.setup.GameGenerator;

public class TutorialGenerator extends GameGenerator {
	
	public TutorialGenerator() {
		super();
	}

	private int idx = 0;
	private List<Float> followerTypeProbList = Arrays.asList(new Float(0.1), new Float(0.34), new Float(1), new Float(0.1), new Float(0.34), new Float(1), new Float(0.1), new Float(0.34), new Float(1));
	public void setCrowdProperties() {
		starterX = 1;
		followerTypeProb = followerTypeProbList.get(idx);
		idx++;
	}

}
