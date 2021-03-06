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

import be.ac.ucl.lfsab1509.bouboule.game.ai.MapNode;
import be.ac.ucl.lfsab1509.bouboule.game.entity.Entity;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GameLoop;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Timer;

/**
 * Class that define bonus that can be found on the board.
 *
 */
public class Bonus extends GameBody {

	private TextureRegion texture;   // Texture of the Bonus
	private Sprite sprite;           // Sprite to draw the Bonus

	private float fDelta = 0;        // used for the end effects
	private boolean bKilled = false; // avoid double destroy
	private static final float TIME_END_EFFECT = .75f;
	private boolean bIsOpening = true;
	private static final float TIME_OPEN_EFFECT = .25f;

	private static final Random random = new Random();
	private Timer timer;


	public static final String[][] bonusInfo = {
			{"bonus/elasticity/elasticity_high.png", "Collisions are more elastic"},
			{"bonus/elasticity/elasticity_low.png",  "Collisions are less elastic"},
			{"bonus/heart/heart.png", "Extra life"},
			{"bonus/inverse/inverse.png", "The axes are inverted"},
			{"bonus/invincible/invincible.png", "Bouboule is invincible"},
			{"bonus/invisible/invisible.png", "Bouboule is invisible"},
			{"bonus/speed/speed_high.png", "Bouboule runs faster"},
			{"bonus/speed/speed_low.png", "Bouboule runs slower"},
			{"bonus/star/star.png", "More points"},
			{"bonus/time/timeup.png", "More time before the end of the game"},
			{"bonus/time/timedown.png", "Less time before the end of the game"},
			{"bonus/weight/weight_high.png", "Bouboule is heavier"},
			{"bonus/weight/weight_low.png", "Bouboule is lighter"}
		};

	/**
	 * Constructor for a Bonus object 
	 * @param px/py			: initial position
	 * @param angle			: initial rotation
	 * @param texRegionPath : Path to the image file
	 * @param jsonFile		: Path to the jsonFile if needed ( "" else)
	 * @param jsonName		: jsonName of the object ( must match the json file attribute )
	 *
	 * public Bonus( final float px, final float py, 
	 *		final float angle, final String texRegionPath, 
	 *		final String jsonFile, final String jsonName, final short bonusType)
	 */
	public Bonus(final float angle, final String texRegionPath, 
			final String jsonFile, final String jsonName,
			final Entity.BonusType bonusType) {

		super();

		int size = GlobalSettings.ARENAWAYPOINTALLOW.size();
		MapNode node = GlobalSettings.ARENAWAYPOINTALLOW.get(random.nextInt(size));

		Vector2 pos	= new Vector2(node.xToPixel() - 32, node.yToPixel()-32);
		Vector2 radius = new Vector2(node.weightToPixel() * random.nextFloat(), 0);
		radius.rotate(random.nextInt(359));
		pos.add(radius);

		this.texture = new TextureRegion(new Texture(texRegionPath));

		this.sprite = new Sprite(texture);

		MakeBody(0, 0, 0, BodyType.StaticBody, 0, 0, true, pos, angle, jsonFile, jsonName,
				GraphicManager.convertToGame(texture.getRegionWidth()));

		//Ensure that the object don't rotate.
		body.setFixedRotation(true);

		//Create the userData of type Bonus and bonusType
		this.entity = new Entity(Entity.BONUS, true, bonusType);

		body.setUserData(this.entity);


		//Ensure that the body image position is set on the origin defined by 
		//the jsonFile
		if (origin != null)
		{
			pos = positionVector.cpy();
			pos = pos.sub(origin);
			sprite.setPosition(pos.x, pos.y);
			sprite.setOrigin(origin.x, origin.y);
			sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		}

		// removed the bonus after a delay
		Timer.Task task = new Timer.Task() {
			@Override
			public void run() {
				entity.setAlive(false);
			}
		};
		Timer timer = new Timer();
		timer.scheduleTask(task, 10f);
	}

	/**
	 * (non-Javadoc).
	 * @see be.ac.ucl.lfsab1509.bouboule.game.body.GameBody#
	 * draw(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	public void draw(final SpriteBatch sp) {
		if (bIsOpening)
			setOpeningEffect(sp);
		else if (entity.isAlive()) {
			if (origin != null)
				sprite.draw(sp);
			else
				sp.draw(texture, positionVector.x, positionVector.y);
		}
		else if (! bKilled)
			setEndingEffect(sp);
	}

	private void setOpeningEffect(final SpriteBatch sp) {
		fDelta += Gdx.graphics.getDeltaTime();
		if (fDelta > TIME_OPEN_EFFECT)
			fDelta = TIME_OPEN_EFFECT;
		// zoom
		sprite.setScale(fDelta / TIME_OPEN_EFFECT);

		// transparency
		Color color = sprite.getColor();
		color.a = fDelta / TIME_OPEN_EFFECT;
		sprite.setColor(color);

		sprite.draw(sp);
		if (fDelta == TIME_OPEN_EFFECT) {
			bIsOpening = false;
			fDelta = 0;
		}
	}

	private void setEndingEffect(final SpriteBatch sp) {
		fDelta += Gdx.graphics.getDeltaTime();
		if (fDelta > TIME_END_EFFECT) {
			bKilled = true;
			destroyBody();
			return;
		}
		// zoom
		sprite.setScale(1 + fDelta / TIME_END_EFFECT);

		// transparency
		Color color = sprite.getColor();
		color.a = 1 - fDelta / TIME_END_EFFECT;
		sprite.setColor(color);

		sprite.draw(sp);
	}

	/**
	 * Destroy the body + kill speed Task if needed if needed
	 */
	@Override
	public void destroyBody() {
		super.destroyBody();
		GameLoop.removeBonus(this);
		if (timer != null) {
			timer.stop();
			timer.clear();
		}
		entity.stopTask();
		texture.getTexture().dispose();
	}
	
}
