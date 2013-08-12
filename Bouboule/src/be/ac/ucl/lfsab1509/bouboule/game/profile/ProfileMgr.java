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

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.EndGameListener;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ProfileMgr {
	private Preferences prefs;
	private ProfileGlobal profileGlobal;

	protected final String SEPARATOR = System.getProperty ("file.separator"); // /!\ can change if we use it on different systems

	private static final String LAST_PROFILE_KEY = "last_profile";
	private static final String PROFILES_KEY = "profiles";

	/**
	 * Create PREFS_GLOBAL, PROFILE and load default settings.
	 */
	public ProfileMgr() {
		prefs = Gdx.app.getPreferences(GlobalSettings.PREFS_GLOBAL);
		loadDefaultProfile();
		profileGlobal = new ProfileGlobal(prefs, SEPARATOR);
		profileGlobal.loadDefaultSettings();
	}

	/**
	 * @return get the last profile name or null if we need the default one.
	 */
	private String getDefaultProfileName() {
		return prefs.getString(LAST_PROFILE_KEY, null);
	}

	/**
	 * Load an profile that already exists.
	 * @pre this profile should have been created with {@link #createAndLoadNewProfile(String)}
	 * @param cName, the profile name
	 */
	private void loadProfile(final String cName) {
		GlobalSettings.PROFILE = new Profile(cName);
		prefs.putString(LAST_PROFILE_KEY, cName);
		prefs.flush();
	}

	private boolean gameCenterHasNewId(String cName) {
		if (GlobalSettings.GAMECENTER != null) {
			String playerID = GlobalSettings.GAMECENTER.getPlayerID();
			return playerID != null && ! playerID.equals(cName);
		}
		return false;
		
	}

	/**
	 * Load the default profile (the last profile that has been used or.
	 * DEFAULT_PROFILE_NAME)
	 */
	private void loadDefaultProfile() {
		String profileName = getDefaultProfileName();

		// it's the first time we launch the game: we need tuto
		if (profileName == null || gameCenterHasNewId(profileName)) {
			if (GlobalSettings.GAMECENTER != null)
				loadProfile(GlobalSettings.GAMECENTER.getPlayerID());
			else
				loadProfile(GlobalSettings.DEFAULT_PROFILE_NAME);
			GlobalSettings.PROFILE.setNeedTutorial(true); // display the tutorial the first time
		}
		else
			loadProfile(profileName);
	}

	private String getAllProfilesAsString() {
		return prefs.getString(PROFILES_KEY, GlobalSettings.DEFAULT_PROFILE_NAME);
	}

	/**
	 * @return an array with all profiles' name
	 */
	public String[] getAllProfiles() {
		return getAllProfilesAsString().split(SEPARATOR);
	}
	
	public ArrayList<String> getAllProfilesAL() {
		ArrayList<String> profiles = new ArrayList<String>(Arrays.asList(getAllProfiles()));
		return profiles;
	}

	/**
	 * @return an arraylist with all profiles that can't be used when creating
	 * a new one
	 */
	public ArrayList<String> getAllProfilesAndExceptions() {
		ArrayList<String> profiles = new ArrayList<String>(Arrays.asList(getAllProfiles()));
		profiles.add(GlobalSettings.PREFS_GLOBAL); // we can't use a profile name with this name
		return profiles;
	}

	/**
	 * Change the current profile.
	 * @param cName the profile that already exists.
	 */
	public void changeProfile(final String cName) {
		Gdx.app.log("LN", "Load: " + cName);
		EndGameListener.cancelGame();
		loadProfile(cName);
	}

	/**
	 * Create and load a new profile.
	 * @pre: cName should not be included in PROFILES_KEY and can't contain invalid char:
	 *  => String.IndexOfAny (System.IO.Path.GetInvalidPathChars ()) == 0
	 * @param cName: the new profile name
	 */
	public void createAndLoadNewProfile(final String cName) {
		EndGameListener.cancelGame();
		String profiles = getAllProfilesAsString();
		profiles += SEPARATOR + cName; // should at least contain Bouboule (default)
		prefs.putString(PROFILES_KEY, profiles); // no need to flush => done in loadProfile
		loadProfile(cName);
		GlobalSettings.PROFILE.setNeedTutorial(true); // display the tutorial the first time
	}

	/**
	 * Switch to user: create a new one if it's needed
	 * @param cName the new profile name
	 */
	public void switchUser(final String cName) {
		// check the last user
		if (getDefaultProfileName().equals(cName)) {
			Gdx.app.log("PROFILE", "Not a new user: " + cName);
			return;
		}
		for (String profile : getAllProfiles()) {
			if (profile.equals(cName)) {
				Gdx.app.log("PROFILE", "This profile already exists");
				changeProfile(cName);
				return;
			}
		}
		createAndLoadNewProfile(cName);
	}

	public ProfileGlobal getProfileGlobal() {
		return this.profileGlobal;
	}
}
