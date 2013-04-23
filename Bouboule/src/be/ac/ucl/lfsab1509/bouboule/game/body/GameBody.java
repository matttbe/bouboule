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
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;
import be.ac.ucl.lfsab1509.bouboule.game.physicEditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class GameBody {

	protected Body body;

	protected Entity entity;

	//object direction depending on the force applied to it
	protected Vector2 positionVector;

	//Origin is the origin defined by a jsonFile
	protected Vector2 origin;



	/**
	 * Default constructor that set the object to living and initialise
	 * the position vector
	 * 
	 * GameBody()
	 */
	public GameBody() {

		positionVector 	= new Vector2();
	}


	/**
	 * Generical constructor for bodies ( circle, rectangle or jsonFiled )
	 * Arguments :
	 * - width 		: width of a rectangle object
	 * - height		: height of a rectangle object
	 * - radius		: radius of a circle object
	 * - bodyType	: Static or Dynamic
	 * - density	: Mass in [kg] of the body
	 * - elasticity	: define the elastical property of Bouboul [0..1]f
	 * - sensor		: true/false
	 * - pos		: initial position
	 * - angle		: initial rotated angle
	 * - jsonFile	: path to the jsonFile
	 * - jsonName	: name of the jsonFile (must match the json file attribute) 
	 * - size		: size in pixel of the image to match the object
	 * 
	 * 
	 * MakeBody(float width, float height,float radius,BodyDef.BodyType bodyType,
	 * 	float density,float elasticity,boolean sensor, Vector2 pos,float angle, 
	 * 	String jsonFile, String jsonName, float size)
	 */
	public void MakeBody(final float width, final float height, final float radius,
			final BodyDef.BodyType bodyType, final float density, final float elasticity,
			final boolean sensor, final Vector2 pos, final float angle, 
			final String jsonFile, final String jsonName, final float size) {

		World world = GraphicManager.getWorld();


		//Set up of a body that has a physical interpretation
		BodyDef bodyDef = new BodyDef();

		bodyDef.type 	= bodyType;
		bodyDef.angle	= angle;
		bodyDef.position.set(GraphicManager.convertToGame(pos.x), 
				GraphicManager.convertToGame(pos.y));
		//dont forget to use the game dimension instead of real world dimension

		//Storage in the main variable
		body = world.createBody(bodyDef);

		if (jsonFile == "") {
			if (radius == 0) {

				makeRectBody(width, height, bodyType, density, elasticity, sensor, pos, angle);

			} else {
				makeCircleBody(radius, bodyType, density, elasticity, sensor, pos, angle);
			}

		} else {

			makeJsonBody(bodyType, density, elasticity, sensor, pos, angle, 
					jsonFile, jsonName, size);
		}


		//Set up of the Vector2 that define the object durection

		positionVector.set(GraphicManager.convertToWorld(body.getPosition().x),
				GraphicManager.convertToWorld(body.getPosition().y));

	}

	/**
	 * return the position vector of the body
	 * 
	 * getPositionVector()
	 */
	public Vector2 getPositionVector() {

		return positionVector;
	}

	/**
	 * Make a rectangle object with the constructor argument MakeBody
	 * 
	 * makeRectBody(float width,float height,BodyDef.BodyType bodyType,
	 * 	float density,float elasticity, Vector2 pos,float angle)
	 */
	void makeRectBody(final float width, final float height, final BodyDef.BodyType bodyType,
			final float density, final float elasticity, final boolean sensor,
			final Vector2 pos, final float angle) {


		/** IN case of future need**/
	}



	/**
	 * Make a circle object with the constructor argument MakeBody
	 * 
	 * makeCircleBody(float radius,BodyDef.BodyType bodyType,
	 * 	float density,float elasticity, Vector2 pos,float angle)
	 */
	void makeCircleBody(final float radius, final BodyDef.BodyType bodyType,
			final float density, final float elasticity, final boolean sensor,
			final Vector2 pos, final float angle) {

		//Basoic Object definition for Physics
		FixtureDef fixtureDef	= new FixtureDef();
		fixtureDef.density		= density;
		fixtureDef.restitution	= elasticity;
		fixtureDef.isSensor     = sensor;
		//fixtureDef.friction 	= 0.5f;
		fixtureDef.shape		= new CircleShape();

		//Game adimenstionalition
		fixtureDef.shape.setRadius(GraphicManager.convertToGame(radius));

		body.createFixture(fixtureDef);
		fixtureDef.shape.dispose();
	}


	/**
	 * Make a json object with the constructor argument MakeBody
	 * 
	 * makeJsonBody(BodyDef.BodyType bodyType, float density,
	 * 	float elasticity, Vector2 pos,float angle, String jsonFile, String jsonName, float size)
	 */
	void makeJsonBody(final BodyDef.BodyType bodyType, final float density,
			final float elasticity, final boolean sensor, final Vector2 pos,
			final float angle, final String jsonFile, final String jsonName, 
			final float size) {

		//Basoic Object definition for Physics
		FixtureDef fixtureDef	= new FixtureDef();
		fixtureDef.density		= density;
		fixtureDef.restitution	= elasticity;
		fixtureDef.isSensor		= sensor;

		//Load the json Loader
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(jsonFile));

		loader.attachFixture(body, jsonName, fixtureDef, size);
		origin = loader.getOrigin(jsonName, size).cpy();

	}

	/**
	 * Destroy the body if needed
	 * 
	 * DestroyBody()
	 */
	public void destroyBody() {

		if (body != null) {

			entity.setAlive(false);
			GraphicManager.getWorld().destroyBody(body);
			body	= null;
		}
	}

	/**
	 * Update the vector position of the object by getting the world status
	 * 
	 * updatePositionVector()
	 */
	public void updatePositionVector() {

		//Gdx.app.log ("updatePositionVector", positionVector.x + " " +positionVector.y );

		positionVector.set(GraphicManager.convertToWorld(body.getPosition().x),
				GraphicManager.convertToWorld(body.getPosition().y));	
	}

	//Getter and Setter

	public void setBody(final Body bodi) {
		this.body = bodi;
	}

	public Body getBody() {
		return body;
	}


	public Boolean getIsAlive() {
		return entity.isAlive();
	}

	public void setIsAlive(final Boolean alive) {
		entity.setAlive(alive);
	}

	public Vector2 getOrigin() {
		return origin;
	}

	public void setOrigin(final Vector2 orig) {
		this.origin = orig;
	}

	/**
	 * Set the body to a specific position but don't change the angle 
	 * with a x/y coordinate
	 * 
	 * SetPosition(float px,float py)
	 */
	public void setPosition(final float px, final float py) {

		//Adimentionalision
		float pX = GraphicManager.convertToGame(px);
		float pY = GraphicManager.convertToGame(py);

		body.setTransform(pX, pY, body.getAngle());
		updatePositionVector();
	}

	/**
	 * Set the body to a specific position but don't change the angle 
	 * with a position coordinate
	 * 
	 * SetPosition(Vector2 v)
	 */
	public void setPosition(final Vector2 v) {
		setPosition(v.x, v.y);
	}

	/**
	 * Launch the specific draw of the object
	 * 
	 * draw(SpriteBatch batch)
	 */
	public abstract void draw(final SpriteBatch batch);


	/**
	 * Update in dt time of the vectorPosition after a world frame
	 * 
	 * update(float dt)
	 */
	public void update() { 

		//Gdx.app.log ("GameBody", "updatePositionVector");

		updatePositionVector();
	}

	private Fixture getFixture () {
		return body.getFixtureList ().get (0);
	}

	public void increaseWeight (int iMult) {
		Fixture fixture = getFixture ();
		fixture.setDensity (fixture.getDensity () * iMult);
		body.resetMassData ();
	}

	public void biggerWeight () {
		increaseWeight (2);
	}

	public void lowerWeight () {
		increaseWeight (1/2);
	}

	public void increaseElasticity (int iMult) {
		Fixture fixture = getFixture ();
		fixture.setRestitution (fixture.getRestitution () * iMult);
	}

	public void biggerElasticity () {
		increaseElasticity (2);
	}

	public void lowerElasticity () {
		increaseElasticity (1/2);
	}

}
