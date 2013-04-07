package be.ac.ucl.lfsab1509.bouboule;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;

import android.content.Intent;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings.GameExitStatus;
import be.ac.ucl.lfsab1509.bouboule.game.menu.Menus;

public class MyAndroidMenus implements Menus {

	private AndroidApplication app;

	public MyAndroidMenus (AndroidApplication app) {
		this.app = app;
	}

	@Override
	public void launchInitMenu () {
		Intent intent = new Intent (app, Menu.class);
		app.startActivityForResult (intent, MainActivity.CODE_MENU_ACTIVITY);
	}


	// TODO: remove this toast when the score will be displayed somewhere else
	/*private void displayInfo () {
		Context context = app.getApplicationContext ();
		CharSequence text = "Score: " + GlobalSettings.PROFILE.getScore ()
				+ "\nLifes: " + GlobalSettings.PROFILE.getNbLifes ()
				+ "\nLevel: " + GlobalSettings.PROFILE.getLevel ()
				+ "\n" + (GlobalSettings.PROFILE.isNewHighScore () ? "NEW " : "")
				+ "HighScore: " + GlobalSettings.PROFILE.getHighScore ();
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText (context, text, duration);
		toast.show ();
	}*/

	@Override
	public void launchEndGameMenu () {
		Gdx.app.log ("Matth", "MainActivity: pause: " + GlobalSettings.GAME_EXIT);
		Intent intent = null;
		switch (GlobalSettings.GAME_EXIT)
		{
			case NONE :
				return;
			case WIN :
				intent = new Intent (app, VictoryActivity.class);
				break;
			case LOOSE :
				intent = new Intent (app, LoosingActivity.class);
				break;
			case GAMEOVER :

				intent = new Intent (app, GameOverActivity.class);
				break;
		}

		app.startActivityForResult (intent, MainActivity.CODE_END_GAME);
		app.overridePendingTransition (android.R.anim.fade_in,
				android.R.anim.fade_out);
		GlobalSettings.GAME_EXIT = GameExitStatus.NONE;
	}

}
