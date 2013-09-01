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

import be.ac.ucl.lfsab1509.bouboule.game.anim.CountDown;
import be.ac.ucl.lfsab1509.bouboule.game.body.Arena;
import be.ac.ucl.lfsab1509.bouboule.game.body.GameBody;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 
 * Class that manage all the graphics from the application.
 *
 */
public class GraphicManager {

	private static World world;
	private static final float GAME_TO_WORLD = 100.0f;
	private static final float WORLD_TO_GAME = 0.01f;

	// settings about the levels
	public static boolean			ALLOW_BONUS = false;
	public static int				BONUS_SPAWN_RATE = 0;
	public static ArrayList<String>	BONUS_ENABLED = null;
	public static int				TIME = 30;

	//Store all the body of the game
	private ArrayList<GameBody> bodies;
	private Texture scoreboard;
	private Arena arena;
	// background
	private Texture background;
	private TextureRegion backgroundRegion;
	private CountDown tutorial;

	/**
	 * Create the world, the body container and define the game as notPaused
	 * 
	 * GraphicManager()
	 */
	public GraphicManager() {
		world = new World(new Vector2(0, 0), true);

		world.setContactListener(new EndGameListener());
		//world.setVelocityThreshold(10.0f);
		bodies = new ArrayList<GameBody>();

		loadScoreboard();
	}

	/**
	 * Return the world
	 * 
	 * World getWorld()
	 */
	public static World getWorld() {
		return world;
	}
	
	/**
	 * Load the scoreboard
	 */
	protected void loadScoreboard() {
		this.scoreboard = new Texture("terrain/ScoreBoard/scoreboard.png");
	}
	
	/**
	 * Load the background
	 * @pre: {@link #dispose(false)} should have been called before
	 */
	protected void loadBackground(final String arenaName) {

		background = new Texture(arenaName + "bg.jpg");
		
		if (GlobalSettings.SCALE != 1f)
			backgroundRegion = new TextureRegion(background);
	}


	/**
	 * Static function that convert the value to the game reality (for distances)
	 * @param x : value to convert 
	 * 
	 * convertToGame(float x)
	 */
	public static float convertToGame(final float x) {
		return x * WORLD_TO_GAME;
	}

	/**
	 * Static function that convert the value to the world reality (for distances) 
	 * @param x : value to convert 
	 * 
	 * convertToWorld(float x)
	 */
	public static float convertToWorld(final float x) {
		return x * GAME_TO_WORLD;
	}

	/**
	 * Remove all the stored bodies and delete the container
	 * @param bDisposeWorld true to dispose the world (at the end)
	 */
	public void dispose(boolean bDisposeWorld) {
		
		// Clear the background for a new use.
		if (this.background != null) {
			this.background.dispose();
			this.background = null;
		}
		
		if (backgroundRegion != null && backgroundRegion.getTexture() != null) {
			backgroundRegion.getTexture().dispose();
			backgroundRegion = null;
		}
		
		if (this.tutorial != null) {
			this.tutorial.dispose();
			this.tutorial = null;
		}
		
		// Clear arena
		if (this.arena != null) {
			this.arena.destroyBody();
			this.arena = null;
		}
		
		// Clear the bodies
		for (GameBody body : bodies) {
			body.destroyBody();
		}
		bodies.clear();

		// at the end
		if (bDisposeWorld) {
			scoreboard.dispose();
			world.dispose();
		}
	}

	/**
	 * Add the specified body to the graphical container to be used
	 * in the generated world
	 * 
	 * @param body : the GameBody to add 
	 * 
	 * addBody(GameBody body)
	 */
	public void addBody(final GameBody body) {
		bodies.add(body);
	}
	
	/**
	 * Set the specified arena to the graphical container to be used
	 * in the generated world
	 * 
	 * @param body : the Arena to use
	 * @pre: {@link #dispose(false)} should have been called before
	 * 
	 * setArena(Arena body)
	 */
	public void setArena(final Arena body) {
		this.arena = body;
	}
	
	/**
	 * Add the specified arena to the graphical container to be used
	 * in the generated world
	 * 
	 * @param body : the Arena to add 
	 * 
	 * addArena(Arena body)
	 */
	public CountDown getTutorial() {
		
		if (this.tutorial == null)
			this.tutorial = new CountDown(2, 1, 4f, "anim/tuto.png", false);
		
		return this.tutorial;
	}


	/**
	 * Remove the specified body from the graphical container to no longer be
	 * used in the generated world
	 * @param body : the GameBody to remove
	 */
	public void removeBody(final GameBody body) {
		// do that on a temp arraylist to avoid concurrent modifications
		ArrayList<GameBody> bodiesTmp = new ArrayList<GameBody>(bodies);
		bodiesTmp.remove(body);
		// old bodies will be automatically freed by gc
		bodies = bodiesTmp;
	}
	
	
	/**
	 * Update all the bodies definition after a timeStep of the world of dt 
	 * (the timeStep is done in the function)
	 * 
	 * update()
	 */
	public void update() {
		
		world.step(GlobalSettings.BOX_STEP, GlobalSettings.VELOCITY_ITERATIONS, 
				GlobalSettings.POSITION_ITERATIONS);
		
		for (GameBody body:bodies) {
			body.update();
		}
	}

	public static float getGameToWorld() {
		return GAME_TO_WORLD;
	}

	public static float getWorldToGame() {
		return WORLD_TO_GAME;
	}

	/**
	 * Draw all the bodies definition
	 * @param sp : SpriteBatch to draw to
	 * 
	 * draw(SpriteBatch batch)
	 */
	public void draw(final SpriteBatch batch) {

		for (GameBody body : bodies) {
			body.draw(batch);
		}
	}
	
	/**
	 * Draw the Arena > Arena + scoreboard
	 * @param sp : SpriteBatch to draw to
	 * 
	 * draw(SpriteBatch batch)
	 */
	public void drawArena(final SpriteBatch batch) {

		arena.draw(batch);
		batch.draw(scoreboard, 0, 0);
	}
	
	/**
	 * Draw the background with scalling if needed
	 * @param sp : SpriteBatch to draw to
	 * 
	 * draw(SpriteBatch batch)
	 */
	public void drawBackground(final SpriteBatch batch) {

		// If the background is upscaled, we must use a TextureRegion
		if (GlobalSettings.SCALE != 1f) {
			batch.draw(backgroundRegion, GlobalSettings.SHIFT_BG_WIDTH,
					GlobalSettings.SHIFT_BG_HEIGHT, 0f, 0f,
					backgroundRegion.getRegionWidth(),
					backgroundRegion.getRegionHeight(), GlobalSettings.SCALE,
					GlobalSettings.SCALE, 0f);

		} else {
			batch.draw(background, GlobalSettings.SHIFT_BG_WIDTH, 0f);
		}
	}
	
	/**
	 * Draw the tutorial
	 * @param sp : SpriteBatch to draw to
	 * 
	 * draw(SpriteBatch batch)
	 */
	public boolean drawTutorial(final SpriteBatch batch, final float delta) {
		
		return getTutorial().draw(batch, delta);
	}
}
