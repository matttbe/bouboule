package be.ac.ucl.lfsab1509.bouboule.game.gameManager;

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

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings.GameExitStatus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;


/*
 * Listener for detecting the end of the game.
 */
public class EndGameListener implements ContactListener{

	private static int isAlivePlayer 	= 0;
	private static int isAliveMonster	= 0;
	
	@Override
	public void beginContact(final Contact contact) {
		short entity1 = (Short) contact.getFixtureA().getBody().getUserData();
		short entity2 = (Short) contact.getFixtureB().getBody().getUserData();


		if (entity1 == GlobalSettings.SCENERY 
				| entity2 == GlobalSettings.SCENERY) {
			
			if (entity1 == GlobalSettings.PLAYER 
					| entity2 == GlobalSettings.PLAYER) {

				isAlivePlayer ++;
				Gdx.app.log("Alive", "Begin Contact = "+isAlivePlayer);


			} else {

				
				isAliveMonster ++;

			}
		} else if((entity1 == GlobalSettings.PLAYER  && entity2 == GlobalSettings.MONSTER) |
				  (entity1 == GlobalSettings.MONSTER && entity2 == GlobalSettings.PLAYER) ) {
			Gdx.app.log("Chocs de Bouboules", "CHOCS || CHOCS");
			GlobalSettings.GAME.hitSound (); 
		}

	}

	@Override
	public void endContact(final Contact contact) {


		short entity1 = (Short) contact.getFixtureA().getBody().getUserData();
		short entity2 = (Short) contact.getFixtureB().getBody().getUserData();


		if (entity1 == GlobalSettings.SCENERY 
				| entity2 == GlobalSettings.SCENERY) {
			
			if (entity1 == GlobalSettings.PLAYER 
					| entity2 == GlobalSettings.PLAYER) {
				
				if (isAlivePlayer > 1) {
					//DO NOTHING =)
					isAlivePlayer --;
					// Gdx.app.log("Alive", "End Contact = "+isAlivePlayer);
					//DO NOTHING =)

				}
				else {
					GlobalSettings.GAME.looseSound ();
					Gdx.app.log("KILL", "Bouboule est MORT =/");
					
					GlobalSettings.GAME_EXIT = GameExitStatus.LOOSE;
					GlobalSettings.PROFILE.cancelNewScore ();
					if (! GlobalSettings.PROFILE.addLifes (-1)) {
						GlobalSettings.GAME_EXIT = GameExitStatus.GAMEOVER;
						GlobalSettings.PROFILE.resetProfile (); // TODO: what to do?
					}
					
					GlobalSettings.GAME.getScreen ().hide (); // notify the screen that we'll need a new game
					
					GlobalSettings.MENUS.launchEndGameMenu ();
					//Gdx.app.exit();
				}
			}
			else {
				if (isAliveMonster > 1) {
					// Gdx.app.log("Alive", "MONSTER est VIVANT =)");
					//DO NOTHING
					isAliveMonster --;
				}
				else {
					GlobalSettings.GAME.winSound ();
					Gdx.app.log("KILL", "Bouboule a gagné =P");
					GlobalSettings.PROFILE.saveScore ();
					if (GlobalSettings.PROFILE.LevelUp ())
						GlobalSettings.GAME_EXIT = GameExitStatus.WIN;
					else {
						GlobalSettings.GAME_EXIT = GameExitStatus.GAMEOVER;
						GlobalSettings.PROFILE.resetProfile (); // TODO: what to do?
					}
					GlobalSettings.GAME.getScreen ().hide (); // notify the screen that we'll need a new game

					GlobalSettings.MENUS.launchEndGameMenu ();
				}
				
			}
		}
	}

	@Override
	public void preSolve(final Contact contact, final Manifold oldManifold) {
	}

	@Override
	public void postSolve(final Contact contact, final ContactImpulse impulse) {
		
	}
	
	public static void resetListener() {
		
		isAlivePlayer 	= 0;
		isAliveMonster	= 0;
	}



}
