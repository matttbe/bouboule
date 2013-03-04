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

package be.ac.ucl.lfsab1509.bouboule.game.gameManager;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.bouboule.game.body.GameBody;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GraphicManager {
	
	static World world;
	static final float GAME_TO_WORLD=100.0f;
	static final float WORLD_TO_GAME=0.01f;
	
	public ArrayList<GameBody> bodies;
	
	boolean isPaused;
	
	
	public GraphicManager(){
		world=new World(new Vector2(0,0), true);
		bodies=new ArrayList<GameBody>();
		isPaused=false;
	}
	
	public static World getWorld(){
		return world;
	}

	public static float convertToBox(float x){
		return x*WORLD_TO_GAME;
	}
	
	public static float convertToWorld(float x){
		return x*GAME_TO_WORLD;
	}
	
	public void pause(){
		isPaused=true;
	}
	
	public void resume(){
		isPaused=false;
	}
	
	public void dispose(){
		for(GameBody body:bodies){
			body.DestroyBody();
		}
		bodies.clear();
	}
	
	public void addBody(GameBody body){
		bodies.add(body);
	}
	
	public void update(float dt){
		for(GameBody body:bodies){
			
			world.step(GlobalSettings.BOX_STEP,GlobalSettings.VELOCITY_ITERATIONS, GlobalSettings.POSITION_ITERATIONS);
			body.update(dt);
		}
	}
	
	public void draw(SpriteBatch batch){
		for(GameBody body:bodies){
			body.draw(batch);
		}
	}
	


	
}
