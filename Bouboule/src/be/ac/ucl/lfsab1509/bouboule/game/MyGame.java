package be.ac.ucl.lfsab1509.bouboule.game;

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


import be.ac.ucl.lfsab1509.bouboule.game.util.CameraHelper;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GameLoop;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.Score;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;


public class MyGame implements ApplicationListener {

	static final int APPWIDTH 	= 800;
	static final int APPHEIGHT 	= 1280;
	
	//Test Update


	//private SpriteBatch             		batch;
	private OrthographicCamera      		camera;

	private GameLoop 						game;
	private Score score;

	private Timer timer = null;
	private int iCountDown = 0;
	private boolean bIsPause = false;
	private Music loopMusic;
	private Sound hitSound;
	private Sound winSound;
	private Sound looseSound;
	@Override
	public void create() {

		camera = CameraHelper.GetCamera(APPWIDTH, APPHEIGHT);

		// start the playback of the background music immediately
		loopMusic = Gdx.audio.newMusic(Gdx.files.internal("music/klez.mp3"));
		loopMusic.setLooping(true);
		loopMusic.play();
		
		hitSound = Gdx.audio.newSound(Gdx.files.internal("music/drop.mp3")); // TODO: find sound and use parameters?
		winSound = Gdx.audio.newSound(Gdx.files.internal("music/drop.mp3"));
		looseSound = Gdx.audio.newSound(Gdx.files.internal("music/drop.mp3"));


		score= (GlobalSettings.SCORE == null)? new Score (GlobalSettings.INIT_SCORE,
				GlobalSettings.INIT_LIFES,
				GlobalSettings.INIT_LEVEL):GlobalSettings.SCORE; // TODO: Init_scrore and life: take data from config
		
		GlobalSettings.SCORE = score;

		game = new GameLoop(camera, true);
		
		score.LaunchTimer ();
	}



	@Override
	public void render() {
		if (bIsPause)
		{
			// we need to draw something...
			return;
		}
		
		// float dt = Gdx.graphics.getDeltaTime();

		game.update();
		game.render();

	}

	@Override
	public void dispose() {
		game.dispose();
		score.stop ();
		loopMusic.dispose ();
		hitSound.dispose ();
		winSound.dispose ();
		looseSound.dispose ();
	}

	@Override
	public void resize(final int width, final int height) {
	}

	@Override
	public void pause() {
		if (score.getScore () == GlobalSettings.INIT_SCORE)
			return;
		bIsPause = true;
		score.pause ();
		loopMusic.pause ();
	}

	private void startCountDown (int iNbTime) {
		iCountDown = iNbTime - 1;
		Timer.Task task = new Timer.Task () {
			@Override
			public void run () {
				if (iCountDown > 0) {
					// add new image?
					iCountDown--;
				}
				else {
					resumeStart ();
					this.cancel (); // cancel the task
					timer.clear (); // maybe not needed?
					timer = null;
				}
			}
		};
		timer = new Timer ();
		timer.scheduleTask (task, 1, 1); // first time, time between
		timer.start ();
	}
	
	private void resumeStart () {
		bIsPause = false;
		score.play ();
		loopMusic.play ();
	}

	@Override
	public void resume() {
		if (bIsPause && score.getScore () < GlobalSettings.INIT_SCORE && // this function is called one or two time when starting the game
				timer == null) // not already launched
			startCountDown (GlobalSettings.PAUSE_TIME);
	}
		
	public Score getUserScore () {
		return score;
	}
	
	public void hitSound () {
		hitSound.play ();
	}
	
	public void winSound () {
		winSound.play ();
	}
	
	public void looseSound () {
		looseSound.play ();
	}
}