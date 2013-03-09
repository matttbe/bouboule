/*
 * This file is part of Bouboule.
 * 
 * Copyright 2013 UCLouvain
 * 
 * Authors: * Group 7 - Course:
 * http://www.uclouvain.be/en-cours-2013-lfsab1509.html Matthieu Baerts
 * <matthieu.baerts@student.uclouvain.be> Baptiste Remy
 * <baptiste.remy@student.uclouvain.be> Nicolas Van Wallendael
 * <nicolas.vanwallendael@student.uclouvain.be> Hélène Verhaeghe
 * <helene.verhaeghe@student.uclouvain.be>
 * 
 * Bouboule is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package be.ac.ucl.lfsab1509.bouboule;

import android.os.Bundle;
import android.view.WindowManager.LayoutParams;
import android.content.Intent;
import be.ac.ucl.lfsab1509.bouboule.game.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication
{
	private Game game;
	
	private static final int CODE_PAUSE_ACTIVITY = 1;


	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate (savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration ();
		// cfg.useGL20 = false;

		cfg.useGL20 = true;
		cfg.useAccelerometer = true;
		cfg.useCompass = false;
		// cfg.useWakelock = true; // to not lock the screen // CRASH!!
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON); // to not lock the screen
	
		game = new Game ();
		initialize (game, cfg);
	}

	@Override
	public void onBackPressed ()
	{
		game.pause ();
		Intent intent = new Intent (MainActivity.this, MenuPause.class);
		startActivityForResult (intent, CODE_PAUSE_ACTIVITY);
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode,
			Intent data)
	{
		Gdx.app.log ("Matth", "onActivityResult (" + requestCode + ", " + resultCode + ")");
		switch (requestCode)
		{
			case CODE_PAUSE_ACTIVITY: // menu pause
				switch (resultCode) // it's the id of the button
				{
					case R.id.PauseContinueButton: // cas ou on continue
						game.resume ();
						return;
					case R.id.PauseMenuButton: // cas ou on stoppe
						Intent intent = new Intent(MainActivity.this, Menu.class);
						startActivity(intent);
						finish ();
					case R.id.PauseQuitButton: // just quit without new activity => quit
						finish ();
						exit ();
				}
		}
		return;
	}
}
