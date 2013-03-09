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

package be.ac.ucl.lfsab1509.bouboule.game.gameManager;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.bouboule.game.body.GameBody;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class GraphicManager {
	
	static World world;
	static final float GAME_TO_WORLD=100.0f;
	static final float WORLD_TO_GAME=0.01f;
	
	//Store all the body of the game
	public ArrayList<GameBody> bodies;
	
	boolean isPaused;
	
	/*
	 * Create the world, the body container and define the game as notPaused
	 * 
	 * GraphicManager
	 */
	

	public GraphicManager(){
		world=new World(new Vector2(0,0), true);
		
		world.setContactListener( new ContactListener() {
	        @Override
	        public void endContact(Contact contact) {
	        	Fixture f1 = contact.getFixtureA();
			    Fixture f2 = contact.getFixtureB();

	        	if ( f1.getBody().getType() == BodyType.StaticBody | 
	        			f2.getBody().getType() == BodyType.StaticBody){
	        		
	        		Gdx.app.log("KILL", "Bouboule est mort =P");
	        		
	        		//TODO : END GAME BECAUSE WE HAVE A WINNER.
	        	}
	        }
	        @Override
	        public void beginContact(Contact contact) {
	        }
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
	    });
		bodies=new ArrayList<GameBody>();
		isPaused=false;
	}
	
	/*
	 * Return the world
	 * 
	 * World getWorld()
	 */
	public static World getWorld(){
		return world;
	}

	/*
	 * Static function that convert the value to the game reality (for distances)
	 * 
	 * convertToGame(float x)
	 */
	public static float convertToGame(float x){
		return x*WORLD_TO_GAME;
	}
	
	/*
	 * Static function that convert the value to the world reality (for distances) 
	 * 
	 * convertToWorld(float x)
	 */
	public static float convertToWorld(float x){
		return x*GAME_TO_WORLD;
	}
	
	/*
	 * Set up the pause definition to true
	 * 
	 * pause()
	 */
	public void pause(){
		isPaused=true;
	}
	
	/*
	 * Set up the pause definition to false
	 * 
	 * resume()
	 */
	public void resume(){
		isPaused=false;
	}
	
	/*
	 * Remove all the stored bodies and delete the container
	 * 
	 * dispose()
	 */
	public void dispose(){
		for(GameBody body:bodies){
			body.DestroyBody();
		}
		bodies.clear();
	}
	
	
	/*
	 * Add the specified body to the graphical container to be used
	 * in the generated world
	 * 
	 * addBody(GameBody body)
	 */
	public void addBody(GameBody body){
		bodies.add(body);
	}
	
	
	/*
	 * Update all the bodies definition after a timeStep of the world of dt 
	 * (the timeStep is done in the function)
	 * 
	 * update(float dt)
	 */
	public void update(float dt){
		for(GameBody body:bodies){
			
			world.step(GlobalSettings.BOX_STEP,GlobalSettings.VELOCITY_ITERATIONS, GlobalSettings.POSITION_ITERATIONS);
			body.update(dt);
		}
	}
	
	
	/*
	 * Draw all the bodies definition
	 * 
	 * draw(SpriteBatch batch)
	 */
	public void draw(SpriteBatch batch){
		for(GameBody body:bodies){
			body.draw(batch);
		}
	}
	


	
}
