package com.causal.game.main;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.causal.game.setup.GameGenerator;
import com.causal.game.sprite.DropSprite;
import com.causal.game.sprite.GameSprite;
import com.causal.game.sprite.SwipeSprite;
import com.causal.game.state.Follower;
import com.causal.game.state.FollowerType;
import com.causal.game.state.GameScoreState;
import com.causal.game.state.GameScoreState.State;
import com.causal.game.state.GameScoreState.VoteState;
import com.causal.game.state.PlayerState;
import com.causal.game.state.ScoreState;
import com.causal.game.tutorial.TutorialGameGenerator;

public class Game extends ApplicationAdapter {
	
	Texture background;
	OrthographicCamera camera;
	private boolean isAndroid = false;
	private InputMultiplexer multiplexer = null;
	public Stage gameStage;
	
	//Make easier to access
	PlayerState plState = null;
	
	final int[] playerGoals = new int[2];
	
	//Refactor to GameSetup
	private GameScoreState scoreState = null;
	GameGenerator gameGenerator = null;
//	IInteractionType interactionType = null;
	Label remainingVotesCounter = null;
	Label player1Goal = null;
	Label player2Goal = null;
	Label touchActionCounter = null;
	Label endScoreCounter = null;
	Label swipeCounter = null;
	boolean isAssetsLoaded = false;
	
	public static float universalTimeRatio = 0.7f;

	public boolean isAndroid() {
		return isAndroid;
	}

	public void setAndroid(boolean isAndroid) {
		this.isAndroid = isAndroid;
	}

	@Override
	public void create () {
		
    	Gdx.app.setLogLevel(2);
    	Gdx.app.log("Game", "Creating Game");

		plState = PlayerState.get();
		plState.loadDummy();
		
		gameStage = setView();
		
		GameProperties.get().setStage(gameStage);
		
		GameProperties.get().setMainStage(setView());
		
		setGestureDetector();
		
		setTitleScreen();
	}
	
	private void setGestureDetector() {
		multiplexer = new InputMultiplexer(gameStage, GameProperties.get().getMainStage());
		Gdx.input.setInputProcessor(multiplexer);
	}
	
	private Stage setView() {
		Stage stage = null;
		if(!isAndroid) {
			stage = new Stage();
		}
		else {
			stage = setViewport(stage);
		}
		
		return stage;
	}
	
	private Stage setViewport(Stage stage) {
		OrthographicCamera camera = new OrthographicCamera(1080, 1520);
		StretchViewport viewport = new StretchViewport(1080, 1920);
		camera.zoom = 0.38f;
		stage = new Stage(viewport);
		stage.getViewport().setCamera(camera);
		
		return stage;
	}

	@Override
	public void render () {
		
		if(!isAssetsLoaded) { loadAssets();}

		GameProperties.get().renderStage();
		
		if(scoreState != null && scoreState.isActive && scoreState.getCurrentState() != State.FINISHED) {
			updateScoreState();
		}
	}
	
	@Override
	public void resize(int width, int height) {
		GameProperties.get().resizeStage(height, width);
	}
	@Override
	public void dispose() {
		GameProperties.get().dispose();
		this.dispose();
	}
	
	
	private Actor getButton(String type) {
		TextureAtlas buttonAtlas;
		Skin skin = new Skin();
		BitmapFont font = new BitmapFont();
		TextButtonStyle style = new TextButtonStyle();
		
		buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttonsPack.pack"));
		skin.addRegions(buttonAtlas);
		style.font = font;
		style.up = skin.getDrawable(type);
		style.down = skin.getDrawable(type);
		
		return new TextButton("", style);
	}
	
	private Actor getImage(String type, String pack) {

		TextureAtlas txAtlas = null;
		Skin txSkin = null;
		
		try {
			txAtlas = new TextureAtlas(Gdx.files.internal(pack+".pack"));
			txSkin = new Skin(txAtlas);
		}
		catch(Exception e) {
		    Gdx.app.log("Game", "Exception "+e.getMessage());			
		}
	
		Actor image = new Image(txSkin.getDrawable(type));
		image.setName(type);
		image.setTouchable(Touchable.disabled);
		return image;
	}
	
