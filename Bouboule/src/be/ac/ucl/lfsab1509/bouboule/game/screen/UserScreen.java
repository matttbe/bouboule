package be.ac.ucl.lfsab1509.bouboule.game.screen;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class UserScreen extends AbstractScreen{

	private String font = "default";
	private float fontScale = 3f;
	
	Button resetButton;
	public UserScreen() {
		super(false);
	}

	@Override
	public void show() {
		super.show();

		//Set Background
		addBackGround("drawable-xhdpi/settings_blank.jpg");
		
		addLabel("Select User:", font, fontScale, Color.WHITE,40,1150);
		addLabel("Create new user:", font, fontScale, Color.WHITE,40,950);
		addLabel("Chose your Bouboule:", font, fontScale, Color.WHITE,40,750);
		addLabel("Show the tutorial:", font, fontScale, Color.WHITE,40,550);
		addLabel("Reset your game!",font,fontScale,Color.WHITE,30,200);
		resetButton = createButton("default", 350,100 , 30,200);
		Gdx.app.log("LN", "width : "+ GlobalSettings.APPWIDTH + " height : "+ GlobalSettings.APPHEIGHT);
		
		
		
		
		addBackButton(false);
		
		resetButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "resetButton " + x + ", " + y);
				//TODO : reset
			}
		});
	}
}