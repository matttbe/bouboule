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
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.net.Uri;

public class BackgroundSound {
	private Uri file;
	private AsyncPlayer player;
	
	public BackgroundSound (String cTag, int iResId) {
		file = Uri.parse("android.resource://be.ac.ucl.lfsab1509.bouboule/" + iResId);
		player = new AsyncPlayer (cTag);
	}
	
	public void stop () {
		player.stop ();
	}
	
	public void play (Context context) {
		player.play (context, file, true, AudioManager.STREAM_MUSIC);
	}

}
