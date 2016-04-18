package com.causal.game.data;

public class PlayerStateEntity {
	
	private int id;
	private int level;
	private int levelUpThreshold;
	private int reputation;
	private int maxLevel;

	public PlayerStateEntity(int id, int level, int levelUpThreshold, int reputation, int maxLevel) {
		this.level = level;
		this.levelUpThreshold = levelUpThreshold;
		this.reputation = reputation;
		this.id = id;
		this.maxLevel = maxLevel;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setLevelUpThreshold(int levelUpThreshold) {
		this.levelUpThreshold = levelUpThreshold;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public int getLevelUpThreshold() {
		return levelUpThreshold;
	}

	public int getReputation() {
		return reputation;
	}

	public int getId() {
		return id;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

}
