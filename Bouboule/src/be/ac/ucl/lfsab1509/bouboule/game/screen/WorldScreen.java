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
import be.ac.ucl.lfsab1509.bouboule.game.profile.BoubImages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class WorldScreen extends AbstractScreen {

	/* Current World INFO */
	private int iWorld = (GlobalSettings.PROFILE.getBestLevel() - 1) / 4;
	private int lastUnlockedWorld = iWorld + 1;

	private Label infoLabel = null;
	private boolean bInfoDisplayed = false;

	/* Array to store the world position on the screen and size of the buttons */
	private final int[] posX =  {  85, 320, 519, 504, 275,  85, 159, 122 };
	private final int[] posY =  {  31,  61, 122, 244, 299, 366, 519, 806 };
	private final int[] sizeX = { 183, 183, 183, 183, 183, 183, 204, 549 };
	private final int[] sizeY = {  73,  67,  67, 122, 110, 122, 146, 198 };

	
	public WorldScreen() {
		super(false);
	}

	@Override
	public void show() {
		super.show();

		// Set Background
		Image background = addBackGround("GdxMenus/levels/bglevel0.jpg");
		background.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				displayInfoWorlds();
			}
		});

		// Add levels images "recursively"
		// Level are noted from 0 to 6
		// Image zero and seven ( begin and end of the game) are always
		// displayed. -> Corresponding to World 1 and 8.
		for (int i = 1; i < lastUnlockedWorld
				&& i < GlobalSettings.NBLEVELS / 4 - 1; i++) {
			addBackGround("GdxMenus/levels/bglevel" + i + ".png").setTouchable(null);
		}

		// Add Boub Image selected by the user
		String boubPath = BoubImages.BOUB_DIR_NORMAL
				+ GlobalSettings.PROFILE.getBoubName()
				+ BoubImages.BOUB_EXTENTION;

		Image boub = addImage(boubPath, 0, 0);

		// Set action for the moving Boub
		// Attention, the array for the World position begin at 0 not 1 -> (-1)
		final MoveBoub moveBoub = new MoveBoub(lastUnlockedWorld - 1);
		this.stage.addAction(moveBoub);
		moveBoub.setActor(boub);
		moveBoub.draw(); // First draw

		// Create on the world images from 0 to max 7

		for (int i = 0; i < lastUnlockedWorld
				&& i < GlobalSettings.NBLEVELS / 4; i++) {

			Button button = createButton("transparent",
					(int) (sizeX[i] * GlobalSettings.HD),
					(int) (sizeY[i] * GlobalSettings.HD),
					(int) (posX[i] * GlobalSettings.HD),
					(int) (posY[i] * GlobalSettings.HD));
			addLevelListener(button, moveBoub, i);
		}

		// click on the bouboule
		boub.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "The world " + -1 + " is selected");
				moveBoub.init(-1);
			}
		});

		if (GlobalSettings.PROFILE.needTutorial())
			displayInfoWorlds();
	}

	/*
	 * Listener that store the world associated with the button
	 */
	private void addLevelListener(Button button, final MoveBoub action,
			final int world) {

		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "The world " + world + " is selected");
				action.init(world);
			}
		});
	}

	private class MoveBoub extends Action {

		private int startPos, stopPos;

		// Position of Boub through the worlds 0 to 7
		private int[] posX = { 85, 320, 519, 503, 275,  92, 177, 311 };
		private int[] posY = { 31,  61, 122, 244, 299, 385, 580, 861 };

		// Postion of Boub
		private float x = 0;
		private float y = 0;

		// Moving state
		private boolean moving = false;

		// Fraction between every world
		private final float span = 50f;

		/*
		 * Constructor > Only the initial world position is needed We can't draw
		 * here because actor is not yet set
		 */
		public MoveBoub(int startPos) {
			if (GlobalSettings.ISHD) {
				for (int i = 0; i < posX.length; i++) {
					posX[i] = (int) (posX[i] * GlobalSettings.HD);
					posY[i] = (int) (posY[i] * GlobalSettings.HD);
				}
			}
			this.startPos = startPos;
			this.stopPos = startPos;
			x = posX[startPos];
			y = posY[startPos];
		}

		private void centerBouboule() {
			// center it
			x = posX[startPos];
			y = posY[startPos];
		}

		public boolean act(float delta) {

			// If Boub need to move
			if (startPos != stopPos) {

				moving = true;

				// Upwards
				if (startPos < stopPos) {
					/* stopPos - startPos to have a bigger speed when switching
					 * to more than one level
					 */
					x += (posX[startPos + 1] - posX[startPos])
							* (stopPos - startPos) / span ;
					y += (posY[startPos + 1] - posY[startPos])
							* (stopPos - startPos) / span ;

					// We have reached a new world position
					if (Math.round(y) >= posY[(startPos + 1)]) {
						startPos++;
						centerBouboule();
					}
				}
				// Downwards
				else {
					x += (posX[startPos - 1] - posX[startPos])
							* (startPos - stopPos) / span ;
					y += (posY[startPos - 1] - posY[startPos])
							* (startPos - stopPos) / span ;

					// We have reached a new world position
					if (Math.round(y) <= posY[(startPos - 1)]) {
						startPos--;
						centerBouboule();
					}
				}

				draw();

			}
			else {
				moving = false;
				// return true;
			}

			return false; // To continue looping
		}

		// When a new world is selected and Boub is already moving, the initial
		// position must be adapted if the player wants the Boub to change
		// direction during moving == true;
		// stopPos = -1 when clicking on the bouboule
		public void init(int stopPos) {

			if (moving && stopPos >= 0) {

				// Verify if modification are needed
				if (stopPos <= this.startPos && this.startPos <= this.stopPos)
					this.startPos++;
				else if (stopPos >= this.startPos
						&& this.startPos >= this.stopPos)
					this.startPos--;
			}
			else if (! moving) {

				// Not moving and on the same World as the pushed button
				if (stopPos < 0 || this.startPos == stopPos) {
					GlobalSettings.PROFILE.setLevel((this.startPos) * 4 + 1, true);
					setScreenWithFading(null);
				}
			}

			// Set the new endpoint of the animation
			if (stopPos >= 0)
				this.stopPos = stopPos;
		}

		public void draw() {
			actor.setPosition(x, y);
		}
	}

	private void displayInfoWorlds() {
		Gdx.app.log("SCREEN", "disp Info: " + bInfoDisplayed);
		if (bInfoDisplayed || GlobalSettings.PROFILE.getBestLevel() / 4 > 5)
			return;

		bInfoDisplayed = true;
		if (infoLabel == null) {
			String text = "There are " + GlobalSettings.NBLEVELS / 4 + " worlds\n\n"
					+ "To unlock the next world,\n"
					+ "you have to win 4 fights in a row.\n\n"
					+ "Click on your Bouboule to start the game!";
			infoLabel = addLabel(text, "osaka-font", 1f,
					new Color(.388f, .733f, .984f, 1f), 0,
					(int) (425 * GlobalSettings.HD));
			infoLabel.setAlignment(Align.center);
			infoLabel.setWidth(GlobalSettings.APPWIDTH);
			infoLabel.setOrigin(GlobalSettings.APPWIDTH / 2,
					infoLabel.getHeight() / 2);
			infoLabel.setTouchable(null);
		}
		else
			stage.addActor(infoLabel);

		ActionInfo actionInfo = new ActionInfo();
		this.stage.addAction(actionInfo);
		actionInfo.setActor(infoLabel);
	}

	private class ActionInfo extends Action {

		float fTimer = 0f;

		@Override
		public boolean act(float delta) {
			fTimer += delta;
			Label label = (Label) actor;

			if (fTimer < 3) // fade in
				label.setColor(.388f, .733f, .984f, fTimer / 3f);
			else if (fTimer < 31) { // blink
				int iTimer = (int) fTimer;
				label.setColor(.388f, .733f, .984f,
						iTimer % 2 == 0 ?
								.9f + (fTimer - iTimer) / 10f : // fade in 9/10
								1f - (fTimer - iTimer) / 10f); // fade out 9/10
			}
			else if (fTimer < 34) // fade out
				label.setColor(.388f, .733f, .984f, 1 - (fTimer - 31f) / 3f);
			else {
				bInfoDisplayed = false;
				actor.removeAction(this);
				actor.remove();
				return true; // stop
			}
			return false;
		}
	}
}
