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


import be.ac.ucl.lfsab1509.bouboule.game.body.Arena;
import be.ac.ucl.lfsab1509.bouboule.game.body.Bouboule;
import be.ac.ucl.lfsab1509.bouboule.game.physicEditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;


public class GameLoop {
	
	GraphicManager 		graphicManager;
	Box2DDebugRenderer 	debugRenderer;
	Matrix4 			debugMatrix;
	
	SpriteBatch			batch;
	
	Bouboule		 	bouboule;
	Bouboule		 	bouboule2;
	Arena				arena;
	Texture				bottleImg;		
	
	//TODO: tempory object ( need to be removed after update)
	private Body bottleModel;
    Texture bottleTexture;
	
	
	/*
	 * Launch the creation of the batch thanks to the camera.
	 * if debug == true, set up the debugger matrix
	 * 
	 * GameLoop(OrthographicCamera cam, boolean debug)
	 */
	public GameLoop(OrthographicCamera cam, boolean debug){

		//creation of the batch and matrix (physical edges of the bodies) debugger
		batch 			= new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);

		if ( debug ){
			debugMatrix		= new Matrix4(cam.combined);
			debugMatrix.scale(GraphicManager.GAME_TO_WORLD, GraphicManager.GAME_TO_WORLD, 1f);

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
		
		initArena();
		initBall();
		initBall2();
		
	}

	/*
	 * Non generic implementation to create a Bouboule thanks to the .json 
	 * stored in jsonFile/boub.json with the code name boub and the texture 
	 * image, images/boub.png, on position 400/1000
	 * 
	 * initBall()
	 */
	private void initBall() {
				
		int gPositionX	= 400;
		int gPositionY	= 1000;
		
		bouboule = new Bouboule(0, BodyType.DynamicBody,
				10, 0.8f, gPositionX, gPositionY, 0,
				"images/boub.png", "data/jsonFile/boub.json", "boub",
				GlobalSettings.MONSTER);
		

		//Add the new object to the graphic and physic manager
		graphicManager.addBody(bouboule);
		
	}
	
	/*
	 * Non generic implementation to create an Arena thanks to the .json file 
	 * ...
	 * ...
	 * 
	 * initArena()
	 */
	private void initArena() {
		
		int gPositionX	= 400;
		int gPositionY	= 0;
		
		arena = new Arena(500, gPositionX, gPositionY, 0,
				"data/gfx/bottle.png", "data/test.json", "bottle",
				GlobalSettings.SCENERY);
		

		//Add the new object to the graphic and physic manager
		graphicManager.addBody(arena);
		
		/*
		// 0. Create a loader for the file saved from the editor.
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/test.json"));

		// 1. Create a BodyDef, as usual.
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(GraphicManager.convertToGame(400), 0);

		// 2. Create a FixtureDef, as usual.
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		//fd.restitution = 0.3f;
		//fd.filter.categoryBits = GlobalSettings.CATEGORY_SCENERY;
		//fd.filter.maskBits = GlobalSettings.MASK_SCENERY;
		
		fd.isSensor=true;

		// 3. Create a Body, as usual.
		bottleModel = GraphicManager.getWorld().createBody(bd);
		bottleModel.setUserData(GlobalSettings.SCENERY);

		// 4. Create the body fixture automatically by using the loader.
		loader.attachFixture(bottleModel, "bottle", fd, 18);
		//bottleModelOrigin = loader.getOrigin("test01", 8).cpy();
		
        bottleImg = new Texture(Gdx.files.internal("data/gfx/bottle.png"));
		*/

	}

	/*
	 * Non generic implementation to create a Bouboule thanks to the .json 
	 * stored in jsonFile/boub.json with the code name boub and the texture 
	 * image, images/boub.png, on position 400/350
	 * 
	 * initBall()
	 */
	private void initBall2() {
		
		int gPositionX	= 400;
		int gPositionY	= 350;
		
		bouboule2 = new Bouboule(0, BodyType.DynamicBody,
				1, 0.9f, gPositionX, gPositionY, 0, 
				"images/boub.png","data/jsonFile/boub.json", "boub",
				GlobalSettings.PLAYER);
		
		graphicManager.addBody(bouboule2);
		
	}
	
	
	/*
	 * Update the ball position thanks to the accelerometer and
	 * launch the physical update function of dt the time between 2 frames
	 * 
	 * update(float dt)
	 */
	public void update(float dt) {
		
		float accelX = Gdx.input.getAccelerometerX();
		float accelY = Gdx.input.getAccelerometerY();
		//float accelZ = Gdx.input.getAccelerometerZ();
		
		
		bouboule.body.applyForceToCenter(new Vector2(0,-1));
		bouboule2.body.applyForceToCenter(new Vector2(-accelX*0.3f,-accelY*0.3f));
		
		graphicManager.update(dt);
	}
	
	/*
	 * Draw all the needed bodies of the game
	 * 
	 * render()
	 */
	public void render() {

		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		

		batch.disableBlending();
		//Allow to draw the background fast because it disable the color blending (override the background).
		batch.enableBlending();
		
		graphicManager.draw(batch);
		
		//Draw all the know bodies
		//batch.draw(bottleImg, bottleModel.getPosition().x, bottleModel.getPosition().y);
		
		batch.end();
		
		//Draw the debugging matrix
		batch.begin();
		debugRenderer.render(GraphicManager.getWorld(), debugMatrix);
		batch.end();
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
