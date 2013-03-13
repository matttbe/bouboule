package be.ac.ucl.lfsab1509.bouboule.game.body;

import java.util.Iterator;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class IA {

	public static Vector2 compute(int IALevel, Bouboule local){
		float AccX = -0.01f;
		float AccY = 0f;
		Vector2 LocalIA, VelocityIA, LocalEnemi, VelocityEnemi;
		Body arena;
		
		Iterator<Body> iter = GraphicManager.getWorld().getBodies();
		
		Body bodytemp;
		
		while(iter.hasNext()){
			bodytemp = iter.next();
			if(bodytemp.getUserData() == (Object) GlobalSettings.MONSTER){ 
				LocalIA =  bodytemp.getLocalCenter();
				VelocityIA  = bodytemp.getLinearVelocity();
			}else if(bodytemp.getUserData() == (Object) GlobalSettings.PLAYER){
				LocalEnemi = bodytemp.getLocalCenter();
				VelocityEnemi = bodytemp.getLinearVelocity();
			}else{
				arena = bodytemp;
			}
		}
		
		//ok mnt on a la position et la vitesse de tout le monde.
		
		
		
		return new Vector2(AccX,AccY);
	}
}
