package be.ac.ucl.lfsab1509.bouboule.game.menu;

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

/**
 * Interface needed to interact with menus.
 * Menus are (currently) not created with GDX
 * (mainly because Android Menu are really nice and easy to do and maintain :) )
 *
 */
public interface Menus {

	/**
	 * Launch the init menu: to play a new game, change parameters, see
	 * highscores, etc.
	 */
	public void launchInitMenu();
	
	/**
	 * Launch the right menu at the end of a game: when winning, loosing or when
	 * the game is over.
	 */
	public void launchEndGameMenu();
}
