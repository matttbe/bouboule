package be.ac.ucl.lfsab1509.bouboule.game.body;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Arena extends GameBody {

	//Texture texture;
	TextureRegion texture;
	Sprite sprite;
	
	/*
	 * Constructor for a Arena object 
	 * - radius 	: radius of Arena for a shape like body
	 * - Bodytype 	: Dynamic or Static 
	 * - density 	: Mass in [kg] of Arena 
	 * - elasticity : define the elastical property of Arena [0..1]f
	 * - px/py		: initial posistion
	 * - angle		: initil rotation
	 * - texRegionPath : Path to the image file
	 * - jsonFile	: Path to the jsonFile if needed ( "" else)
	 * - jsonName	: jsonName of the object ( must match the json file attribute )
	 *
	 * public Arena(float radius, BodyType bodyType,float density,
	 * 	float elasticity,float px,float py, float angle,String texRegionPath,
	 *  String jsonFile, String jsonName)
	 */
	public Arena(float radius,float px,float py, float angle,String texRegionPath, 
			String jsonFile, String jsonName,short entity) {

		super();

		Vector2 pos	= new Vector2(px,py);

		//
		this.texture = new TextureRegion(new Texture(texRegionPath));

		this.sprite = new Sprite(texture);

		MakeBody(0, 0, radius, BodyType.StaticBody, 0, 0, true, pos, angle, jsonFile, jsonName,
				GraphicManager.convertToGame(texture.getRegionWidth()));

		body.setUserData(entity);
	}
	
	@Override
	public void draw(SpriteBatch sp) {
		if ( origin != null){

			//Ensure that the body image position is set on the origin defined by 
			//the jsonFile

			Vector2 pos = positionVector.sub(origin);
			sprite.setPosition(pos.x, pos.y);
			sprite.setOrigin(origin.x, origin.y);

			sprite.draw(sp);
		} else {
			sp.draw(texture, positionVector.x, positionVector.y);
		}
	}

}
