package be.ac.ucl.lfsab1509.bouboule.game.gameManager;


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

					Gdx.app.log("Alive", "Bouboule est VIVANT =)");
					//DO NOTHING =)
					isAlivePlayer = !isAlivePlayer;
				} else {
					//TODO : END GAME BECAUSE WE HAVE A LOOSER.
					Gdx.app.log("KILL", "Bouboule est MORT =/");
					
					GlobalSettings.GAME_EXIT = -1;
					GlobalSettings.LIVES	--;
					
					
					Gdx.app.exit();
				}


			} else {

				if (isAliveMonster) {

					Gdx.app.log("Alive", "MONSTER est VIVANT =)");
					//DO NOTHING
					isAliveMonster = !isAliveMonster;
				} else {
					
					//TODO : END GAME BECAUSE WE HAVE A LOOSER.
					Gdx.app.log("KILL", "Bouboule a gagn� =P");
					GlobalSettings.GAME_EXIT = 1;
					GlobalSettings.LEVEL ++;
					
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
