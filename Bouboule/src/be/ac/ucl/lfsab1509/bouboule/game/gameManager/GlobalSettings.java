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

package be.ac.ucl.lfsab1509.bouboule.game.gameManager;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.bouboule.game.MyGame;
import be.ac.ucl.lfsab1509.bouboule.game.ia.MapNode;
import be.ac.ucl.lfsab1509.bouboule.game.menu.Menus;
import be.ac.ucl.lfsab1509.bouboule.game.profile.Profile;
import be.ac.ucl.lfsab1509.bouboule.game.profile.ProfileMgr;

public class GlobalSettings {
	
	//Definition for the time step properties
	public static final float BOX_STEP			= 1 / 60f;
	public static final int VELOCITY_ITERATIONS	= 8;
	public static final int POSITION_ITERATIONS	= 3;
 	
 	public static final float LIMITACC	= 0.5f;
 	
 	
 	public static ArrayList<MapNode> ARENAWAYPOINTALLOW = new ArrayList<MapNode>(); //TODO: A mettre dasn IA
 	
 	public static enum GameExitStatus {NONE, WIN, LOOSE, GAMEOVER};
 	public static GameExitStatus 	GAME_EXIT	= GameExitStatus.NONE;

	public final static int 		INIT_LEVEL = 1;
	public final static int 		INIT_SCORE = 100;
	public final static int 		INIT_LIFES = 3; // TODO: Init_scrore and life: take data from config

	public static int				NBLEVELS; 		// nb of levels in the current xml files
	public static Menus 			MENUS; 			// interface to display menus

	// Settings that can be changed from ProfileGlobal
	public static boolean			SOUND_IS_MUTED = false; // false = sound on
	public static int 			SENSITIVITY = 500; // from 0 to 1000
	public static final int 		SENSITIVITY_MAX = 1000;

	// Profiles
 	public final static String		DEFAULT_PROFILE_NAME = "Bouboule";
	public final static String		PREFS_GLOBAL = "_GLOBAL_SETTINGS_";
	public static ProfileMgr		PROFILE_MGR = null;
	public static Profile			PROFILE = null;
	public static MyGame			GAME = null;


 	
}
