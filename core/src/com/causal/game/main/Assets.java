package com.causal.game.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {

	public static Assets instance = null;
	private List<String> gameSpriteFilePaths = null;
	private float progress = 0.0f;
	
	private Assets() {
		assetManager = new AssetManager();
		createGameSpritePaths();
		queue();
	}
	
	public static Assets get() {
		if(instance == null) {
			instance = new Assets();
		}
		return instance;
	}
	
	private AssetManager assetManager = null;
	public AssetManager getAssetManager() {
		return assetManager;
	}
	
	private void queue() {
		for(String filePath : gameSpriteFilePaths) {
			assetManager.load(filePath, TextureAtlas.class);
		}
	}
	
	public void load() {
		if (!assetManager.update()) {
			progress = assetManager.getProgress();
		}
	}
	
	private void createGameSpritePaths() {
		
		gameSpriteFilePaths = new ArrayList<String>();
		
		List<String> spriteTypes = Arrays.asList("Default.pack", "Left.pack", "Right.pack", "Below.pack", "Above.pack");
		
		for(String type : spriteTypes) {	
			gameSpriteFilePaths.add("sprites/Meep/Gossiper/"+type); 
			gameSpriteFilePaths.add("sprites/Meep/Promoter/"+type);
			gameSpriteFilePaths.add("sprites/Meep/Deceiver/"+type);
		}
		
		gameSpriteFilePaths.add("sprites/Meep/TapSprite/TapSprite.pack");
		gameSpriteFilePaths.add("sprites/Meep/SwipingSprite/SwipingSprite.pack");
	}
	
	public float getProgress() {
		return progress;
	}
	
	public boolean isLoaded() {
		return assetManager.update();
	}
}
