package be.ac.ucl.lfsab1509.bouboule.game.body;

import java.util.Iterator;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class IA {

	public static Vector2 compute(int IALevel, Bouboule local){
		float AccX = 0.4f;
		float AccY = 0.4f;
		Vector2 LocalIA = null, VelocityIA, LocalEnemi, VelocityEnemi;
		Body arena = null;
		
		Iterator<Body> iter = GraphicManager.getWorld().getBodies();
		
		Body bodytemp;
		
		while(iter.hasNext()){
			bodytemp = iter.next();
			if(bodytemp.getUserData() == (Object) GlobalSettings.PLAYER){ 
				LocalEnemi = bodytemp.getPosition();
				Gdx.app.log ("updatePositionVector","kikoa"+ LocalEnemi.x + " " +LocalEnemi.y );
				VelocityEnemi = bodytemp.getLinearVelocity();
			}else if(bodytemp.getUserData() == (Object) GlobalSettings.MONSTER){
				LocalIA =  bodytemp.getPosition();
				Gdx.app.log ("updatePositionVector","kikob"+ LocalIA.x + " " +LocalIA.y );
				VelocityIA  = bodytemp.getLinearVelocity();
			}else{
				arena = bodytemp;
				Gdx.app.log ("updatePositionVector","kikoc"+ bodytemp.getPosition().x + " " +bodytemp.getPosition().y );
			}
		}
		
		//ok mnt on a la position et la vitesse de tout le monde.
		
		if(LocalIA.x > 4){
			AccX = -0.3f;
		}
		
		if(LocalIA.y > 4){
			AccY = -0.3f;
		}
		
		
		
		return new Vector2(AccX,AccY);
	}
}
