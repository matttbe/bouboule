package be.ac.ucl.lfsab1509.bouboule.game.profile;

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
 *    Hélène Verhaeghe <helene.verhaeghe@student.uclouvain.be>
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


import com.badlogic.gdx.Preferences;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

public class ProfileGlobal {
	
	private static final String MUTE_SOUND_KEY = "mute_sound";

	private Preferences prefs;
	
	public ProfileGlobal (Preferences prefs) {
		this.prefs = prefs;
	}

	public void loadDefaultSettings () {
		GlobalSettings.SOUND_IS_MUTED = prefs.getBoolean (MUTE_SOUND_KEY, false);
	}
	
	public void toggleSoundSettings () {
		GlobalSettings.SOUND_IS_MUTED = ! GlobalSettings.SOUND_IS_MUTED;
		prefs.putBoolean (MUTE_SOUND_KEY, GlobalSettings.SOUND_IS_MUTED);
		prefs.flush ();
	}
}
