package be.ac.ucl.lfsab1509.bouboule.game.profile;

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

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class BoubImages {
	private static final String GIANT_SUFFIX = "_giant.png";
	private static final String SMALL_SUFFIX = "_small.png";
	private static final String BOUB_DIR = "images/boub";
	
	/**
	 * @return a list of files all bouboules images which have a normal size
	 */
	public static ArrayList<FileHandle> getAllNormalBoub () {
		FileHandle pDir = Gdx.files.internal(BOUB_DIR);
		FileHandle pAllFiles[] = pDir.list ();
		ArrayList<FileHandle> pList = new ArrayList<FileHandle> (pAllFiles.length);

		for (int i = 0; i < pAllFiles.length; i++) {
			FileHandle pCurrFile = pAllFiles[i];

			if (! pCurrFile.name ().endsWith (GIANT_SUFFIX) // only normal files
					&& ! pCurrFile.name ().endsWith (SMALL_SUFFIX))
				pList.add (pCurrFile);
		}

		return pList;
	}
}
