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
 *    H??l??ne Verhaeghe <helene.verhaeghe@student.uclouvain.be>
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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class LooseScreen extends AbstractScreen {

	private BitmapFont fontOsaka;

	public LooseScreen() {
		super(true);
	}

	@Override
	public void show() {
		super.show();

		// Set Font

		fontOsaka = new BitmapFont(Gdx.files.internal("fonts/Osaka/Osaka.fnt"),
				Gdx.files.internal("fonts/Osaka/Osaka.png"), false);

		fontOsaka.setColor(0f, 0f, 0f, 0.85f);

		// Set Background

		addBackGround("drawable-xhdpi/you_lose.jpg");

		// Set Lives

		switch (GlobalSettings.PROFILE.getNbLifes()) {
		case 3:
			addBackGround("drawable-xhdpi/coeur3.png");
			break;
		case 2:
			addBackGround("drawable-xhdpi/coeur2.png");
			break;
		case 1:
			addBackGround("drawable-xhdpi/coeur1.png");
			break;

		default:
			break;
		}

		// Create all Buttons - Play Button

		Button retryButton = createButton("transparent", 290, 90, 63, 608);
		Button menuButton = createButton("transparent", 290, 90, 448, 608);

		retryButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickStart " + x + ", " + y);
				GlobalSettings.GAME.setScreenGame();
			}
		});

		menuButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickParam " + x + ", " + y);
				GlobalSettings.MENUS.launchInitMenu();
			}
		});

	}

	@Override
	public void render(float delta) {
		super.render(delta);

		getBatch().begin();
		fontOsaka.draw(getBatch(),
				Integer.toString(GlobalSettings.PROFILE.getScore()), 200, 745);
		getBatch().end();

	}
}
