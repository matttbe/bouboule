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
 *    H??l??ne Verhaeghe <helene.verhaeghe@student.uclouvain.be>
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
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GameOverScreen extends AbstractScreen {

	private boolean endGame;
	private Label highScoreLabel;

	public GameOverScreen(boolean endGame) {
		super(true);
		this.endGame = endGame;
	}

	@Override
	public void show() {
		super.show();

		// Set Background

		addBackGroundShift("GdxMenus/gameover/gameoverbg.jpg");
		
		//Add buttons
		
		addBackGround("GdxMenus/gameover/gameoverbuttons.png");

		// Create all Buttons - Play Button

		Button restartButton = GlobalSettings.ISHD ?
				createButton("transparent", 475, 147, 103, 814) :
				createButton("transparent", 290, 90, 63, 497);
		Button menuButton = GlobalSettings.ISHD ?
				createButton("transparent", 475, 147, 734, 814) :
				createButton("transparent", 290, 90, 448, 497);

		restartButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickStart " + x + ", " + y);
				setScreenWithFading(new WorldScreen());
			}
		});

		menuButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickParam " + x + ", " + y);
				setScreenWithFading(new MenuScreen());
			}
		});

		// Set Font for end Score

		float fScaleFont = GlobalSettings.ISHD ? 1f : 0.7f;
		int iY = GlobalSettings.ISHD ? 1100 : 650;
		int endScore = GlobalSettings.PROFILE.getEndGameScore();
		String text = endGame ? "End! " + endScore : Integer.toString(endScore);
		Label label = addLabel(text, "chinyen-font", fScaleFont,
				new Color(1f, 1f, 1f, 1f), 0, iY);

		label.setX(GlobalSettings.APPWIDTH / 2f - 
				label.getTextBounds().width / 2f);

		if (GlobalSettings.PROFILE.isNewHighScore())
			showHighScore();
	}

	private void showHighScore() {
		highScoreLabel = new Label("HIGH SCORE", getSkin(),
				"darktimes-font",
				new Color(1f, 1f, .212f, 1f));
		highScoreLabel.setFontScale(.6f);
		highScoreLabel.setAlignment(Align.center);
		highScoreLabel.setWidth(GlobalSettings.APPWIDTH);
		highScoreLabel.setY(GlobalSettings.ISHD ? 950 : 560);
		highScoreLabel.setTouchable(null); // we can click through it

		stage.addActor(highScoreLabel);
		ActionHighScore highScoreAction = new ActionHighScore();
		stage.addAction(highScoreAction);
		highScoreAction.setActor(highScoreLabel);
	}

	private class ActionHighScore extends Action {

		private float fTime = 0;

		@Override
		public boolean act(float delta) {
			fTime += delta;

			int iTime = (int) fTime;
			float fDiffTime = fTime - iTime;
			if (iTime % 2 == 0) { // grow down => .6 => .5
				highScoreLabel.setFontScale(.6f - fDiffTime / 10f);
				fTime = fDiffTime; // avoid overflow... ok almost impossible :)
			}
			else
				highScoreLabel.setFontScale(.5f + fDiffTime / 10f);

			return false; // continue the animation...
		}
	}
}
