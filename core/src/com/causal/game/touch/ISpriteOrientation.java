package com.causal.game.touch;

import com.causal.game.main.WorldSystem.Orientation;

public interface ISpriteOrientation {
	
	Orientation onRandomChange();
	
	Orientation getOrientation();
	
	void onCyclicChange();

}
