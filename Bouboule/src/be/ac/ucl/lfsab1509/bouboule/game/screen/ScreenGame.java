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


import be.ac.ucl.lfsab1509.bouboule.game.profile.Profile;
import be.ac.ucl.lfsab1509.bouboule.game.util.CameraHelper;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GameLoop;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings.GameExitStatus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class ScreenGame implements Screen {

	public static final float APPWIDTH 	= 800f;
	public static final float APPHEIGHT = 1250f;
	
	//Test Update


	private OrthographicCamera      		camera;

	private GameLoop 						game;
	private Profile profile;
	private Music loopMusic;


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

		bIsPause = true; // show the countdown at startup
		game.getCountDown ().reset ();
		bNewGame = false;

		loopMusic.stop(); // to play at startup
		game.start ();
		profile.createTimer ();
	}


	@Override
	public void render (float delta) {
		if (!bIsPause)
			game.update();
		bIsPause = game.render(bIsPause, delta);
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
		// nothing to do
	}

	@Override
	public void pause() {
		//Gdx.app.log ("Matth", "Screen: PAUSE");
		Gdx.app.log ("Matth", "Screen: PAUSE + pause status : "+bIsPause + " " + profile.isRunning () + " " + game.getCountDown ().isLaunched ());
		game.getCountDown ().reset (); // reset the countdown (if it's running)
		if (bIsPause || ! profile.isRunning ()) // already stopped... we start a new game?
			return;
		bIsPause = true;
		profile.pause ();
		loopMusic.pause ();
		Gdx.app.log ("Matth", "Screen: PAUSE + pause status ok : "+bIsPause);
	}

	/**
	 * Will be launch when resuming after the pause but we have to skip that
	 * because it will be use a second time when the countdown is over
	 */
	@Override
	public void resume() {
		Gdx.app.log ("Matth", "Screen: RESUME + pause status : " + bIsPause + " Count " + game.getCountDown ().isLaunched () + " " + bNewGame);
		
		if (bNewGame)
			show (); //must relaunch the game when the activity is not paused
					 //and comes back from a menu or what ever
		else if (bIsPause && game.getCountDown ().isLaunched ()) { // resume from CountDown
			profile.play ();
			if (! GlobalSettings.SOUND_IS_MUTED)
				loopMusic.play ();
			bIsPause = false;
		}
		// else: nothing to do, we are waiting for the signal from the countdown
	}

	@Override
	public void hide () {
		Gdx.app.log ("Matth", "Screen: HIDE + pause " + bIsPause);
		Gdx.app.log ("Matth", "Screen: HIDE + exit: " + GlobalSettings.GAME_EXIT);
		
		bNewGame = GlobalSettings.GAME_EXIT != GameExitStatus.NONE; // a new game is needed?
	}

	/**
	 * @param cNewMusic should be a file that can be read by GDX in 'music'
	 */
	public void setNewLoopMusic (String cNewMusic) {
		boolean bWasPlaying = loopMusic.isPlaying ();
		loopMusic.stop ();

		loopMusic = Gdx.audio.newMusic(Gdx.files.internal("music/" + cNewMusic));
		loopMusic.setLooping(true);

		if (bWasPlaying)
			loopMusic.play ();
	}
}