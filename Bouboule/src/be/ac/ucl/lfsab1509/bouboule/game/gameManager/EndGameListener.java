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

	private int isAlivePlayer 	= 0;
	private int isAliveMonster	= 0;
	
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
		} else if((entity1 != GlobalSettings.SCENERY) && (entity2 != GlobalSettings.SCENERY)) {
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
				
				if (isAlivePlayer>1) {
					//GlobalSettings.GAME.winSound ();
					isAlivePlayer --;
					Gdx.app.log("Alive", "End Contact = "+isAlivePlayer);
					//DO NOTHING =)

				} else {
					//GlobalSettings.GAME.looseSound ();
					//TODO : END GAME BECAUSE WE HAVE A LOOSER.
					Gdx.app.log("KILL", "Bouboule est MORT =/");
					
					GlobalSettings.GAME_EXIT = GameExitStatus.LOOSE;
					GlobalSettings.PROFILE.cancelNewScore ();
					if (! GlobalSettings.PROFILE.addLifes (-1))
					{
						GlobalSettings.GAME_EXIT = GameExitStatus.GAMEOVER;
						GlobalSettings.PROFILE.resetProfile (); // TODO: what to do?
					}
					
					Gdx.app.exit();
				}


			} else {

				if (isAliveMonster>1) {
					//GlobalSettings.GAME.looseSound ();
					Gdx.app.log("Alive", "MONSTER est VIVANT =)");
					//DO NOTHING
					isAliveMonster --;
				} else {
					//GlobalSettings.GAME.winSound ();
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
					
					Gdx.app.exit();
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
