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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Bouboule extends GameBody{
	
	TextureRegion texture;
	float radius;
	
	
	public Bouboule(float radius, BodyType bodyType,float density,
			float elasticity,float px,float py, float angle,TextureRegion texRegion) {
		
		super();
		
		Vector2 pos	= new Vector2(px,py);
		texture		= new TextureRegion(texRegion);//, pos); TODO:VERIF
		this.radius	= radius;

		MakeBody(0, 0, radius, bodyType, density, elasticity, pos, angle);
	}
	
	public void draw(SpriteBatch sp) {
		// TODO Auto-generated method stub
		if(isAlive){
			
			sp.draw(texture, positionVector.x-radius, positionVector.y-radius);
			//TODO: SET up;
		}
	}
	
	public void update(float dt){
		if(isAlive){
			
			Gdx.app.log ("Bouboule", "Update boubloule");
			super.update(dt);
			
			
			//TODO: set UP;
		}
		
	}
	
	public float GetBodyRotationInDegrees(){
		return body.getAngle()*MathUtils.radiansToDegrees;
	}
	
}
