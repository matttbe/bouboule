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


import be.ac.ucl.lfsab1509.bouboule.game.entity.Entity;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Arena extends GameBody {

	private TextureRegion 	texture;		//Texture of the Arena
	private Sprite 			sprite;			//Sprite to draw the Arena

	/**
	 * Constructor for a Arena object.
	 * @param radius 		: radius of Arena for a shape like body
	 * @param Bodytype 		: Dynamic or Static 
	 * @param density 		: Mass in [kg] of Arena 
	 * @param elasticity	: define the elastical property of Arena [0..1]f
	 * @param px/py			: initial posistion
	 * @param angle			: initil rotation
	 * @param texRegionPath : Path to the image file
	 * @param jsonFile		: Path to the jsonFile if needed ( "" else)
	 * @param jsonName		: jsonName of the object ( must match the json file attribute )
	 *
	 * public Arena(float radius, BodyType bodyType,float density,
	 * 	float elasticity,float px,float py, float angle,String texRegionPath,
	 *  String jsonFile, String jsonName)
	 */
	public Arena(final float radius, final float px, final float py,
			final float angle, final String texRegionPath, 
			final String jsonFile, final String jsonName) {

		super();

		Vector2 pos	= new Vector2(px, py);

		this.texture = new TextureRegion(new Texture(texRegionPath));

		this.sprite = new Sprite(texture);

		MakeBody(0, 0, radius, BodyType.StaticBody, 0, 0, true, pos, angle, jsonFile, jsonName,
				GraphicManager.convertToGame(texture.getRegionWidth()));

		this.entity = new Entity(Entity.SCENERY);
		body.setUserData(this.entity);
	}
	
	/**
	 * draw the Arena object. (bg drawing > Disable Blending)
	 * @param SpriteBatch sp to draw
	 * 
	 * 	public void draw(final SpriteBatch sp) {
	 */
	@Override
	public void draw(final SpriteBatch sp) {
		
		//sp.disableBlending();
		
		if (origin != null) {

			//Ensure that the body image position is set on the origin defined by 
			//the jsonFile

			Vector2 pos = positionVector.sub(origin);
			sprite.setPosition(pos.x, pos.y);
			sprite.setOrigin(origin.x, origin.y);

			sprite.draw(sp);
		} else {
			sp.draw(texture, positionVector.x, positionVector.y);
		}
		
		//sp.enableBlending();
	}

	@Override
	public void destroyBody() {
		super.destroyBody();
		texture.getTexture().dispose();
	}

}
