package com.causal.game.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.causal.game.main.GameProperties;

public class TutorialDisplayMessage extends Image {
	
	private int displayStage;
	private int hideStage;
	
	public TutorialDisplayMessage(float x, float y, int displayStage, int hideStage, String spriteName) {
		super(new TextureAtlas(Gdx.files.internal("sprites/Meep/TutorialSprite/"+spriteName+".pack")).getRegions().get(0));
		
		this.setPosition(x, y);
		GameProperties.get().addActorToStage(this);
		
	}
	
	public int getDisplayStage() {
		return displayStage;
	}
	
	public int getHideStage() {
		return hideStage;
	}

}
