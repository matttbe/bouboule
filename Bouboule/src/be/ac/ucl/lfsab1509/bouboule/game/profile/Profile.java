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
import be.ac.ucl.lfsab1509.bouboule.game.timer.TimerListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Application.ApplicationType;

public class Profile {
	// user
	private String cName;
	private String cBoubName;

	private Preferences prefs;
	private static final String LIFES_KEY = "Lifes";
	private static final String LEVEL_KEY = "Level";
	private static final String BEST_LEVEL_KEY = "BestLevel";
	private static final String START_LEVEL_KEY = "StartLevel";
	private static final String SCORE_KEY = "Score";
	private static final String HIGHSCORE_KEY = "HighScore";
	private static final String BOUB_NAME_KEY = "BoubName";
	private static final String DEATHS_KEY = "Deaths";
	private static final String BONUS_KEY = "Bonus";
	private static final String ALL_LEVELS_WON = "AllLevelsWon";
	private static final String WORLDS_IN_A_ROW = "WorldsInARow";

	// score
	private int iScore;
	private int iNewInitScore;
	private int iOldScore; // point before the battle
	private int iEndGameScore; // score just before the reset
	private int iHighScore;
	private boolean bNewHighScore = false;
	private boolean bNeedSaveScoreEvenIfCancel = false;

	// timer
	private int iRemainingTime;

	// lifes
	private int iLifes;

	// level
	private int iLevel;
	private int iBestLevel;

	// tutorial
	private boolean bNeedTuto = false;

	// Misc. statistics
	private int iNbDeaths;
	private int iNbBonus;
	private int iStartLevel;
	private boolean bAllLevelsWon;
	private boolean bWorldsInARow[];

	public Profile(final String cName) {
		
		this.cName = cName;
		
		// In iOS, the storage folder is home/Documents/* not home/Library
		prefs = Gdx.app.getPreferences(
				Gdx.app.getType() == ApplicationType.iOS ? 
						"../Documents/"	+ cName
						: cName);

		// reset at the end of the game
		iLifes = prefs.getInteger(LIFES_KEY, GlobalSettings.INIT_LIFES);
		iLevel = prefs.getInteger(LEVEL_KEY, GlobalSettings.INIT_LEVEL);
		iScore = prefs.getInteger(SCORE_KEY, 0);
		iStartLevel = prefs.getInteger(START_LEVEL_KEY, GlobalSettings.INIT_LEVEL);

		// should not be reset
		if (cName.equals(GlobalSettings.CHEATER_NAME))
			iBestLevel = GlobalSettings.NBLEVELS;
		else
			iBestLevel = prefs.getInteger(BEST_LEVEL_KEY, GlobalSettings.INIT_LEVEL);
		iHighScore = prefs.getInteger(HIGHSCORE_KEY, Integer.MIN_VALUE);
		cBoubName = prefs.getString(BOUB_NAME_KEY, GlobalSettings.DEFAULT_BOUB_NAME);
		iNbDeaths = prefs.getInteger(DEATHS_KEY, 0);
		iNbBonus = prefs.getInteger(BONUS_KEY, 0);
		bAllLevelsWon = prefs.getBoolean(ALL_LEVELS_WON, false);
		bWorldsInARow = new boolean[GlobalSettings.NBLEVELS - 1];
		for (int i = 2; i <= GlobalSettings.NBLEVELS; i++)
			bWorldsInARow[i-2] = prefs.getBoolean(WORLDS_IN_A_ROW + i, false);

		addNewTimerListener();
	}

	/**
	 * Check if there is a new high score (for this profile and for all profiles).
	 * Reset all data about the score (init score, lifes, level, current score).
	 * @pre need to be done asap (before calling gamecenter, etc. to avoid cheating)
	 * @return true if there is a new highscore global (for all profiles)
	 */
	public boolean checkHighScoreAndResetProfile() {
		boolean bNewHighScoreGlobal = checkHighScore();

		iLifes = GlobalSettings.INIT_LIFES;
		prefs.putInteger(LIFES_KEY, iLifes);
		iLevel = GlobalSettings.INIT_LEVEL;
		prefs.putInteger(LEVEL_KEY, iLevel);
		iStartLevel = GlobalSettings.INIT_LEVEL;
		prefs.putInteger(START_LEVEL_KEY, iStartLevel);
		iEndGameScore = iScore;
		iScore = 0;
		prefs.putInteger(SCORE_KEY, iScore);
		prefs.flush(); // Makes sure the preferences are persisted.
		// don't reset the highscore...

		return bNewHighScoreGlobal;
	}

