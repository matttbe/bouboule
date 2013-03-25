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


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;


public class GraphicManager {
	
	private static World world;
	private static final float GAME_TO_WORLD = 100.0f;
	private static final float WORLD_TO_GAME = 0.01f;
	
	//Store all the body of the game
	public ArrayList<GameBody> bodies;
	
	/*
	 * Create the world, the body container and define the game as notPaused
	 * 
	 * GraphicManager
	 */
	

	public GraphicManager() {
		world = new World(new Vector2(0, 0), true);
		
		world.setContactListener(new EndGameListener());
		bodies	 = new ArrayList<GameBody>();
	}
	
	/*
	 * Return the world
	 * 
	 * World getWorld()
	 */
	public static World getWorld() {
		return world;
	}

	/*
	 * Static function that convert the value to the game reality (for distances)
	 * 
	 * convertToGame(float x)
	 */
	public static float convertToGame(final float x) {
		return x * WORLD_TO_GAME;
	}
	
	/*
	 * Static function that convert the value to the world reality (for distances) 
	 * 
	 * convertToWorld(float x)
	 */
	public static float convertToWorld(final float x) {
		return x * GAME_TO_WORLD;
	}
	
	/*
	 * Remove all the stored bodies and delete the container
	 * 
	 * dispose()
	 */
	public void dispose() {
		for (GameBody body:bodies) {
			body.destroyBody();
		}
		bodies.clear();
	}
	
	
	/*
	 * Add the specified body to the graphical container to be used
	 * in the generated world
	 * 
	 * addBody(GameBody body)
	 */
	public void addBody(final GameBody body) {
		bodies.add(body);
	}
	
	
	/*
	 * Update all the bodies definition after a timeStep of the world of dt 
	 * (the timeStep is done in the function)
	 * 
	 * update(float dt)
	 */
	public void update() {
		for (GameBody body:bodies) {
			
			world.step(GlobalSettings.BOX_STEP, GlobalSettings.VELOCITY_ITERATIONS, 
						GlobalSettings.POSITION_ITERATIONS);
			body.update();
		}
	}
	
	
	public static float getGameToWorld() {
		return GAME_TO_WORLD;
	}

	public static float getWorldToGame() {
		return WORLD_TO_GAME;
	}

	/*
	 * Draw all the bodies definition
	 * 
	 * draw(SpriteBatch batch)
	 */
	public void draw(final SpriteBatch batch) {
		for (GameBody body:bodies) {
			body.draw(batch);
		}
	}
	


	
}
