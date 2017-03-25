package com.causal.game.act;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.causal.game.interact.AutonomousInteraction;
import com.causal.game.main.Assets;
import com.causal.game.main.GameProperties;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.sprite.GameSprite.Status;
import com.causal.game.touch.ISpriteOrientation;

public class OnAnimateDiscrete implements IOnAct{
		
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
		private String FrameAction = "Interacting"; 
		
		private AutonomousInteraction interaction;
		private ISpriteOrientation spriteOrientation;
		private float interactP;
		private float rotateP;
		private Random rand = new Random();
		
		public OnAnimateDiscrete(float rotateProbability, float interactProbability, AutonomousInteraction interaction, ISpriteOrientation spriteOrientation, String framesPath) 
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
			setDefaultFrame();
			interaction.interact(spriteOrientation.getOrientation());
			
			if(GameProperties.get().isAutoInteractionAllowed == false) {
				changeSpriteOrientation();
			}
		}
		
		private void setDefaultFrame() {
			if(!frames.equals(animationFrames.get("Default")) && GameProperties.get().isAutoInteractionAllowed == true) {
				frames = animationFrames.get("Default");
				currentFrame = frames.first();
				Gdx.app.debug("OnAnimateSpaceShip", "Setting default frame");
			}
		}
		
		private void setFramePacks(String framesPath) {
			Array<AtlasRegion> talkRight = Assets.get().getAssetManager().get(framesPath + "Right.pack", TextureAtlas.class).getRegions();
			
			animationFrames.put("InteractingEast", talkRight);
			
			Array<AtlasRegion> talkLeft = Assets.get().getAssetManager().get(framesPath + "Left.pack", TextureAtlas.class).getRegions();
			
			animationFrames.put("InteractingWest", talkLeft);
			
			Array<AtlasRegion> talkAbove = Assets.get().getAssetManager().get(framesPath + "Above.pack", TextureAtlas.class).getRegions();
			
			animationFrames.put("InteractingNorth", talkAbove);
			
			Array<AtlasRegion> talkBelow = Assets.get().getAssetManager().get(framesPath + "Below.pack", TextureAtlas.class).getRegions();
			
			animationFrames.put("InteractingSouth", talkBelow);
			
			Array<AtlasRegion> defaultSprite = new TextureAtlas(Gdx.files.internal(framesPath+"Default.pack")).getRegions();
			
			animationFrames.put("Default", defaultSprite);
			
			Array<AtlasRegion> neutralEast = Assets.get().getAssetManager().get(framesPath + "NeutralEast.pack", TextureAtlas.class).getRegions();
			
			animationFrames.put("NeutralEast", neutralEast);
			
			Array<AtlasRegion> neutralWest = Assets.get().getAssetManager().get(framesPath + "NeutralWest.pack", TextureAtlas.class).getRegions();
			
			animationFrames.put("NeutralWest", neutralWest);
			
			Array<AtlasRegion> neutralNorth = Assets.get().getAssetManager().get(framesPath + "NeutralNorth.pack", TextureAtlas.class).getRegions();
			
			animationFrames.put("NeutralNorth", neutralNorth );
			
			Array<AtlasRegion> neutralSouth = Assets.get().getAssetManager().get(framesPath + "NeutralSouth.pack", TextureAtlas.class).getRegions();
			
			animationFrames.put("NeutralSouth", neutralSouth);
		
		}
		
		private void setFrame(Status interactStatus) {
			
			if(interactStatus == Status.NEUTRAL) {
				FrameAction = "Neutral";
			}
			else {
				FrameAction = "Interacting";
				Gdx.app.debug("OnAnimateDiscrete", "Setting frame. InteractStatus "+interactStatus+", FrameAction "+FrameAction);
			}
			
			//Based on rotation probability
			if(interactStatus == Status.INFLUENCED && rand.nextFloat() < this.rotateP) {
				
				this.spriteOrientation.onCyclicChange();
			}
			
			changeSpriteOrientation();
		
			
		}
		
		@Override
		public void changeSpriteOrientation() {
			
			if(spriteOrientation.getOrientation() == Orientation.N) {
				frames = animationFrames.get(FrameAction+"North");
			}
			else if(spriteOrientation.getOrientation() == Orientation.E) {
				frames = animationFrames.get(FrameAction+"East");
			}
			else if(spriteOrientation.getOrientation() == Orientation.W) {
				frames = animationFrames.get(FrameAction+"West");
			}
			else if(spriteOrientation.getOrientation() == Orientation.S) {
				frames = animationFrames.get(FrameAction+"South");
			}
		}

		@Override
		public AtlasRegion getCurrentFrame() {
			return currentFrame;
		}
}
