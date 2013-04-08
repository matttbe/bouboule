package be.ac.ucl.lfsab1509.bouboule;

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


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;

import android.content.Intent;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings.GameExitStatus;
import be.ac.ucl.lfsab1509.bouboule.game.menu.Menus;

public class MyAndroidMenus implements Menus {

	private AndroidApplication app;

	public MyAndroidMenus (AndroidApplication app) {
		this.app = app;
	}

	@Override
	public void launchInitMenu () {
		Intent intent = new Intent (app, Menu.class);
		app.startActivityForResult (intent, MainActivity.CODE_MENU_ACTIVITY);
	}


	// TODO: remove this toast when the score will be displayed somewhere else
	/*private void displayInfo () {
		Context context = app.getApplicationContext ();
		CharSequence text = "Score: " + GlobalSettings.PROFILE.getScore ()
				+ "\nLifes: " + GlobalSettings.PROFILE.getNbLifes ()
				+ "\nLevel: " + GlobalSettings.PROFILE.getLevel ()
				+ "\n" + (GlobalSettings.PROFILE.isNewHighScore () ? "NEW " : "")
				+ "HighScore: " + GlobalSettings.PROFILE.getHighScore ();
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText (context, text, duration);
		toast.show ();
	}*/

	@Override
	public void launchEndGameMenu () {
		Gdx.app.log ("Matth", "MainActivity: pause: " + GlobalSettings.GAME_EXIT);
		Intent intent = null;
		switch (GlobalSettings.GAME_EXIT)
		{
			case NONE :
				return;
			case WIN :
				intent = new Intent (app, VictoryActivity.class);
				break;
			case LOOSE :
				intent = new Intent (app, LoosingActivity.class);
				break;
			case GAMEOVER :
				intent = new Intent (app, GameOverActivity.class);
				break;
		}

		app.startActivityForResult (intent, MainActivity.CODE_END_GAME);
		app.overridePendingTransition (android.R.anim.fade_in,
				android.R.anim.fade_out);
		GlobalSettings.GAME_EXIT = GameExitStatus.NONE;
	}

}
