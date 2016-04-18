package com.causal.game.state;

import com.causal.game.main.Game.Head;

public class Follower {
	
	public FollowerType type = null;
	private int id = 0;
	
	public Follower(Head type, int id, String spritePath) {
		this.id = id;
		this.type = new FollowerType(spritePath, type);
	}
	
	public Follower(int id, FollowerType type) {
		this.id = id;
		this.type = type;
		this.type.imagePath += "Default.pack";
	}

	public int getId() {
		return id;
	}
	
	public FollowerType getFollowerType() {
		return this.type;
	}

}
