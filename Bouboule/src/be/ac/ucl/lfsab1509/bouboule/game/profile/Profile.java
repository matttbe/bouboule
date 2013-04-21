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
	private String cBoubName;
	private Preferences prefs;
	private final static String INIT_SCORE_KEY = "InitScore";
	private final static String LIFES_KEY = "Lifes";
	private final static String LEVEL_KEY = "Level";
	private final static String BEST_LEVEL_KEY = "BestLevel";
	private final static String SCORE_KEY = "Score";
	private final static String HIGHSCORE_KEY = "HighScore";
	private final static String BOUB_NAME_KEY = "BoubName";

	// score
	private int iScore;
	private int iInitScore;
	private int iNewInitScore;
	private int iOldScore; // point before the battle
	private int iEndGameScore; // score just before the reset
	private int iHighScore;
	private boolean bNewHighScore = false;
	private boolean bNeedSaveScoreEvenIfCancel = false;

	// lifes
	private int iLifes;

	// level
	private int iLevel;
	private int iBestLevel;

	// tutorial
	private boolean bNeedTuto = false;
	
	public Profile (String cName)
	{
		this.cName = cName;
		prefs = Gdx.app.getPreferences (cName);

		iInitScore = prefs.getInteger (INIT_SCORE_KEY, GlobalSettings.INIT_SCORE);
		iLifes = prefs.getInteger (LIFES_KEY, GlobalSettings.INIT_LIFES);
		iLevel = prefs.getInteger (LEVEL_KEY, GlobalSettings.INIT_LEVEL);
		iBestLevel = prefs.getInteger (BEST_LEVEL_KEY, GlobalSettings.INIT_LEVEL);
		iScore = prefs.getInteger (SCORE_KEY, 0);
		iHighScore = prefs.getInteger (HIGHSCORE_KEY, 0);
		cBoubName = prefs.getString (BOUB_NAME_KEY, GlobalSettings.DEFAULT_BOUB_NAME);
	}

	/**
	 * Check if there is a new high score (for this profile and for all profiles)
	 * Reset all data about the score (init score, lifes, level, current score)
	 * @return true if there is a new highscore global (for all profiles)
	 */
	public boolean checkHighScoreAndResetProfile () {
		boolean bNewHighScoreGlobal = checkHighScore ();

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

		return bNewHighScoreGlobal;
	}

	//__________ NAME and IMAGE

	public String getName () {
		return cName;
	}

	/**
	 * @return the name of the image (ex: boub -> boub.png, boub.json)
	 */
	public String getBoubName () {
		return cBoubName;
	}

	/**
	 * Set a new name for the bouboube (image)
	 * @param cBoubName, the name of the image (ex: boub -> boub.png, boub.json)
	 */
	public void setBoubName (String cBoubName) {
		this.cBoubName = cBoubName;
		prefs.putString (BOUB_NAME_KEY, cBoubName);
		prefs.flush ();
	}

	//__________ TIMER
	
	/**
	 * Create a new timer and init the score (but do not start this timer)
	 * The timer will decrement the score each second
	 * The new init score will increase when playing new levels
	 */
	public void createTimer () {
		if (timer != null) { // stop the previous timer
			this.stop ();
		}

		this.iOldScore = this.iScore;
		this.iNewInitScore = this.iScore + this.iInitScore + this.iInitScore / 4 * (this.iLevel - 1);
		this.iScore = iNewInitScore; // TODO: more score?
		this.bNewHighScore = false;
		this.bNeedSaveScoreEvenIfCancel = false;

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

	/**
	 * Add score: bonus (will only be saved if the player wins the current level)
	 * @param iNewScore, the new bonus
	 */
	public void addScore (int iNewScore) {
		iScore += iNewScore;
	}

	/**
	 * Add score: bonus (will be saved even if the player looses the current level)
	 * @param iNewScore, the new bonus
	 */
	public void addScorePermanent (int iNewScore) {
		iScore += iNewScore;
		iOldScore += iNewScore;
		bNeedSaveScoreEvenIfCancel = true;
	}

	/**
	 * Stop the timer and revert the score
	 * @return true if there is a new highscore
	 * (e.g. if the user get permanent bonus {@link #addScorePermanent(int)})
	 */
	public void cancelNewScore () {
		iScore = iOldScore;
		if (bNeedSaveScoreEvenIfCancel)
			saveScore ();
		else
			stop ();
	}
	
	/**
	 * @return the score before playing this game
	 */
	public int getOldScore() {
		return iOldScore;
	}

	/**
	 * @return the score at the beginning of the current game
	 */
	public int getNewInitScore () {
		return iNewInitScore;
	}

	/**
	 * @return the score that the user had just before the reset
	 */
	public int getEndGameScore () {
		return iEndGameScore;
	}

	public int getHighScore () {
		return iHighScore;
	}

	public boolean isNewHighScore () {
		return bNewHighScore;
	}

	private boolean checkHighScore () {
		bNewHighScore = false;
		if (iScore > iHighScore)
		{
			iHighScore = iScore;
			prefs.putInteger (HIGHSCORE_KEY, iHighScore);
			prefs.flush ();
			bNewHighScore = true;
		}
		return GlobalSettings.PROFILE_MGR.getProfileGlobal ().checkHighScoreGlobal ();
	}

	/**
	 * Stop the timer, check if there is a new highscore and save the scores
	 * @return true if there is a new highscore
	 */
	public void saveScore () {
		stop ();
		prefs.putInteger (SCORE_KEY, iScore);
		prefs.flush ();
	}

	//__________ LIFES

	public int getNbLifes () {
		return iLifes;
	}

	/**
	 * 
	 * @param iNewLifes, the new life(s) (can be negative)
	 * @return false if there is no more life.
	 */
	public boolean addLifes (int iNewLifes) {
		int iNewLifesTmp = iLifes + iNewLifes;
		if (iNewLifesTmp <= GlobalSettings.MAX_LIFES) // we can't add more than 3 lifes
			iLifes = iNewLifesTmp;

		if (iLifes <= 0) // no more life
			return false;

		this.prefs.putInteger (LIFES_KEY, iLifes);
		prefs.flush ();
		return true;
	}

	//__________ LEVEL

	/**
	 * @return true if there is no more level
	 */
	public boolean LevelUp () {
		if (iLevel >= GlobalSettings.NBLEVELS) // we are already on the last level
			return false;

		setLevel (iLevel + 1);
		return true;
	}

	/**
	 * Set the new level
	 * @pre iNewLevel < GlobalSettings.NBLEVELS
	 */
	public void setLevel (int iNewLevel) {
		iLevel = iNewLevel;
		checkBestLevel ();
		this.prefs.putInteger (LEVEL_KEY, iLevel);
		prefs.flush ();
	}

	public int getLevel () {
		return iLevel;
	}

	private void checkBestLevel () {
		if (iLevel > iBestLevel) {
			iBestLevel = iLevel; // no need to flush
			this.prefs.putInteger (BEST_LEVEL_KEY, iBestLevel);
		}
	}

	/**
	 * @return the best level that has already been played
	 */
	public int getBestLevel () {
		return iBestLevel;
	}

	//__________ TUTORIAL
	public boolean needTutorial () {
		return bNeedTuto;
	}

	public void setNeedTutorial (boolean bNeedTuto) {
		this.bNeedTuto = bNeedTuto;
	}
}