	//__________ NAME and IMAGE

	public String getName() {
		if (GlobalSettings.GAMECENTER != null)
			return GlobalSettings.GAMECENTER.getPlayerName();
		return cName;
	}

	/**
	 * @return the name of the image (ex: boub -> boub.png, boub.json)
	 */
	public String getBoubName() {
		return cBoubName;
	}

	/**
	 * Set a new name for the bouboube (image).
	 * @param cBoubName, the name of the image (ex: boub -> boub.png, boub.json)
	 */
	public void setBoubName(final String cBoubname) {
		this.cBoubName = cBoubname;
		prefs.putString(BOUB_NAME_KEY, cBoubName);
		prefs.flush();
	}

	//__________ TIMER
	private void addNewTimerListener() {
		TimerListener listener = new TimerListener() {
			@Override
			public void run() {
				iScore--; // launched in the main loop (no need to use mutex)
				iRemainingTime--;
			}
			
			@Override
			public void newTimer(final int iTimer) {
				iRemainingTime = iTimer;

				iOldScore = iScore;
				iNewInitScore = iScore + GlobalSettings.INIT_SCORE
						+ GlobalSettings.INIT_SCORE / 4 * (iLevel - 1);
				iScore = iNewInitScore;
				bNewHighScore = false;
				bNeedSaveScoreEvenIfCancel = false;
			}
		};

		GlobalSettings.GAME.getTimer().addTimerListener(listener);
	}

	/**
	 * @return the remaining time
	 */
	public int getRemainingTime() {
		return iRemainingTime;
	}

	public void addRemainingTime (int iAddTime) {
		iRemainingTime += iAddTime;
	}

	//__________ SCORE

	/**
	 * @return the current score
	 */
	public int getScore() {
		return iScore;
	}

	/**
	 * Add score: bonus (will only be saved if the player wins the current level).
	 * @param iNewScore, the new bonus
	 */
	public void addScore(final int iNewScore) {
		iScore += iNewScore;
	}

	/**
	 * Add score: bonus (will be saved even if the player looses the current level).
	 * @param iNewScore, the new bonus
	 */
	public void addScorePermanent(final int iNewScore) {
		iScore += iNewScore;
		iOldScore += iNewScore;
		bNeedSaveScoreEvenIfCancel = true;
	}

	/**
	 * Stop the timer and revert the score.
	 * @return true if there is a new highscore
	 * (e.g. if the user get permanent bonus {@link #addScorePermanent(int)})
	 */
	public void cancelNewScore() {
		iScore = iOldScore;
		if (bNeedSaveScoreEvenIfCancel) {
			saveScore();
		} else {
			endGame(true);
		}
	}

	/**
	 * @return the score at the beginning of the current game
	 */
	public int getNewInitScore() {
		return iNewInitScore;
	}

	/**
	 * @return the score that the user had just before the reset
	 */
	public int getEndGameScore() {
		return iEndGameScore;
	}

	public int getHighScore() {
		return iHighScore;
	}

	public boolean isNewHighScore() {
		return bNewHighScore;
	}

	private boolean checkHighScore() {
		bNewHighScore = false;
		if (iScore > iHighScore) {
			iHighScore = iScore;
			prefs.putInteger(HIGHSCORE_KEY, iHighScore);
			prefs.flush();
			bNewHighScore = true;
		}
		return GlobalSettings.PROFILE_MGR.getProfileGlobal().checkHighScoreGlobal();
	}

	/**
	 * Stop the timer, check if there is a new highscore and save the scores.
	 * @return true if there is a new highscore
	 */
	public void saveScore() {
		endGame(false);
		prefs.putInteger(SCORE_KEY, iScore);
		prefs.flush();
	}

	//__________ LIFES

	public int getNbLifes() {
		return iLifes;
	}

