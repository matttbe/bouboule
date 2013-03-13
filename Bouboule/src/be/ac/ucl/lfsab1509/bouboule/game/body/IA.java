package be.ac.ucl.lfsab1509.bouboule.game.body;

import java.util.Iterator;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class IA {

	public static Vector2 compute(int IALevel, Bouboule local){
		float AccX = -0.01f;
		float AccY = 0f;
		
		Iterator<Body> iter = GraphicManager.getWorld().getBodies();
		
		Body bodytemp;
		
		while(iter.hasNext()){
			bodytemp = iter.next();
			Gdx.app.log ("Position X", "valuehello" + bodytemp.getLocalCenter().x);
			if(bodytemp.getClass().equals(Bouboule.class)){ // && bodytemp == local.body){
				Gdx.app.log ("Position X", "valuehello" + bodytemp.getLocalCenter().x);
				Gdx.app.log ("Vitesse  X", "valuehello" + bodytemp.getLinearVelocity().x);
			}
		}
		
		return new Vector2(AccX,AccY);
	}
}
