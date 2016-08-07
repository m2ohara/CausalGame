package com.causal.game.state;

import java.util.ArrayList;
import java.util.List;

import com.causal.game.behaviour.DeceiverBehaviour;
import com.causal.game.behaviour.GossiperBehaviour;
import com.causal.game.behaviour.PromoterBehaviour;
//import com.causal.game.data.FollowerRepo;
//import com.causal.game.data.FollowerTypeRepo;
//import com.causal.game.data.PlayerRepo;
import com.causal.game.data.PlayerStateEntity;
import com.causal.game.gestures.SwipeInteraction;
import com.causal.game.interact.IInteractionType;
import com.causal.game.main.Game.Head;

public class PlayerState {
	
	private List<FollowerType> followerTypes = new ArrayList<FollowerType>();
	private List<Follower> followers = new ArrayList<Follower>();
	private PlayerStateEntity playerStateEntity = null;
	private static PlayerState instance;
	
	public static PlayerState get() {
		
		if(instance == null) {
			instance = new PlayerState();
		}
		return instance;
	}
	
	private PlayerState() {
	}
	
	public void load() {
//		followers = FollowerRepo.instance().getFollowers();
//		followerTypes = FollowerTypeRepo.instance().get();
//		playerStateEntity = PlayerRepo.instance().get();
	}
	
	public void loadDummy() {
		
		//Dummy data
		followerTypes.add(new FollowerType("sprites/Meep/Gossiper/", Head.GOSSIPER, new GossiperBehaviour()));
		followerTypes.add(new FollowerType("sprites/Meep/Promoter/", Head.INFLUENCER, new PromoterBehaviour()));
		followerTypes.add(new FollowerType("sprites/Meep/Deceiver/", Head.DECEIVER, new DeceiverBehaviour()));
		
		followers.add(new Follower(followerTypes.get(0), 1));
		followers.add(new Follower(followerTypes.get(1), 2));
		followers.add(new Follower(followerTypes.get(2), 3));
		followers.add(new Follower(followerTypes.get(0), 4));
		followers.add(new Follower(followerTypes.get(0), 5));
		this.playerStateEntity = new PlayerStateEntity(0, 0, 10000, 1000, 5);
	}

	
	public List<Follower> getFollowers() {
		return followers;
	}
	
	public void addFollowers(List<Follower> followersToAdd) {
		followers.addAll(followersToAdd);
	}
	
	public List<FollowerType> getFollowerTypes() {
		return followerTypes;
	}
	
	public FollowerType getFollowerTypeByHead(Head head) {
		for(FollowerType type : followerTypes) {
			if(type.head == head) {
				return type;
			}
		}
		
		return null;
	}
	
	public int getLevel() {
		return this.playerStateEntity.getLevel();
	}
	
	public void setLevel(int level) {
		this.playerStateEntity.setLevel(level);
	}
	
	public int getMaxLevel() {
		return this.playerStateEntity.getMaxLevel();
	}
	
	public void setReputationPoints(int points) {
		this.playerStateEntity.setReputation(points);
	}
	
	public int getReputationPoints() {
		return this.playerStateEntity.getReputation();
	}
	
	public int getLevelUpThreshold() {
		return this.playerStateEntity.getLevelUpThreshold();
	}
	
	//TODO: Implement player state entity
	public int getInfluenceLimit() {
		return 3;
	}
	
	public int getTapLimit() {
		return 2;
	}
	
	public boolean isFirstGame() {
		return false;
	}
	

}
