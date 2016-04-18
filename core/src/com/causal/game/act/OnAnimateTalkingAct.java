package com.causal.game.act;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.causal.game.main.Assets;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;
import com.causal.game.main.GameSprite.InteractorType;
import com.causal.game.main.GameSprite.Status;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.touch.SpriteOrientation;

public class OnAnimateTalkingAct implements IOnAct{
	
	private float animateStateLength = 2.0f * GameProperties.get().getUniversalTimeRatio();
	private float animateStateTime = animateStateLength;
	private float attemptInteractStateLength = 0.7f * GameProperties.get().getUniversalTimeRatio();
	private float attemptInteractStateTime = attemptInteractStateLength;
	
	private float frameLength = 0.2f * GameProperties.get().getUniversalTimeRatio();
	private float frameTime = frameLength;
	private int frameCount = 0;
	
	private HashMap<String, Array<AtlasRegion>> animationFrames = new HashMap<String, Array<AtlasRegion>>();
	private Array<AtlasRegion> frames;
	
	private GameSprite actor;
	
	private SpriteOrientation changeOrientation;
	private float interactP;
	private float rotateP;
	private Random rand = new Random();
	
	public OnAnimateTalkingAct(float rotateProbability, float interactProbability, GameSprite actor, SpriteOrientation onRandom) 
	{

		this.rotateP = rotateProbability;
		this.interactP = interactProbability;
		this.actor = actor;
		this.changeOrientation = onRandom;
		
		frames = new TextureAtlas(Gdx.files.internal(actor.getFramesPath()+"Default.pack")).getRegions();
		
		setFramePacks(actor.getFramesPath());
		
		this.changeOrientation.onRandomChange();
		changeSpriteOrientation();
		
	}

	@Override
	public void performActing(float delta) {
		
		//Change actor's orientation
		if(animateStateTime >= animateStateLength) {
			animateStateTime = 0.0f;		
			setFrame();
		}
		
		//Animate frames for current direction
		if(animateStateTime < animateStateLength) {
			updateSprite(delta, actor);
		}
		
		//Attempt interaction
		if( attemptInteractStateTime >= attemptInteractStateLength && GameProperties.get().isAutoInteractionAllowed == true) {
			attemptInteractStateTime = 0.0f;
			attemptAutonomousInteraction();
		}
		
		//If not interacting increment states
		if(!actor.isInteracting && actor.interactorType == InteractorType.None) {
			animateStateTime += delta;
			attemptInteractStateTime += delta;
		}
		//Otherwise continue interaction
		else {
			continueAutonomousInteraction();
		}
		
	}
	
	private void updateSprite(float delta, GameSprite actor) {
	
		if(frameTime >= frameLength) {
			frameTime = 0.0f;
			
			if(frameCount > frames.size -1) {
				frameCount = 0;
			}
			
			actor.setDrawable(new TextureRegionDrawable(new TextureRegion(frames.get(frameCount++))));
			
		}
		
		frameTime += delta;
	}
	private void attemptAutonomousInteraction() {
		Random rand = new Random();
		if(rand.nextFloat() < this.interactP) {
			actor.interaction.interact(actor, GameProperties.get().getActorGroup(), changeOrientation.getOrientation());
			
		}
	}
	
	private void continueAutonomousInteraction() {
		actor.interaction.interact(actor, GameProperties.get().getActorGroup(), changeOrientation.getOrientation());
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
	
	public void setFrame() {
		//Based on rotation probability
		if(actor.interactStatus == Status.INFLUENCED && rand.nextFloat() < this.rotateP) {
			this.changeOrientation.onCyclicChange();
			changeSpriteOrientation();
		}
		
	}
	
	@Override
	public void changeSpriteOrientation() {
		
		if(changeOrientation.getOrientation() == Orientation.N) {
			frames = animationFrames.get("TalkAbove");
		}
		else if(changeOrientation.getOrientation() == Orientation.E) {
			frames = animationFrames.get("TalkRight");
		}
		else if(changeOrientation.getOrientation() == Orientation.W) {
			frames = animationFrames.get("TalkLeft");
		}
		else if(changeOrientation.getOrientation() == Orientation.S) {
			frames = animationFrames.get("TalkBelow");
		}
	}

}
