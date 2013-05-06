package be.ac.ucl.lfsab1509.bouboule.game.screen;

import be.ac.ucl.lfsab1509.bouboule.game.entity.Entity;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.EndGameListener;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GameLoop;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class MyGestureListener implements GestureListener {
	
	public static final String CHEATER_NAME = "Deville";

	@Override
	public boolean touchDown(final float x, final float y, final int pointer, final int button) {
		return false;
	}

	@Override
	public boolean tap(final float x, final float y, final int count, final int button) {
		if (GlobalSettings.PROFILE.getName().equals(CHEATER_NAME)) {
			GameLoop.bonus(true);
		}
		return false;
	}

	@Override
	public boolean longPress(final float x, final float y) {
		if (GlobalSettings.PROFILE.getName().equals(CHEATER_NAME)) {
			GameLoop.iBonus = (GameLoop.iBonus + 1) % (Entity.BonusType.values().length + 2);
		}
		return false;
	}

	@Override
	public boolean fling(final float velocityX, final float velocityY, final int button) {	
		return false;
	}

	@Override
	public boolean pan(final float x, final float y, final float deltaX, final float deltaY) {
		return false;
	}

	@Override
	public boolean zoom(final float initialDistance, final float distance) {	
		return false;
	}

	@Override
	public boolean pinch(final Vector2 initialPointer1, final Vector2 initialPointer2,
			final Vector2 pointer1, final Vector2 pointer2) {
		
		Gdx.app.log("Touch", "Pinch event " + GlobalSettings.GAME.getTimer().isRunning());
		
		if (GlobalSettings.PROFILE.getName().equals(CHEATER_NAME)
				&& GlobalSettings.GAME.getTimer().isRunning() 
				// prevent double stop, double level up, etc.
				&& GlobalSettings.PROFILE.getScore() < GlobalSettings.PROFILE.getNewInitScore()) { 
			// it seems this method is called just after the resume... 
			// wait at least one second before cheating ;)
			EndGameListener.winGame(); // cheater!!!!
		}
			
		return false;
	}

}
