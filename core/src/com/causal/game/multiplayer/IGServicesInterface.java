package com.causal.game.multiplayer;

public interface IGServicesInterface {
	
	public void Login();
	public void LogOut();
	 
	//Play with your friends
	public void goToWaitingRoom();
	public boolean getSignedIn();
	
	//Checks if game is ready to start
	public boolean isGameReadyToStart();
	// Has player received an invitation
	public boolean isInvitationReceived();
	//Checks if game is cancelled
	public boolean isGameCancelled();
	// Sets game ready to start
	public void setGameReadyToStart(boolean ready);
	
	public void sendCrowdSetupData(Object data);
	public void sendPlayerReputationData(Object data);
	public void sendPlayerRoundData(Object data);
	public void sendEndGameResult(Object data);
	
	public Object getCrowdSetupData();
	public Object getPlayerReputationData();
	public Object getPlayerRoundData();
	public Object getEndGameResult();

}
