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

	public static float FORCE_MAX_IA;
	public static float FORCE_MAX_PLAYER;
	public static float AXE_POSITION = -1;
	private static float SENSIBILITY_DEFAULT = .15f;
	
	
	//initialiser la variable en début de niveau
	public static int countframe=0;
	
	//level 0 => gyroscope
	//level 1 => go mid
	//level 2 => troll
	//level 3 => arret mid
	//level 4 => aggresif attenticipe
	//level 5 => defencif
	//level 6 => hybrid aggresif/defencif anticipation
	//level 7 => go mid + slow
	//level 8 => multipoint
	//level 9 => aggresif sans anticipation
	//level 10 => hybrid sans anticipation

	public static Vector2 compute(int IALevel, Bouboule bouboule) {


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
			
			/*Acc = middeler (LocalEnemi,2);
			Vector2 slowi = new Vector2(VelocityEnemi).mul(2f);
			Acc.sub(slowi);*/
			
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
			Acc = aggretion(IA,VelocityIA,LocalEnemi,VelocityEnemi,true);
			break;
		case 5:
			Acc = defence(IA,VelocityIA,LocalEnemi);
			break;
		case 6:
			Acc = hybrid(IA,VelocityIA,LocalEnemi,VelocityEnemi,true);
			break;
		case 7:
			Acc = middeler (IA,1);
			Vector2 slow = new Vector2(VelocityIA).mul(2f);
			Acc.sub(slow);
			break;
		case 8:
			Acc = multipoint(IA,VelocityIA,LocalEnemi,VelocityEnemi);
			break;
		case 9:
			Acc = aggretion(IA,VelocityIA,LocalEnemi,VelocityEnemi,false);
			break;
		case 10:
			Acc = hybrid(IA,VelocityIA,LocalEnemi,VelocityEnemi,false);
			break;
		default:
			break;
		}


		// Vector2 slow;
		if(IALevel != 0){
			Acc=Acc.limit(FORCE_MAX_IA);
			//slow = new Vector2(VelocityIA).nor().mul(0.02f);
			//Acc.set(0, 0);
			countframe++;
		}else{
			Acc=Acc.limit(FORCE_MAX_PLAYER);
			
			//slow = new Vector2(VelocityEnemi).nor().mul(0.02f);
			//slow = new Vector2(0, 0);
		}
		//Acc.sub(slow);
		
		/*if(IALevel == 0){
			Gdx.app.log ("Player","playerx"+LocalEnemi.x+"y"+LocalEnemi.y+"speed2"+VelocityEnemi.len2()+"acc"+Acc.len());
		}*/
		
		Acc.mul(3.0f);
		
		return Acc;
	}


	private static Vector2 multipoint(Vector2 iA, Vector2 velocityIA,Vector2 localEnemi, Vector2 velocityEnemi) {
		
		MapNode close=getClosestCentre2(iA, localEnemi);
		//search next node
		if(close==null){
			//Gdx.app.log("ia","multipoint:crosspoint");
			MapNode temp = getnextnode(iA,localEnemi);
			close=getClosestCentre(iA);
			Vector2 dirmid = middeler(iA, close);
			if(velocityIA.len2()*1.75 + dirmid.len() > close.getWeight()+0.3f){
				// Gdx.app.log ("Player","IA:defence slow");
				return stopMid(iA, velocityIA);
			}
			return middeler(iA, temp);
		}
		
		//attack!
		return aggretion(iA, velocityIA, localEnemi, velocityEnemi,true);
		
		
		
		//return null;
	}
	
	private static MapNode getnextnode(Vector2 IA,Vector2 localEnemi){
		
		ArrayList<MapNode> tabNode = GlobalSettings.ARENAWAYPOINTALLOW;
		MapNode tempNode;
		float distance = 100;
		int closest = -1;
		float angle = angleCentre(IA, localEnemi);
		float tempdist;
		for(int i=0;i<tabNode.size();i++){
			tempNode=tabNode.get(i);
			float angletemp = angleCentre(IA, tempNode.getVector());
			if((angle - angletemp) < 45 && (angle - angletemp) > -45){
				tempdist = tempNode.getVector().dst(IA);
				if(distance > tempdist){
					distance = tempdist;
					closest = i;
				}
			}
		}
		if(closest !=-1)
			return tabNode.get(closest);
		return getClosestCentre(localEnemi);
		
	}


	private static Vector2 hybrid(Vector2 IA, Vector2 velocityIA,Vector2 localEnemi,Vector2 VelocityEnemi,boolean predict) {
		
		MapNode close = getClosestCentre(IA);
		float anglerelatif = angleCentre(IA, close.getVector()) - angleCentre(localEnemi, close.getVector());
		anglerelatif=casteangle(anglerelatif);
		float distIA,distEnemi;
		distIA = close.getVector().dst(IA);
		distEnemi = close.getVector().dst(localEnemi);

		
		//Gdx.app.log("batman", "count frame"+countframe);
		if(countframe < 150){
			
			return aggretion(IA, velocityIA, localEnemi, VelocityEnemi,predict);
		}
		
		if(anglerelatif < 45 && anglerelatif > -45 && distIA > distEnemi){
			return defence(IA, velocityIA, localEnemi);
		}else{
			return aggretion(IA, velocityIA, localEnemi, VelocityEnemi,predict);
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
			// Gdx.app.log ("Player","IA:defence slow");
			return stopMid(IA, velocityIA);
		}
		// Gdx.app.log ("Player","IA:defence normal");
		return acc;
	}


	private static Vector2 aggretion(Vector2 position, Vector2 velocity,Vector2 localEnemi, Vector2 VelocityEnemi,boolean predict) {
		//variable de calcul
		Vector2 vtempcal;
		float angletemp;
		
		
		Vector2 directionenemi , dirmid , fictposition;
		MapNode centreIA = getClosestCentre(position);
		dirmid = middeler(position, centreIA);
		//dirmid.nor();
		
		//calcul de la position fictive de l'IA
		fictposition = new Vector2(position);
		//fictposition.add(new Vector2(velocity).rotate(position.angle()).set(0, velocity.y).rotate(0-position.angle()).mul(1));
		if(predict){
			angletemp=angleCentre(position, localEnemi) - 90f;
			vtempcal =  new Vector2(velocity).rotate(-angletemp);
			vtempcal.y=0;
			vtempcal.rotate(angletemp);
			fictposition.add(vtempcal);
		}
		
		//calcul de la position fictive de l'ennemi
		directionenemi = new Vector2(localEnemi);
		//directionenemi.add(new Vector2(VelocityEnemi).rotate(localEnemi.angle()).set(0, VelocityEnemi.y).rotate(0-localEnemi.angle()).mul(1));
		if(predict){
			angletemp = angleCentre(localEnemi,position) - 90f;;
			vtempcal =  new Vector2(VelocityEnemi).rotate(-angletemp);
			vtempcal.y=0;
			vtempcal.rotate(angletemp);
			directionenemi.add(vtempcal);
		}
		directionenemi.sub(fictposition).nor();
		
		
		if(!(Math.abs(angleCentre(fictposition, centreIA.getVector())-angleCentre(localEnemi,centreIA.getVector())) < 15 &&
				centreIA == getClosestCentre(localEnemi)))
		if(dirmid.dot(velocity) < 0.0f && velocity.len2()*1.75 + dirmid.len() > centreIA.getWeight()){
			//Gdx.app.log ("Player","IA:attaque slow");
			//évitement des bords
			return stopMid(position, velocity);
		}
		// Gdx.app.log ("Player","IA:attaque normal");
		return directionenemi;
	}


	public static Vector2 gyroscope(){
		float sensibility = SENSIBILITY_DEFAULT * GlobalSettings.SENSITIVITY * 2 / GlobalSettings.SENSITIVITY_MAX;
		float accelX = AXE_POSITION * sensibility * Gdx.input.getAccelerometerX();
		float accelY = AXE_POSITION * sensibility * Gdx.input.getAccelerometerY();
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
				getClosestCentre(position).getVector().dst(position) < vitesse.dot(dirmid)*vitesse.dot(dirmid)/(2*FORCE_MAX_IA)){
			//Acc.rotate(180);
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
		MapNode temp=tabNode.get(0);
		float maxdist = temp.getWeight() - position.dst(temp.getVector());
		int max = 0;
		float tempdist;
		for(int i=1;i<tabNode.size();i++){
			temp=tabNode.get(i);
			tempdist=temp.getWeight() - position.dst(temp.getVector());
			if(tempdist > maxdist){
				maxdist = tempdist;
				max = i;
			}
		}
		return tabNode.get(max);
	}
	
	private static MapNode getClosestCentre2(Vector2 position1,Vector2 position2){
		
		MapNode noeud1,noeud2;
		noeud1= getClosestCentre(position1);
		noeud2= getClosestCentre(position2);
		boolean temp=false;
		
		if(position2.dst(noeud1.getVector())-noeud1.getWeight() < 0){
			temp=true;
		}
		
		if(position1.dst(noeud2.getVector())-noeud2.getWeight() < 0){
			if(temp){
				if(noeud1.getWeight()>noeud2.getWeight())
					return noeud1;
				return noeud2;
			}else{
				return noeud2;
			}
		}
		if(temp)
			return noeud1;
		return null;
	}

}
