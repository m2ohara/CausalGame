package com.causal.game.state;

import com.causal.game.main.Game.Head;

public class FollowerType {
	
	public Head head;
	public String imagePath;

	private int id;
	
	public FollowerType(String path, Head head) {
		this.head = head;
		this.imagePath = path;
	}
	
	public FollowerType(int typeId, String imagePath) {
		this.head = Head.values()[typeId-1];
		this.id = typeId;
		this.imagePath = imagePath;
	}

	public int getId() {
		return id;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	

}
