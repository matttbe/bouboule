package be.ac.ucl.lfsab1509.bouboule.game.screen;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class AboutScreen extends AbstractScreen {

	public AboutScreen() {
		super(false); // without music delay
	}

	@Override
	public void show() {
		super.show();

		// Note: no accent with the default font and multiple spaces or \t are not working
		String cAboutText =
				  "App's name:\n"
				+ " Bouboule\n\n"
				+ "Authors:\n"
				+ " Baerts Matthieu, Remy Baptiste, Van Wallendael "
					+ "Nicolas and Verhaeghe Hélène\n\n"
				+ "Graphisms:\n"
				+ " Van Wallendael Julien\n\n"
				+ "Music and sounds:\n"
				+ " Adans Paschal (adanspaschal@gmail.com) and"
					+ " Klez (The gateway - SpaceShip level)\n\n"
				+ "Credits & License:\n"
				+ " Bouboule was developed during the course"
					+ " FSAB1509 - Projet Informatique (Yves Deville, Université"
					+ " Catholique de Louvain, Belgium), 2012–2013.\nOpen-Source"
					+ " GPL3 license and developed with LibGDX (Free and"
					+ " Open-Source game development framework under Apache"
					+ " License 2.0).";
		int iVersion = Gdx.app.getVersion();
		if (iVersion > 0)
			cAboutText += "\n\nVersion: " + iVersion;
		Label label = new Label(cAboutText, getSkin(), "default", Color.WHITE);

		// not on the border
		label.setWidth(GlobalSettings.APPWIDTH - 20);
		label.setHeight(GlobalSettings.APPHEIGHT - 20);
		label.setPosition(10, 10);

		label.setWrap(true); // return to the next lines

		label.setFontScale(1, 1); // avoid transformations
		label.setScale(1);

		// filters
		// label.getStyle().font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// label.setFillParent(true); used all the screen: too large

		//Set Background
		addBackGround("drawable-xhdpi/settings_blank.jpg");

		stage.addActor(label);

		//Create all Buttons
		addBackButton(false);
	}
}
