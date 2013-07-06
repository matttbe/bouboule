package be.ac.ucl.lfsab1509.bouboule.game.screen;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.lang.String;

public class GlobalScreen extends AbstractScreen{

	public GlobalScreen() {
		super(false);
	}

	@Override
	public void show() {
		super.show();

		//Set Background
		addBackGround("drawable-xhdpi/settings_blank.jpg");
		
		Label label = new Label("text", getSkin(), "default", Color.WHITE);
		label.setWidth(GlobalSettings.APPWIDTH - 20);
		label.setHeight(GlobalSettings.APPHEIGHT - 20);
		label.setPosition(10, 10);
		label.setWrap(true);
		stage.addActor(label);
		
		
		addBackButton(false);
	}
}
