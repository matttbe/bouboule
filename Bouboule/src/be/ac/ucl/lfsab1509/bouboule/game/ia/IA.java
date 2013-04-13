package be.ac.ucl.lfsab1509.bouboule.game.ia;

import java.util.ArrayList;
import java.util.Iterator;

import be.ac.ucl.lfsab1509.bouboule.game.body.Bouboule;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;
import be.ac.ucl.lfsab1509.bouboule.game.entity.Entity;

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
	//level 6 => hybrid aggresif/defencif
	//level 7 => attenticipe

	public static Vector2 compute(int IALevel, Bouboule bouboule){


		Vector2 IA = null, VelocityIA =null, LocalEnemi=null, VelocityEnemi = null;
		//Body arena = null;

		Vector2 Acc = null;

		Iterator<Body> iter = GraphicManager.getWorld().getBodies();

		Body bodytemp;

		while(iter.hasNext()){
			bodytemp = iter.next();
			if( ((Entity) bodytemp.getUserData()).getEntity()  == Entity.PLAYER){ 
				LocalEnemi = bodytemp.getPosition();
				VelocityEnemi = bodytemp.getLinearVelocity();
			}else if( ((Entity) bodytemp.getUserData()).getEntity() == Entity.MONSTER){
				IA =  bodytemp.getPosition();
				VelocityIA  = bodytemp.getLinearVelocity();
			}/*else{
				arena = bodytemp;
			}*/
		}





		switch (IALevel) {
		case 0:
			Acc = gyroscope();
			//Acc = troll2(LocalEnemi, VelocityEnemi);
			//Acc = middeler(LocalEnemi,0);
			//Acc = aggretion(LocalEnemi, VelocityEnemi,IA,VelocityIA);
			//Acc = hybrid(LocalEnemi,VelocityEnemi,IA,VelocityIA);
			//Acc = stopMid(LocalEnemi, VelocityEnemi);
			//Gdx.app.log ("Player","player"+LocalEnemi.x+"speed"+VelocityEnemi.x+"acc"+Acc.x);
			break;

		case 1:
			Acc = middeler(IA,0);
			break;

		case 2:
			Acc = troll2(IA, VelocityIA);
			break;
		case 3:
			Acc = stopMid(IA, VelocityIA);
			break;
		case 4:
			Acc = aggretion(IA,VelocityIA,LocalEnemi,VelocityEnemi);
			break;
		case 5:
			Acc = defence(IA,VelocityIA,LocalEnemi);
			break;
		case 6:
			Acc = hybrid(IA,VelocityIA,LocalEnemi,VelocityEnemi);
			break;
		case 7:
			break;
		default:
			break;
		}

		

		Vector2 slow;
		if(IALevel != 0){
			Acc=Acc.limit(GlobalSettings.LIMITACC);
			slow = new Vector2(VelocityIA).nor().mul(0.02f);
		}else{
			Acc=Acc.limit(GlobalSettings.LIMITACC);
			slow = new Vector2(VelocityEnemi).nor().mul(0.02f);
			//slow = new Vector2(0, 0);
		}
		//Acc.sub(slow);
		
		if(IALevel == 0)
		Gdx.app.log ("Player","playerx"+LocalEnemi.x+"y"+LocalEnemi.y+"speed2"+VelocityEnemi.len2()+"acc"+Acc.len());
		
		return Acc;
	}


	private static Vector2 hybrid(Vector2 IA, Vector2 velocityIA,Vector2 localEnemi,Vector2 VelocityEnemi) {
		MapNode close = getClosestCentre(IA);
		float anglerelatif = angleCentre(IA, close.getVector()) - angleCentre(localEnemi, close.getVector());
		anglerelatif=casteangle(anglerelatif);
		float distIA,distEnemi;
		distIA = close.getVector().dst(IA);
		distEnemi = close.getVector().dst(localEnemi);

		if(anglerelatif < 45 && anglerelatif > -45 && distIA > distEnemi){
			return defence(IA, velocityIA, localEnemi);
		}else{
			return aggretion(IA, velocityIA, localEnemi, VelocityEnemi);
		}

	}


	private static Vector2 defence(Vector2 IA, Vector2 velocityIA,Vector2 localEnemi) {
		MapNode close =getClosestCentre(IA);
		Vector2 vClose = close.getVector();
		Vector2 acc = new Vector2(middeler(IA, close)).nor();
		float angleEnemi = angleCentre(localEnemi, vClose) - angleCentre(IA, vClose);
		float direction = angleCentre(IA, vClose) - velocityIA.angle();

		angleEnemi = casteangle(angleEnemi);
		direction = casteangle(direction);
		if(angleEnemi>0 == direction<0){
			acc.rotate(75);
		}else{
			acc.rotate(-75);
		}

		Vector2 dirmid = middeler(IA, close);
		if(dirmid.dot(velocityIA) < 0.25f && velocityIA.len2()+dirmid.len() > close.getWeight()){
			//évitement des bords
			Gdx.app.log ("Player","IA:defence slow");
			return stopMid(IA, velocityIA);
		}
		Gdx.app.log ("Player","IA:defence normal");
		return acc;
	}


	private static Vector2 aggretion(Vector2 position, Vector2 velocity,Vector2 localEnemi, Vector2 VelocityEnemi) {
		Vector2 directionenemi , dirmid , fictposition;
		MapNode centreIA = getClosestCentre(position);
		dirmid = middeler(position, centreIA);
		//dirmid.nor();
		fictposition = new Vector2(position);
		fictposition.add(new Vector2(velocity).rotate(position.angle()).set(0, velocity.y).rotate(0-position.angle()).mul(2));
		directionenemi = new Vector2(localEnemi);
		directionenemi.add(new Vector2(VelocityEnemi).rotate(localEnemi.angle()).set(0, VelocityEnemi.y).rotate(0-localEnemi.angle()).mul(2)).sub(fictposition).nor();
		
		if(!(Math.abs(angleCentre(fictposition, centreIA.getVector())-angleCentre(localEnemi,centreIA.getVector())) < 10 &&
				centreIA == getClosestCentre(localEnemi)))
		if(dirmid.dot(velocity) < 0.0f && velocity.len2()*1.75 + dirmid.len() > centreIA.getWeight()){
			Gdx.app.log ("Player","IA:attaque slow");
			//évitement des bords
			return stopMid(position, velocity);
		}
		Gdx.app.log ("Player","IA:attaque normal");
		return directionenemi;
	}


	public static Vector2 gyroscope(){
		float accelX = -Gdx.input.getAccelerometerX()*0.1f;
		float accelY = -Gdx.input.getAccelerometerY()*0.1f;
		Vector2 Acc= new Vector2(accelX, accelY);
		/*
		 * Uniquement pour mes tests
		 */
		//accelX=0;
		//accelY=0;
		//Gdx.app.log ("IA","gyroscope"+ Acc.len());

		return Acc;
	}

	public static Vector2 middeler(Vector2 IA,int centre){
		
		Vector2 Acc = new Vector2(GlobalSettings.ARENAWAYPOINTALLOW.get(centre).getVector());
		Acc.sub(IA);
		
		return Acc;
	}
	
	public static Vector2 middeler(Vector2 IA,MapNode centre){
		
		Vector2 Acc = new Vector2(centre.getVector());
		Acc.sub(IA);
		
		return Acc;
	}

	public static Vector2 stopMid(Vector2 position,Vector2 velocity){
		Vector2 vitesse , dirmid , Acc;

		vitesse = new Vector2(velocity).nor().mul(0.9f);
		dirmid = new Vector2(middeler(position, 0)).nor();

		Acc = new Vector2(dirmid).sub(vitesse).nor();

		if(dirmid.dot(vitesse) > 0 &&
				getClosestCentre(position).getVector().dst(position) < vitesse.dot(dirmid)*vitesse.dot(dirmid)/(2*GlobalSettings.LIMITACC)){
			Acc.rotate(180);
		}else{
		}

		//newAcc.sub(velocity.mul(1.5f*position.dst(GlobalSettings.ARENAWAYPOINTALLOW.get(1).getVector()))).mul(1.5f);

		return Acc;
	}

	public static Vector2 troll3(Vector2 position,Vector2 velocity){
		Vector2 newAcc = new Vector2(middeler(position, 0));
		Vector2 temp1 = new Vector2(newAcc).nor();
		Vector2 temp2 = new Vector2(velocity).nor();

		float angle = 90*temp1.dot(temp2);
		if(angle > 0 ){
			newAcc.rotate(angle-15);
		}else{
			newAcc.rotate(angle+15);
		}

		return newAcc;
	}

	public static Vector2 troll2(Vector2 position,Vector2 velocity){
		Vector2 newAcc = new Vector2(middeler(position, 0));
		Vector2 temp1 = new Vector2(newAcc).nor();
		Vector2 temp2 = new Vector2(velocity).nor();

		float angle = 90*temp1.dot(temp2);
		if(angle > 0 ){
			newAcc.rotate(angle-15);
		}else{
			newAcc.rotate(angle+15);
		}

		return newAcc;
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

	/*
	 * renvoi un angle entre ]-180;180[
	 */
	private static float angleCentre(Vector2 position,Vector2 centre){
		Vector2 temp = new Vector2(position);
		temp.sub(centre);
		return casteangle(temp.angle());
	}

	/*
	 * remet l'angle entre ]-180;180[
	 */
	private static float casteangle(float angle){
		return ((angle + 180)% 360 - 180);
	}
	
	private static MapNode getClosestCentre(Vector2 position){
		ArrayList<MapNode> tabNode = GlobalSettings.ARENAWAYPOINTALLOW;
		float mindist = position.dst(tabNode.get(0).getVector());
		int min = 0;
		float tempdist;
		for(int i=1;i<tabNode.size();i++){
			tempdist=position.dst(tabNode.get(i).getVector());
			if(tempdist < mindist){
				mindist = tempdist;
				min = i;
			}
		}
		return tabNode.get(min);
	}

}
