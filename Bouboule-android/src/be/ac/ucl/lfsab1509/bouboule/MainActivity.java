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
import be.ac.ucl.lfsab1509.bouboule.game.*;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication
{
	private Game game;

	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate (savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration ();
		// cfg.useGL20 = false;

		cfg.useGL20 = true;
		cfg.useAccelerometer = true;
		cfg.useCompass = false;

		game = new Game ();
		initialize (game, cfg);
	}

	@Override
	public void onBackPressed ()
	{
		game.pause ();
		// TODO Menu
	}
}
