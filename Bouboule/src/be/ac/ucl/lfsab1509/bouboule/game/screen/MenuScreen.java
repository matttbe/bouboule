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

import java.text.Normalizer;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.profile.HighScoreInfo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class MenuScreen extends AbstractScreen {

	public MenuScreen() {
		super(false);
	}

	@Override
	public void show() {
		super.show();

		// Set Background

		addBackGround("drawable-xhdpi/bgmenu.jpg");

		// Create the 2 Bouboules out of the screen

		Image imgBoubouleR = addImage("drawable-xhdpi/boubouleright.png",800,0);
		Image imgBoubouleL = addImage("drawable-xhdpi/boubouleleft.png",-800,0);

		
		//add action on the bouboule
		final ActionBouboul actionbouL = new ActionBouboul(false);
		this.stage.addAction(actionbouL);
		actionbouL.setActor(imgBoubouleL);

		final ActionBouboul actionbouR = new ActionBouboul(true);
		this.stage.addAction(actionbouR);
		actionbouR.setActor(imgBoubouleR);

		//add the title
		Label title = 
				addLabel("BOUBOULE", "darktimes-font", 1f, Color.WHITE,
				100, 1000);

		//add action on the title
		final ActionTitle actiontitre = new ActionTitle();
		this.stage.addAction(actiontitre);
		actiontitre.setActor(title);

		//Add 5 button transparent
		Button playButton = createButton("transparent", 430, 160, 200, 725);
		Button paramButton = createButton("transparent", 430, 160, 200, 555);
		Button scoreButton = createButton("transparent", 430, 160, 200, 385);
		Button boubouleButton = createButton("transparent", 500, 350, 200, 0);
		Button titleButton = createButton("transparent", 500, 500, 200, 885);
		
		//Listener for the button
		playButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickStart " + x + ", " + y);
				GlobalSettings.GAME.setScreenGame();
			}
		});

		paramButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickParam " + x + ", " + y);
				GlobalSettings.GAME.setScreen(new ParamScreen());
			}
		});

		scoreButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickScore " + x + ", " + y);

				new Dialog("HighScore", getSkin(), "default") { // TODO: improved default
					// protected void result(Object object) {} // Just hide the dialog
				}.text(getHighScoreText()).button("Close", null).show(stage);
			}
		});

		boubouleButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickBouboule " + x + ", " + y);
				actionbouL.init();
				actionbouR.init();
			}
		});
		
		titleButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickBouboule " + x + ", " + y);
				actiontitre.init();
			}
		});

	}

	/**
	 * @param text Text to be nomalise
	 * @return a 'normalised' text (without special chars)
	 */
	private String normaliseTextForTitle(String text) {
		return Normalizer.normalize(text, Normalizer.Form.NFD)
			.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase();
	}

	private String getHighScoreText () {
		HighScoreInfo highscores[] = GlobalSettings.PROFILE_MGR.getProfileGlobal ().getAllHighScores (false);

		// no highscore: display a message even if there is no high scores
		if (highscores.length == 0 || highscores[0] == null) { // the first time, we receive only one elem which is null
			return "No high score yet";
		}

		String cText = "";
		int i;
		for (i = 0; i < highscores.length; i++) {
			HighScoreInfo info = highscores[i];

			if (info == null)
				break;

			// note: we have to used only chars that are available in the font
			cText += normaliseTextForTitle(info.getName()) + " - Score "
					+ info.getScore() + " - Level " + info.getLevel() + "\n";
		}

		return cText;
	}

	private class ActionBouboul extends Action {
		private float position;
		private boolean right;

		//Constructor of Action
		public ActionBouboul(boolean right) {
			init();
			//initial position
			position = -850f;
			//true if it's the action for the right Bouboule 
			this.right = right;
		}

		public boolean act(float delta) {
			position += delta * 1100f;
			//max position 800f
			if (position >= 800f)
				position = 800f;
			// set position for the right and left Bouboule
			if (right) {
				actor.setPosition(800f - position, (800f - position) / 3.5f);
			} else {
				actor.setPosition(-800f + position, (800f - position) / 3.5f);
			}
			return false;
		}

		
		//reset the position when the player push on the Bouboule
		public void init() {
			position = 0f;
		}

	}

	private class ActionTitle extends Action {
		private float timer;

		public ActionTitle() {
			init();
		}

		public boolean act(float delta) {
			Label temp = (Label) actor;
			timer = timer + delta;
			//var witch set the time since the period
			float temptimer;
			
			if (timer < 1.0f) {
				//1) emergence during 1sec
				temptimer=timer;
				temp.setFontScale(0.0f + temptimer * 0.6f);
			} else if (timer < 4.0f) {
				//2)beat during 3 sec
				temptimer=timer % 1.0f;
				if(temptimer < 0.5f){
				}else{
					temptimer = 1f-temptimer;
				}
				temp.setFontScale(0.6f + temptimer * 0.1f);
			} else if (timer < 5.0f) {
				//3) extinction during 1sec
				temptimer=timer-4.0f;
				temp.setFontScale(0.6f - temptimer * 0.6f);
			} else {
				//4)restart
				timer = timer-5.0f;
				
				String name;
				if(MathUtils.random(2)==0){
					name="BOUBOULE";
				}else{
					name=normaliseTextForTitle(GlobalSettings.PROFILE.getName());
				}
				
				switch (MathUtils.random(7)) {
				case 0:
					temp.setText("HELLO\n"+name);
					break;
				case 1:
					temp.setText("MAY THE\nBOUBOULE BE\nWITH YOU");
					break;
				case 2:
					temp.setText("BIG BOUBOULE\nIS WATCHING\n YOU");
					break;
				case 3:
					temp.setText("KICK THEM\n ALL!");
					break;
				case 4:
					temp.setText("WELCOME TO\n" + name);
					break;
				case 5:
					temp.setText(""+name+"\nIS GREAT");
					break;
				default:
					temp.setText("BOUBOULE");
					break;
				}
			}
			
			//center the title
			temp.setPosition(GlobalSettings.APPWIDTH/2.0f-temp.getTextBounds().width/2, 950);


			//wrapper.setTransform(true);
			//wrapper.setOrigin(wrapper.getPrefWidth() / 2, wrapper.getPrefHeight() / 2);
			//wrapper.setRotation(45);
			return false;
		}

		//rest the time when the player push on the title
		public void init() {
			timer = 0.0f;
		}

	}
}
