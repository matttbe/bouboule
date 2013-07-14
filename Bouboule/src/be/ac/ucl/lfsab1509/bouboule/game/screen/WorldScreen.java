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
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class WorldScreen extends AbstractScreen {

	/* Current World INFO */
	private int iWorld = (GlobalSettings.PROFILE.getBestLevel() - 1) / 4;
	private int lastUnlockedWorld = iWorld + 1;

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
		addBackGround("drawable-xhdpi/bglevel0.jpg");

		// Add levels images "recursively"
		// Level are noted from 0 to 6
		// Image zero and seven ( begin and end of the game) are always
		// displayed. -> Corresponding to World 1 and 8.
		for (int i = 1; i < lastUnlockedWorld
				&& i < GlobalSettings.NBLEVELS / 4 - 1; i++) {

			addImage("drawable-xhdpi/bglevel" + i + ".png", 0, 0);
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

			Button button = createButton("transparent", sizeX[i], sizeY[i],
					posX[i], posY[i]);
			addLevelListener(button, moveBoub, i);

		}

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
		private final float[] posX = { 85, 320, 519, 503, 275,  92, 177, 311 };
		private final float[] posY = { 31,  61, 122, 244, 299, 385, 580, 861 };

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
			this.startPos = startPos;
			this.stopPos = startPos;
			x = posX[startPos];
			y = posY[startPos];
		}

		public boolean act(float delta) {

			// If Boub need to move
			if (startPos != stopPos) {

				moving = true;

				// Upwards
				if (startPos < stopPos) {
					x += (posX[startPos + 1] - posX[startPos]) / span;
					y += (posY[startPos + 1] - posY[startPos]) / span;

					// We have reached a new world position
					if (Math.round(x) == posX[(startPos + 1)])
						startPos++;

					// Downwards
				} else {

					x += (posX[startPos - 1] - posX[startPos]) / span;
					y += (posY[startPos - 1] - posY[startPos]) / span;

					// We have reached a new world position
					if (Math.round(x) == posX[(startPos - 1)])
						startPos--;
				}

				Gdx.app.log("Boub", "Moving" + startPos);
				draw();

			} else {

				moving = false;
				// return true;
			}

			return false; // To continue looping
		}

		// When a new world is selected and Boub is already moving, the initial
		// position must be adapted if the player wants the Boub to change
		// direction during moving == true;
		public void init(int stopPos) {

			if (moving) {

				// Verify if modification are needed
				if (stopPos <= this.startPos && this.startPos <= this.stopPos)
					this.startPos++;
				else if (stopPos >= this.startPos
						&& this.startPos >= this.stopPos)
					this.startPos--;
			} else {

				// Not moving and on the same World as the pushed button
				if (this.startPos == stopPos) {
					GlobalSettings.PROFILE.setLevel((stopPos) * 4 + 1);
					setScreenWithFading(null);
				}

			}

			// Set the new endpoint of the animation
			this.stopPos = stopPos;
		}

		public void draw() {
			actor.setPosition(x, y);
		}
	}
}
