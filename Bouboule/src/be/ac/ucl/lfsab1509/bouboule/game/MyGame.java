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

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.profile.ProfileMgr;
import be.ac.ucl.lfsab1509.bouboule.game.screen.MyGestureListener;
import be.ac.ucl.lfsab1509.bouboule.game.screen.ScreenGame;
import be.ac.ucl.lfsab1509.bouboule.game.timer.TimerMgr;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.input.GestureDetector;

public class MyGame extends Game {

	private ScreenGame screenGame;
	private Sound hitSound;
	private Sound winSound;
	private Sound looseSound;

	private TimerMgr timer;

	/**
	 * This class should be the first one which is called after having
	 * initialized GDX
	 */
	public void init () {
		GlobalSettings.GAME = this;
		timer = new TimerMgr (1, 1);
		GlobalSettings.PROFILE_MGR = new ProfileMgr ();
	}

	@Override
	public void create () {
		Gdx.app.log ("Matth", "Game: Create");
		hitSound = Gdx.audio.newSound(Gdx.files.internal("music/drop.mp3")); // TODO: find sound and use parameters?
		winSound = Gdx.audio.newSound(Gdx.files.internal("music/drop.mp3"));
		looseSound = Gdx.audio.newSound(Gdx.files.internal("music/drop.mp3"));

		if (GlobalSettings.GAME == null) // should not happen!!!
			init ();

		screenGame = new ScreenGame();
		setScreen (screenGame); // 
		
		Gdx.input.setInputProcessor( new GestureDetector(new MyGestureListener()));
	}

	@Override
	public void dispose () {
		super.dispose ();
		hitSound.dispose ();
		winSound.dispose ();
		looseSound.dispose ();
	}
	
	public void hitSound () {
		if (! GlobalSettings.SOUND_IS_MUTED)
			hitSound.play ();
	}
	
	public void winSound () {
		if (! GlobalSettings.SOUND_IS_MUTED)
			winSound.play ();
	}
	
	public void looseSound () {
		if (! GlobalSettings.SOUND_IS_MUTED)
			looseSound.play ();
	}

	/**
	 * @param cNewMusic should be a file that can be read by GDX in 'music'
	 */
	public void setNewLoopMusic (String cNewMusic) {
		if (screenGame == null) // not loaded yet.
			return;
		if (cNewMusic != null) {
			FileHandle pMusicFile = Gdx.files.internal ("music/" + cNewMusic);
			if (pMusicFile.exists ()) {
				screenGame.setNewLoopMusic (pMusicFile);
				return;
			}
		}// load the default one (if it's not currently playing)
		screenGame.setDefaultLoopMusicPath ();
	}

	public TimerMgr getTimer () {
		return timer;
	}
}
