package be.ac.ucl.lfsab1509.bouboule.game.entity;

import com.badlogic.gdx.Gdx;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

public class Entity {

	public static final short PLAYER 	=  1;
	public static final short MONSTER 	= -1;
	public static final short SCENERY 	=  0;
	public static final short OBSTACLE 	= -2;
	public static final short BONUS 	= -3;

	public static final short BONUS_LIVE  = -31;
	public static final short BONUS_SPEED = -32;
	public static final short BONUS_POINT = -33;


	private short 	entity;
	private short 	bonus;
	private boolean isAlive;

	public Entity (final short type, final boolean live, final short bonusType) {

		this.entity = type;
		this.bonus  = bonusType; 
		this.isAlive= live;
	}


	public Entity (final short type, final boolean live) {

		this.entity = type;
		this.isAlive= live;
	}

	public Entity (final short type ) {

		this.entity = type;
	}

	public void attributeBonus(final short type) {

		if (this.isAlive) {
			
			if (type == PLAYER) {

				switch (this.bonus) {
				case BONUS_LIVE:
					Gdx.app.log("heart","Adding 1 life to player");
					GlobalSettings.PROFILE.addLifes(1);
					break;
				case BONUS_POINT:
					GlobalSettings.PROFILE.addScore(100);				
					break;

				case BONUS_SPEED:

					break;

				default:
					break;
				}
			}

			this.isAlive = false;
		}
	}

	public short getEntity() {
		return entity;
	}

	public void setEntity(short entity) {
		this.entity = entity;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public short getBonus() {
		return bonus;
	}

	public void setBonus(short bonus) {
		this.bonus = bonus;
	}

}
