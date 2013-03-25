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

package be.ac.ucl.lfsab1509.bouboule.game.gameManager;

import com.badlogic.gdx.utils.Timer;

public class Score {

	private Timer timer;

	// point
	private int iScore;
	private int iInitScore;
	
	// lifes
	private int iLifes;

	public Score (int iInitScore, int iInitLifes) {
		this.iInitScore = iInitScore;
		this.iLifes = iInitLifes;
	}
	
	public void LaunchTimer () {
		if (timer != null) { // stop the previous timer
			this.stop ();
		}

		this.iScore = this.iInitScore;

		Timer.Task task = new Timer.Task () {
			@Override
			public void run () {
				iScore--; // launched in the main loop (no need to use mutex)
			}
		};
		timer = new Timer ();
		timer.scheduleTask (task, 1, 1); // first time, time between
	}

	/**
	 * @return the current score
	 */
	public int getScore () {
		return iScore;
	}
	
	public void addScore (int iNewScore) {
		this.iScore += iNewScore;
	}

	public int getNbLifes () {
		return iLifes;
	}

	public void addLifes (int iNewLifes) {
		this.iLifes += iNewLifes;
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

}
