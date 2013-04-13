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

import java.util.ArrayList;
import java.util.Arrays;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ProfileMgr {
	private Preferences prefs;
	private static final String LAST_PROFILE_KEY = "last_profile";
	private static final String PROFILES_KEY = "profiles";

	public ProfileMgr () {
		prefs = Gdx.app.getPreferences (GlobalSettings.PREFS_GLOBAL);
		loadDefaultProfile ();
	}

	/**
	 * @return get the last profile or the default one
	 */
	public String getDefaultProfileName () {
		return prefs.getString (LAST_PROFILE_KEY, GlobalSettings.DEFAULT_PROFILE_NAME);
	}

	public void loadProfile (String cName) {
		GlobalSettings.PROFILE = new Profile (cName);
		prefs.putString (LAST_PROFILE_KEY, cName);
		prefs.flush ();
	}

	public void loadDefaultProfile () {
		loadProfile (getDefaultProfileName());
	}

	private String getAllProfilesAsString () {
		return prefs.getString (PROFILES_KEY, GlobalSettings.DEFAULT_PROFILE_NAME);
	}

	public String[] getAllProfiles () {
		return getAllProfilesAsString ().split (","); 
	}

	public ArrayList<String> getAllProfilesAndExceptions () {
		ArrayList<String> profiles = new ArrayList<String> (Arrays.asList(getAllProfiles ()));
		profiles.add (GlobalSettings.PREFS_GLOBAL); // we can't use a profile name with this name
		return profiles;
	}
	
	/**
	 * @pre: cName should not be included in PROFILES_KEY
	 * @param cName: the new profile name
	 */
	public void createAndLoadProfile (String cName) {
		String profiles = getAllProfilesAsString ();
		profiles += "," + cName;
		prefs.putString (PROFILES_KEY, profiles); // no need to flush => done in loadProfile
		loadProfile (cName);
	}
}
