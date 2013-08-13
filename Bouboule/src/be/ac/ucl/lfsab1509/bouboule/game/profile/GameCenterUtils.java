package be.ac.ucl.lfsab1509.bouboule.game.profile;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

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


public class GameCenterUtils {

	public static void submitScore(int iNewScore) {
		if (GlobalSettings.GAMECENTER == null) return;

		GlobalSettings.GAMECENTER.submitScore(iNewScore);
	}

	private static int getWorld(int iLevel) {
		return (iLevel - 1) / 4 + 1; // 1 => 4 = W1 ; 5 => 8 = W2 (...)
	}

	// iLevel > 1 - iLevel: the best level played but NOT WON!
	public static void newBestLevel(int iLevel) {
		if (GlobalSettings.GAMECENTER == null) return;

		int iWorld = getWorld(iLevel - 1);
		int iPercents = (iLevel - 1 % 4) * 25; // iLevel == 2: only iLevel 1 won = 25%
		if (iPercents == 0)
			iPercents = 100; // 4 = 100%

		GlobalSettings.GAMECENTER.submitAchievement("world" + iWorld, iPercents);
	}

	public static void newDeath(int iNbDeaths) {
		if (GlobalSettings.GAMECENTER == null) return;

		if (iNbDeaths <= 50)  // 50 / 3 => 16.66 games
			GlobalSettings.GAMECENTER.submitAchievement("NbDeaths50", iNbDeaths * 2);
		if (iNbDeaths <= 100) // 33.3
			GlobalSettings.GAMECENTER.submitAchievement("NbDeaths100", iNbDeaths);
		if (iNbDeaths <= 500 && iNbDeaths % 5 == 0) // 166.6/2
			GlobalSettings.GAMECENTER.submitAchievement("NbDeaths500", iNbDeaths / 5);
	}

	public static void newBonus(int iNbBonus) {
		if (GlobalSettings.GAMECENTER == null) return;

		if (iNbBonus <= 10)
			GlobalSettings.GAMECENTER.submitAchievement("NbBonus10", iNbBonus * 10);
		if (iNbBonus <= 50)
			GlobalSettings.GAMECENTER.submitAchievement("NbBonus50", iNbBonus * 2);
		if (iNbBonus <= 100)
			GlobalSettings.GAMECENTER.submitAchievement("NbBonus100", iNbBonus);
	}

	// iEndLevel is not won
	public static void endGame(int iStartLevel, int iEndLevel) {
		if (GlobalSettings.GAMECENTER == null) return;

		int iNbWorlds = getWorld(iEndLevel) - getWorld(iStartLevel);
		if (iNbWorlds > 1) // min 2, max NBLEVELS
			GlobalSettings.GAMECENTER.submitAchievement("WorldsInARow" + iNbWorlds, 100);
	}
}
