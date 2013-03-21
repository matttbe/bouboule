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

package be.ac.ucl.lfsab1509.bouboule.game;

import be.ac.ucl.lfsab1509.bouboule.game.util.CameraHelper;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GameLoop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MyGame implements ApplicationListener {

	static final int APPWIDTH = 800;
	static final int APPHEIGTH = 1280;
	
	//Test Update


	SpriteBatch             		batch;
	OrthographicCamera      		camera;

	GameLoop 						game;

	@Override
	public void create() {

		camera=CameraHelper.GetCamera(appWidth,appHeigth);

		game = new GameLoop(camera, true);
		
	}



	@Override
	public void render() {
		
		// float dt = Gdx.graphics.getDeltaTime();

		game.update();
		game.render();

	}

	@Override
	public void dispose() {
		game.dispose();
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