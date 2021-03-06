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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class LooseScreen extends AbstractScreen {

	public LooseScreen() {
		super(true);
	}

	@Override
	public void show() {
		super.show();

		// Set Background
		addBackGroundShift("GdxMenus/loose/loosebg.jpg");
		addBackGround("GdxMenus/loose/loosebuttons.png");

		// Set Lives

		switch (GlobalSettings.PROFILE.getNbLifes()) {
		case 3:
			addBackGround("GdxMenus/endgame/heart3.png");
			break;
		case 2:
			addBackGround("GdxMenus/endgame/heart2.png");
			break;
		case 1:
			addBackGround("GdxMenus/endgame/heart1.png");
			break;

		default:
			break;
		}

		// Create all Buttons - Play Button

		int iX, iY;
		Button retryButton, menuButton;
		if (GlobalSettings.ISHD) {
			iX = 328;
			iY = 1190;
			retryButton = createButton("transparent", 475, 147, 103, 996);
			menuButton = createButton("transparent", 475, 147, 734, 996);
		}
		else {
			iX = 200;
			iY = 703;
			retryButton = createButton("transparent", 290, 90, 63, 608);
			menuButton = createButton("transparent", 290, 90, 448, 608);
		}

		retryButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				setScreenWithFading(null);
			}
		});

		menuButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				setScreenWithFading(new MenuScreen());
			}
		});

		// Create all fonts
		addLabel(Integer.toString(GlobalSettings.PROFILE.getScore()),
				"osakaMid-font", 1f,
				new Color(0.2f, 0.188f, 0.094f, 1f),
				iX, iY + (GlobalSettings.ISHD ? 34 : 44));
	}
}
