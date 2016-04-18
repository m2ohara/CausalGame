package com.causal.game.touch;

import java.util.ArrayList;

import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;

public abstract class TouchAction implements ITouchAction {
	
	private int interactorX;
	private int interactorY;
	private Orientation interactorDir;
	protected ArrayList<Integer> validXCoords = new ArrayList<Integer>();
	protected ArrayList<Integer> validYCoords = new ArrayList<Integer>();
	protected GameSprite interactor = null;
	
	public TouchAction(int x, int y) {
		setInteractorX(x);
		setInteractorY(y);
	}

	protected abstract void generateValidInteractees();

	public int getInteractorX() {
		return interactorX;
	}

	public void setInteractorX(int interactorX) {
		this.interactorX = interactorX;
	}

	public int getInteractorY() {
		return interactorY;
	}

	public void setInteractorY(int interactorY) {
		this.interactorY = interactorY;
	}

	public Orientation getInteractorDir() {
		return interactorDir;
	}

	public void setInteractorDir(Orientation interactorDir) {
		this.interactorDir = interactorDir;
	}
	
	public boolean isSelectedInteractor() {
		if(WorldSystem.get().getMemberFromCoords(getInteractorX(), getInteractorY()).interactStatus == Status.SELECTED) {;
			return true;
		}
		return false;
	}
	
	public void removeEmptyCoordinates() {
		for(int idx = 0; idx < validXCoords.size(); idx++) {
			if(WorldSystem.get().getMemberFromCoords(validXCoords.get(idx), validYCoords.get(idx)) != null) {
				validXCoords.remove(idx);
				validYCoords.remove(idx);
			}
		}
	}

}
