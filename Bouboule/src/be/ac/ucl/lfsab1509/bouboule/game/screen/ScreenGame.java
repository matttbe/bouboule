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
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings.GameExitStatus;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.Profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;


public class ScreenGame implements Screen {

	static final float APPWIDTH 	= 800f;
	static final float APPHEIGHT 	= 1250f;
	
	//Test Update


	//private SpriteBatch             		batch;
	private OrthographicCamera      		camera;

	private GameLoop 						game;
	private Profile profile;
	private Music loopMusic;

	private Timer timer = null;
	// private int iCountDown = 0;
	private boolean bIsPause = false;
	private boolean bNewGame = true;

	// INIT => on create
	public ScreenGame () {
		profile = GlobalSettings.PROFILE;
		loopMusic = Gdx.audio.newMusic(Gdx.files.internal("music/klez.mp3"));
		loopMusic.setLooping(true);

		camera = CameraHelper.GetCamera(APPWIDTH, APPHEIGHT);
		game = new GameLoop(camera, true);
	}

	@Override
	public void show() {
		Gdx.app.log ("Matth", "Screen: SHOW");
	
		loopMusic.stop(); // to play at startup
		loopMusic.play();
		game.start ();
		profile.LaunchTimer ();
		bIsPause = false;
		bNewGame = false;

		// start the playback of the background music immediately

		// GlobalSettings.LISTENER.dispose (); // display the menu for the first time
		//start ();// TODO: remove...?
	}


	@Override
	public void render (float delta) {
		if (!bIsPause)
			game.update();
		bIsPause = game.render(bIsPause);
	}

	@Override
	public void dispose() {
		Gdx.app.log ("Matth", "Screen: DISPOSE");
		if (game != null)
			game.dispose();
		profile.stop ();
		loopMusic.dispose ();
	}

	@Override
	public void resize(final int width, final int height) {
		//TODO? position of the countdown, the score, etc.?
	}

	@Override
	public void pause() {
		//Gdx.app.log ("Matth", "Screen: PAUSE");
		Gdx.app.log ("Matth", "Screen: PAUSE + pause status : "+bIsPause);
		if (timer == null || ! profile.isRunning ())
			return;
		bIsPause = true;
		profile.pause ();
		loopMusic.pause ();
	}

	/*private void startCountDown (int iNbTime) {
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
		profile.play ();
		loopMusic.play ();
	}*/

	@Override
	public void resume() {
		Gdx.app.log ("Matth", "Screen: RESUME + pause staus : "+bIsPause);
		
		if (bNewGame)
			show (); //must relaunch the game when the activity is not paused
					 //and comes back from a menu or what ever
		else if (bIsPause) {
			bIsPause = false;
			profile.play ();
			loopMusic.play ();
			// TODO: launch the countdown: 3 -> 2 -> 1
		}
		
		/*if (bIsPause
				// this function is called one or two time when starting the game
				&& profile.isRunning ()
				&& timer == null) // not already launched
			startCountDown (GlobalSettings.PAUSE_TIME);*/
	}

	@Override
	public void hide () {
		Gdx.app.log ("Matth", "Screen: HIDE + pause " + bIsPause);
		Gdx.app.log ("Matth", "Screen: HIDE + exit: " + GlobalSettings.GAME_EXIT);
		
		bNewGame = GlobalSettings.GAME_EXIT != GameExitStatus.NONE; // a new game is needed?
	}
}