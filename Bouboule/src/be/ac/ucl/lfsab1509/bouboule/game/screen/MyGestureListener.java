package be.ac.ucl.lfsab1509.bouboule.game.screen;

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

import be.ac.ucl.lfsab1509.bouboule.game.entity.Entity;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.EndGameListener;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GameLoop;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class MyGestureListener implements GestureListener {

	@Override
	public boolean touchDown(final float x, final float y, final int pointer, final int button) {
		return false;
	}

	@Override
	public boolean tap(final float x, final float y, final int count, final int button) {
		if (GlobalSettings.PROFILE.getName().equals(GlobalSettings.CHEATER_NAME)) {
			GameLoop.bonus(true);
		}
		else if (GlobalSettings.GAME.isGdxMenus()) {
			// if (y < 150) // the top of the screen
			Gdx.app.log("SCREEN", "click: " + x + "x" + y);
			if (GlobalSettings.GAME.isGeneralPause() // pause and clicking on 'menu' icon
					&& x < 100 && y > (Gdx.graphics.getHeight() - 100))
				GlobalSettings.MENUS.launchInitMenu();
			else
				GlobalSettings.MENUS.launchPauseMenu();
			return true;
		}
		return false;
	}

	@Override
	public boolean longPress(final float x, final float y) {
		if (GlobalSettings.PROFILE.getName().equals(GlobalSettings.CHEATER_NAME)) {
			GameLoop.iBonus = (GameLoop.iBonus + 1) % (Entity.BonusType.values().length + 2);
		}
		return false;
	}

	@Override
	public boolean fling(final float velocityX, final float velocityY, final int button) {	
		return false;
	}

	@Override
	public boolean pan(final float x, final float y, final float deltaX, final float deltaY) {
		return false;
	}

	@Override
	public boolean zoom(final float initialDistance, final float distance) {	
		return false;
	}

	@Override
	public boolean pinch(final Vector2 initialPointer1, final Vector2 initialPointer2,
			final Vector2 pointer1, final Vector2 pointer2) {
		
		Gdx.app.log("Touch", "Pinch event " + GlobalSettings.GAME.getTimer().isRunning());
		
		if (GlobalSettings.PROFILE.getName().equals(GlobalSettings.CHEATER_NAME)
				&& GlobalSettings.GAME.getTimer().isRunning() 
				// prevent double stop, double level up, etc.
				&& GlobalSettings.PROFILE.getScore() < GlobalSettings.PROFILE.getNewInitScore()) { 
			// it seems this method is called just after the resume... 
			// wait at least one second before cheating ;)
			EndGameListener.winGame(); // cheater!!!!
		}
			
		return false;
	}

}