	/**
	 * 
	 * @param iNewLifes, the new life(s) (can be negative)
	 * @return false if there is no more life.
	 */
	public boolean addLifes(final int iNewLifes) {
		iLifes += iNewLifes;
		if (iLifes > GlobalSettings.MAX_LIFES) // we can't add more than 3 lifes
			iLifes = GlobalSettings.MAX_LIFES;

		this.prefs.putInteger(LIFES_KEY, iLifes);

		if (iNewLifes < 0) {
			iNbDeaths -= iNewLifes;
			prefs.putInteger(DEATHS_KEY, iNbDeaths);


			if (iLifes <= 0) {
				int iStartLevel = this.iStartLevel;
				int iEndLevel = this.iLevel;
				checkHighScoreAndResetProfile(); // reset iLevel and iStartLevel
				GameCenterUtils.newDeath(iNbDeaths);
				gameOver(iStartLevel, iEndLevel);
				return false; // false <=> no more life
			}

			GameCenterUtils.newDeath(iNbDeaths);
			/* flush only if we loose lifes: no need to flush during the game
			 * (if we quit during the game, we lost bonus)
			 */
			prefs.flush();
		}

		return true;
	}

	//__________ LEVEL

	/**
	 * @return true if there is no more level
	 */
	public boolean levelUp() {
		if (iLevel >= GlobalSettings.NBLEVELS) { // we are already on the last level
			int iStartLevel = this.iStartLevel;
			int iEndLevel = this.iLevel;
			checkHighScoreAndResetProfile(); // reset iLevel and iStartLevel
			if (! bAllLevelsWon) { // notif endlevel only one
				bAllLevelsWon = true;
				prefs.putBoolean(ALL_LEVELS_WON, bAllLevelsWon);
				prefs.flush();
				GameCenterUtils.newBestLevel(iEndLevel + 1); // => last level not won
			}
			gameOver(iStartLevel, iEndLevel + 1); // => last level not won
			return false;
		}

		setLevel(iLevel + 1, false);
		return true;
	}

	/**
	 * Set the new level.
	 * @pre iNewLevel < GlobalSettings.NBLEVELS
	 */
	public void setLevel(final int iNewLevel, final boolean bNewStartLevel) {
		iLevel = iNewLevel;
		checkBestLevel();
		this.prefs.putInteger(LEVEL_KEY, iLevel);
		if (bNewStartLevel) {
			iStartLevel = iNewLevel;
			prefs.putInteger(START_LEVEL_KEY, iStartLevel);
		}
		prefs.flush();
	}

	public int getLevel() {
		return iLevel;
	}

	private void checkBestLevel() {
		if (iLevel > iBestLevel) {
			iBestLevel = iLevel; // no need to flush
			this.prefs.putInteger(BEST_LEVEL_KEY, iBestLevel);

			GameCenterUtils.newBestLevel(iLevel);
		}
	}

	/**
	 * @return the best level that has already been played
	 */
	public int getBestLevel() {
		return iBestLevel;
	}

	//__________ TUTORIAL
	public boolean needTutorial() {
		return bNeedTuto;
	}

	public void setNeedTutorial(final boolean bNeedtuto) {
		this.bNeedTuto = bNeedtuto;
	}

	//__________ BONUS
	public void newBonus() {
		iNbBonus++;
		GameCenterUtils.newBonus(iNbBonus);
		// 'prefs' will be modified at the end of the game
	}

	//__________ GAME
	/**
	 * Stop the timer and check some achievements
	 */
	private void endGame(boolean bFlush) {
		GlobalSettings.GAME.getTimer().stop();

		if (iNbBonus != prefs.getInteger(BONUS_KEY, 0)) {
			prefs.putInteger(BONUS_KEY, iNbBonus);
			if (bFlush)
				prefs.flush();
		}
		// other achievements?
	}

	// iLastLevel <=> level not won
	private void gameOver(int iStartLevel, int iLastLevel) {
		boolean bNeedFlush = false;

		int iNbWorlds = GameCenterUtils.getWorld(iLastLevel)
				- GameCenterUtils.getWorld(iStartLevel);

		for (int i = 2; i <= iNbWorlds; i++) {
			if (! bWorldsInARow[i-2]) { // the first time we do that
				bNeedFlush = true;
				bWorldsInARow[i-2] = true;
				prefs.putBoolean(WORLDS_IN_A_ROW + i, true);
				GameCenterUtils.worldsInARow(i);
			}
		}

		if (bNeedFlush)
			prefs.flush();
	}
}
