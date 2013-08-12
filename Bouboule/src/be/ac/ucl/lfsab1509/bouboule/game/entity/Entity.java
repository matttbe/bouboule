package be.ac.ucl.lfsab1509.bouboule.game.entity;

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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Timer;

import be.ac.ucl.lfsab1509.bouboule.game.ai.AI;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

public class Entity {

	// Constant for all the Generic Body's
	public static final short PLAYER = 1;
	public static final short MONSTER = -1;
	public static final short SCENERY = 0;
	public static final short OBSTACLE = -2;
	public static final short BONUS = -3;

	// Bonus options
	public static enum BonusType {
		LIVE_UP, SPEED_HIGH, SPEED_LOW, POINT, WEIGHT_HIGH, WEIGHT_LOW,
		ELASTICITY_HIGH, ELASTICITY_LOW, INVINCIBLE, INVISIBLE, INVERSE,
		TIME_UP, TIME_DOWN
	};

	private short entity; // Store the Constant of the Generic Body's
	private BonusType bonus; // Store the bonus option
	private boolean isAlive; // isAlive =)
	private Fixture fixture;

	private static final float SPEED_MULT_VALUE = 2f;
	private static final float DEFAULT_MULT_VALUE = 2f;
	private static final int TIMER_DEFAULT_TIME = 5;
	private static final int ADD_TIME_DEFAULT = 15;

	private Timer timer;

	/**
	 * Constructor for a Bonus Entity
	 * 
	 * @param type
	 *            = Constant of the Generic Body's
	 * @param live
	 *            = Is the body alive
	 * @param bonusType
	 *            = Bonus Option
	 * 
	 *            public Entity (final short type, final boolean live, final
	 *            short bonusType)
	 */
	public Entity(final short type, final boolean live,
			final BonusType bonusType) {

		this.entity = type;
		this.bonus = bonusType;
		this.isAlive = live;
	}

	/**
	 * Constructor for a Bouboule Object
	 * 
	 * @param type
	 *            = Constant of the Generic Body's
	 * @param live
	 *            = Is the body alive
	 * 
	 *            public Entity (final short type, final boolean live)
	 */
	public Entity(final short type, final boolean live) {

		this.entity = type;
		this.isAlive = live;
	}

	/**
	 * Constructor for a Arena Object
	 * 
	 * @param type
	 *            = Constant of the Generic Body's
	 * 
	 *            public Entity (final short type ) {
	 */
	public Entity(final short type) {

		this.entity = type;
	}

	/**
	 * Launch the right bonus
	 * 
	 * @param type
	 *            = Type of the object
	 * 
	 *            public void attributeBonus(final short type)
	 * @param fixture
	 */
	public void attributeBonus(final short type, Fixture fixture) {

		if (this.isAlive) {
			this.isAlive = false;
			this.fixture = fixture;

			Gdx.app.log("bonus", bonus + " " + (type == PLAYER));

			if (type == PLAYER)
				GlobalSettings.PROFILE.newBonus();

			// some bonus have differents effects if it's the AI or the player
			if (attributeBonusSpecialsCases(type))
				return;

			attributeBonusForAll(type);
		}
	}

	private boolean attributeBonusSpecialsCases(final short type) {

		if (type == PLAYER) {
			switch (this.bonus) {
			case LIVE_UP:
				GlobalSettings.PROFILE.addLifes(1);
				return true;
			case POINT:
				GlobalSettings.PROFILE
						.addScorePermanent(GlobalSettings.SCORE_BONUS);
				return true;

			case SPEED_HIGH:
				AI.FORCE_MAX_PLAYER *= SPEED_MULT_VALUE;
				resetSpeedBonus(type, TIMER_DEFAULT_TIME);
				return true;

			case SPEED_LOW:
				AI.FORCE_MAX_PLAYER /= SPEED_MULT_VALUE;
				resetSpeedBonus(type, TIMER_DEFAULT_TIME);
				return true;

			case INVERSE:
				inverse(true);
				resetBonus(type, TIMER_DEFAULT_TIME * 2);
				return true;

			case TIME_UP:
				GlobalSettings.PROFILE.addRemainingTime(ADD_TIME_DEFAULT);
				return true;

			case TIME_DOWN:
				GlobalSettings.PROFILE.addRemainingTime(ADD_TIME_DEFAULT * -1);
				return true;

			default:
				break;
			}
		} else { // AI
			switch (this.bonus) {
			case SPEED_HIGH:
				AI.FORCE_MAX_AI *= SPEED_MULT_VALUE;
				resetSpeedBonus(type, TIMER_DEFAULT_TIME);
				return true;

			case SPEED_LOW:
				AI.FORCE_MAX_AI /= SPEED_MULT_VALUE;
				resetSpeedBonus(type, TIMER_DEFAULT_TIME);
				return true;

			case INVERSE:
				inverse(false);
				resetBonus(type, TIMER_DEFAULT_TIME * 2);
				return true;

			default:
				break;
			}
		}
		return false;
	}