	public void setToStage(Actor actor, float _xCentreOffset, float _yCentreOffset) {
		
		setRelativePosition(actor, _xCentreOffset, _yCentreOffset);

		GameProperties.get().addActorToStage(actor);
	}
	
	protected void setRelativePosition(Actor actorToSet, float _xCentreOffset, float _yCentreOffset) {
		
		//Centre actor
		float x = (Gdx.graphics.getWidth() - actorToSet.getWidth()) /2 ;
		float y = (Gdx.graphics.getHeight()  - actorToSet.getHeight()) / 2;
		
		x += _xCentreOffset;
		y += _yCentreOffset;
		
		actorToSet.setPosition(x, y);
		
		_xCentreOffset = actorToSet.getX();
		_yCentreOffset = actorToSet.getY();
	}
	
	private void loadAssets() {
		
		if(!Assets.get().isLoaded()) {
			Assets.get().load();
		}
		else {
			setPlayButton();
			isAssetsLoaded = true;
		}
	}
	
	private void setTitleScreen() {
		
		setToStage(getImage("TitleScreen", "screens/screensPack"), 0, 0);
		Actor btn = getButton("LoadingBtn");
		setToStage(btn, 0, -260);
		
	}
	
	private void setPlayButton() {
		
		final Actor screen = getImage("TitleScreen", "screens/screensPack");
		setToStage(screen, 0, 0);
		final Actor btn = getButton("PlayGameBtn");
		setToStage(btn, 0, -260);
		
		btn.addListener(new ClickListener() {
			 public void clicked(InputEvent event, float x, float y) {
				 btn.remove();
				 setSpeechScreen();
				 screen.remove();
			 }
		});
	}
	
	private void setSpeechScreen() {
		
		if(Assets.get().isLoaded()) {
			final Actor screen = getImage("SpeechScreen", "screens/screensPack");
			setToStage(screen, 0, 0);
			final Actor btn = getButton("CreateSpeechBtn");
			setToStage(btn, 0, -260);
			
			btn.addListener(new ClickListener() {
				 public void clicked(InputEvent event, float x, float y) {
					 btn.remove();
					 displaySpeechScroll();
				 }
			});
		}
	}
	
	private void displaySpeechScroll() {
		Actor icon = getImage("Scroll", "icons/iconsPack");
		setToStage(icon, 0, -60);
		
		final Actor btn = getButton("PlayGameBtn");
		setToStage(btn, 0, -260);
		
		setupGame();
		
		btn.addListener(new ClickListener() {
			 public void clicked(InputEvent event, float x, float y) {
				 btn.remove();
				 selectPlayerGoalScreen(0);
			 }
		});
	}
	
