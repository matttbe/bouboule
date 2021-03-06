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

import java.util.concurrent.atomic.AtomicBoolean; // @@COMMENT_GWT@@

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings.GameExitStatus;
import be.ac.ucl.lfsab1509.bouboule.game.entity.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


/**
 * Listener for detecting the end of the game.
 */
public class EndGameListener implements ContactListener{

	private static int isAlivePlayer 	= 0;
	private static int isAliveMonster	= 0;

/* // @@COMMENT_GWT@@
	private class AtomicBoolean {
		private boolean bStatus;
		public AtomicBoolean() {
			bStatus = false;
		}

		public boolean compareAndSet(boolean isStatus, boolean newStatus) {
			if (bStatus == isStatus)
				return false;
			bStatus = newStatus;
			return true;
		}

		public void set(boolean newStatus) {
			bStatus = newStatus;
		}
	}
*/ // @@COMMENT_GWT@@

	// it's maybe not needed to use that if all actions are launched in the mainloop but is it?
	private static AtomicBoolean bIsEnding = null;

	public EndGameListener() {
		super();
		bIsEnding = new AtomicBoolean();
	}

	@Override
	public void beginContact(final Contact contact) {
		short entity1 = ((Entity) contact.getFixtureA().getBody().getUserData()).getEntity();
		short entity2 = ((Entity) contact.getFixtureB().getBody().getUserData()).getEntity();

		// Gdx.app.log("Contact", entity1 + "    "+ entity2);

		if (entity1 == Entity.SCENERY 
				| entity2 == Entity.SCENERY) {

			if (entity1 == Entity.BONUS | entity2 == Entity.BONUS) {

				((Entity) contact.getFixtureA().getBody().getUserData()).setAlive(true);
				((Entity) contact.getFixtureB().getBody().getUserData()).setAlive(true);

			} else {

				if (entity1 == Entity.PLAYER 
						| entity2 == Entity.PLAYER) {

					isAlivePlayer++;
					// Gdx.app.log("Alive", "Begin Contact = "+isAlivePlayer);

				} else if (entity1 == Entity.MONSTER 
						| entity2 == Entity.MONSTER) {

					isAliveMonster++;
				}

			}

		} else if ((entity1 == Entity.PLAYER  && entity2 == Entity.MONSTER) 
				| (entity1 == Entity.MONSTER && entity2 == Entity.PLAYER)) {

			// Gdx.app.log("Chocs de Bouboules", "CHOCS || CHOCS");
			GlobalSettings.GAME.hitSound();

		
		} else if (entity1 == Entity.BONUS) {
			((Entity) contact.getFixtureA().getBody().getUserData()).attributeBonus(entity2, 
					contact.getFixtureB());
		
		} else if (entity2 == Entity.BONUS) {
			((Entity) contact.getFixtureB().getBody().getUserData()).attributeBonus(entity1, 
					contact.getFixtureA());
		}

	}

