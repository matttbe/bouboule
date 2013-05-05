package be.ac.ucl.lfsab1509.bouboule.game.timer;

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
import java.util.Collection;

import com.badlogic.gdx.utils.Timer;

public class TimerMgr {

	private Timer timer;
	private int iDelay, iInterval;
	
	private final Collection<TimerListener> timerListeners = new ArrayList<TimerListener>();
	
	public TimerMgr(int iDelay, int iInterval) {
		this.iDelay = iDelay;
		this.iInterval = iInterval;
	}

	//_____________ TIMER
	/**
	 * Create a new timer which should be launch with {@link #play()}.
	 */
	public void createNewTimer(final int iRemainingTime) {
		if (timer != null) { // stop the previous timer
			this.stop();
		}

		fireNewRemainingTime(iRemainingTime);

		Timer.Task task = new Timer.Task() {
			@Override
			public void run() {
				fireRun();
			}
		};
		timer = new Timer();
		timer.scheduleTask(task, iDelay, iInterval); // first time, time between
		timer.stop(); // do not launch it immediately
	}

	public boolean isRunning() {
		return (timer != null); // timer created
	}

	public void play() {
		timer.start();
	}

	public void pause() {
		timer.stop();
	}
	
	public void stop() {
		timer.stop();
		timer.clear(); // maybe not needed?
		timer = null;
	}

	//_______________ LISTENER
	//_______ Management
	public void addTimerListener(final TimerListener listener) {
		timerListeners.add(listener);
	}

	public void removeTimerListener(final TimerListener listener) {
		timerListeners.remove(listener);
	}

	//_______ Actions
	/**
	 * Launch.
	 */
	protected void fireRun() {
		for (TimerListener timerListener : timerListeners) {
			timerListener.run();
		}
	}

	protected void fireNewRemainingTime(final int iRemainingTime) {
		for (TimerListener timerListener : timerListeners) {
			timerListener.newTimer(iRemainingTime);
		}
	}

}
