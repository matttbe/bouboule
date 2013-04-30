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
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class BoubImages {

	public static final String BOUB_EXTENTION = ".png";

	public static final String BOUB_DIR_NORMAL = "boub/normal/";
	public static final String BOUB_DIR_GIANT  = "boub/giant/";
	public static final String BOUB_DIR_SMALL  = "boub/small/";

	public static final String BOUB_JSON_EXT   = "boub.json";
	public static final String BOUB_PATH = "android_asset/";
	
	
	/**
	 * @return a list of files all bouboules images which have a normal size
	 */
	public static ArrayList<FileHandle> getAllNormalBoub () {
		FileHandle pDir = Gdx.files.internal(BOUB_DIR_NORMAL);

		return new ArrayList<FileHandle>(Arrays.asList(pDir.list (".png")));
	}
	
	public static ArrayList<FileHandle> getAllGiantBoub () {
		FileHandle pDir = Gdx.files.internal(BOUB_DIR_GIANT);

		return new ArrayList<FileHandle>(Arrays.asList(pDir.list (".png")));
	}
	
	public static FileHandle getBoubFH(String name){
		FileHandle pDir = Gdx.files.internal(BOUB_DIR_NORMAL);
		return pDir.child(name + ".png");
	}
	
	public static String getBoubS(FileHandle fileHandle){
		return fileHandle.nameWithoutExtension();
	}
	
}
