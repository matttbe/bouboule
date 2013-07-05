package be.ac.ucl.lfsab1509.bouboule.game.screen;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class AboutScreen extends AbstractScreen {

	public AboutScreen() {
		super(false); // without music delay
	}

	@Override
	public void show() {
		super.show();

		Label label = new Label("Ceci est un super test :)\nSur 2 lignes\net un super super super super super super super super super super super super super super super super super super super long text", getSkin(), "default", Color.WHITE);
		//label.setFontScale(.5f);
		label.setWidth(GlobalSettings.APPWIDTH - 20);
		label.setHeight(GlobalSettings.APPHEIGHT - 20);
		label.setPosition(10, 10);
		label.setWrap(true);
		// label.setFillParent(true); used all the screen: too large

		//Set Background
		addBackGround("drawable-xhdpi/settings_blank.jpg");

		// addLabel("Ceci est un super test :)\nSur 2 lignes", "oswald-font", .5f, Color.WHITE, 10, 10);
		stage.addActor(label);

		//Create all Buttons
		addBackButton(false);
	}
}
