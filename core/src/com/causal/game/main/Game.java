package com.causal.game.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
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
import com.causal.game.gestures.DefaultGestures;
import com.causal.game.gestures.GameGestures;
import com.causal.game.gestures.SwipeInteraction;
import com.causal.game.interact.IInteractionType;
import com.causal.game.interact.IndividualInteractionType;
import com.causal.game.setup.GameGenerator;
import com.causal.game.state.Follower;
import com.causal.game.state.FollowerType;
import com.causal.game.state.GameScoreState;
import com.causal.game.state.GameScoreState.State;
import com.causal.game.state.PlayerState;
import com.causal.game.state.ScoreState;
import com.causal.game.tutorial.TutorialGameGenerator;
import com.causal.game.tutorial.TutorialSwipeInteraction;

public class Game extends ApplicationAdapter {
	
	Texture background;
	OrthographicCamera camera;
	private boolean isAndroid = false;
	public InputMultiplexer im = null;
	public Stage stage;
	
	//Make easier to access
	PlayerState plState = null;
	
	final int[] playerGoals = new int[2];
	State EndState = null;
	
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
		
		stage = setView();
		
		GameProperties.get().setStage(stage);
		
		createNewGame();
		
		setTitleScreen();
	}
	
	private void createNewGame() {		
		setGestureDetector(new GestureDetector(new GameGestures(stage)));
	}
	
	private void setGestureDetector(GestureDetector gd) {
		im = new InputMultiplexer(gd, stage);
		Gdx.input.setInputProcessor(im);
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
		
		if(scoreState != null && scoreState.getCurrentState() != State.FINISHED) {
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
	
	private void setScreen() {
		
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
//				 setCrowdScreen();
				 selectPlayerGoalScreen(0);
			 }
		});
	}
	
	private void selectPlayerGoalScreen(final int playerIdx) {

		
		final Actor playerBtn1 = getButton("PlayGameBtn");
		setToStage(playerBtn1, 0, -90);
		
		final Actor playerBtn2 = getButton("PlayGameBtn");
		setToStage(playerBtn2, 0, -190);
		
		playerBtn1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				playerGoals[playerIdx] = 0;
				
				if(playerIdx == 1) {
					playerBtn1.remove();
					playerBtn2.remove();
					setCrowdScreen();
				}
				else {
					selectPlayerGoalScreen(1);
				}
			}
		});
		
		
		playerBtn2.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				playerGoals[playerIdx] = 1;
				
				if(playerIdx == 1) {
					playerBtn1.remove();
					playerBtn2.remove();
					setCrowdScreen();
				}
				else {
					selectPlayerGoalScreen(1);
				}
			}
		});
	}
	
	
	private void setupGame() {
		
		Gdx.app.log("Game", "Is tutorial "+PlayerState.get().isFirstGame());
		gameGenerator = PlayerState.get().isFirstGame() ? TutorialGameGenerator.get() : new GameGenerator(); 
		
		gameGenerator.setGameVoteRules();
		
		setToStage(gameGenerator.getTopLabel(), 0, 0);
		setToStage(gameGenerator.getBottomLabel(), 0, -90);
		
		setGestureDetector(new GestureDetector(new DefaultGestures()));
	}
	
	private void setCrowdScreen() {
		
		Actor screen = getImage("GameScreen", "screens/screensPack");
		screen.setTouchable(Touchable.disabled);
		setToStage(screen, 0, 0);
		
		GameProperties.get().addActorToStage(GameProperties.get().getGameSpriteGroup());
		
		setFollowerScreen();
	
	}
	
	private void setFollowerScreen() {	
		
		final ArrayList<MoveableSprite> followers = new ArrayList<MoveableSprite>();
		final ArrayList<Image> placeHolders = new ArrayList<Image>();
	
		
		final List<Follower> plFollowers = plState.getFollowers();
		List<FollowerType> types = plState.getFollowerTypes();
		
		for(int i = 0; i < types.size(); i++) {
			Image placeHolder = (Image)createTargetImage("icons/iconsPack",WorldSystem.get().getHudXCoords().get(i), WorldSystem.get().getHudYCoords().get(i));
			placeHolders.add(placeHolder);
			for(Follower follower : plFollowers) {
				if(follower.type.head == types.get(i).head) {
					MoveableSprite followerInstance = new MoveableSprite(follower, WorldSystem.get().getHudXCoords().get(i), WorldSystem.get().getHudYCoords().get(i), placeHolder);
					followers.add(followerInstance);
				}
			}
		}
		
		final Actor playBtn = getButton("PlayGameBtn");
		setToStage(playBtn, 0, -290);
		
		playBtn.addListener(new ClickListener() {
			 public void clicked(InputEvent event, float x, float y) {
				 playBtn.remove();
				 activateGame(followers, placeHolders);
			 }
		});
		
	}
	
	private Actor createTargetImage(String framesPath, float origX, float origY) {
		Actor targetImage = getImage("ExpressionBox", framesPath);
		targetImage.setPosition(origX, origY);
		targetImage.setScale(WorldSystem.get().getLevelScaleFactor());
		GameProperties.get().addActorToStage(targetImage);
		targetImage.setTouchable(Touchable.disabled);
		
		return targetImage;
	}
	
	private void activateGame(List<MoveableSprite> followers, ArrayList<Image> placeHolders) {
		
		scoreState = new GameScoreState(gameGenerator.getLevelWinAmount(), gameGenerator.getWinState(), GameProperties.get().getGameSpriteGroup().getChildren().size);
		
		//Set dropped followers into game
		for(MoveableSprite follower : followers) {
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
		
		//Set remaining votes icon
		setVoteCount();
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
		font.getData().scale(3.5f);
		skin.add("default", new LabelStyle(font, Color.YELLOW));
		String value = scoreState.getRemaingVotes() < 10 ? "0"+Integer.toString(scoreState.getRemaingVotes()) : Integer.toString(scoreState.getRemaingVotes());
		remainingVotesCounter = new Label(value, skin);
		setToStage(remainingVotesCounter, 40, 200);
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
			String value = scoreState.getRemaingVotes() < 10 ? "0"+Integer.toString(scoreState.getRemaingVotes()) : Integer.toString(scoreState.getRemaingVotes());
			remainingVotesCounter.setText(value);
		}
		
		if(GameProperties.get().getTapCount() >= 0) {
			String value = Integer.toString(GameProperties.get().getTapCount());
			touchActionCounter.setText(value);
		}
		
		if(GameProperties.get().getSwipeCount() >= 0) {
			String value = Integer.toString(GameProperties.get().getSwipeCount());
			swipeCounter.setText(value);
		}
		
		
		if(scoreState.getCurrentState() == GameScoreState.State.SUPPORT) {
			Actor image = getImage("WinSprite", "sprites/textPack");
			setScoreStateSprite(image);
			EndState = GameScoreState.State.SUPPORT;

		}
		else if(scoreState.getCurrentState() == GameScoreState.State.OPPOSE) {
			Actor image = getImage("LoseSprite", "sprites/textPack");
			setScoreStateSprite(image);
			EndState = GameScoreState.State.OPPOSE;
		}
		else if(scoreState.getCurrentState() == GameScoreState.State.DRAW) {
			Actor image = getImage("DrawSprite", "sprites/textPack");
			setScoreStateSprite(image);
		}
		else if(scoreState.getCurrentState() == GameScoreState.State.FINISHED) {	
			setEndGameScreen();
		}
		
		if(scoreState.getCurrentState() != GameScoreState.State.PLAYING) {
			GameProperties.get().isAutoInteractionAllowed = true;
		}
	}
	
	private void setScoreStateSprite(Actor image) {
		image.setOriginX(image.getHeight()/2);
		image.setOriginY(image.getWidth()/2);
		image.scaleBy(-0.5f);
		setToStage(image, -40, 155);
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
		font.getData().scale(3.5f);
		skin.add("default", new LabelStyle(font, Color.YELLOW));

		String player1Result = "P1 WINS";
		String player2Result = "P2 WINS";
		
		if(EndState == GameScoreState.State.SUPPORT) {
			player1Result = playerGoals[0] == 0 ? "P1 WINS" : "P1 LOSES";
			player2Result = playerGoals[1] == 0 ? "P2 WINS" : "P2 LOSES";
		}
		else if(EndState == GameScoreState.State.OPPOSE) {
			player1Result = playerGoals[0] == 1 ? "P1 WINS" : "P1 LOSES";
			player2Result = playerGoals[1] == 1 ? "P2 WINS" : "P2 LOSES";
		}
		
		player1Goal = new Label(player1Result, skin);
		setToStage(player1Goal, 0, 150);
		
		player2Goal = new Label(player2Result, skin);
		setToStage(player2Goal, 0, 70);
		
		EndState = null;
	}
	
	private void setEndScoreValue() {
		final Skin skin = new Skin();
		BitmapFont font = new BitmapFont();
		font.getData().scale(3.5f);
		skin.add("default", new LabelStyle(font, Color.YELLOW));
		int score = plState.getReputationPoints() + scoreState.getEndScore();
		plState.setReputationPoints(score);
		String value = Integer.toString(score);
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
		createNewGame();
	}


	public enum Head { GOSSIPER, INFLUENCER, DECEIVER}

}
