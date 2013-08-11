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

package be.ac.ucl.lfsab1509.bouboule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import be.ac.ucl.lfsab1509.bouboule.game.MyGame;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
	
	private MyGame game;
	public static final int CODE_PAUSE_ACTIVITY = 1;
	public static final int CODE_MENU_ACTIVITY 	= 2;
	public static final int CODE_END_GAME 		= 3;
	public static final int CODE_CHOOSING_LEVEL	= 4;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		//cfg.useGL20 = false; 

		cfg.useGL20 = true;
		cfg.useAccelerometer = true;
		cfg.useCompass = false;

		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON); // to not lock the screen


		game = new MyGame ();

		Log.d ("Matth","initialize now");
		initialize (game, cfg);
		Log.d ("Matth","initialized");

	}

	@Override
	protected void onResume() {
		super.onResume();
		/* Set low profile:
		 * Navigation buttons dim and other elements in the system bar also hide
		 * Enabling this is useful for creating more immersive games without
		 * distraction for the system navigation buttons.
		 */
		getWindow().getDecorView().
			setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	@Override
	public void onBackPressed() {
		// game.getScreen ().pause(); // will do that automatically
		Gdx.app.log("Matth","Game should pause now !");
		if (GlobalSettings.GAME.isGameScreen())
			GlobalSettings.MENUS.launchPauseMenu();
	}

}