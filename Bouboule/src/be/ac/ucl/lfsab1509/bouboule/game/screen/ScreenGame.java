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


import be.ac.ucl.lfsab1509.bouboule.game.ai.AI;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GameLoop;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings.GameExitStatus;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.input.GestureDetector;


public class ScreenGame implements Screen {

	private GameLoop game;
	private FileHandle loopMusicFile;
	private FileHandle loopMusicDefaultFile;
	private Music loopMusic;


	private boolean bIsPause = false;
	private boolean bNewGame = true;

	// INIT => on create
	public ScreenGame() {
		loopMusicDefaultFile = loopMusicFile = Gdx.files.internal("music/levels/klez.mp3");
		loopMusic = Gdx.audio.newMusic(loopMusicFile);
		loopMusic.setLooping(true);

		game = new GameLoop(GlobalSettings.GAME.getCamera(), true);
		if (Gdx.app.getType() == ApplicationType.iOS) // axes are inverted in iOS!
			AI.INIT_ORIENTATION = -1;
	}

	@Override
	public void show() {
		Gdx.app.log("Matth", "Screen: SHOW");
		// With GdxMenus, this method is called twice
		if (GlobalSettings.GAME.getTimer().isRunning())
			return;

		bIsPause = true; // show the countdown at startup
		game.resumeGame();
		bNewGame = false;

		loopMusic.stop(); // to play at startup
		game.start();
		GlobalSettings.GAME.getTimer().createNewTimer(GraphicManager.TIME);

		Gdx.input.setInputProcessor(new GestureDetector(new MyGestureListener()));
	}


	@Override
	public void render(final float delta) {
		if (!bIsPause) {
			game.update();
		}
		bIsPause = game.render(bIsPause, delta);
	}

	@Override
	public void dispose() {
		Gdx.app.log("Matth", "Screen: DISPOSE");
		if (game != null) {
			game.dispose();
		}
		GlobalSettings.GAME.getTimer().stop();
		loopMusic.dispose();
	}

	@Override
	public void resize(final int width, final int height) {
		// nothing to do
	}

	@Override
	public void pause() {
		//Gdx.app.log ("Matth", "Screen: PAUSE");
		Gdx.app.log("Matth", "Screen: PAUSE + pause status : " + bIsPause 
				+ " " + GlobalSettings.GAME.getTimer().isRunning() + " " 
				+ game.isCountDownLaunched());
		
		game.resumeGame(); // reset the countdown (if it's running)
		if (!GlobalSettings.GAME.getTimer().isRunning()) {
			// already stopped... we start a new game?
			return;
		}

		GlobalSettings.GAME.stopCountdownSound(); // stop the sound if any
		bIsPause = true;
		GlobalSettings.GAME.getTimer().pause();
		loopMusic.pause();
		Gdx.app.log("Matth", "Screen: PAUSE + pause status ok : " + bIsPause);
	}

	/**
	 * Will be launch when resuming after the pause but we have to skip that
	 * because it will be use a second time when the countdown is over.
	 */
	@Override
	public void resume() {
		Gdx.app.log("Matth", "Screen: RESUME + pause status : " + bIsPause + " Count " 
	+ game.isCountDownLaunched() + " " + bNewGame);
		
		if (bNewGame) {
			show(); //must relaunch the game when the activity is not paused
					 //and comes back from a menu or what ever
			
		}
		else if (bIsPause && game.isCountDownLaunched()) { // resume from CountDown
			GlobalSettings.GAME.getTimer().play();
			if (!GlobalSettings.SOUND_IS_MUTED) {
				loopMusic.play();
			}
			
			bIsPause = false;
		}
		// else nothing to do more, we are waiting for the signal from the countdown
	}

	@Override
	public void hide() {
		Gdx.app.log("Matth", "Screen: HIDE + pause " + bIsPause);
		Gdx.app.log("Matth", "Screen: HIDE + exit: " + GlobalSettings.GAME_EXIT);
		
		bNewGame = GlobalSettings.GAME_EXIT != GameExitStatus.NONE; // a new game is needed?
		loopMusic.pause();
	}

	/**
	 * @param cNewMusic should be a file that can be read by GDX
	 */
	public void setNewLoopMusic(final FileHandle pNewMusicFile) {
		if (pNewMusicFile.equals(loopMusicFile)) {
			return;
		}

		boolean bWasPlaying = loopMusic.isPlaying();
		loopMusic.stop();
		loopMusic.dispose();

		loopMusicFile = pNewMusicFile;
		loopMusic = Gdx.audio.newMusic(pNewMusicFile);
		loopMusic.setLooping(true);

		if (bWasPlaying) {
			loopMusic.play();
		}
	}

	public void setDefaultLoopMusicPath() {
		setNewLoopMusic(loopMusicDefaultFile);
	}
}