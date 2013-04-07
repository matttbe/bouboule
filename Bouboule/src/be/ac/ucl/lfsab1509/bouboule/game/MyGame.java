package be.ac.ucl.lfsab1509.bouboule.game;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.Profile;
import be.ac.ucl.lfsab1509.bouboule.game.screen.ScreenGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class MyGame extends Game {

	public ScreenGame screenGame;
	private Sound hitSound;
	private Sound winSound;
	private Sound looseSound;

	@Override
	public void create () {
		Gdx.app.log ("Matth", "Game: Create");
		hitSound = Gdx.audio.newSound(Gdx.files.internal("music/drop.mp3")); // TODO: find sound and use parameters?
		winSound = Gdx.audio.newSound(Gdx.files.internal("music/drop.mp3"));
		looseSound = Gdx.audio.newSound(Gdx.files.internal("music/drop.mp3"));

		createProfile ();

		screenGame = new ScreenGame();
		setScreen (screenGame); // 
	}

	public void createProfile () {
		if (GlobalSettings.PROFILE == null)
			GlobalSettings.PROFILE = new Profile (GlobalSettings.PROFILE_NAME);
	}

	@Override
	public void dispose () {
		super.dispose ();
		hitSound.dispose ();
		winSound.dispose ();
		looseSound.dispose ();
	}
	
	public void hitSound () {
		hitSound.play ();
	}
	
	public void winSound () {
		winSound.play ();
	}
	
	public void looseSound () {
		looseSound.play ();
	}

}