	private void selectPlayerGoalScreen(final int playerIdx) {
		

		final Actor playerLabel = getButton("Player"+(playerIdx+1)+"Btn");
		setToStage(playerLabel, 0, 140 - (playerIdx*40));
		
		final Actor winVotesLabel = getButton("WinVotesBtn");
		setToStage(winVotesLabel, 0, -210);
		
		final Actor winTickBtn = getButton("TickBtn");
		setToStage(winTickBtn, 160, -210);
		
		final Actor loseVotesLabel = getButton("LoseVotesBtn");
		setToStage(loseVotesLabel, 0, -260);
		
		final Actor loseTickBtn = getButton("TickBtn");
		setToStage(loseTickBtn, 160, -260);
		
		winTickBtn.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				playerGoals[playerIdx] = 0;
				
				if(playerIdx == 1) {
					winVotesLabel.remove();
					loseVotesLabel.remove();
					winTickBtn.remove();
					setCrowdScreen();
				}
				else {
					selectPlayerGoalScreen(1);
				}
				
				playerLabel.remove();
			}
		});
		
		
		loseTickBtn.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				playerGoals[playerIdx] = 1;
				
				if(playerIdx == 1) {
					winVotesLabel.remove();
					loseVotesLabel.remove();
					winTickBtn.remove();
					loseTickBtn.remove();
					setCrowdScreen();
				}
				else {
					selectPlayerGoalScreen(1);
				}
				
				playerLabel.remove();
			}
		});
	}
	
	
	private void setupGame() {
		
		Gdx.app.debug("Game", "Is tutorial "+PlayerState.get().isFirstGame());
		gameGenerator = PlayerState.get().isFirstGame() ? TutorialGameGenerator.get() : new GameGenerator(); 
		
		gameGenerator.setGameVoteRules();
		
		setToStage(gameGenerator.getTopLabel(), 0, 10);
		setToStage(gameGenerator.getMiddleLabel(), 0, -50);
		setToStage(gameGenerator.getBottomLabel(), 0, -120);
		
//		setGestureDetector(new GestureDetector(new DefaultGestures()));
		setGestureDetector();
	}
	
	private void setCrowdScreen() {
		
		Actor screen = getImage("GameScreen", "screens/screensPack");
		screen.setTouchable(Touchable.disabled);
		setToStage(screen, 0, 0);
		
		final Actor playerLabel = getButton("Player1Btn");
		setToStage(playerLabel, 0, 100);
		
		GameProperties.get().addActorToStage(GameProperties.get().getGameSpriteGroup());
		
		scoreState = new GameScoreState(gameGenerator.getLevelWinAmount(), gameGenerator.getVoteState(), GameProperties.get().getGameSpriteGroup().getChildren().size);

		setVoteCount();
		
		setVoteImage();
		
		setItemDropScreen();
	
	}
	
	private void setItemDropScreen() {	
		
		final ArrayList<DropSprite> followers = new ArrayList<DropSprite>();
		final ArrayList<Image> placeHolders = new ArrayList<Image>();
		
		gameGenerator.createDropSprites(followers, placeHolders);
		
		final Actor playerLabel = getButton("Player2Btn");
		
		final Actor playBtn = getButton("PlayGameBtn");
		setToStage(playBtn, 0, -290);
		
		playBtn.addListener(new ClickListener() {
			 public void clicked(InputEvent event, float x, float y) {
				 playBtn.remove();
				 activateGame(followers, placeHolders);
				 
				 setToStage(playerLabel, 0, 100);
			 }
		});
		
	}
	