	private void attributeBonusForAll(final short type) {
		switch (this.bonus) {
		case WEIGHT_HIGH:
			biggerWeight();
			resetBonus(type, TIMER_DEFAULT_TIME);
			break;
		case WEIGHT_LOW:
			lowerWeight();
			resetBonus(type, TIMER_DEFAULT_TIME);
			break;
		case ELASTICITY_HIGH:
			biggerElasticity();
			resetBonus(type, TIMER_DEFAULT_TIME);
			break;
		case ELASTICITY_LOW:
			lowerElasticity();
			resetBonus(type, TIMER_DEFAULT_TIME);
			break;
		case INVINCIBLE:
			invincible(true);
			resetBonus(type, TIMER_DEFAULT_TIME);
			break;
		case INVISIBLE:
			invisible(true);
			resetBonus(type, (TIMER_DEFAULT_TIME + 1) / 2);
			break;
		default:
			break;
		}
	}

	private void resetSpeedBonus(final short type, final int time) {
		Timer.Task task = new Timer.Task() {
			@Override
			public void run() {
				Gdx.app.log("bonus", "reset " + bonus + " " + type);

				if (type == PLAYER) {
					if (bonus == BonusType.SPEED_HIGH) {
						AI.FORCE_MAX_PLAYER /= SPEED_MULT_VALUE;

					} else {
						AI.FORCE_MAX_PLAYER *= SPEED_MULT_VALUE;
					}
				} else {
					if (bonus == BonusType.SPEED_HIGH) {
						AI.FORCE_MAX_AI /= SPEED_MULT_VALUE;

					} else {
						AI.FORCE_MAX_AI *= SPEED_MULT_VALUE;
					}
				}
			}

		};// Program a task to reset the Speed to initial value;

		timer = new Timer();
		timer.scheduleTask(task, time);
	}

	private void resetBonus(final short type, final int time) {
		Timer.Task task = new Timer.Task() {
			@Override
			public void run() {
				Gdx.app.log("bonus", "reset " + bonus);
				switch (bonus) {
				case WEIGHT_HIGH:
					lowerWeight();
					break;
				case WEIGHT_LOW:
					biggerWeight();
					break;
				case ELASTICITY_HIGH:
					biggerElasticity();
					break;
				case ELASTICITY_LOW:
					lowerElasticity();
					break;
				case INVINCIBLE:
					invincible(false);
					break;
				case INVISIBLE:
					invisible(false);
					break;
				case INVERSE:
					inverse(true);

				default:
					break;
				}
			}

		};// Program a task to reset the Speed to initial value;

		timer = new Timer();
		timer.scheduleTask(task, time);
	}

	public void stopTask() {

		if (this.timer != null)
			this.timer.clear();

		// revert axe if it's inverted
		AI.setNormalOrientation();

		Gdx.app.log("Timer", "Stopped the Timer if needed");
	}

	public short getEntity() {
		return entity;
	}

	public void setEntity(final short entity) {
		this.entity = entity;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(final boolean isAlive) {
		this.isAlive = isAlive;
	}

	private void increaseWeight(final float iMult) {
		Gdx.app.log("bonus",
				"increase: " + iMult + " was: " + fixture.getDensity());
		fixture.setDensity(fixture.getDensity() * iMult);
		fixture.getBody().resetMassData();
	}

	private void biggerWeight() {
		increaseWeight(DEFAULT_MULT_VALUE);
	}

	private void lowerWeight() {
		increaseWeight(1f / DEFAULT_MULT_VALUE);
	}

	private void increaseElasticity(final float iMult) {
		fixture.setRestitution(fixture.getRestitution() * iMult);
	}

	private void biggerElasticity() {
		increaseElasticity(DEFAULT_MULT_VALUE);
	}

	private void lowerElasticity() {
		increaseElasticity(1f / DEFAULT_MULT_VALUE);
	}

	private void invincible(final boolean bInvincible) {
		Gdx.app.log("bonus",
				"Invincible: " + bInvincible + " " + fixture.isSensor());
		fixture.setSensor(bInvincible);
	}

	private void invisible(final boolean bInvisible) {
		Sprite userData = (Sprite) fixture.getUserData();
		if (userData != null) // check if the game has been stopped
			userData.setColor(1f, 1f, 1f, bInvisible ? .025f : 1f);
	}

	private void inverse(boolean bInverseAxe) {
		Sprite userData = (Sprite) fixture.getUserData();
		if (userData != null) {
			((Sprite) fixture.getUserData()).rotate90(true);
			((Sprite) fixture.getUserData()).rotate90(true);
			if (bInverseAxe)
				AI.inverse();
		}
	}
}
