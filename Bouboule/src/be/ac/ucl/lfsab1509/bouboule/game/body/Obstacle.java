package be.ac.ucl.lfsab1509.bouboule.game.body;

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


import java.util.ArrayList;

import be.ac.ucl.lfsab1509.bouboule.game.entity.Entity;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * Load obstacle and draw it on the screen.
 *
 */
public class Obstacle extends GameBody {

	private TextureRegion 	texture;		//Texture of the Obstacle
	private Sprite 			sprite;			//Sprite to draw the Obstacle
	private Vector2			initialPos;		//Initial Position


	/**
	 * Constructor for a Obstacle object .
	 * @param Bodytype 	: Dynamic or Static 
	 * @param density 	: Mass in [kg] of the obstacle 
	 * @param elasticity : define the elastical property of Bouboul [0..1]f
	 * @param px/py		: initial position
	 * @param angle		: initial rotation
	 * @param texRegionPath : Path to the image file
	 * @param jsonFile	: Path to the jsonFile if needed ( "" else)
	 * @param jsonName	: jsonName of the object ( must match the json file attribute )
	 *
	 * public Obstacle( final BodyType bodyType, final float density,
	 * 	final float elasticity, final float px, final float py,
	 *  final float angle, final String texRegionPath, 
	 *  final String jsonFile, final String jsonName)
	 */
	public Obstacle( final BodyType bodyType, final float density,
			final float elasticity, final float px, final float py, 
			final float angle, final String texRegionPath, 
			final String jsonFile, final String jsonName,
			final float initAccX, final float initAccY, final float scale) {

		super();

		this.initialPos	= new Vector2(px, py);

		this.texture = new TextureRegion(new Texture(texRegionPath));

		this.sprite = new Sprite(texture);
		/* We need bigger images in HD but we have to use image where dimensions
		 * are power of 2. So we have to scale these images here.
		 */
		this.sprite.setScale(scale);

		MakeBody(0, 0, 0, bodyType, density, elasticity, false, initialPos,
				angle, jsonFile, jsonName,
				GraphicManager.convertToGame(
						texture.getRegionWidth() * sprite.getScaleX()));

		//Ensure that the object don't rotate.
		//body.setFixedRotation(true);

		//Add Initail Velocity

		body.applyForceToCenter(new Vector2(initAccX, initAccY), true);


		this.entity = new Entity(Entity.OBSTACLE, true);
		body.setUserData(this.entity);
	}

	/**
	 * (non-Javadoc).
	 * @see be.ac.ucl.lfsab1509.bouboule.game.body.GameBody#
	 * draw(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	public void draw(final SpriteBatch sp) {

		if (entity.isAlive()) {

			if (origin != null) {

				//Ensure that the body image position is set on the origin defined by 
				//the jsonFile
				Vector2 pos = positionVector.cpy();

				pos = pos.sub(origin);
				sprite.setPosition(pos.x, pos.y);
				sprite.setOrigin(origin.x, origin.y);
				sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);

				sprite.draw(sp);
			} else {
				sp.draw(texture, positionVector.x, positionVector.y);
			}
		}
	}

	/**
	 * (non-Javadoc).
	 * @see be.ac.ucl.lfsab1509.bouboule.game.body.GameBody#update(float)
	 */
	public void update() {

		if (positionVector.x > GlobalSettings.APPHEIGHT 
				| positionVector.x < -GlobalSettings.APPHEIGHT / 4 
				| positionVector.y > GlobalSettings.APPHEIGHT 
				| positionVector.y < -GlobalSettings.APPHEIGHT / 4) {
			this.entity.setAlive(false);
		}
		
		super.update();
	}

	/**
	 * Method that that remove/display the body from the screen and make it 
	 * insensible/sensible to all chocks.
	 * 
	 * inverseBlink()
	 */
	public void inverseBlink() {
		Gdx.app.log("Obstacle", "reset Obstacle");
		ArrayList<Fixture> fixt = getBody().getFixtureList();

		for (Fixture fix : fixt) {
			fix.setSensor(!fix.isSensor());
		}

		getEntity().setAlive(!getEntity().isAlive());
	}

	@Override
	public void destroyBody() {
		super.destroyBody();
		texture.getTexture().dispose();
	}
}
