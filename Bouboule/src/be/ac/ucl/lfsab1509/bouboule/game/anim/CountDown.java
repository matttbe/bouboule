package be.ac.ucl.lfsab1509.bouboule.game.anim;

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
 *    Helene Verhaeghe <helene.verhaeghe@student.uclouvain.be>
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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Class that load a countDown from the assets and display it.
 *
 */
public class CountDown {

	private int						FRAME_COLS;				//Number of Cols in the Atlas file
	private int						FRAME_ROWS;				//Number of Rows in the Atlas file
	private int						N_FRAME;

	private Animation				countDownAnimation; 	//The animation of the countDown
	private Texture					countDownSheet;			//Initial big file of the countDown
	private TextureRegion[]			countDownFrames;		//Tab that contains all the frames
	private TextureRegion			currentFrame;			//Current image displayed

	private float					STEPTIME;				//Step time between 2 frames
	private float					stateTime;				//Current time of the animation
	private int						iFirstFrames;	//we need to skip the first render (can be launched when switching menus)
	private boolean					RESUME_AFTER_END;
	private static final int		WAIT_FRAME = 10;		//wait 20 frames before playing a sound

	/**
	 * Constructor for a CountDown Object 
	 * Automatically load the images/anim/countdown.png, countDown
	 * 
	 * public CountDown(final int col, final int row
	 * 				final String path, final boolean resume)
	 */
	public CountDown(final int col, final int row, final float time,
			final String path, final boolean resume) {
		
		FRAME_COLS = col;
		FRAME_ROWS = row;
		N_FRAME	   = col * row;
		STEPTIME   = time;
		RESUME_AFTER_END = resume;
		
		countDownSheet = new Texture(path);
		TextureRegion[][] tmp = TextureRegion.split(countDownSheet, countDownSheet.getWidth() 
				/ FRAME_COLS, countDownSheet.getHeight() / FRAME_ROWS);
		countDownFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				countDownFrames[index++] = tmp[i][j];
			}
		}
		countDownAnimation = new Animation(STEPTIME, countDownFrames);
		stateTime = 0f;
	}

	public void reset() {
		stateTime = 0f;
		iFirstFrames = 0;
	}

	/**
	 * Return the status of the pause => true = pause.
	 */
	public boolean draw(final SpriteBatch batch, final float delta) {
		if(!isLaunched()) { // skip frames before playing a sound
			iFirstFrames++;
			return true;
		}
		else if(iFirstFrames == WAIT_FRAME) {
			iFirstFrames++; // stop playing something next frames
			GlobalSettings.GAME.countdownSound();
		}
		
		//we are not in the pause menu		
		if (delta < 1f) {
			stateTime += delta;
		}
		
		currentFrame = countDownAnimation.getKeyFrame(stateTime, true);
		batch.draw(currentFrame, 400 - currentFrame.getRegionWidth() / 2,
				625 - currentFrame.getRegionHeight() / 2);
		
		if (stateTime > N_FRAME * STEPTIME - 0.1f) {
			if (RESUME_AFTER_END) {
				GlobalSettings.GAME.getScreen().resume();
			}
			
			reset();
			return false;
		}
		return true;
	}
	
	/**
	 * Remove used memory.
	 */
	public void dispose() {
		countDownSheet.dispose();
	}
	
	/**
	 * 
	 * @return launch status
	 */
	public boolean isLaunched() {
		return iFirstFrames >= WAIT_FRAME;
	}
}