	@Override
	public void endContact(final Contact contact) {

		Fixture fixture1 = contact.getFixtureA();
		Fixture fixture2 = contact.getFixtureB();
		/* With nightly 20130729, endContact is called when bodies are destroyed
		 * http://code.google.com/p/libgdx/issues/detail?id=1515
		 * https://github.com/libgdx/libgdx/commit/5bf1e73
		 */
		if (fixture1 == null || fixture2 == null) {
			Gdx.app.log("KILL", "endContact but no fixture: skip");
			return;
		}
		short entity1 = ((Entity) fixture1.getBody().getUserData()).getEntity();
		short entity2 = ((Entity) fixture2.getBody().getUserData()).getEntity();


		if (entity1 == Entity.SCENERY 
				| entity2 == Entity.SCENERY) {

			if (entity1 == Entity.PLAYER 
					| entity2 == Entity.PLAYER) {

				if (isAlivePlayer > 1) {
					//DO NOTHING =)
					isAlivePlayer--;
					// Gdx.app.log("Alive", "End Contact = "+isAlivePlayer);
					//DO NOTHING =)

				
				} else if (GlobalSettings.GAME_EXIT == GameExitStatus.NONE) { 
					// we can loose and win the game at the same time!!
					looseGame();
					//Gdx.app.exit();
				}
				
			} else if (entity1 == Entity.MONSTER 
					| entity2 == Entity.MONSTER) {
				if (isAliveMonster > 1) {
					/// Gdx.app.log("Alive", "MONSTER is alive " + (isAliveMonster-1));
					//DO NOTHING
					isAliveMonster--;
				
				} else if (GlobalSettings.GAME_EXIT == GameExitStatus.NONE) { 
					// we can loose and win the game at the same time!!
					winGame();
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

	private static void endGame(final boolean bWithMenu) {
		Gdx.app.log("KILL", "EndGame: hide + launch menu " + bWithMenu);
		GlobalSettings.GAME.setScreenGameHide(); // notify the screen that we'll need a new game

		if (bWithMenu) {
			GlobalSettings.MENUS.launchEndGameMenu();
		}
	}

	public static void looseGame() {
		Gdx.app.log("KILL", "Bouboule is dead!");

		if (GlobalSettings.GAME.getTimer().isRunning()
				&& bIsEnding.compareAndSet(false, true)) {
			// avoid the case where both bouboules loose (go away at the "same" time)
			Gdx.app.log("KILL", "Bouboule is dead: end game");

			GlobalSettings.PROFILE.addScorePermanent(-GlobalSettings.INIT_SCORE / 2);
			GlobalSettings.PROFILE.cancelNewScore();
			// do not lose life if we are a cheater!
			if (GlobalSettings.PROFILE.getName().equals(GlobalSettings.CHEATER_NAME)
					|| GlobalSettings.PROFILE.addLifes(-1))
				GlobalSettings.GAME_EXIT = GameExitStatus.LOOSE;
			else
				GlobalSettings.GAME_EXIT = GameExitStatus.GAMEOVER_LOOSE;

			endGame(true);
			GlobalSettings.GAME_EXIT = GameExitStatus.NONE;

			bIsEnding.set(false);
		}
	}

	public static void winGame() {
		Gdx.app.log("KILL", "Bouboule win!");

		if (GlobalSettings.GAME.getTimer().isRunning()
				&& bIsEnding.compareAndSet(false, true)) {
			// avoid the case where both bouboules loose (go away at the "same" time)
			Gdx.app.log("KILL", "Bouboule has won: end game");

			GlobalSettings.PROFILE.saveScore();
			if (GlobalSettings.PROFILE.levelUp())
				GlobalSettings.GAME_EXIT = GameExitStatus.WIN;
			else // no more level: end game
				GlobalSettings.GAME_EXIT = GameExitStatus.GAMEOVER_END;

			endGame(true);
			GlobalSettings.GAME_EXIT = GameExitStatus.NONE;

			bIsEnding.set(false);
		}
	}

	public static void cancelGame() {
		Gdx.app.log("KILL", "Cancel Game!");
		if (GlobalSettings.GAME.getTimer().isRunning()) {
			GlobalSettings.PROFILE.cancelNewScore();
			GlobalSettings.GAME_EXIT = GameExitStatus.GAMEOVER_LOOSE; // we just need a new game
			endGame(false);
			GlobalSettings.GAME_EXIT = GameExitStatus.NONE; // we need a new game
		}
	}

	public static void resetGame() {
		Gdx.app.log("KILL", "Reset Game!");
		if (GlobalSettings.GAME.getTimer().isRunning()) {
			GlobalSettings.PROFILE.cancelNewScore();
		}
		GlobalSettings.GAME_EXIT = GameExitStatus.GAMEOVER_LOOSE; // we need a new game
		endGame(false);
		GlobalSettings.PROFILE.checkHighScoreAndResetProfile(); // reset profile
		GlobalSettings.GAME_EXIT = GameExitStatus.NONE;
	}


}
