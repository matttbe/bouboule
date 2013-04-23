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

package be.ac.ucl.lfsab1509.bouboule.game.body;

import be.ac.ucl.lfsab1509.bouboule.game.entity.Entity;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;
import be.ac.ucl.lfsab1509.bouboule.game.ia.IA;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/*
 * Bouboule class generated by a jsonFile
 */
public class Bouboule extends GameBody{

	private final TextureRegion 	texture;		//Texture of the Bouboule
	private final Sprite			sprite;			//Sprite to draw the Bouboule

	private final int				IALevel;		//Level of the IA or controller
	
	
	/**
	 * Constructor for a Bouboule object 
	 * - radius 	: radius of Bouboule for a shape like body
	 * - Bodytype 	: Dynamic or Static 
	 * - density 	: Mass in [kg] of Bouboule 
	 * - elasticity : define the elastical property of Bouboul [0..1]f
	 * - px/py		: initial posistion
	 * - angle		: initil rotation
	 * - texRegionPath : Path to the image file
	 * - jsonFile	: Path to the jsonFile if needed ( "" else)
	 * - jsonName	: jsonName of the object ( must match the json file attribute )
	 *
	 * public Bouboule(float radius, BodyType bodyType,float density,
	 * 	float elasticity,float px,float py, float angle,String texRegionPath,
	 *  String jsonFile, String jsonName)
	 */
	public Bouboule(final float radius, final BodyType bodyType, final float density,
			final float elasticity, final float px, final float py, 
			final float angle, final String texRegionPath, 
			final String jsonFile, final String jsonName, final short type, final int IALevel) {

		super();

		this.IALevel = IALevel;
		Vector2 pos	= new Vector2(px, py);

		
		this.texture = new TextureRegion(new Texture(texRegionPath));

		this.sprite = new Sprite(texture);

		MakeBody(0, 0, radius, bodyType, density, elasticity, false, pos, angle, jsonFile, jsonName,
				GraphicManager.convertToGame(texture.getRegionWidth()));

		// added sprite to the fixture (to modify it somewhere else
		body.getFixtureList ().get (0).setUserData (sprite);

		//Ensure that the object don't rotate.
		body.setFixedRotation(GlobalSettings.FIXED_ROTATION);
		
		this.entity = new Entity(type, true);
		
		body.setUserData(this.entity);
	}


	/*
	 * (non-Javadoc)
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

	/*
	 * (non-Javadoc)
	 * @see be.ac.ucl.lfsab1509.bouboule.game.body.GameBody#update(float)
	 */
	public void update() {
		if (entity.isAlive()) {
			Vector2 Acceleration =  IA.compute(IALevel,this);
			body.applyForceToCenter(Acceleration);
			super.update();

		}

	}

	/*
	 * Get body rotation in degrees
	 * if needed =)
	 * 
	 * getBodyRotationInDegrees()
	 */
	public float getBodyRotationInDegrees() {
		return body.getAngle() * MathUtils.radiansToDegrees;
	}

}
