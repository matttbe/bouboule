package be.ac.ucl.lfsab1509.bouboule.game.screen;

import be.ac.ucl.lfsab1509.bouboule.game.entity.Entity;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.EndGameListener;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GameLoop;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class MyGestureListener implements GestureListener {
	
	private static final String CHEATER_NAME = "Deville";

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (GlobalSettings.PROFILE.getName().equals(CHEATER_NAME))
			GameLoop.bonus (true);
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		if (GlobalSettings.PROFILE.getName().equals(CHEATER_NAME))
			GameLoop.iBonus = (GameLoop.iBonus + 1) % Entity.BonusType.values ().length;
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {	
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {	
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		
		Gdx.app.log("Touch","Pinch event " + GlobalSettings.GAME.getTimer ().isRunning ());
		
		if (GlobalSettings.PROFILE.getName().equals(CHEATER_NAME)
				&& GlobalSettings.GAME.getTimer ().isRunning () // prevent double stop, double level up, etc.
				&& GlobalSettings.PROFILE.getScore () < GlobalSettings.PROFILE.getNewInitScore ()) { // it seems this method is called just after the resume... wait at least one second before cheating ;)
			EndGameListener.winGame (); // cheater!!!!
		}
			
		return false;
	}

}
