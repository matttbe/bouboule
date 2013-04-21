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


import java.util.Random;

import be.ac.ucl.lfsab1509.bouboule.game.entity.Entity;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;
import be.ac.ucl.lfsab1509.bouboule.game.ia.MapNode;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * Class that define bonus that can be found on the board
 *
 */
public class Bonus extends GameBody{

	private TextureRegion 	texture;		//Texture of the Bonus
	private Sprite 			sprite;			//Sprite to draw the Bonus
	
	private static final Random			random = new Random();
	

	/**
	 * Constructor for a Bonus object 
	 * - px/py		: initial position
	 * - angle		: initial rotation
	 * - texRegionPath : Path to the image file
	 * - jsonFile	: Path to the jsonFile if needed ( "" else)
	 * - jsonName	: jsonName of the object ( must match the json file attribute )
	 *
	 * public Bonus( final float px, final float py, 
	 *		final float angle, final String texRegionPath, 
	 *		final String jsonFile, final String jsonName, final short bonusType)
	 */
	public Bonus( final float angle, final String texRegionPath, 
			final String jsonFile, final String jsonName, final short bonusType) {

		super();

		int size = GlobalSettings.ARENAWAYPOINTALLOW.size();
		MapNode node = GlobalSettings.ARENAWAYPOINTALLOW.get(random.nextInt(size));
		
		
		Vector2 pos	= new Vector2(node.xToPixel(), node.yToPixel());
		
		this.texture = new TextureRegion(new Texture(texRegionPath));

		this.sprite = new Sprite(texture);

		MakeBody(0, 0, 0, BodyType.StaticBody, 0, 0, true, pos, angle, jsonFile, jsonName,
				GraphicManager.convertToGame(texture.getRegionWidth()));

		//Ensure that the object don't rotate.
		body.setFixedRotation(true);
		
		//Create the userData of type Bonus and bonusType
		this.entity = new Entity(Entity.BONUS, true, bonusType);
		
		body.setUserData(this.entity);
	}

	/**
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
	
}
