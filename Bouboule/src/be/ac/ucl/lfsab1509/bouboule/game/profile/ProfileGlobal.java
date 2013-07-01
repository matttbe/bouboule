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


import java.text.SimpleDateFormat;
import java.util.Date;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.ia.IA;

public class ProfileGlobal {
	private String SEPARATOR;
	private String HIGHSCORE_DEFAULT_VALUE;

	private  SimpleDateFormat dateFormat;

	private static final int iMaxNbHighScore = 5;

	private static final String MUTE_SOUND_KEY = "mute_sound";
	private static final String HIGHSCORE_KEY = "highscore"; // score + name + level + date
	private static final String SENSITIVITY_KEY = "sensitivity";
	private static final String FIXED_ROTATIONS_KEY = "fixed_rotations";

	private Preferences prefs;

	public ProfileGlobal(Preferences prefs, String SEPARATOR) {

		if (Gdx.app.getType() != ApplicationType.iOS)
			this.dateFormat = new SimpleDateFormat("dd MM yyyy HH:mm:ss Z");
		
		this.prefs = prefs;
		this.SEPARATOR = SEPARATOR;
		this.HIGHSCORE_DEFAULT_VALUE = 0 + SEPARATOR // score
				+ GlobalSettings.DEFAULT_PROFILE_NAME + SEPARATOR // name
				+ 0 + SEPARATOR // level
				+ "01 01 1970 00:00:00 0000"; // date
	}

	/**
	 * Load settings that can be saved between sessions.
	 */
	public void loadDefaultSettings() {
		GlobalSettings.SOUND_IS_MUTED = prefs.getBoolean(MUTE_SOUND_KEY, false);
		GlobalSettings.SENSITIVITY = prefs.getInteger(SENSITIVITY_KEY,
				(GlobalSettings.SENSITIVITY_MAX + GlobalSettings.SENSITIVITY_MIN) / 2);
		GlobalSettings.FIXED_ROTATION = prefs.getBoolean(FIXED_ROTATIONS_KEY, true);
	}

	//_________________ SAVE SETTINGS THAT CAN BE CHANGED FROM THE CONFIG PANEL
	public void changeSoundSettings(final boolean newSoundIsMuted) {
		GlobalSettings.SOUND_IS_MUTED = newSoundIsMuted;
		prefs.putBoolean(MUTE_SOUND_KEY, GlobalSettings.SOUND_IS_MUTED);
		prefs.flush();
	}

	public void changeSensibilitySettings(final int newSensitivity) {
		GlobalSettings.SENSITIVITY = newSensitivity;
		IA.setNewSensitivity ();
		prefs.putInteger(SENSITIVITY_KEY, newSensitivity);
		prefs.flush();
	}

	public void changeFixedRotation(final boolean bFixedRotation) {
		GlobalSettings.FIXED_ROTATION = bFixedRotation;
		prefs.putBoolean(FIXED_ROTATIONS_KEY, bFixedRotation);
		prefs.flush();
	}

	//_______________ HighScore Mgr
	/**
	 * Check if there is a new highscore. If yes, save it in the global settings
	 * @param iScore, the new score
	 * @return true if there is a new highscore
	 */
	public boolean checkHighScoreGlobal() {
		boolean bNewHighScore = false;
		int iNewScore = GlobalSettings.PROFILE.getScore();

		String cCurrInfo, cPrevInfo = null;
		for (int i = 0; i < iMaxNbHighScore; i++) {
			cCurrInfo = prefs.getString(HIGHSCORE_KEY + i, null);
			if (bNewHighScore) { // move the other highscore
				if (cPrevInfo != null) {
					prefs.putString(HIGHSCORE_KEY + i, cPrevInfo);
				}
				if (cCurrInfo == null) { // no need to continue
					break;
				}
				cPrevInfo = cCurrInfo;
				continue;
			}

			int iCurrScore = Integer.MIN_VALUE;
			if (cCurrInfo != null) {
				cPrevInfo = cCurrInfo; // save the older string
				cCurrInfo = cCurrInfo.substring(0, cCurrInfo.indexOf(SEPARATOR)); // get the score
				try {
					iCurrScore = Integer.parseInt(cCurrInfo);
				} catch (NumberFormatException e) {
					iCurrScore = Integer.MIN_VALUE;
				}

			} else {
				cPrevInfo = null;
			}

			if (iNewScore > iCurrScore) { // new high score!
				cCurrInfo = iNewScore + SEPARATOR // Score
						+ GlobalSettings.PROFILE.getName() + SEPARATOR // name
						+ GlobalSettings.PROFILE.getLevel(); // level
				if (Gdx.app.getType() != ApplicationType.iOS)
					cCurrInfo += SEPARATOR // level
						+ dateFormat.format(new Date()).toString(); // date TODO:iOS => no date
				prefs.putString(HIGHSCORE_KEY + i, cCurrInfo); // save the new score here
				bNewHighScore = true;
			}
		}

		if (bNewHighScore) {
			prefs.flush();
		}

		return bNewHighScore;
	}

	/**
	 * @param bFillWithDefaultValues: true to fill highscore array with default
	 * value if no highscore is saved for this id.
	 * @return all HighScore as an array (fill with default values if needed)
	 */
	public HighScoreInfo[] getAllHighScores(final boolean bFillWithDefaultValues) {

		HighScoreInfo[] highScores = new HighScoreInfo[iMaxNbHighScore];

		for (int i = 0; i < highScores.length; i++) {
			String[] infos;
			if (bFillWithDefaultValues) {
				infos = prefs.getString(HIGHSCORE_KEY + i,
						HIGHSCORE_DEFAULT_VALUE).split(SEPARATOR);
			} else {
				String info = prefs.getString(HIGHSCORE_KEY + i, null);
				if (info == null) {
					break;
				}

				infos = info.split(SEPARATOR);
			}
			String cName = infos[1];
			int iScore;
			int iLevel;
			Date pDate;
			try {
				iScore = Integer.parseInt(infos[0]);
				iLevel = Integer.parseInt(infos[2]);
				if (Gdx.app.getType() != ApplicationType.iOS)
					pDate = dateFormat.parse(infos[3]);
				else
					pDate = new Date(); // TODO:iOS
			} catch (Exception e) { // should not happen...
				iScore = 0;
				iLevel = 0;
				pDate = new Date();
			}
			highScores[i] = new HighScoreInfo(cName, iScore, iLevel, pDate);
		}
		return highScores;
	}
}
