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

	boolean isAlivePlayer 	= true;
	boolean isAliveMonster	= true;
	
	@Override
	public void beginContact(Contact contact) {
		short Entity1 = (Short) contact.getFixtureA().getBody().getUserData();
		short Entity2 = (Short) contact.getFixtureB().getBody().getUserData();


		if (Entity1 == GlobalSettings.SCENERY |
				Entity2 == GlobalSettings.SCENERY){
			
			if ( Entity1 == GlobalSettings.PLAYER |
					Entity2 == GlobalSettings.PLAYER){

				isAlivePlayer = !isAlivePlayer;


			} else {

				
				isAliveMonster = !isAliveMonster;

			}
		}

	}

	@Override
	public void endContact(Contact contact) {


		short Entity1 = (Short) contact.getFixtureA().getBody().getUserData();
		short Entity2 = (Short) contact.getFixtureB().getBody().getUserData();


		if (Entity1 == GlobalSettings.SCENERY |
				Entity2 == GlobalSettings.SCENERY){
			
			if ( Entity1 == GlobalSettings.PLAYER |
					Entity2 == GlobalSettings.PLAYER){
				
				if(isAlivePlayer){

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

				if(isAliveMonster){

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
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}



}
