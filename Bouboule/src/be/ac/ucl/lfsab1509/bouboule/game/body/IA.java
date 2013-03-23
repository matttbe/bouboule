package be.ac.ucl.lfsab1509.bouboule.game.body;

import java.util.Iterator;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import java.lang.Math;

public class IA {
	
	//level 0 => gyroscope
		//level 1 => go mid
		//level 2 => troll
		//level 3 => arret mid
		//level 4 => aggresif
		//level 5 => defencif
		//level 6 => hybrid aggresif/defencif
		//level 7 => attenticipe

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
			Acc = troll(LocalEnemi, VelocityEnemi);
			break;
			
		case 1:
			Acc = middeler(IA,0);
			break;
			
		case 2:
			Acc = troll(IA, VelocityIA);
			break;
		case 3:
			Acc = stopMid(IA, VelocityIA);
			break;
		case 4:
			Acc = aggretion(IA,VelocityIA,LocalEnemi);
			break;
		case 5:

			break;
		case 6:

			break;
		case 7:
			
			break;
		default:
			break;
		}
		
		Acc=Acc.limit(GlobalSettings.LIMITACC);
		
		return Acc;
	}
	
	
	private static Vector2 aggretion(Vector2 position, Vector2 velocity,Vector2 localEnemi) {
		Vector2 directionenemi , dirmid;
		dirmid = middeler(position, 1);
		//dirmid.nor();
		directionenemi = new Vector2(localEnemi);
		directionenemi.sub(position).nor();
		Gdx.app.log ("updatePositionVector","vitesse2"+ velocity.len2() + "distance" +dirmid.len());
		if(dirmid.dot(velocity) < 0 && velocity.len2()+dirmid.len()*8 > 25){
			Gdx.app.log ("updatePositionVector","stop");
			
			//évitement des bords
			return stopMid(position, velocity);
		}
		return directionenemi;
	}


	public static Vector2 gyroscope(){
		
		float accelX = -Gdx.input.getAccelerometerX()*0.1f;
		float accelY = -Gdx.input.getAccelerometerY()*0.1f;
		/*
		 * Uniquement pour mes tests
		 */
		//accelX=0;
		//accelY=0;
		
		return new Vector2(accelX,accelY);
	}
	
	public static Vector2 middeler(Vector2 IA,int temp){
		
		Vector2 Acc = new Vector2(GlobalSettings.ARENAWAYPOINTALLOW[temp]);
		Acc.sub(IA);
		
		//Gdx.app.log ("updatePositionVector","kikoa"+ GlobalSettings.ARENAWAYPOINTALLOW[1].x + " " +GlobalSettings.ARENAWAYPOINTALLOW[1].y );
		
				
		return Acc;
	}
	
	public static Vector2 stopMid(Vector2 position,Vector2 velocity){
		Vector2 vitesse , dirmid , Acc;

		vitesse = new Vector2(velocity).nor().mul(0.9f);
		dirmid = new Vector2(middeler(position, 0)).nor();
		
		Acc = new Vector2(dirmid).sub(vitesse).nor();
		
		if(dirmid.dot(vitesse) > 0 &&
				 GlobalSettings.ARENAWAYPOINTALLOW[0].dst(position) < vitesse.dot(dirmid)*vitesse.dot(dirmid)/(2*GlobalSettings.LIMITACC)){
			Gdx.app.log ("IA","stopmid STOP" );
			Acc.rotate(180);
		}else{
			Gdx.app.log ("IA","stopmid Continue" );
		}
		
		//newAcc.sub(velocity.mul(1.5f*position.dst(GlobalSettings.ARENAWAYPOINTALLOW[1]))).mul(1.5f);
		
		return Acc;
	}
	
	public static Vector2 troll(Vector2 position,Vector2 velocity){
		Vector2 newAcc = new Vector2(middeler(position, 0));
		Vector2 temp1 = new Vector2(newAcc).nor();
		Vector2 temp2 = new Vector2(velocity).nor();
		
		float angle;
		
		angle = ((temp2.angle() - temp1.angle()) + 360) % 360;
		if(angle<85){
			newAcc.rotate(angle-80);
		}else if(angle < 180){
			newAcc.rotate(angle-84);
		}else if(angle < 275){
			newAcc.rotate(angle+80);
		}else{
			newAcc.rotate(angle+84);
		}
		
		newAcc.nor();
		
		//newAcc.add(middeler(position, 0)).nor();
		
		return newAcc;
	}
}
