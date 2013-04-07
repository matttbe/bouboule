package be.ac.ucl.lfsab1509.bouboule.game.body;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Obstacle extends GameBody{

	//Texture texture;
	private TextureRegion texture;
	private Sprite sprite;

	public Obstacle( final BodyType bodyType, final float density,
			final float elasticity, final float px, final float py, 
			final float angle, final String texRegionPath, 
			final String jsonFile, final String jsonName, final short entity) {

		super();

		Vector2 pos	= new Vector2(px, py);
		
		this.texture = new TextureRegion(new Texture(texRegionPath));

		this.sprite = new Sprite(texture);

		MakeBody(0, 0, 0, bodyType, density, elasticity, false, pos, angle, jsonFile, jsonName,
				GraphicManager.convertToGame(texture.getRegionWidth()));

		//Ensure that the object don't rotate.
		body.setFixedRotation(true);
		body.setUserData(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see be.ac.ucl.lfsab1509.bouboule.game.body.GameBody#
	 * draw(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	public void draw(final SpriteBatch sp) {

		if (isAlive) {

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
		
			super.update();
	}





}
