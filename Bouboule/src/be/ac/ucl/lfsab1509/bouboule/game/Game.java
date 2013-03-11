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

package be.ac.ucl.lfsab1509.bouboule.game;

import com.badlogic.gdx.math.Intersector;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

public class Game implements ApplicationListener {

	final static int appWidth = 480;//Gdx.graphics.getWidth();
	final static int appHeigth = 800;//Gdx.graphics.getHeight();

	Texture                         boubouleImg;
	Texture                         plateauImg;
	Circle                          bouboule;
	Circle                          plateau;
	SpriteBatch             		batch;
	OrthographicCamera      		camera;
	Intersector						intersector;


	@Override
	public void create() {

		intersector = new Intersector();
		
		boubouleImg = new Texture(Gdx.files.internal("images/boub.png"));
		plateauImg = new Texture(Gdx.files.internal("images/plateau.png"));

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, appWidth, appHeigth);
		
		/**
		float viewportWidth = virtualWidth;
		float viewportHeight = virtualHeight;
		float physicalWidth = Gdx.graphics.getWidth();
		float physicalHeight = Gdx.graphics.getHeight();
		float aspect = virtualWidth / virtualHeight;
		//This is to maintain the aspect ratio.
		//If the virtual aspect ration does not match with the aspect ratio
		//of the hardware screen then the viewport would scaled to
		//meet the size of one dimension and other would not cover full dimension
		//If we stretch it to meet the screen aspect ratio then textures will
		//get distorted either become fatter or elongated
		if (physicalWidth / physicalHeight >= aspect) {
			// Letterbox left and right.
			viewportHeight = virtualHeight;
			viewportWidth = viewportHeight * physicalWidth / physicalHeight;
		} else {
			// Letterbox above and below.
			viewportWidth = virtualWidth;
			viewportHeight = viewportWidth * physicalHeight / physicalWidth;
		}

		OrthographicCamera camera = new OrthographicCamera(viewportWidth, viewportHeight);
		camera.position.set(virtualWidth/2, virtualHeight/2, 0);
		camera.update();
		return camera;
		 */
		 

		
		
		

		//Batch
		batch = new SpriteBatch();


		// create a Circle to logically represent Bouboule

		plateau = new Circle(appWidth/2 , (appHeigth-appWidth) /2 + appWidth/2, appWidth/2 - 80/2);
		bouboule = new Circle(plateau.x, plateau.y, 80/2);




	}




	@Override
	public void render() {

		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);
		// begin a new batch and draw Bouboule and
		batch.begin();
		batch.draw(plateauImg, plateau.x - plateau.radius, plateau.y - plateau.radius, plateau.radius*2 , plateau.radius*2 ); 
		batch.draw(boubouleImg, bouboule.x - 64, bouboule.y - 64);
		batch.end();
		
 


		//Mouvement 

		//EN X
		float accelX = Gdx.input.getAccelerometerX();
		float accelY = Gdx.input.getAccelerometerY();
		float accelZ = Gdx.input.getAccelerometerZ();

		Gdx.app.log ("Accelerometer", accelX+"  "+accelY+"  "+accelZ+"  ");

		bouboule.x -= 100 * accelX * Gdx.graphics.getDeltaTime();
		bouboule.y -= 100 * accelY * Gdx.graphics.getDeltaTime();
		
		/*
		if(!intersector.overlapCircles(plateau, bouboule)){
			bouboule.x=plateau.x;
			bouboule.y=plateau.y;
		}
		*/
		
		//remetre la boule au centre quand elle tombe hors du ring
		if((bouboule.x-plateau.x)*(bouboule.x-plateau.x) + (bouboule.y-plateau.y)*(bouboule.y-plateau.y) >= plateau.radius*plateau.radius){
			bouboule.x=plateau.x;
			bouboule.y=plateau.y;
		}

		


	}




	@Override
	public void dispose() {

		boubouleImg.dispose();
		plateauImg.dispose();
		batch.dispose();

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}