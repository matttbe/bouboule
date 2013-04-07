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


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CountDown {

	private static final int        FRAME_COLS = 2;
	private static final int        FRAME_ROWS = 2;

	Animation                       countDownAnimation;
	Texture                         countDownSheet;
	TextureRegion[]                 countDownFrames;
	TextureRegion                   currentFrame;

	float STEPTIME = 0.7f;
	float stateTime;

	
	public CountDown() {
		countDownSheet = new Texture("images/anim/countdown.png");
		TextureRegion[][] tmp = TextureRegion.split(countDownSheet, countDownSheet.getWidth() / 
				FRAME_COLS, countDownSheet.getHeight() / FRAME_ROWS);
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

	

	public boolean draw(SpriteBatch batch) {
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = countDownAnimation.getKeyFrame(stateTime, true);
		batch.draw(currentFrame,400-currentFrame.getRegionWidth()/2,625-currentFrame.getRegionHeight()/2);
		
		if (stateTime > 4*STEPTIME) 
			stateTime = 0f;
		
		return stateTime != 0f;
	}

	
}