package be.ac.ucl.lfsab1509.bouboule.game.ia;

/*
 * This file is part of Bouboule.
 * 
 * Copyright 2013 UCLouvain
 * 
 * Authors:
 *  * Group 7 - Course: http://www.uclouvain.be/en-cours-2013-lfsab1509.html
 *    Matthieu Baerts <matthieu.baerts@student.uclouvain.be>
 *    Baptiste Remy <baptiste.remy@student.uclouvain.be>
 *    Nicolas Van Wallendael <nicolas.vanwallendael@student.uclouvain.be>
 *    Helene Verhaeghe <helene.verhaeghe@student.uclouvain.be>
 * 
 * Bouboule is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


import java.util.Iterator;

import be.ac.ucl.lfsab1509.bouboule.game.body.Bouboule;
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
			if(bodytemp.getUserData() == (Object) GlobalSettings.PLAYER){ 
				LocalEnemi = bodytemp.getPosition();
				VelocityEnemi = bodytemp.getLinearVelocity();
			}else if(bodytemp.getUserData() == (Object) GlobalSettings.MONSTER){
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
			//Acc = aggretion(LocalEnemi, VelocityEnemi,IA);
			//Acc = hybrid(LocalEnemi,VelocityEnemi,IA);
			//Acc = stopMid(LocalEnemi, VelocityEnemi);
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
			Acc = aggretion(IA,VelocityIA,LocalEnemi);
			break;
		case 5:
			Acc = defence(IA,VelocityIA,LocalEnemi);
			break;
		case 6:
			Acc = hybrid(IA,VelocityIA,LocalEnemi);
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
		Acc.sub(slow);
		

		return Acc;
	}


	private static Vector2 hybrid(Vector2 IA, Vector2 velocityIA,Vector2 localEnemi) {
		float anglerelatif = angleCentre(IA, 0) - angleCentre(localEnemi, 0);
		anglerelatif=casteangle(anglerelatif);
		float distIA,distEnemi;
		distIA = GlobalSettings.ARENAWAYPOINTALLOW.get(0).getVector().dst(IA);
		distEnemi = GlobalSettings.ARENAWAYPOINTALLOW.get(0).getVector().dst(localEnemi);

		if(anglerelatif < 45 && anglerelatif > -45 && distIA>distEnemi){
			return defence(IA, velocityIA, localEnemi);
		}else{
			return aggretion(IA, velocityIA, localEnemi);
		}

	}


	private static Vector2 defence(Vector2 IA, Vector2 velocityIA,Vector2 localEnemi) {
		Vector2 acc = new Vector2(middeler(IA, 0)).nor();
		float angleEnemi = angleCentre(localEnemi, 0) - angleCentre(IA, 0);
		float direction = angleCentre(IA, 0) - velocityIA.angle();

		angleEnemi = casteangle(angleEnemi);
		direction = casteangle(direction);
		if(angleEnemi>0 == direction<0){
			acc.rotate(75);
		}else{
			acc.rotate(-75);
		}

		Vector2 dirmid = middeler(IA, 0);
		if(dirmid.dot(velocityIA) < 0.25f && velocityIA.len2()+dirmid.len()*8 > 10){
			//évitement des bords
			return stopMid(IA, velocityIA);
		}

		return acc;
	}


	private static Vector2 aggretion(Vector2 position, Vector2 velocity,Vector2 localEnemi) {
		Vector2 directionenemi , dirmid;
		dirmid = middeler(position, 0);
		//dirmid.nor();
		directionenemi = new Vector2(localEnemi);
		directionenemi.sub(position).nor();
		if(dirmid.dot(velocity) < 0.15f && velocity.len2()+dirmid.len()*8 > 30){

			//évitement des bords
			return stopMid(position, velocity);
		}
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
		// Gdx.app.log ("IA","gyroscope"+ Acc.len());
		Gdx.app.log ("IA","gyroscope"+ Acc.len());

		return Acc;
	}

	public static Vector2 middeler(Vector2 IA,int temp){
		
		Vector2 Acc = new Vector2(GlobalSettings.ARENAWAYPOINTALLOW.get(temp).getVector());
		Acc.sub(IA);
		
		
		return Acc;
	}

	public static Vector2 stopMid(Vector2 position,Vector2 velocity){
		Vector2 vitesse , dirmid , Acc;

		vitesse = new Vector2(velocity).nor().mul(0.9f);
		dirmid = new Vector2(middeler(position, 0)).nor();

		Acc = new Vector2(dirmid).sub(vitesse).nor();

		if(dirmid.dot(vitesse) > 0 &&
				GlobalSettings.ARENAWAYPOINTALLOW.get(0).getVector().dst(position) < vitesse.dot(dirmid)*vitesse.dot(dirmid)/(2*GlobalSettings.LIMITACC)){
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
	private static float angleCentre(Vector2 position,int centre){
		Vector2 temp = new Vector2(position);
		temp.sub(GlobalSettings.ARENAWAYPOINTALLOW.get(centre).getVector());
		return casteangle(temp.angle());
	}

	/*
	 * remet l'angle entre ]-180;180[
	 */
	private static float casteangle(float angle){
		return ((angle + 180)% 360 - 180);
	}

}
