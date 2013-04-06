package be.ac.ucl.lfsab1509.bouboule.game.gameManager;


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

	private boolean isAlivePlayer 	= true;
	private boolean isAliveMonster	= true;
	
	@Override
	public void beginContact(final Contact contact) {
		short entity1 = (Short) contact.getFixtureA().getBody().getUserData();
		short entity2 = (Short) contact.getFixtureB().getBody().getUserData();


		if (entity1 == GlobalSettings.SCENERY 
				| entity2 == GlobalSettings.SCENERY) {
			
			if (entity1 == GlobalSettings.PLAYER 
					| entity2 == GlobalSettings.PLAYER) {

				isAlivePlayer = !isAlivePlayer;


			} else {

				
				isAliveMonster = !isAliveMonster;

			}
		}else if((entity1 != GlobalSettings.SCENERY) && (entity2 != GlobalSettings.SCENERY)){
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
				
				if (isAlivePlayer) {
					GlobalSettings.GAME.winSound ();
					Gdx.app.log("Alive", "Bouboule est VIVANT =)");
					//DO NOTHING =)
					isAlivePlayer = !isAlivePlayer;
				}
				else {
					GlobalSettings.GAME.looseSound ();
					//TODO : END GAME BECAUSE WE HAVE A LOOSER.
					Gdx.app.log("KILL", "Bouboule est MORT =/");
					
					GlobalSettings.GAME_EXIT = GameExitStatus.LOOSE;
					GlobalSettings.PROFILE.cancelNewScore ();
					if (! GlobalSettings.PROFILE.addLifes (-1))
					{
						GlobalSettings.GAME_EXIT = GameExitStatus.GAMEOVER;
						GlobalSettings.PROFILE.resetProfile (); // TODO: what to do?
					}
					
					GlobalSettings.MENUS.launchEndGameMenu ();
					GlobalSettings.GAME.screen.hide (); // TODO: stop this screen to return to mainactivity
					//Gdx.app.exit();
				}
			}
			else {
				if (isAliveMonster) {
					GlobalSettings.GAME.looseSound ();
					Gdx.app.log("Alive", "MONSTER est VIVANT =)");
					//DO NOTHING
					isAliveMonster = !isAliveMonster;
				}
				else {
					GlobalSettings.GAME.winSound ();
					//TODO : END GAME BECAUSE WE HAVE A LOOSER.
					Gdx.app.log("KILL", "Bouboule a gagné =P");
					GlobalSettings.PROFILE.saveScore ();
					if (GlobalSettings.PROFILE.LevelUp ())
						GlobalSettings.GAME_EXIT = GameExitStatus.WIN;
					else
					{
						GlobalSettings.GAME_EXIT = GameExitStatus.GAMEOVER;
						GlobalSettings.PROFILE.resetProfile (); // TODO: what to do?
					}

					GlobalSettings.MENUS.launchEndGameMenu ();
					GlobalSettings.GAME.screen.hide ();
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



}
