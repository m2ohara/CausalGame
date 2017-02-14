package com.causal.game.touch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.causal.game.interact.Interactee;
import com.causal.game.main.WorldSystem;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.sprite.GameSprite;
import com.causal.game.sprite.GameSprite.Status;

public class SpriteOrientation implements ISpriteOrientation {
	
	private ArrayList<Orientation> validDirections;
	protected Orientation orientation;
	private Random rand = new Random();
	protected int xGameCoord;
	protected int yGameCoord;
	private Interactee interactee;
	
	public SpriteOrientation(int xGameCoord, int yGameCoord) 
	{
		this.xGameCoord = xGameCoord;
		this.yGameCoord = yGameCoord;
		setValidDirections(xGameCoord, yGameCoord);
		interactee = new Interactee();
	}
	
	public boolean cyclicChange() {
		GameSprite sprite = WorldSystem.get().getMemberFromCoords(xGameCoord, yGameCoord);
		if(sprite != null && sprite.interactStatus != Status.NEUTRAL) {
			return false;
		}
		else {
			onCyclicChange();
			return true;
		}
	}
	
	@Override
	public void onCyclicChange() {
		Orientation firstOrientation = orientation;
		//Change orientation to next value in list while it is valid until end
		setNextOrientation();
		while(!isValidInteractee() && orientation != firstOrientation) {
			setNextOrientation();
		}
	}
	
	public boolean cyclicChangeOnInvalidInteractee() {
		Orientation firstOrientation = orientation;
		//If current orientation invalid
		if(!isValidInteractee()) {
			//Change orientation to next value in list while it is valid until end
			setNextOrientation();
			while(!isValidInteractee()) {
				if(orientation == firstOrientation) {
					return false;
				}
				else 
					setNextOrientation();
			}
			return true;
		}
		else {
			return true;
		}
		
	}
	
	public Orientation onRandomChange() {
		//Change actor's orientation
		int choice = rand.nextInt(this.validDirections.size());
		orientation = this.validDirections.get(choice);
		Gdx.app.debug("SpriteOrientation", "Setting sprite "+xGameCoord+", "+yGameCoord+" to orientation index "+orientation);
		return orientation;
	
	}
	
	protected void setNextOrientation() {
		int index = validDirections.indexOf(orientation)+1 == validDirections.size() ? 0 : validDirections.indexOf(orientation)+1;
		orientation = this.validDirections.get(index);
		Gdx.app.debug("SpriteOrientation", "Switching orientation to "+orientation+" at idx "+index);
	}
	
	protected boolean isValidInteractee() {
		//Check that facing gamesprite is able to interact
		return interactee.isInteracteeNeutral(xGameCoord, yGameCoord, orientation);
	}
	
	@Override
	public Orientation getOrientation() {
		return orientation;
	}


	public void setValidDirections(int xGameCoord, int yGameCoord) {
		
		validDirections = new ArrayList<Orientation> (Arrays.asList(Orientation.N, Orientation.E, Orientation.S, Orientation.W));
		
		if(xGameCoord == WorldSystem.get().getSystemWidth()-1 || WorldSystem.get().getMemberFromCoords(xGameCoord + 1, yGameCoord) == null) {
			validDirections.remove(Orientation.E);
		}
		if(xGameCoord == 0 || WorldSystem.get().getMemberFromCoords(xGameCoord - 1, yGameCoord) == null) {
			validDirections.remove(Orientation.W);
		}
		if(yGameCoord == WorldSystem.get().getSystemHeight()-1 || WorldSystem.get().getMemberFromCoords(xGameCoord, yGameCoord + 1) == null) {
			validDirections.remove(Orientation.S);
		}
		if(yGameCoord == 0 || WorldSystem.get().getMemberFromCoords(xGameCoord, yGameCoord - 1) == null) {
			validDirections.remove(Orientation.N);
		}

	}
}
