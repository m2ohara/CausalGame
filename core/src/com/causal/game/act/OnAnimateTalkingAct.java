package com.causal.game.act;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.causal.game.interact.GenericInteraction;
import com.causal.game.main.Assets;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.touch.ISpriteOrientation;
import com.causal.game.touch.SpriteOrientation;

public class OnAnimateTalkingAct implements IOnAct{
	
	private float animateStateLength = 2.0f * GameProperties.get().getUniversalTimeRatio();
	private float animateStateTime = animateStateLength;
	private float attemptInteractStateLength = 0.7f * GameProperties.get().getUniversalTimeRatio();
	private float attemptInteractStateTime = attemptInteractStateLength;
	
	private float frameLength = 0.2f * GameProperties.get().getUniversalTimeRatio();
	private float frameTime = frameLength;
	private int frameCount = 0;
	private AtlasRegion currentFrame = null;
	
	private HashMap<String, Array<AtlasRegion>> animationFrames = new HashMap<String, Array<AtlasRegion>>();
	private Array<AtlasRegion> frames;
	
	private GenericInteraction interaction;
	private ISpriteOrientation spriteOrientation;
	private float interactP;
	private float rotateP;
	private Random rand = new Random();
	
	public OnAnimateTalkingAct(float rotateProbability, float interactProbability, GenericInteraction interaction, ISpriteOrientation spriteOrientation, String framesPath) 
	{

		this.rotateP = rotateProbability;
		this.interactP = interactProbability;
		this.interaction = interaction;
		this.spriteOrientation = spriteOrientation;
		
		frames = new TextureAtlas(Gdx.files.internal(framesPath+"Default.pack")).getRegions();
		currentFrame = frames.first();
		
		setFramePacks(framesPath);
		
		spriteOrientation.onRandomChange();
		Gdx.app.debug("OnAnimateTalkingAct", "Random sprite changed for "+interaction.interactor.getXGameCoord()+", "+interaction.interactor.getYGameCoord());		
		changeSpriteOrientation();
		
	}

	@Override
	public void performActing(float delta, Status interactStatus, boolean isInteracting) {
		
		//Change actor's orientation
		if(animateStateTime >= animateStateLength) {
			animateStateTime = 0.0f;		
			setFrame(interactStatus);
		}
		
		//Animate frames for current direction
		if(animateStateTime < animateStateLength) {
			updateSprite(delta);
		}
		
		//Attempt interaction
		if( attemptInteractStateTime >= attemptInteractStateLength && GameProperties.get().isAutoInteractionAllowed == true) {
			attemptInteractStateTime = 0.0f;
			attemptAutonomousInteraction();
		}
		
		//If not interacting increment states
		if(!isInteracting) {
			animateStateTime += delta;
			attemptInteractStateTime += delta;
		}
		//Otherwise continue interaction
		else {
			continueAutonomousInteraction();
		}
		
	}
	
	private void updateSprite(float delta) {
	
		if(frameTime >= frameLength) {
			frameTime = 0.0f;
			
			if(frameCount > frames.size -1) {
				frameCount = 0;
			}
			
			currentFrame = frames.get(frameCount++);
			
		}
		
		frameTime += delta;
	}
	
	private void attemptAutonomousInteraction() {
		Random rand = new Random();
		if(rand.nextFloat() < this.interactP) {
			interaction.interact(spriteOrientation.getOrientation());
			
		}
	}
	
	private void continueAutonomousInteraction() {
		interaction.interact(spriteOrientation.getOrientation());
	}
	
	private void setFramePacks(String framesPath) {
		Array<AtlasRegion> talkRight = Assets.get().getAssetManager().get(framesPath + "Right.pack", TextureAtlas.class).getRegions();
		
		animationFrames.put("TalkRight", talkRight);
		
		Array<AtlasRegion> talkLeft = Assets.get().getAssetManager().get(framesPath + "Left.pack", TextureAtlas.class).getRegions();
		
		animationFrames.put("TalkLeft", talkLeft);
		
		Array<AtlasRegion> talkAbove = Assets.get().getAssetManager().get(framesPath + "Above.pack", TextureAtlas.class).getRegions();
		
		animationFrames.put("TalkAbove", talkAbove);
		
		Array<AtlasRegion> talkBelow = Assets.get().getAssetManager().get(framesPath + "Below.pack", TextureAtlas.class).getRegions();
		
		animationFrames.put("TalkBelow", talkBelow);
	}
	
	private void setFrame(Status interactStatus) {
		//Based on rotation probability
		if(interactStatus == Status.INFLUENCED && rand.nextFloat() < this.rotateP) {
			this.spriteOrientation.onCyclicChange();
			changeSpriteOrientation();
		}
		
	}
	
	@Override
	public void changeSpriteOrientation() {
		
		if(spriteOrientation.getOrientation() == Orientation.N) {
			frames = animationFrames.get("TalkAbove");
		}
		else if(spriteOrientation.getOrientation() == Orientation.E) {
			frames = animationFrames.get("TalkRight");
		}
		else if(spriteOrientation.getOrientation() == Orientation.W) {
			frames = animationFrames.get("TalkLeft");
		}
		else if(spriteOrientation.getOrientation() == Orientation.S) {
			frames = animationFrames.get("TalkBelow");
		}
	}

	@Override
	public AtlasRegion getCurrentFrame() {
		return currentFrame;
	}

}
