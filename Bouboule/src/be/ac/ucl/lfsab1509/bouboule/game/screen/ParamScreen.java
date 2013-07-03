package be.ac.ucl.lfsab1509.bouboule.game.screen;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ParamScreen extends AbstractScreen {

	public ParamScreen(boolean bMusicNeedsDelay) {
		super(bMusicNeedsDelay);
	}

	@Override
	public void show() {
		super.show();

		//Set Background
		
		addBackGround("drawable-xhdpi/settings.jpg");
		
		//Create all Buttons - Play Button
		
		Button userButton  = createButton("transparent", 430, 160, 200, 725);
		Button globalButton = createButton("transparent", 430, 160, 200, 555);
		Button aboutButton = createButton("transparent", 430, 160, 200, 385);
		
		
		userButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "clickStart " + x + ", " + y);
				//TODO: setScreen(new UserScreen())
			}
		});
		
		globalButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "clickParam " + x + ", " + y);
				//TODO: setScreen(new GlobalScreen());
			}
		});
		
		aboutButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "clickScore " + x + ", " + y);
				//TODO: setScreen(new AboutScreen()); // or something else
			}
		});

	}
}
