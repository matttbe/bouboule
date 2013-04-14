package be.ac.ucl.lfsab1509.bouboule.game.entity;

import com.badlogic.gdx.Gdx;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

public class Entity {

	//Constant for all the Generic Body's
	public static final short PLAYER 	=  1;
	public static final short MONSTER 	= -1;
	public static final short SCENERY 	=  0;
	public static final short OBSTACLE 	= -2;
	public static final short BONUS 	= -3;

	//Bonus options
	public static final short BONUS_LIVE  = -31;
	public static final short BONUS_SPEED = -32;
	public static final short BONUS_POINT = -33;

	
	private short 	entity;					//Store the Constant of the Generic Body's
	private short 	bonus;					//Store the bonus option
	private boolean isAlive;				//isAlive =)

	/**
	 * Constructor for a Bonus Entity
	 * 
	 * @param type = Constant of the Generic Body's
	 * @param live = Is the body alive
	 * @param bonusType = Bonus Option
	 * 
	 * 	public Entity (final short type, final boolean live, final short bonusType)
	 */
	public Entity (final short type, final boolean live, final short bonusType) {

		this.entity = type;
		this.bonus  = bonusType; 
		this.isAlive= live;
	}

	/**
	 * Constructor for a Bouboule Object
	 * 
	 * @param type = Constant of the Generic Body's
	 * @param live = Is the body alive
	 * 
	 * 	public Entity (final short type, final boolean live) 
	 */
	public Entity (final short type, final boolean live) {

		this.entity = type;
		this.isAlive= live;
	}

	
	/**
	 * Constructor for a Arena Object
	 * 
	 * @param type = Constant of the Generic Body's
	 * 
	 * public Entity (final short type ) {
	 */
	public Entity (final short type ) {

		this.entity = type;
	}

	/**
	 * Launch the right bonus
	 * 
	 * @param type = Type of the object
	 * 
	 * public void attributeBonus(final short type)
	 */
	public void attributeBonus(final short type) {

		if (this.isAlive) {
			
			if (type == PLAYER) {

				switch (this.bonus) {
				case BONUS_LIVE:
					Gdx.app.log("heart","Adding 1 life to player");
					GlobalSettings.PROFILE.addLifes(1);
					break;
				case BONUS_POINT:
					Gdx.app.log("star","Adding 100 point to player");
					//GlobalSettings.PROFILE.addScore(100);				
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
