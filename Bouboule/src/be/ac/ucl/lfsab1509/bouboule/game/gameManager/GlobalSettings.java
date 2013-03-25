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

import com.badlogic.gdx.math.Vector2;

public class GlobalSettings {
	
	//Definition for the time step properties
	public static final float BOX_STEP			= 1 / 60f;
	public static final int VELOCITY_ITERATIONS	= 8;
	public static final int POSITION_ITERATIONS	= 3;
	
	public static final short CATEGORY_PLAYER 	= 0x0001;  // 0000000000000001 in binary
	public static final short CATEGORY_MONSTER 	= 0x0002; // 0000000000000010 in binary
	public static final short CATEGORY_SCENERY 	= 0x0004; // 0000000000000100 in binary
	
	public static final short MASK_PLAYER 	= CATEGORY_MONSTER | CATEGORY_SCENERY; 
											// or ~CATEGORY_PLAYER
	public static final short MASK_MONSTER 	= CATEGORY_PLAYER | CATEGORY_SCENERY; 
											// or ~CATEGORY_MONSTER
	public static final short MASK_SCENERY 	= 0;
	public static final short MASK_ALLCONT 	= -1;
	
	public static final short GROUP_PLAYER 	= -1;
	public static final short GROUP_MONSTER = -2;
	public static final short GROUP_SCENERY = 1;
	
	public static final short PLAYER 	=  1;
	public static final short MONSTER 	= -1;
 	public static final short SCENERY 	=  0;
 	
 	
 	public static final float LIMITACC	= 0.4f;
 	
 	
 	public static Vector2[] ARENAWAYPOINTALLOW = null;
 	public static Vector2[] ARENAWAYPOINTDENY = null;
 	
 	public static short		 GAME_EXIT	=  0;
 	public static int  		 LEVEL		=  1;
 	public static int  		 LIVES		=  3;

	
	public static void init(){
		ARENAWAYPOINTALLOW = new Vector2[]{new Vector2(3.5f,5.0f)};//, new Vector2(4,6)};
		ARENAWAYPOINTDENY = new Vector2[]{new Vector2(-3.5f,5.0f), new Vector2(10.5f,5.0f)};
	}
 	
}