//	private void createDropSprites(ArrayList<DropSprite> followers, ArrayList<Image> placeHolders) {
//		final List<Follower> plFollowers = plState.getFollowers();
//		List<FollowerType> types = plState.getFollowerTypes();
//		
//		for(int i = 0; i < types.size(); i++) {
//			Image placeHolder = (Image)createTargetImage("icons/iconsPack",WorldSystem.get().getHudXCoords().get(i), WorldSystem.get().getHudYCoords().get(i));
//			placeHolders.add(placeHolder);
//			for(Follower follower : plFollowers) {
//				if(follower.type.head == types.get(i).head) {
//					DropSprite followerInstance = new DropSprite(follower, WorldSystem.get().getHudXCoords().get(i), WorldSystem.get().getHudYCoords().get(i), placeHolder);
//					followers.add(followerInstance);
//				}
//			}
//		}
//	}
//	
//	private Actor createTargetImage(String framesPath, float origX, float origY) {
//		Actor targetImage = getImage("ExpressionBox", framesPath);
//		targetImage.setPosition(origX, origY);
//		targetImage.setScale(WorldSystem.get().getLevelScaleFactor());
//		GameProperties.get().addActorToStage(targetImage);
//		targetImage.setTouchable(Touchable.disabled);
//		
//		return targetImage;
//	}
	
	private void activateGame(List<DropSprite> followers, ArrayList<Image> placeHolders) {
		
		scoreState.isActive = true;
		
		//Set dropped followers into game
		for(DropSprite follower : followers) {
			if(follower.isActive()) {
				GameProperties.get().replaceActorInGroup(follower);
			}
			else {
				//Remove remaining followers
				follower.getSourceSprite().remove();
			}
		}
		
		//Remove placeholders
		for(Image placeHolder : placeHolders) {
			placeHolder.remove();
		}
		
		//Activate crowd members
		for(GameSprite actor : GameProperties.get().getGameSprites()) {
			actor.create(gameGenerator.getInteractionType());
		}
		setReputationCount();
		
		setTapCount();
		setSwipeCount();
		
		SwipeSprite.get().activate();
		
		GameProperties.get().activateActors();
		
	}
	
	private void setReputationCount() {
		final Skin skin = new Skin();
		BitmapFont font = new BitmapFont();
		font.getData().scale(1.5f);
		skin.add("default", new LabelStyle(font, Color.YELLOW));
		String value = Integer.toString(PlayerState.get().getReputationPoints());
		setToStage(new Label(value, skin), 40, 270);
	}
	
	private void setVoteCount() {
		final Skin skin = new Skin();
		BitmapFont font = new BitmapFont();
		font.getData().scale(3.0f);
		skin.add("default", new LabelStyle(font, Color.YELLOW));
		String value = scoreState.getRemaingVotes() < 10 ? "0"+Integer.toString(scoreState.getRemaingVotes()) : Integer.toString(scoreState.getRemaingVotes());
		remainingVotesCounter = new Label(value, skin);
		setToStage(remainingVotesCounter, 40, 200);
	}
	
	private void setVoteImage() {
		Actor goalType;
		if(scoreState.getVoteState() == VoteState.SUPPORT) {
			goalType = new Image(new TextureAtlas(Gdx.files.internal("sprites/Meep/Gestures/HandSigns.pack")).getRegions().get(0));
		}
		else {
			goalType = new Image(new TextureAtlas(Gdx.files.internal("sprites/Meep/Gestures/HandSigns.pack")).getRegions().get(1));
		}
		goalType.setScale(2f);
		setToStage(goalType, -120, 120);
	}
	
	private void setTapCount() {
		final Skin skin = new Skin();
		BitmapFont font = new BitmapFont();
		font.getData().scale(1.5f);
		skin.add("default", new LabelStyle(font, Color.YELLOW));
		String value = Integer.toString(GameProperties.get().getTapCount());
		touchActionCounter = new Label(value, skin);
		setToStage(touchActionCounter, -30, -250);
	}
	
	private void setSwipeCount() {
		final Skin skin = new Skin();
		BitmapFont font = new BitmapFont();
		font.getData().scale(1.5f);
		skin.add("default", new LabelStyle(font, Color.YELLOW));
		String value =  Integer.toString(GameProperties.get().getSwipeCount());
		swipeCounter = new Label(value, skin);
		setToStage(swipeCounter, 70, -250);
	}
	
	private void updateScoreState() {
		scoreState.update();
		
		//Update remaining votes icon
		if(scoreState.getRemaingVotes() >= 0) {
			String value = Integer.toString(scoreState.getWinVotes() - scoreState.getRemaingVotes());
			String total = Integer.toString(scoreState.getWinVotes());
			remainingVotesCounter.setText(value + "/"+total);
		}
		
		if(GameProperties.get().getTapCount() >= 0) {
			String value = Integer.toString(GameProperties.get().getTapCount());
			touchActionCounter.setText(value);
		}
		
		if(GameProperties.get().getSwipeCount() >= 0) {
			String value = Integer.toString(GameProperties.get().getSwipeCount());
			swipeCounter.setText(value);
		}
		
		
		if(scoreState.getCurrentState() == GameScoreState.State.WIN) {

			String result = scoreState.getVoteState() == VoteState.SUPPORT ? "Success!" : "Success!";
			setScoreStateSprite("Mission "+result);	
		}
		else if(scoreState.getCurrentState() == GameScoreState.State.LOSE) {
			
			String result = scoreState.getVoteState() == VoteState.SUPPORT ? "Fail" : "Fail";
			setScoreStateSprite("Mission "+result);	
		}
		else if(scoreState.getCurrentState() == GameScoreState.State.DRAW) {

			setScoreStateSprite("Vote Undetermined");
		}
		else if(scoreState.getCurrentState() == GameScoreState.State.FINISHED) {	
			setEndGameScreen();
		}
		
		if(scoreState.getCurrentState() != GameScoreState.State.PLAYING) {
			GameProperties.get().isAutoInteractionAllowed = true;
		}
	}
	
	private void setScoreStateSprite(String result) {
		swipeCounter.remove();
		touchActionCounter.remove();

		final Skin skin = new Skin();
		BitmapFont font = new BitmapFont();
		font.getData().scale(3f);
		skin.add("default", new LabelStyle(font, Color.YELLOW));
		Label image = new Label(result, skin);
		
		image.setOriginX(image.getHeight()/2);
		image.setOriginY(image.getWidth()/2);
		image.scaleBy(-0.5f);
		setToStage(image, 0, -260);
	}
	
	private void setEndGameScreen() {
		setToStage(getImage("EndScreen", "screens/screensPack"), 0, 0);
		
		setPlayerCoopResults();
		
		setEndScoreValue();
		
		ScoreState.get().setLevel();
		
		setFollowerRewards();
		
		final Actor btn = getButton("PlayGameBtn");
		setToStage(btn, 0, -260);	
		btn.addListener(new ClickListener() {
			 public void clicked(InputEvent event, float x, float y) {
				btn.remove();
				disposeGame();
				setSpeechScreen();
			 }
		});
	}
	
	private void setPlayerCoopResults() {
		final Skin skin = new Skin();
		BitmapFont font = new BitmapFont();
		font.getData().scale(1.5f);
		skin.add("default", new LabelStyle(font, Color.YELLOW));

		
		String player1Result = playerGoals[0] == 0 ? "P1 SPLIT" : "P1 STOLE";
		String player2Result = playerGoals[1] == 0 ? "P2 SPLIT" : "P2 STOLE";
		
		Gdx.app.log("Game", "Score state "+scoreState.getWinState().toString());

		
		int score = plState.getReputationPoints() + scoreState.getEndScore();
		plState.setReputationPoints(score);
		
		//Both cooperate
		if(playerGoals[0] == 0 && playerGoals[1] == 0) {
			player1Result += ": "+Integer.toString(scoreState.getEndScore()/2);
			player2Result += ": "+Integer.toString(scoreState.getEndScore()/2);
		}
		
		//Both defect
		else if(playerGoals[0] == 1 && playerGoals[1] == 1) {
			player1Result += ": 0";
			player2Result += ": 0";
		}
		
		
		else {
			//Player one sucker's payoff
			if(playerGoals[0] == 0 && playerGoals[1] == 1) {
				player1Result += ": 0";
				player2Result += ": "+Integer.toString(scoreState.getEndScore());
			}
			//Player two sucker's payoff
			else if(playerGoals[0] == 1 && playerGoals[1] == 0) {
				player1Result += ": "+Integer.toString(scoreState.getEndScore());
				player2Result += ": 0";
			}
		}
		
		player1Goal = new Label(player1Result, skin);
		setToStage(player1Goal, 0, 120);
		
		player2Goal = new Label(player2Result, skin);
		setToStage(player2Goal, 0, 70);
	}
	
	private void setEndScoreValue() {
		final Skin skin = new Skin();
		BitmapFont font = new BitmapFont();
		font.getData().scale(2.5f);
		skin.add("default", new LabelStyle(font, Color.YELLOW));

		String value = Integer.toString(scoreState.getEndScore());
		remainingVotesCounter = new Label(value, skin);
		setToStage(remainingVotesCounter, 0, -70);
	}
	
	private void setFollowerRewards() {
		int levelUpPoints = plState.getLevelUpThreshold();
		int points = plState.getReputationPoints();
		if(points >= levelUpPoints) {
			setRewardFollowers(points / levelUpPoints);
			plState.setReputationPoints(points % levelUpPoints);
		}
	}
	
	private void setRewardFollowers(int amount) {
		
		List<Follower> rewardedFollowers = gameGenerator.generateRewardFollowers(amount);
		
		setRewardFollowers(rewardedFollowers);
	}
	
	private void setRewardFollowers(List<Follower> rewardedFollowers) {
		
		for(int count = 0; count < rewardedFollowers.size(); count++) {
			setRewardImage(rewardedFollowers.get(count).type.imagePath, WorldSystem.get().getHudXCoords().get(count), WorldSystem.get().getRewardYCoord());
		}
	}
	
	private void setRewardImage(String framesPath, float origX, float origY) {
		Image targetImage = new Image(new TextureAtlas(Gdx.files.internal(framesPath+"Default.pack")).getRegions().get(0));
		targetImage.setPosition(origX, origY);
		GameProperties.get().addActorToStage(targetImage);
		targetImage.setTouchable(Touchable.disabled);
	}
	
	private void disposeGame() {
		
		if(PlayerState.get().isFirstGame() && TutorialGameGenerator.Round == 1) { PlayerState.get().isFirstGame(false);}
		scoreState = null;
		GameProperties.get().dispose();
		WorldSystem.get().dispose();
		setGestureDetector();
	}


	public enum Head { GOSSIPER, INFLUENCER, DECEIVER}

}
