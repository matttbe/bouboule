package be.ac.ucl.lfsab1509.bouboule.game.screen;

import com.badlogic.gdx.Gdx;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

public class ScreenMainMenu extends AbstractScreen {
	
	public ScreenMainMenu() {
		super(false);
	}

	@Override
	public void show() {
		super.show();
		Gdx.app.log("SCREEN", "Main => GAME");
		GlobalSettings.GAME.setScreenGameResume();
		GlobalSettings.GAME.setScreenGame();
		GlobalSettings.GAME.setScreenGameResume();
	}
}
