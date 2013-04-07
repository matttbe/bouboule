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


import be.ac.ucl.lfsab1509.bouboule.game.anim.CountDown;
import be.ac.ucl.lfsab1509.bouboule.game.level.LevelLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;


public class GameLoop {

	public  GraphicManager 		graphicManager;
	private Box2DDebugRenderer 	debugRenderer;
	private Matrix4 			debugMatrix;

	private CountDown 			countDown;
	private BitmapFont			fontOswald;
	private BitmapFont			fontOsaka;
	public SpriteBatch			batch;



	/*
	 * Launch the creation of the batch thanks to the camera.
	 * if debug == true, set up the debugger matrix
	 * 
	 * GameLoop(OrthographicCamera cam, boolean debug)
	 */
	public GameLoop(final OrthographicCamera cam, final boolean debug) {

		//creation of the batch and matrix (physical edges of the bodies) debugger
		batch 			= new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);

		if (debug) {
			debugMatrix		= new Matrix4(cam.combined);
			debugMatrix.scale(GraphicManager.getGameToWorld(), GraphicManager.getGameToWorld(), 1f);

			debugRenderer	= new Box2DDebugRenderer();
		}

		graphicManager = new GraphicManager();

		init();
	}

	/*
	 * Initialise all the object needed 
	 * 
	 * init()
	 */
	private void init() {

		//load level
		LevelLoader level = new LevelLoader();
		GlobalSettings.NBLEVELS = level.getNbLevels ();
		int iLevel = GlobalSettings.PROFILE.getLevel();
		try {
			level.loadLevel ("Level" + iLevel);
		} catch (GdxRuntimeException e) {
			level.loadLevel ("Level1"); // TODO: should not happen...
		}
		level.readLevelArena	(graphicManager);
		level.readLevelBouboule (graphicManager);
		level.readLevelObstacles(graphicManager);
		level.readLevelMapNodes ();

		//load the counter 
		countDown = new CountDown();

		//Load the font + enable white
		fontOswald = new BitmapFont(Gdx.files.internal("fonts/Oswald/Oswald.fnt"),
				Gdx.files.internal("fonts/Oswald/Oswald.png"), false);
		fontOswald.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		fontOsaka = new BitmapFont(Gdx.files.internal("fonts/Osaka/Osaka.fnt"),
				Gdx.files.internal("fonts/Osaka/Osaka.png"), false);
		fontOsaka.setColor(1.0f, 1.0f, 1.0f, 1.0f);

	}


	/*
	 * Update the ball position thanks to the accelerometer and
	 * launch the physical update function of dt the time between 2 frames
	 * 
	 * update(float dt)
	 */
	public void update() {

		/*float accelX = Gdx.input.getAccelerometerX();
		float accelY = Gdx.input.getAccelerometerY();
		 */
		//float accelZ = Gdx.input.getAccelerometerZ();


		/*bouboule.body.applyForceToCenter(new Vector2(0,-0.5f));
		bouboule2.body.applyForceToCenter(new Vector2(-accelX*0.3f,-accelY*0.3f));
		 */

		graphicManager.update();
	}

	/*
	 * Draw all the needed bodies of the game
	 * 
	 * render()
	 */
	public boolean render(final boolean pause) {
		
		boolean status = false;

		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

		CharSequence lives = Integer.toString(GlobalSettings.PROFILE.getNbLifes ());
		CharSequence levelD= Integer.toString(GlobalSettings.PROFILE.getLevel   ()/10);
		CharSequence levelU= Integer.toString(GlobalSettings.PROFILE.getLevel   ()%10);
		CharSequence score = Integer.toString(GlobalSettings.PROFILE.getOldScore());
		

		batch.begin();


		//batch.disableBlending();
		//Allow to draw the background fast because it disable 
		//the color blending (override the background).
		//batch.enableBlending();

		//Draw all the know bodies

		graphicManager.draw(batch);

		fontOsaka .draw(batch, lives , 630, 1167);
		fontOsaka .draw(batch, score , 630, 1205);
		fontOswald.draw(batch, levelD, 285, 1180);
		fontOswald.draw(batch, levelU, 345, 1180);
		
		if ( pause )
			status = countDown.draw(batch);

		batch.end();

		batch.begin();
		//Draw the debugging matrix
		debugRenderer.render(GraphicManager.getWorld(), debugMatrix);
		batch.end();
		
		return status;
	}


	/*
	 * Remove all the memory used object 
	 *
	 * dispose()
	 */
	public void dispose() {
		//Remove the memory of the managed object and the debug matrix
		debugRenderer.dispose();
		graphicManager.dispose();
	}



}
