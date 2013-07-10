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


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ParamScreen extends AbstractScreen {

	public ParamScreen() {
		super(false); // without music delay
	}

	@Override
	public void show() {
		super.show();

		//Set Background
		
		addBackGround("drawable-xhdpi/settings.jpg");
		
		//Create all Buttons
		Button userButton  	= createButton("transparent", 690, 250, 312, 1150);
		Button globalButton = createButton("transparent", 690, 250, 312, 887);
		Button aboutButton 	= createButton("transparent", 690, 250, 312, 615);
		
		
		userButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "clickUser " + x + ", " + y);
				setScreenWithFading(new UserScreen());
			}
		});
		
		globalButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "clickGlobal " + x + ", " + y);
				setScreenWithFading(new GlobalScreen());
			}
		});
		
		aboutButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "clickAbout " + x + ", " + y);
				setScreenWithFading(new AboutScreen());
			}
		});

		addBackButton(true);
	}
}
