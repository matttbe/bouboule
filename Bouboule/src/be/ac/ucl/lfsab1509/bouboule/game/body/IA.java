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
		
		
		Vector2 IA = null, VelocityIA =null, LocalEnemi=null, VelocityEnemi=null;
		Body arena = null;
		
		Vector2 Acc = null;
		
		Iterator<Body> iter = GraphicManager.getWorld().getBodies();
		
		Body bodytemp;
		
		while(iter.hasNext()){
			bodytemp = iter.next();
			if(bodytemp.getUserData() == (Object) GlobalSettings.PLAYER){ 
				LocalEnemi = bodytemp.getPosition();
				//Gdx.app.log ("updatePositionVector","kikoa"+ LocalEnemi.x + " " +LocalEnemi.y );
				VelocityEnemi = bodytemp.getLinearVelocity();
			}else if(bodytemp.getUserData() == (Object) GlobalSettings.MONSTER){
				IA =  bodytemp.getPosition();
				//Gdx.app.log ("updatePositionVector","kikob"+ IA.x + " " +IA.y );
				VelocityIA  = bodytemp.getLinearVelocity();
			}else{
				arena = bodytemp;
				//Gdx.app.log ("updatePositionVector","kikoc"+ bodytemp.getPosition().x + " " +bodytemp.getPosition().y );
			}
		}
		
		
		
		
		
		switch (IALevel) {
		case 0:
			Acc = gyroscope();
			//Acc = middeler(LocalEnemi,0);
			break;
			
		case 1:
			Acc = middeler(IA,0);
			break;
			
		case 2:

			break;
		case 3:
			Acc = stopMid(IA, VelocityIA);
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
		
		Acc=Acc.limit(0.4f);
		
		return Acc;
	}
	
	
	public static Vector2 gyroscope(){
		
		float accelX = -Gdx.input.getAccelerometerX()*0.1f;
		float accelY = -Gdx.input.getAccelerometerY()*0.1f;
		accelX=0;
		accelY=0;
		
		return new Vector2(accelX,accelY);
	}
	
	public static Vector2 middeler(Vector2 IA,int temp){
		
		Vector2 Acc = new Vector2(GlobalSettings.ARENAWAYPOINTALLOW[temp]);
		Acc.sub(IA);
		
		//Gdx.app.log ("updatePositionVector","kikoa"+ GlobalSettings.ARENAWAYPOINTALLOW[1].x + " " +GlobalSettings.ARENAWAYPOINTALLOW[1].y );
		
				
		return Acc;
	}
	
	public static Vector2 stopMid(Vector2 position,Vector2 velocity){
		Vector2 Acc , newAcc;
		Vector2 pc = new Vector2(GlobalSettings.ARENAWAYPOINTALLOW[1]);
		pc.sub(position);
		Acc = new Vector2(pc).limit(0.4f);
		newAcc = new  Vector2(Acc);
		newAcc.sub(velocity.mul(1.5f*position.dst(GlobalSettings.ARENAWAYPOINTALLOW[1])));
		
		return newAcc;
	}
	
	public static Vector2 troll(Bouboule bouboule){
		return null;
	}
}
