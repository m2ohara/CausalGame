package com.causal.game.state;


public class Follower {
	
	public FollowerType type = null;
	private int id = 0;
	
	public Follower(FollowerType type, int id) {
		this.id = id;
		this.type = type;
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
