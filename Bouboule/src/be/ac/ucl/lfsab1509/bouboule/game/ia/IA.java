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

	//physical constant
	public static float FORCE_MAX_IA=0;
	public static float FORCE_MAX_PLAYER=0;
	public static float ACC_MAX_IA=0;
	public static float ACC_MAX_PLAYER=0;
	//boolean for init constant by level
	public static boolean IS_INIT;
	//invert Axes
	private static float AXE_POSITION = -1;
	//Acceleration constant (speed of game)
	private static final float K_ACC_DEFAULT = 6f;
	private static float K_ACC = K_ACC_DEFAULT;
	
	//init this variable at the beginning of each level
	public static int countframe=0;
	
	//level 0 => gyroscope
	//level 1 => go mid
	//level 2 => troll
	//level 3 => stop mid
	//level 4 => aggressive anticipatory
	//level 5 => defensive
	//level 6 => hybrid aggressive/defensive anticipation
	//level 7 => go mid + slow
	//level 8 => multipoints
	//level 9 => aggressive without anticipation
	//level 10 => hybrid without anticipation
	//level 11 => hybrid plus frein
	//level 12 => gyroscope inverse

	//compute the maximal force for player and IA according to the acceleration max
	private static void init(Body bodyia,Body bodyplayer){
		if(bodyia != null)
			FORCE_MAX_IA = ACC_MAX_IA * bodyia.getMass();
		if(bodyplayer != null)
			FORCE_MAX_PLAYER = ACC_MAX_PLAYER * bodyplayer.getMass();
		IS_INIT = true;
		setNewSensitivity();
	}

	public static void setNewSensitivity () {
		float fSensDefault = ((float) GlobalSettings.SENSITIVITY_MIN + (float) GlobalSettings.SENSITIVITY_MAX) / 2f; // 500
		float fSens = (float) GlobalSettings.SENSITIVITY / fSensDefault; // => .7 -> 1.3
		Gdx.app.log("Matth", "Sens: " + fSens + " " + GlobalSettings.SENSITIVITY);
		K_ACC = K_ACC_DEFAULT * fSens;
	}

	
	//main fonction of IA,
	public static Vector2 compute(int IALevel, Bouboule bouboule) {


		//init variable
		Vector2 IA = null, VelocityIA =null, LocalEnemi=null, VelocityEnemi = null, Acc = null;

		//search the position and speed of the 2 bouboules
		Iterator<Body> iter = GraphicManager.getWorld().getBodies();
		Body bodytemp,bodytempia=null,bodytempplayer=null;

		while(iter.hasNext()){
			bodytemp = iter.next();
			if( ((Entity) bodytemp.getUserData()).getEntity()  == Entity.PLAYER){
				bodytempplayer=bodytemp;
				LocalEnemi = bodytemp.getPosition();
				VelocityEnemi = bodytemp.getLinearVelocity();
			}else if( ((Entity) bodytemp.getUserData()).getEntity() == Entity.MONSTER){
				bodytempia=bodytemp;
				IA =  bodytemp.getPosition();
				VelocityIA  = bodytemp.getLinearVelocity();
				
			}
		}
		
		//compute the maximum force for the 2 player
		if(!IS_INIT){
			init(bodytempia,bodytempplayer);
		}




		//launch the correct fonction for the IA
		switch (IALevel) {
		case 0:
			Acc = gyroscope();
			break;

		case 1:
			Acc = middeler(IA,getClosestCentre(IA));
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
			Acc = middeler (IA,getClosestCentre(IA));
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
		case 11:
			Acc = hybrid(IA,VelocityIA,LocalEnemi,VelocityEnemi,true);
			break;
		case 12:
			Acc = gyroscope().mul(-1);
			break;
		default:
			break;
		}

		


		// limite the Force for the player and IA
		if(IALevel != 0){
			Acc=Acc.limit(FORCE_MAX_IA);
			
			//count the frame to know how many time is passe for the game
			countframe++;
		}else{
			
			//security for player (unuse)
			/* MapNode testtemp = getClosestCentre(LocalEnemi);
			Vector2 dirmid = middeler(LocalEnemi, testtemp);
			if(dirmid.dot(VelocityEnemi) < 0.0f && VelocityEnemi.len2()/(ACC_MAX_PLAYER*2*K_ACC) + dirmid.len() > testtemp.getWeight()){
				// Gdx.app.log ("Player","IA:defence slow");
				Acc = stopMid(LocalEnemi, VelocityEnemi);
			}*/
			
			
			Acc=Acc.limit(FORCE_MAX_PLAYER);
			
			
		}
		
		//mesure arena and speed.
		/*if(IALevel == 0){
			Gdx.app.log ("Player","playerx"+LocalEnemi.x+"y"+LocalEnemi.y+"speed2"+VelocityEnemi.len2()+"acc"+Acc.len());
		}*/
		
		//upgrade the maniability for a IA
		if(IALevel==11){
			Vector2 slow = new Vector2(VelocityIA).mul(0.1f);
			Acc.sub(slow);
		}
		
		//make the game a bit faster
		Acc.mul(K_ACC);
		
		
		
		return Acc;
	}


	//special IA for world with lot of waypoints
	private static Vector2 multipoint(Vector2 iA, Vector2 velocityIA,Vector2 localEnemi, Vector2 velocityEnemi) {
		
		//search if there is a point beetween the to bouboule and return the closest
		MapNode close=getClosestCentre2(iA, localEnemi);
		//search next node
		if(close==null){
			
			//send the bouboule on a point near him but closer to his enemi
			MapNode temp = getnextnode(iA,localEnemi);
			Vector2 dirmid = middeler(iA, temp);
			if(velocityIA.len2()/(ACC_MAX_IA*2*K_ACC) + dirmid.len() > temp.getWeight()){
				// Gdx.app.log ("Player","IA:defence slow");
				return stopMid(iA, velocityIA);
			}
			return middeler(iA, temp);
		}
		
		//when the 2 bouboule are close enought he attack
		return aggretion(iA, velocityIA, localEnemi, velocityEnemi,true);
		
	}
	
	//search for a the next node to join the enemi
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
		//save closest waypoint for the IA
		MapNode close = getClosestCentre(IA);
		
		//get some data about the position and game
		float anglerelatif = angleCentre(IA, close.getVector()) - angleCentre(localEnemi, close.getVector());
		anglerelatif=casteangle(anglerelatif);
		float distIA,distEnemi;
		distIA = close.getVector().dst(IA);
		distEnemi = close.getVector().dst(localEnemi);

		//set the IA aggressive for the first seconde
		if(countframe < 150){
			return aggretion(IA, velocityIA, localEnemi, VelocityEnemi,predict);
		}
		
		//check if the IA is in a good position or if he need to fled
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
		//look for the direction the avoid the enemi on the good side
		if(angleEnemi>0 == direction<0){
			acc.rotate(75);
		}else{
			acc.rotate(-75);
		}

		
		Vector2 dirmid = middeler(IA, close);
		
		//avoid going out by fledings
		if(dirmid.dot(velocityIA) < 0.25f && velocityIA.len2()/(ACC_MAX_IA*2*K_ACC) +dirmid.len() > close.getWeight()){
			return stopMid(IA, velocityIA);
		}
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
		
		//compute a fictif position of him self if needed (predict)
		fictposition = new Vector2(position);
		if(predict){
			angletemp=angleCentre(position, localEnemi) - 90f;
			vtempcal =  new Vector2(velocity).rotate(-angletemp);
			vtempcal.y=0;
			vtempcal.rotate(angletemp);
			fictposition.add(vtempcal);
		}
		
		//compute the fictive position of the enemi
		directionenemi = new Vector2(localEnemi);
		if(predict){
			angletemp = angleCentre(localEnemi,position) - 90f;;
			vtempcal =  new Vector2(VelocityEnemi).rotate(-angletemp);
			vtempcal.y=0;
			vtempcal.rotate(angletemp);
			directionenemi.add(vtempcal);
		}
		
		//check the acc need to touch the enemi
		directionenemi.sub(fictposition).nor();
		
		//avoid too big acceleration and going out
		if(!(Math.abs(angleCentre(fictposition, centreIA.getVector())-angleCentre(localEnemi,centreIA.getVector())) < 15 &&
				centreIA == getClosestCentre(localEnemi)))
			if(dirmid.dot(velocity) < 0.0f && velocity.len2()/(ACC_MAX_IA*2*K_ACC) + dirmid.len() > centreIA.getWeight()){
				return stopMid(position, velocity);
			}

		return directionenemi;
	}

	//check if the axe are inverted
	public static boolean isInverted() {
		return AXE_POSITION == 1;
	}

	//inverse axe
	public static void inverse() {
		AXE_POSITION *= -1;
	}

	public static void setInvertedOrientation() {
		AXE_POSITION = 1;
	}

	public static void setNormalOrientation() {
		AXE_POSITION = -1;
	}

	//compute the player movement
	private static Vector2 gyroscope(){
		float accelX = Gdx.input.getAccelerometerX() * AXE_POSITION;
		float accelY = Gdx.input.getAccelerometerY() * AXE_POSITION;
		Vector2 Acc= new Vector2(accelX, accelY);
		float div = (GlobalSettings.SENSITIVITY_MAX + // old: 1200 - 500 = 750 ; new: 650+350 - 500 = 500, ok?
				GlobalSettings.SENSITIVITY_MIN -
				GlobalSettings.SENSITIVITY) / 100;
		
		//normalise the acceleration the to the max for the player.
		Acc.div(div).limit(1).mul(ACC_MAX_PLAYER);
	
		return Acc;
	}

	//return a vector to the middle
	private static Vector2 middeler(Vector2 IA,int centre){
		
		Vector2 Acc = new Vector2(GlobalSettings.ARENAWAYPOINTALLOW.get(centre).getVector());
		Acc.sub(IA);
		
		return Acc;
	}
	
	private static Vector2 middeler(Vector2 IA,MapNode centre){
		
		Vector2 Acc = new Vector2(centre.getVector());
		Acc.sub(IA);
		
		return Acc;
	}

	//try to stop on the closest middle
	private static Vector2 stopMid(Vector2 position,Vector2 velocity){
		Vector2 vitesse , dirmid , Acc;

		vitesse = new Vector2(velocity).nor().mul(0.9f);
		dirmid = new Vector2(middeler(position, getClosestCentre(position))).nor();

		Acc = new Vector2(dirmid).sub(vitesse).nor();

		if(dirmid.dot(vitesse) > 0 &&
				getClosestCentre(position).getVector().dst(position) < vitesse.dot(dirmid)*vitesse.dot(dirmid)/(ACC_MAX_IA*2*K_ACC)){
			//Acc.rotate(180);
		}else{
		}

		//newAcc.sub(velocity.mul(1.5f*position.dst(GlobalSettings.ARENAWAYPOINTALLOW.get(1).getVector()))).mul(1.5f);

		return Acc;
	}

/*
	private static Vector2 troll3(Vector2 position,Vector2 velocity){
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
*/
	//IA troll for testing other IA
	private static Vector2 troll2(Vector2 position,Vector2 velocity){
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
	
	//return the mapnode with the more influence on the point
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
	
	//return the mapnode with the best influence on the two node.
	//null if they are too distant
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
