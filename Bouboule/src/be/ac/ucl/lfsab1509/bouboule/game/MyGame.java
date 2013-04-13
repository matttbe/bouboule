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
import be.ac.ucl.lfsab1509.bouboule.game.screen.ScreenGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class MyGame extends Game {

	public ScreenGame screenGame;
	private Sound hitSound;
	private Sound winSound;
	private Sound looseSound;
	private boolean bIsMuted = false;

	@Override
	public void create () {
		Gdx.app.log ("Matth", "Game: Create");
		hitSound = Gdx.audio.newSound(Gdx.files.internal("music/drop.mp3")); // TODO: find sound and use parameters?
		winSound = Gdx.audio.newSound(Gdx.files.internal("music/drop.mp3"));
		looseSound = Gdx.audio.newSound(Gdx.files.internal("music/drop.mp3"));

		loadDefaultProfile ();

		screenGame = new ScreenGame();
		setScreen (screenGame); // 
	}

	public void loadDefaultProfile () {
		if (GlobalSettings.PROFILE_MGR == null)
			GlobalSettings.PROFILE_MGR = new ProfileMgr ();
	}

	@Override
	public void dispose () {
		super.dispose ();
		hitSound.dispose ();
		winSound.dispose ();
		looseSound.dispose ();
	}
	
	public void hitSound () {
		if (! bIsMuted)
			hitSound.play ();
	}
	
	public void winSound () {
		if (! bIsMuted)
			winSound.play ();
	}
	
	public void looseSound () {
		if (! bIsMuted)
			looseSound.play ();
	}

	public boolean getMuteStatus () {
		return bIsMuted;
	}
	
	public void muteAllSounds () {
		screenGame.mute ();
		bIsMuted = true;
	}
	
	public void unMuteAllSounds () {
		screenGame.unmute ();
		bIsMuted = false;
	}
}
