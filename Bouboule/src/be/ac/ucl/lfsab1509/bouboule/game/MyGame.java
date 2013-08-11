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
import be.ac.ucl.lfsab1509.bouboule.game.screen.ScreenGame;
import be.ac.ucl.lfsab1509.bouboule.game.timer.TimerMgr;
import be.ac.ucl.lfsab1509.bouboule.game.util.CameraHelper;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MyGame extends Game implements ApplicationListener {

	private ScreenGame screenGame;
	private boolean bGdxMenus;

	private OrthographicCamera camera;

	private Sound hitSounds[];
	private boolean bNeedEndSounds;
	private Sound winSound; // not used on Android
	private Sound looseSound; // not used on Android
	private Music menusMusic; // not used on Android
	private Sound countdownSound;

	private Random rand;
	private LevelLoader level;

	private TimerMgr timer;

	private boolean bGeneralPause = false; // not used on Android

	//All other
	public MyGame() {
	}
	
	//iOS Launcher 
	public MyGame(GameCenter gc) {
		GlobalSettings.GAMECENTER = gc;
	}
	
	
	
	/**
	 * This class should be the first one which is called after having
	 * initialized GDX
	 */
	public void init(boolean bNeedEndSounds) {
		this.bNeedEndSounds = bNeedEndSounds;
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

		countdownSound = Gdx.audio.newSound(Gdx.files.internal("music/sounds/countdown.mp3"));

		if (GlobalSettings.GAME == null) // should not happen on Android!!!
			init(true);

		if (bNeedEndSounds) {
			winSound = Gdx.audio.newSound(Gdx.files.internal("music/sounds/win.mp3"));
			looseSound = Gdx.audio.newSound(Gdx.files.internal("music/sounds/loose.mp3"));
		}

		camera = CameraHelper.getCamera(GlobalSettings.APPWIDTH,
				GlobalSettings.APPHEIGHT);

		if (GlobalSettings.MENUS == null) {
			bGdxMenus = true;
			GlobalSettings.MENUS = new GdxMenus();
			GlobalSettings.MENUS.launchInitMenu();
		}
		else // e.g. Android => there is only one screen: Screen Game
			setScreenGame();
	}

	@Override
	public void dispose() {
		super.dispose();
		for (Sound hitSound : hitSounds) {
			hitSound.dispose();
		}
		if (bNeedEndSounds) {
			winSound.dispose();
			looseSound.dispose();
		}
		if (menusMusic != null)
			menusMusic.dispose();
		countdownSound.dispose();
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void hitSound() {
		if (!GlobalSettings.SOUND_IS_MUTED) {
			int index = rand.nextInt(hitSounds.length);
			hitSounds[index].play();
		}
	}

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
		Gdx.app.log("SCREEN", "New music: " + cNewMusic + " " + (screenGame == null));
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

	/**
	 * Switch to our 'Game' screen. Create it if it's the first time that we
	 * use it.
	 * If GdxMenus are used, stop the music (for the menus) first and send
	 *  'Resume' signal to correctly use screenGame
	 */
	public void setScreenGame () {
		if (screenGame == null)
			screenGame = new ScreenGame();
		/*
		 * We have to follow the same way when using Gdx and Android menus
		 * This is what we have in ScreenGame when using Android menus:
		 *  First Game: Show ; Resume ; (... countdown ...) ; Resume ; 
		 *              (... end of the game ...) ; Hide ; Pause
		 *  New Game:   Resume ; (then => @First Game)
		 */
		if (bGdxMenus) {
			if (menusMusic != null) { // stop the music and free the allocated memory
				menusMusic.dispose();
				menusMusic = null;
			}
			bGeneralPause = false;
			setScreenGameResume();
			setScreen(screenGame);
			setScreenGameResume();
		}
		else
			setScreen(screenGame);
	}

	public void setScreenGameHide () {
		if (screenGame != null)
			screenGame.hide();
	}

	public void setScreenGamePause () {
		if (screenGame != null)
			screenGame.pause();
	}

	public void setScreenGameResume () {
		if (screenGame != null)
			screenGame.resume();
	}

	public boolean isGameScreen() {
		return getScreen() == screenGame;
	}

	public boolean isPlayingGame() {
		return (GlobalSettings.PROFILE.getLevel() == 1
				&& GlobalSettings.GAME.getTimer().isRunning()
				&& GlobalSettings.PROFILE.getNewInitScore() // and not started
				!= GlobalSettings.PROFILE.getScore())
				|| GlobalSettings.PROFILE.getLevel() > 1;
	}

	/**
	 * @return the music for the menu (create a new one if it doesn't exist)
	 */
	public Music getMenusMusic () {
		if (menusMusic == null) {
			menusMusic = Gdx.audio.newMusic(Gdx.files.internal("music/sounds/menu.mp3"));
			menusMusic.setLooping(true);
		}
		return menusMusic;
	}

	public boolean isGeneralPause() {
		return bGeneralPause;
	}

	public void toogleGeneralPause() {
		bGeneralPause = ! bGeneralPause;
	}

	public boolean isGdxMenus() {
		return bGdxMenus;
	}
}
