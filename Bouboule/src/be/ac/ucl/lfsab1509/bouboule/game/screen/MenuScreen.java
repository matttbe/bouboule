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
import be.ac.ucl.lfsab1509.bouboule.game.profile.HighScoreInfo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;


public class MenuScreen extends AbstractScreen {
	

	private Label title;

	public MenuScreen() {
		super(false);
	}

	@Override
	public void show() {
		super.show();

		// Set Background

		addBackGroundShift("GdxMenus/main/mainmenubg.jpg");
		
		Gdx.app.log("SCREEN", ""+GlobalSettings.SHIFT_BG);
		
		// Add Button image

		addBackGround("GdxMenus/main/mainmenubuttons.png");

		// Create the 2 Bouboules out of the screen

		final Image imgBoubouleR = addImage("GdxMenus/main/boubouleright.png",
				(int) GlobalSettings.APPWIDTH, 0);
		final Image imgBoubouleL = addImage("GdxMenus/main/boubouleleft.png",
				(int) -GlobalSettings.APPWIDTH, 0);

		// add action on the bouboule
		final ActionBouboul actionbouL = new ActionBouboul(false);
		this.stage.addAction(actionbouL);
		actionbouL.setActor(imgBoubouleL);

		final ActionBouboul actionbouR = new ActionBouboul(true);
		this.stage.addAction(actionbouR);
		actionbouR.setActor(imgBoubouleR);

		// add the title
		title = new Label("BOUBOULE", getSkin(), "darktimes-font",
				new Color(.388f, .733f, .984f, 1f));
		title.setFontScale(GlobalSettings.HD); // TODO: removed when darktimes font will be bigger
		title.setAlignment(Align.center); // center
		title.setWidth(GlobalSettings.APPWIDTH);
		// title.setColor(.2f, .7098f, .898f, 1f);  // android color

		// add the title in a table in order to rotate it.
		Table tableTitle = new Table();
		tableTitle.add(title);
		tableTitle.setTransform(true);
		tableTitle.setWidth(GlobalSettings.APPWIDTH); // all the width
		tableTitle.setX(0);
		tableTitle.setY((int) (1050 * GlobalSettings.HD));
		tableTitle.setOrigin(GlobalSettings.APPWIDTH / 2,
				tableTitle.getHeight() / 2); // at the center
		stage.addActor(tableTitle);

		// add action on the title
		final ActionTitle actiontitle = new ActionTitle();
		this.stage.addAction(actiontitle);
		actiontitle.setActor(tableTitle);

		// Add 5 button transparent
		Button playButton, paramButton, scoreButton, boubouleButton, titleButton;
		if (GlobalSettings.ISHD) {
			playButton  = createButton("transparent", 690, 250, 312, 1150);
			paramButton = createButton("transparent", 690, 250, 312, 887);
			scoreButton = createButton("transparent", 690, 250, 312, 615);
			boubouleButton = createButton("transparent", 1000, 600, 200, 0);
			titleButton    = createButton("transparent", 1000, 1000, 200, 1450);
		}
		else {
			playButton = createButton("transparent", 430, 160, 200, 725);
			paramButton = createButton("transparent", 430, 160, 200, 555);
			scoreButton = createButton("transparent", 430, 160, 200, 385);
			boubouleButton = createButton("transparent", 500, 350, 200, 0);
			titleButton = createButton("transparent", 500, 500, 200, 885);
		}

		// Listener for the button
		playButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickStart " + x + ", " + y);
				if (GlobalSettings.GAME.isPlayingGame())
					setScreenWithFading(null);
				else
					setScreenWithFading(new WorldScreen());
			}
		});

		paramButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickParam " + x + ", " + y);
				setScreenWithFading(new ParamScreen());
			}
		});

		scoreButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "Show LeaderBoard " + x + ", " + y);

				if (GlobalSettings.GAMECENTER != null) {
					GlobalSettings.GAMECENTER.showLeaderboard();
				}
				else {
					new Dialog("HighScore", getSkin(), "default") {
						// TODO: improved default skin
						// protected void result(Object object) {} // Just hide the dialog
					}.text(getHighScoreText()).button("Close", null).show(stage);
				}
			}
		});

		boubouleButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickBouboule " + x + ", " + y);
				final ActionBouboul actionbouL = new ActionBouboul(false);
				actionbouL.init();
				stage.addAction(actionbouL);
				actionbouL.setActor(imgBoubouleL);

				final ActionBouboul actionbouR = new ActionBouboul(true);
				actionbouR.init();
				stage.addAction(actionbouR);
				actionbouR.setActor(imgBoubouleR);
			}
		});

		titleButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickBouboule " + x + ", " + y);
				actiontitle.switchName();
			}
		});
	}

	/* => no need to normalise the text with this font on Android/Desktop:
	 *    when adding a new user, it can only contain chars that are available
	 *    in this font
	 */
	private static final char CHAR_WITH_ACCENTS[] = "àâçéèêëîïôöùüÂÀÇÉÈÊËÎÏÔÖÙÜ".toCharArray();
	private static final char CHAR_WITHOUT_ACCENTS[] = "aaceeeeiioouuAACEEEEIIOOUU".toCharArray();
	/**
	 * @param text Text to be nomalise
	 * @return a 'normalised' text (without special chars)
	 */
	private String normaliseTextForTitle(String text) {
		if (Gdx.app.getType() == ApplicationType.Android
				|| Gdx.app.getType() == ApplicationType.Desktop)
			return text;

		String cNewText = text;

		for (int i = 0; i < CHAR_WITH_ACCENTS.length; i++)
			cNewText.replace(CHAR_WITH_ACCENTS[i], CHAR_WITHOUT_ACCENTS[i]);
		Gdx.app.log("SCREEN", "Normalise text: Old " + text + " -> " + cNewText);

		return cNewText;

		/*return Normalizer.normalize(text, Normalizer.Form.NFD) // Not supported on iOS
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
				.toUpperCase();*/
	}

	private String getHighScoreText() {
		HighScoreInfo highscores[] = GlobalSettings.PROFILE_MGR
				.getProfileGlobal().getAllHighScores(false);

		// no highscore: display a message even if there is no high scores
		if (highscores.length == 0 || highscores[0] == null) {
			// the first time, we receive only one elem which is null
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

		// Constructor of Action
		public ActionBouboul(boolean right) {
			init();
			// initial position
			position = -2 * GlobalSettings.APPWIDTH; // delay the animation
			// true if it's the action for the right Bouboule
			this.right = right;
		}

		public boolean act(float delta) {
			position += delta * 1100f;

			// max position APPWIDTH
			if (position >= GlobalSettings.APPWIDTH)
				position = GlobalSettings.APPWIDTH;

			// set position for the right and left Bouboule
			if (right)
				actor.setPosition(GlobalSettings.APPWIDTH - position,
						(GlobalSettings.APPWIDTH - position) / 3.5f);
			else
				actor.setPosition(-GlobalSettings.APPWIDTH + position,
						(GlobalSettings.APPWIDTH - position) / 3.5f);

			if (position == GlobalSettings.APPWIDTH) {
				actor.removeAction(this);
				return true; // stop moving these bouboules
			}
			return false;
		}

		// reset the position when the player click on the Bouboule
		public void init() {
			position = 0f;
		}

	}

	private class ActionTitle extends Action {
		private float timer;
		private int oldRand = 0; // to not display the same name twice, we start with Bouboule
		private float degreeRot = 1f;

		public ActionTitle() {
			init();
		}

		public boolean act(float delta) {
			Table tableTitle = (Table) actor;
			timer = timer + delta;

			tableTitle.setRotation(timer * 5f * degreeRot); // rotate the text

			// var witch set the time since the period
			float temptimer;
			if (timer < 1.0f) {
				// 1) emergence during 1sec
				temptimer = timer;
				tableTitle.setScale(0.0f + temptimer * 0.6f);
			}
			else if (timer < 4.0f) {
				// 2)beat during 3 sec
				temptimer = timer % 1.0f;
				if (temptimer >= 0.5f)
					temptimer = 1f - temptimer;
				tableTitle.setScale(0.6f + temptimer * 0.1f);
			}
			else if (timer < 5.0f) {
				// 3) extinction during 1sec
				temptimer = timer - 4.0f;
				tableTitle.setScale(0.6f - temptimer * 0.6f);
			}
			else {
				// 4)restart
				timer = timer - 5.0f;
				getNewRotation();
				changeName();
			}
			tableTitle.setOrigin(GlobalSettings.APPWIDTH / 2,
					tableTitle.getHeight() / 2);

			return false;
		}

		// rest the time when the player push on the title
		public void init() {
			timer = 0.0f;
		}

		public void switchName() {
			init();
			getNewRotation();
			changeName();
		}

		private void changeName() {
			int newRand;
			while ((newRand = MathUtils.random(8)) == oldRand); // we need a new number

			oldRand = newRand;
			switch (newRand) {
			case 0:
				String name = normaliseTextForTitle(
						GlobalSettings.PROFILE.getName().toUpperCase());
				title.setText("HELLO\n" + name);
				break;
			case 1:
				title.setText("MAY THE\nBOUBOULE BE\nWITH YOU");
				break;
			case 2:
				title.setText("BIG BOUBOULE\nIS WATCHING\n YOU");
				break;
			case 3:
				title.setText("KICK THEM\n ALL!");
				break;
			case 4:
				title.setText("WELCOME TO\nBOUBOULE");
				break;
			case 6:
				title.setText("BOUBOULE\nIS GREAT");
				break;
			default:
				title.setText("BOUBOULE");
				break;
			}
		}

		private void getNewRotation() {
			switch (MathUtils.random(7)) {
			case 0:
				degreeRot = 1f;
				break;
			case 1:
				degreeRot = -1f;
				break;
			case 2:
				degreeRot = 1.5f;
				break;
			case 3:
				degreeRot = -1.5f;
				break;
			case 4:
				degreeRot = -.5f;
				break;
			case 5:
				degreeRot = -.5f;
				break;
			default:
				degreeRot = 0f;
				break;
			}
			// Gdx.app.log("SCREEN", "Rotation: " + degreeRot);
		}
	}
}
