package com.causal.game.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.causal.game.main.GameProperties;
import com.causal.game.sprite.DropSprite;
import com.causal.game.sprite.GameSprite;
import com.causal.game.state.Follower;

public class TutorialDropSprite extends DropSprite {

	public TutorialDropSprite(Follower follower, float x, float y,
			Image sourceTargetImage) {
		super(follower, x, y, sourceTargetImage);
	}
	
	protected void iterateDropTargets() {
		Group group = GameProperties.get().getGameSpriteGroup();
		for(Actor actor : group.getChildren()) {
			
			GameSprite gs = (GameSprite)actor;
			
			Gdx.app.log(this.getClass().getName(), "sprite: "+gs.getXGameCoord()+", "+gs.getYGameCoord());
			
			if(((GameSprite)actor).getXGameCoord() == 1 && ((GameSprite)actor).getYGameCoord() == 2) {
				addDropTarget(actor);
			}
		}
	}

}
