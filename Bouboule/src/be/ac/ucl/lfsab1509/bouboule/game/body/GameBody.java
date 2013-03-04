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
 *    Hélène Verhaeghe <helene.verhaeghe@student.uclouvain.be>
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

package be.ac.ucl.lfsab1509.bouboule.game.body;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class GameBody {
	
	public Body body;
	
	public Boolean isAlive;
	
	/*object direction depending on the force applied to it*/
	public Vector2 positionVector;
	
	
	public GameBody(){
		
		isAlive			= true;
		positionVector 	= new Vector2();
	}
	
	public void MakeBody(float width,float  height,float radius,BodyDef.BodyType bodyType,
			float density,float elasticity, Vector2 pos,float angle){
		
		World world = GraphicManager.getWorld();
		
		
		//Set up of a body that has a physical interpretation
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.type 	= bodyType;
		bodyDef.angle	= angle;
		bodyDef.position.set(GraphicManager.convertToBox(pos.x), GraphicManager.convertToBox(pos.y));
		//dont forget to use the game dimension instead of real world dimension
		
		//Storage in the main variable
		body = world.createBody(bodyDef);
		
		if(radius==0)
		{
			makeRectBody(width,height,bodyType,density,elasticity,pos,angle);
	 		
	 	}else{
	 		makeCircleBody(radius,bodyType,density,elasticity,pos,angle);
	 	}
 

		//Set up of the Vector2 that define the object durection
		
		positionVector.set(GraphicManager.convertToWorld(body.getPosition().x),
				GraphicManager.convertToWorld(body.getPosition().y));
		
	}
	
	public Vector2 getPositionVector(){
		
		return positionVector;
	}
	
	void makeRectBody(float width,float height,BodyDef.BodyType bodyType,
			float density,float restitution, Vector2 pos,float angle){
		
		
		/** IN case of future need**/
	}
	
	void makeCircleBody(float radius,BodyDef.BodyType bodyType,
			float density,float elasticity, Vector2 pos,float angle){
		
		//Basoic Object definition for Physics
		FixtureDef fixtureDef	= new FixtureDef();
 		fixtureDef.density		= density;
 		fixtureDef.restitution	= elasticity;
 		fixtureDef.shape		= new CircleShape();
 		
 		//Game adimenstionalition
 		fixtureDef.shape.setRadius(GraphicManager.convertToBox(radius));
 		
 		body.createFixture(fixtureDef);
		fixtureDef.shape.dispose();
	}
	
	public void DestroyBody(){
		
		if(body!=null)
		{
			isAlive=false;
			GraphicManager.getWorld().destroyBody(body);
			body=null;
		}
	}
	
	public void updatePositionVector(){
		
		Gdx.app.log ("updatePositionVector", positionVector.x + " " +positionVector.y );
		
		positionVector.set(GraphicManager.convertToWorld(body.getPosition().x),
				GraphicManager.convertToWorld(body.getPosition().y));	
	}
	
	public void SetPosition(float wx,float wy){
		
		wx=GraphicManager.convertToBox(wx);
		wy=GraphicManager.convertToBox(wy);

		body.setTransform(wx, wy, body.getAngle());
		updatePositionVector();
	}
	
	public void SetPosition(Vector2 v){
		SetPosition(v.x, v.y);
	}
	
	public abstract void draw(SpriteBatch batch);
	
	
	/**TODO: WEIRD !!!**/
	public void update(float dt){ 
		
		Gdx.app.log ("GameBody", "updatePositionVector");
		
		updatePositionVector();
	}

}
