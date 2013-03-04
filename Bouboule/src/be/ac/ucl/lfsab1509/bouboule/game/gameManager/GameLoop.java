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


import be.ac.ucl.lfsab1509.bouboule.game.body.Bouboule;
import be.ac.ucl.lfsab1509.bouboule.game.physicEditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	TextureRegion 		boubouleImg;
	
	private Body bottleModel;
    Texture bottleTexture;
	
	//Texture 			arenaImg;
	
	
	public GameLoop(OrthographicCamera cam){

		batch 			= new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		
		debugMatrix		= new Matrix4(cam.combined);
		debugMatrix.scale(GraphicManager.GAME_TO_WORLD, GraphicManager.GAME_TO_WORLD, 1f);

		debugRenderer	= new Box2DDebugRenderer();
		
		graphicManager = new GraphicManager();
		
		init();
	}
	
	private void init() {
		
		initArena();
		initBall();
		initBall2();
		
	}

	
	private void initBall() {
		
		boubouleImg = new TextureRegion( new Texture(Gdx.files.internal("images/boub2.png")));
		
				
		int gRadius		= 50;
		int gPositionX	= 400;
		int gPositionY	= 1000;
		
		bouboule = new Bouboule(gRadius, BodyType.DynamicBody,
				1, 0.8f, gPositionX, gPositionY, 0, boubouleImg);
		
		//ball.SetTextureDimension(TextureDimensions.BALL_WIDTH, TextureDimensions.BALL_HEIGHT);
		graphicManager.addBody(bouboule);
		
	}
	
	private void initArena() {
		
		// 0. Create a loader for the file saved from the editor.
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/test.json"));

		// 1. Create a BodyDef, as usual.
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(GraphicManager.convertToBox(400), 0);

		// 2. Create a FixtureDef, as usual.
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.3f;

		// 3. Create a Body, as usual.
		bottleModel = GraphicManager.getWorld().createBody(bd);

		// 4. Create the body fixture automatically by using the loader.
		loader.attachFixture(bottleModel, "test01", fd, 8);
		//bottleModelOrigin = loader.getOrigin("test01", 8).cpy();

	}

	private void initBall2() {
		
		boubouleImg = new TextureRegion( new Texture(Gdx.files.internal("images/boub2.png")));
		
				
		int gRadius		= 50;
		int gPositionX	= 400;
		int gPositionY	= 350;
		
		bouboule2 = new Bouboule(gRadius, BodyType.DynamicBody,
				1, 0.9f, gPositionX, gPositionY, 0, boubouleImg);
		
		//ball.SetTextureDimension(TextureDimensions.BALL_WIDTH, TextureDimensions.BALL_HEIGHT);
		graphicManager.addBody(bouboule2);
		
	}
	
	
	
	public void update(float dt) {
		
		float accelX = Gdx.input.getAccelerometerX();
		float accelY = Gdx.input.getAccelerometerY();
		//float accelZ = Gdx.input.getAccelerometerZ();
		
		
		//bouboule.body.applyForceToCenter(new Vector2(0,-1));
		bouboule2.body.applyForceToCenter(new Vector2(-accelX*0.3f,-accelY*0.3f));
		
		graphicManager.update(dt);
	}
	
	public void render() {

		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		batch.disableBlending();
		//arena.Draw(batch);
		
		batch.enableBlending();
		graphicManager.draw(batch);
		
		
		batch.end();
		
		batch.begin();
		debugRenderer.render(GraphicManager.getWorld(), debugMatrix);
		batch.end();
	}
	

	public void dispose() {
		// TODO Auto-generated method stub
		debugRenderer.dispose();
		graphicManager.dispose();
	}

	
	
}
