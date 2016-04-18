package com.causal.game.touch;

import com.causal.game.main.WorldSystem.Orientation;

public interface ITouchAction {
	
	
	void setInteractorX(int interactorX);
	
	void setInteractorY(int interactorY);
	
	void setInteractorDir(Orientation interactorDir);
	
	void onAction();

}
