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

import java.util.Random;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.level.LevelLoader;
import be.ac.ucl.lfsab1509.bouboule.game.menu.GdxMenus;
import be.ac.ucl.lfsab1509.bouboule.game.profile.ProfileMgr;
import be.ac.ucl.lfsab1509.bouboule.game.screen.MyGestureListener;
import be.ac.ucl.lfsab1509.bouboule.game.screen.ScreenGame;
import be.ac.ucl.lfsab1509.bouboule.game.timer.TimerMgr;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.input.GestureDetector;

public class MyGame extends Game implements ApplicationListener {

	private ScreenGame screenGame;
	private Sound hitSounds[];
	// private Sound winSound; // not used
	// private Sound looseSound;
	private Sound countdownSound;
	private Random rand;
	private LevelLoader level;

	private TimerMgr timer;

	/**
	 * This class should be the first one which is called after having
	 * initialized GDX
	 */
	public void init() {
		GlobalSettings.GAME = this;
		timer = new TimerMgr(1, 1); // needed by some classes: listener
		level = new LevelLoader(); // load the XML now: needed to know how many levels are available
		GlobalSettings.PROFILE_MGR = new ProfileMgr(); // loaded the profile now: needed by menus, etc.
	}

	@Override
	public void create() {
		Gdx.app.log("Matth", "Game: Create");
		hitSounds = new Sound[5];
		hitSounds[0] = Gdx.audio.newSound(Gdx.files.internal("music/sounds/hit1.mp3"));
		hitSounds[1] = Gdx.audio.newSound(Gdx.files.internal("music/sounds/hit2.mp3"));
		hitSounds[2] = Gdx.audio.newSound(Gdx.files.internal("music/sounds/hit3.mp3"));
		hitSounds[3] = Gdx.audio.newSound(Gdx.files.internal("music/sounds/hit4.mp3"));
		hitSounds[4] = Gdx.audio.newSound(Gdx.files.internal("music/sounds/hit5.mp3"));
		rand = new Random();

		// winSound = Gdx.audio.newSound(Gdx.files.internal("music/sounds/win.mp3"));
		// looseSound = Gdx.audio.newSound(Gdx.files.internal("music/sounds/loose.mp3"));
		countdownSound = Gdx.audio.newSound(Gdx.files.internal("music/sounds/countdown.mp3"));

		if (GlobalSettings.GAME == null) { // should not happen!!!
			init();
		}

		if (GlobalSettings.MENUS == null)
			GlobalSettings.MENUS = new GdxMenus();
		
		screenGame = new ScreenGame();
		setScreen(screenGame); // 
		
		Gdx.input.setInputProcessor(new GestureDetector(new MyGestureListener()));
	}

	@Override
	public void dispose() {
		super.dispose();
		for (Sound hitSound : hitSounds) {
			hitSound.dispose();
		}
		// winSound.dispose();
		// looseSound.dispose();
		countdownSound.dispose();
	}
	
	public void hitSound() {
		if (!GlobalSettings.SOUND_IS_MUTED) {
			int index = rand.nextInt(hitSounds.length);
			hitSounds[index].play();
		}
	}
/*
	public void winSound() {
		if (!GlobalSettings.SOUND_IS_MUTED) {
			winSound.play();
		}
	}
	
	public void looseSound() {
		if (!GlobalSettings.SOUND_IS_MUTED) {
			looseSound.play();
		}
	}
*/
	public void countdownSound() {
		if (!GlobalSettings.SOUND_IS_MUTED) {
			countdownSound.play();
		}
	}

	public void stopCountdownSound() {
		countdownSound.stop();
	}

	/**
	 * @param cNewMusic should be a file that can be read by GDX
	 * in 'assets/music/levels'
	 */
	public void setNewLoopMusic(final String cNewMusic) {
		if (screenGame == null) { // not loaded yet.
			return;
		}
		if (cNewMusic != null) {
			FileHandle pMusicFile = Gdx.files.internal("music/levels/" + cNewMusic);
			if (pMusicFile.exists()) {
				screenGame.setNewLoopMusic(pMusicFile);
				return;
			}
		} // load the default one (if it's not currently playing)
		screenGame.setDefaultLoopMusicPath();
	}

	public TimerMgr getTimer() {
		return timer;
	}
	
	public LevelLoader getLevel() {
		return level;
	}
}
