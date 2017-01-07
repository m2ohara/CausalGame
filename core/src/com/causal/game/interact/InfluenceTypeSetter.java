package com.causal.game.interact;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.causal.game.main.GameProperties;
import com.causal.game.main.GameSprite;

public class InfluenceTypeSetter {
	
	public static void setHandSign(GameSprite interactor, Actor sprite) {
		Gdx.app.debug("InfluenceTypeSetter", "Setting first interactor sprite for status: "+interactor.interactStatus);

		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(interactor.getStartingX(), interactor.getStartingY());
		
		sprite.setTouchable(Touchable.disabled);
		
		GameProperties.get().addActorToStage(sprite);
	}
	
	public static void setBlock(GameSprite interactor, Actor sprite) {
		Gdx.app.debug("InfluenceTypeSetter", "Setting interactor sprite for status: "+interactor.interactStatus);
		
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(interactor.getStartingX(), interactor.getStartingY());
		
		sprite.setTouchable(Touchable.disabled);
		
		GameProperties.get().addActorToStage(sprite);
		sprite.setZIndex(GameProperties.get().getStageActor("GameScreen").getZIndex()+1);
	}

}
