package be.ac.ucl.lfsab1509.bouboule.game.screen;

/*
 * This file is part of Bouboule.
 * 
 * Copyright 2013 UCLouvain
 * 
 * Authors:
 *  * Group 7 - Course: http://www.uclouvain.be/en-cours-2013-lfsab1509.html
 *    Matthieu Baerts <matthieu.baerts@student.uclouvain.be>
 *    Baptiste Remy <baptiste.remy@student.uclouvain.be>
 *    Nicolas Van Wallendael <nicolas.vanwallendael@student.uclouvain.be>
 *    Hélène Verhaeghe <helene.verhaeghe@student.uclouvain.be>
 * 
 * Bouboule is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


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
					+ "Nicolas and Verhaeghe Helene\n\n"
				+ "Graphisms:\n"
				+ " Van Wallendael Julien\n\n"
				+ "Music and sounds:\n"
				+ " Adans Paschal (adanspaschal@gmail.com) and"
					+ " Klez (The gateway - SpaceShip level)\n\n"
				+ "Credits & License:\n"
				+ " Bouboule was developed during the course"
					+ " FSAB1509 - Projet Informatique (Yves Deville, Universite"
					+ " Catholique de Louvain, Belgium), 2012–2013.\nOpen-Source"
					+ " GPL3 license and developed with LibGDX (Free and"
					+ " Open-Source game development framework under Apache"
					+ " License 2.0).";
		int iVersion = Gdx.app.getVersion();
		if (iVersion > 0)
			cAboutText += "\n\nVersion: " + iVersion;
		Label label = new Label(cAboutText, getSkin(), "osaka-font", Color.WHITE);

		// not on the border
		label.setWidth(GlobalSettings.APPWIDTH - 20);
		label.setHeight(GlobalSettings.APPHEIGHT - 20);
		label.setPosition(10, 10);

		label.setWrap(true); // return to the next lines

		label.setFontScale(0.5f, 0.5f); // avoid transformations // TODO => set to 1
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
