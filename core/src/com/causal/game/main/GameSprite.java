package com.causal.game.main;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.causal.game.act.OnAnimateTalkingAct;
import com.causal.game.behaviour.Behaviour;
import com.causal.game.behaviour.DeceiverProperties;
import com.causal.game.behaviour.GossiperProperties;
import com.causal.game.behaviour.IBehaviourProperties;
import com.causal.game.behaviour.PromoterProperties;
import com.causal.game.interact.AutonomousInteraction;
import com.causal.game.interact.DeceiverAutonomousBehaviour;
import com.causal.game.interact.GenericInteraction;
import com.causal.game.interact.GossiperAutonomousBehaviour;
import com.causal.game.interact.IInteractionType;
import com.causal.game.interact.PromoterAutonomousBehaviour;
import com.causal.game.main.Game.Head;
import com.causal.game.main.WorldSystem.Orientation;
import com.causal.game.state.GameScoreState;
import com.causal.game.touch.DeceiverTouchAction;
import com.causal.game.touch.GossiperTouchAction;
import com.causal.game.touch.PromoterTouchAction;
import com.causal.game.touch.SpriteOrientation;

public class GameSprite  extends Image {
	
	public Behaviour behaviour;
//	public AutonomousInteraction interaction;
	
	public float startingX;
	public float startingY;
	public boolean isActive = true;
	public Status interactStatus = Status.NEUTRAL;
	public InfluenceType influenceType = InfluenceType.NONE;
	public InteractorType interactorType = InteractorType.NONE;
	public int scoreStatus = 0;
	
	private ArrayList<Orientation> validDirections;
	protected SpriteOrientation spriteOrientation;
	
	public boolean isInteracting = false;
	private boolean isActing = false;
	private String framesPath = null;
	private static String defaultPack = "Default.pack";
	private Head type = null;
	
	public int getXGameCoord() {
		return WorldSystem.get().getGameXCoords().indexOf(this.startingX);
	}
	
	public int getYGameCoord() {
		return WorldSystem.get().getGameYCoords().indexOf(this.startingY);
	}

	public float getStartingX() {
		return startingX;
	}

	public float getStartingY() {
		return startingY;
	}
	
	public boolean isActing() {
		return isActing;
	}
	
	public String getFramesPath() {
		return framesPath;
	}
	
	public GameSprite(Head type, float x, float y, String framesPath, boolean isActive) {
		super(new TextureAtlas(Gdx.files.internal(framesPath+defaultPack)).getRegions().get(0));

		//Centre origin in frame for rotation;
		TextureRegion currentFrame  = new TextureAtlas(Gdx.files.internal(framesPath+defaultPack)).getRegions().get(0);
		this.setOrigin(currentFrame.getRegionWidth()/2, currentFrame.getRegionHeight()/2);
		this.setPosition(x, y);
		
		float scaleFactor = WorldSystem.get().getLevelScaleFactor();
		this.setScale(scaleFactor);
		
		this.startingX = x;
		this.startingY = y;
		this.framesPath = framesPath;
		this.type = type;
	}
	
	public void setValidOrientations() {
		spriteOrientation = new SpriteOrientation(getXGameCoord(), getYGameCoord());
	}
	
	public void activate(IInteractionType manInteraction) {
		
		if(type == type.GOSSIPER) {
			IBehaviourProperties properties = new GossiperProperties();
			//Review
			OnAnimateTalkingAct actType = new OnAnimateTalkingAct(properties.getRotateProbability(), properties.getInteractProbability(), new GenericInteraction(this, new GossiperAutonomousBehaviour()), spriteOrientation, framesPath);
			behaviour = new Behaviour(
					isActive, 
					actType,
					new GossiperTouchAction(manInteraction, getXGameCoord(), getYGameCoord()), 
					properties,
					spriteOrientation);

		}
		if(type == type.DECEIVER) {
			IBehaviourProperties properties = new DeceiverProperties();
			//Review
			OnAnimateTalkingAct actType = new OnAnimateTalkingAct(properties.getRotateProbability(), properties.getInteractProbability(), new GenericInteraction(this, new DeceiverAutonomousBehaviour()), spriteOrientation, framesPath);
			behaviour = new Behaviour(
					isActive, 
					actType,
					new DeceiverTouchAction(manInteraction, getXGameCoord(), getYGameCoord()),
					properties,
					spriteOrientation);
		}
		if(type == type.INFLUENCER) {
			IBehaviourProperties properties = new PromoterProperties();
			//Review
			OnAnimateTalkingAct actType = new OnAnimateTalkingAct(properties.getRotateProbability(), properties.getInteractProbability(), new GenericInteraction(this, new PromoterAutonomousBehaviour()), spriteOrientation, framesPath);
			behaviour = new Behaviour(
					isActive, 
					actType,
					new PromoterTouchAction(manInteraction, getXGameCoord(), getYGameCoord()),
					properties,
					spriteOrientation);
		}
		
		//Refactor into Behaviour
		setTouchAction();

		this.isActing = true;
	}
	
	//Implement onTouch action
	private void setTouchAction() {
		
		this.addListener(new ClickListener() {
			
			public void clicked(InputEvent event, float x, float y) 
		    {
				if(GameScoreState.validTouchAction()) {
					Gdx.app.debug("GameSprite", "Pressed at: x: "+x+", y: "+y+"");
					behaviour.onTouch();
				}
		    }
			
		});
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(isActive && isActing){
			this.setDrawable(new TextureRegionDrawable(new TextureRegion(behaviour.getCurrentFrame())));
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if(isActive && isActing){
			behaviour.onAct(delta, interactStatus, isInteracting == false ? interactorType == InteractorType.NONE ? false : true : true);
		}
	}

	public Orientation getOrientation() {
		return behaviour.getOrientation();
	}
	
	public boolean changeOrientationOnInvalid() {
		return behaviour.changeOrientationOnInvalid();
	}

	public enum Status { NEUTRAL, SELECTED, INFLUENCED }
	
	public enum InfluenceType { SUPPORT, OPPOSE, NONE }
	
	public enum InteractorType { FIRST, INTERMEDIATE, LAST, NONE}
	
}
