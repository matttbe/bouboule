package be.ac.ucl.lfsab1509.bouboule.game.menu;

import com.badlogic.gdx.Gdx;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.screen.MenuScreen;
import be.ac.ucl.lfsab1509.bouboule.game.screen.ScreenMainMenu;

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
 *    Helene Verhaeghe <helene.verhaeghe@student.uclouvain.be>
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

public class GdxMenus implements Menus {

	@Override
	public void launchInitMenu() {
		Gdx.app.log ("Matth", "GdxMenus: InitMenu: " + GlobalSettings.GAME_EXIT);
		GlobalSettings.GAME.setScreen(new MenuScreen(true));
	}

	@Override
	public void launchEndGameMenu() {
		Gdx.app.log ("Matth", "GdxMenus: EndGameMenu: " + GlobalSettings.GAME_EXIT);
		GlobalSettings.GAME.setScreenGamePause();
		switch (GlobalSettings.GAME_EXIT)
		{
			case NONE :
				return;
			case WIN :
				GlobalSettings.GAME.winSound();
				break;
			case LOOSE :
				GlobalSettings.GAME.looseSound();
				break;
			case GAMEOVER_LOOSE :
				GlobalSettings.GAME.looseSound();
				break;
			case GAMEOVER_END :
				GlobalSettings.GAME.winSound();
				break;
		}
		GlobalSettings.GAME.setScreen(new MenuScreen(true));
	}

	@Override
	public void launchPauseMenu() {
		Gdx.app.log ("Matth", "GdxMenus: PauseMenu: " + GlobalSettings.GAME_EXIT);
		GlobalSettings.GAME.setScreen(new ScreenMainMenu());
	}

}
