package be.ac.ucl.lfsab1509.bouboule.game.body;

import java.util.Iterator;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class IA {
	
	//level 0 => gyroscope
		//level 1 => go mid
		//level 2 => troll
		//level 3 => arret mid
		//level 4 => aggresif
		//level 5 => defencif
		//level 6 => attenticipe

	public static Vector2 compute(int IALevel, Bouboule bouboule){
		
		
		Vector2 IA = null, VelocityIA, LocalEnemi, VelocityEnemi;
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
				IA =  bodytemp.getPosition();
				Gdx.app.log ("updatePositionVector","kikob"+ IA.x + " " +IA.y );
				VelocityIA  = bodytemp.getLinearVelocity();
			}else{
				arena = bodytemp;
				Gdx.app.log ("updatePositionVector","kikoc"+ bodytemp.getPosition().x + " " +bodytemp.getPosition().y );
			}
		}
		
		
		
		
		
		switch (IALevel) {
		case 0:
			return gyroscope();

		case 1:
			return middeler(IA);

		case 2:

			break;
		case 3:

			break;
		case 4:

			break;
		case 5:

			break;
		case 6:

			break;

		default:
			break;
		}
		
		
		
		
		
		
		
		
		return null;
	}
	
	
	public static Vector2 gyroscope(){
		
		float accelX = -Gdx.input.getAccelerometerX()*0.1f;
		float accelY = -Gdx.input.getAccelerometerY()*0.1f;
		
		return new Vector2(accelX,accelY);
	}
	
	public static Vector2 middeler(Vector2 IA){
		
		float AccX = 0.4f;
		float AccY = 0.4f;
		
		if(IA.x > 4){
			AccX = -0.3f;
		}
		
		if(IA.y > 4){
			AccY = -0.3f;
		}
		
		return new Vector2(AccX,AccY);
	}
	
	public static Vector2 troll(Bouboule bouboule){
		return null;
	}
}
