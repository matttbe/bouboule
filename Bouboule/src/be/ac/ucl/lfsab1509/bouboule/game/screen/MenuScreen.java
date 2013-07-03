package be.ac.ucl.lfsab1509.bouboule.game.screen;

/*
 * This file is part of Bouboule.
 * 
 * Copyright 2013 UCLouvain
 * 
 * Authors:
 *  * Group 7 - Course: http://www.uclouvain.be/en-cours-2013-lfsab1509.html
 *    Matthieu Baerts <matthieu.baerts@student.uclouvain.be>
 *    Baptiste Remy <baptiste.remy@student.uclouvain.be>
 *    Nicolas Van Wallendael <nicolas.vanwallendael@student.uclouvain.be>
 *    Hélène Verhaeghe <helene.verhaeghe@student.uclouvain.be>
 * 
 * Bouboule is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class MenuScreen extends AbstractScreen {
	

	public MenuScreen() {
		super(false);
	}

	@Override
	public void show() {
		super.show();

		//Set Background
		
		addBackGround("drawable-xhdpi/bgmenu.jpg");
		
		//Create all Buttons - Play Button
		
		
		
		Image imgBoubouleR = new Image(new Texture("drawable-xhdpi/boubouleright.png"));
		Image imgBoubouleL = new Image(new Texture("drawable-xhdpi/boubouleleft.png"));
		this.stage.addActor(imgBoubouleR);
		this.stage.addActor(imgBoubouleL);
		imgBoubouleL.setPosition(-800, 0);
		imgBoubouleR.setPosition(800, 0);
		
		final ActionBouboul actionbouL = new ActionBouboul(false);
		this.stage.addAction(actionbouL);
		actionbouL.setActor(imgBoubouleL);
		
		final ActionBouboul actionbouR = new ActionBouboul(true);
		this.stage.addAction(actionbouR);
		actionbouR.setActor(imgBoubouleR);
		
		Label titre = new Label("BouBoule",getSkin());
		this.stage.addActor(titre);
		titre.setFontScale(5);
		
		
		Button playButton  		= createButton("transparent", 430, 160, 200, 725);
		Button paramButton 		= createButton("transparent", 430, 160, 200, 555);
		Button scoreButton 		= createButton("transparent", 430, 160, 200, 385);
		Button boubouleButton	= createButton("transparent", 500, 350, 200, 0);
		
		
		playButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "clickStart " + x + ", " + y);
				GlobalSettings.GAME.setScreenGame();
			}
		});
		
		paramButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "clickParam " + x + ", " + y);
				GlobalSettings.GAME.setScreen(new ParamScreen());
			}
		});
		
		scoreButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "clickScore " + x + ", " + y);
				//TODO: setScreen(new HighScreen()); // or something else
			}
		});
		
		boubouleButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "clickBouboule " + x + ", " + y);
				actionbouL.init();
				actionbouR.init();
			}
		});

	}
	
	private class ActionBouboul extends Action {
		private float position;
		private boolean right;
		public ActionBouboul(boolean right) {
			init();
			position=-850f;
			this.right=right;
		}
		public boolean act(float delta) {
			position+= delta*1100f;
			if(position>=800f)position=800f;
			if(right){
				actor.setPosition(800f-position, (800f-position)/3.5f);
			}else{
				actor.setPosition(-800f+position, (800f-position)/3.5f);
			}
			return false;
		}
		
		public void init(){
			position = 0f;
		}
		
	}
	
	private class ActionTitle extends Action{
		private float timer;
		
		public ActionTitle(){
			init();
		}
		public boolean act(float delta) {
			timer = timer + delta;
			return false;
		}
		
		public void init(){
			timer=0;
		}
		
	}
}
