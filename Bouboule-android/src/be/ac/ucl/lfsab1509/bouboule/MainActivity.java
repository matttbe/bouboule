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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;
import be.ac.ucl.lfsab1509.bouboule.game.MyGame;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
	
	private MyGame game;
	private static final int CODE_PAUSE_ACTIVITY = 1;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		//cfg.useGL20 = false; 

		cfg.useGL20 = true;
		cfg.useAccelerometer = true;
		cfg.useCompass = false;

		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON); // to not lock the screen
	
		game = new MyGame();
		GlobalSettings.GAME = game;
		initialize(game, cfg);
	}

	@Override
	public void onBackPressed()	{
		game.pause();
		Intent intent = new Intent(MainActivity.this, MenuPause.class);
		startActivityForResult(intent, CODE_PAUSE_ACTIVITY);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode,
			final Intent data) {

		Log.d("Matth", "onActivityResult (" + requestCode + ", " + resultCode + ")");
		switch (requestCode) {
		
			case CODE_PAUSE_ACTIVITY: // menu pause
				switch (resultCode) { // it's the id of the button
				
					case R.id.PauseContinueButton: // cas ou on continue
						game.resume();
						return;
					case R.id.PauseMenuButton: // cas ou on stoppe
						Intent intent = new Intent(MainActivity.this, Menu.class);
						startActivity(intent);
						finish();
						break;
					case R.id.PauseQuitButton: // just quit without new activity => quit
						finish();
						exit();
						break;
				default:
					break;
				}
		default:
			break;
		}
		return;
	}

	@Override
	protected void onPause () {
		super.onPause ();

		if (GlobalSettings.GAME_EXIT != 0) {
			int exit = GlobalSettings.GAME_EXIT;

			GlobalSettings.GAME_EXIT = 0;

			if (exit == 1) {
				Context context = getApplicationContext ();
				CharSequence text = "Score: " + game.getUserScore ().getScore ()
						+ "\nLifes: " + game.getUserScore ().getNbLifes ();
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText (context, text, duration);
				toast.show ();

				Intent intent = new Intent (this, VictoryActivity.class);
				startActivity (intent);
				overridePendingTransition (android.R.anim.fade_in,
						android.R.anim.fade_out);

			}
			else if (exit == -1) {
				Context context = getApplicationContext ();
				CharSequence text = "Score: " + 0 + "\nLifes: " + game.getUserScore ().getNbLifes ();
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText (context, text, duration);
				toast.show ();
				
				Intent intent = new Intent (this, LoosingActivity.class);
				startActivity (intent);
				overridePendingTransition (android.R.anim.fade_in,
						android.R.anim.fade_out);

			}

		}

	}


}