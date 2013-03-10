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

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endContact(Contact contact) {

	    
	    short Entity1 = (Short) contact.getFixtureA().getBody().getUserData();
	    short Entity2 = (Short) contact.getFixtureB().getBody().getUserData();
	    

    	if ( Entity1 == GlobalSettings.PLAYER |
    			Entity2 == GlobalSettings.PLAYER){
    		
    		Gdx.app.log("KILL", "Bouboule est mort =/");

    		
    		//TODO : END GAME BECAUSE WE HAVE A LOOSER.
    	} else {
    		
      		Gdx.app.log("KILL", "Bouboule a gagné =P");

    		//TODO : END GAME BECAUSE WE HAVE A LOOSER.
    	}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
	

}
