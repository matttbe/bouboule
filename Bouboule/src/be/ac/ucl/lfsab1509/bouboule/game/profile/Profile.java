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

package be.ac.ucl.lfsab1509.bouboule.game.profile;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Timer;

public class Profile {

	private Timer timer;

	// user
	private String cName;
	private Preferences prefs;
	private final static String INIT_SCORE_KEY = "InitScore";
	private final static String LIFES_KEY = "Lifes";
	private final static String LEVEL_KEY = "Level";
	private final static String SCORE_KEY = "Score";
	private static final String HIGHSCORE_KEY = "HighScore";

	// score
	private int iScore;
	private int iInitScore;
	private int iNewInitScore;
	private int iOldScore; // point before the battle
	private int iEndGameScore; // score just before the reset
	private int iHighScore;
	private boolean bNewHighScore = false;
	
	// lifes
	private int iLifes;

	// level
	private int iLevel;

	
	public Profile (String cName)
	{
		this.cName = cName;
		prefs = Gdx.app.getPreferences (cName);

		iInitScore = prefs.getInteger (INIT_SCORE_KEY, GlobalSettings.INIT_SCORE);
		iLifes = prefs.getInteger (LIFES_KEY, GlobalSettings.INIT_LIFES);
		iLevel = prefs.getInteger (LEVEL_KEY, GlobalSettings.INIT_LEVEL);
		iScore = prefs.getInteger (SCORE_KEY, 0);
		iHighScore = prefs.getInteger (HIGHSCORE_KEY, 0);
	}

	public void resetProfile () {
		iInitScore = GlobalSettings.INIT_SCORE;
		prefs.putInteger (INIT_SCORE_KEY, iInitScore);
		iLifes = GlobalSettings.INIT_LIFES;
		prefs.putInteger (LIFES_KEY, iLifes);
		iLevel = GlobalSettings.INIT_LEVEL;
		prefs.putInteger (LEVEL_KEY, iLevel);
		iEndGameScore = iScore;
		iScore = 0;
		prefs.putInteger (SCORE_KEY, iScore);
		prefs.flush (); // Makes sure the preferences are persisted.
		// don't reset the highscore...
	}

	public String getName () {
		return cName;
	}

	//__________ TIMER
	
	public void createTimer () {
		if (timer != null) { // stop the previous timer
			this.stop ();
		}

		this.iOldScore = this.iScore;
		this.iNewInitScore = this.iScore + this.iInitScore + this.iInitScore / 4 * (this.iLevel - 1);
		this.iScore = iNewInitScore; // TODO: more score?

		Timer.Task task = new Timer.Task () {
			@Override
			public void run () {
				iScore--; // launched in the main loop (no need to use mutex)
			}
		};
		timer = new Timer ();
		timer.scheduleTask (task, 1, 1); // first time, time between
		timer.stop (); // do not launch it immediately
	}

	public boolean isRunning () {
		return (timer != null); // timer created
	}

	public void play () {
		timer.start ();
	}

	public void pause () {
		timer.stop ();
	}
	
	public void stop () {
		timer.stop ();
		timer.clear (); // maybe not needed?
		timer = null;
	}

	/**
	 * @return the current score
	 */
	public int getScore () {
		return iScore;
	}
	
	public void addScore (int iNewScore) {
		iScore += iNewScore;
		saveScore ();
	}

	public void cancelNewScore () {
		stop ();
		iScore = iOldScore;
	}
	
	public int getOldScore() {
		return iOldScore;
	}

	public int getEndGameScore () {
		return iEndGameScore;
	}

	public int getHighScore () {
		return iHighScore;
	}

	public boolean isNewHighScore () {
		return bNewHighScore;
	}

	public boolean saveScore () {
		stop ();
		prefs.putInteger (SCORE_KEY, iScore);
		bNewHighScore = false;
		if (iScore > iHighScore)
		{
			iHighScore = iScore;
			prefs.putInteger (HIGHSCORE_KEY, iHighScore);
			bNewHighScore = true;
		}
		prefs.flush ();
		return bNewHighScore;
	}

	//__________ LIFES

	public int getNbLifes () {
		return iLifes;
	}

	public boolean addLifes (int iNewLifes) {
		iLifes += iNewLifes;

		if (iLifes <= 0)
			return false;

		this.prefs.putInteger (LIFES_KEY, iLifes);
		prefs.flush ();
		return true;
	}

	//__________ LEVEL

	public boolean LevelUp () {
		if (iLevel >= GlobalSettings.NBLEVELS) // we are already on the last level
			return false;

		iLevel++;
		this.prefs.putInteger (LEVEL_KEY, iLevel);
		prefs.flush ();
		return true;
	}

	public int getLevel () {
		return iLevel;
	}

}
