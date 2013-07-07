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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class UserScreen extends AbstractScreen{

	private String font = "default";
	private float fontScale = 3f;
	
	TextButton resetButton;
	Button leftButton;
	Button rightButton;
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
		addLabel("Show the tutorial:", font, fontScale, Color.WHITE,40,350);
		resetButton = createTButton("default", 350,100 , 30,200,"Reset your game!");
		
		addImage("boub/normal/boub_basic.png",30,550);
		leftButton = createButton("default",100,100,30,550);
		
		addImage("boub/normal/boub_basic.png",530,550);
		rightButton = createButton("default",100,100,530,550);
		
		
		addBackButton(false);
		
		resetButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "resetButton " + x + ", " + y);
				//TODO : reset
			}
		});
		leftButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "leftButton " + x + ", " + y);
				//TODO : left
			}
		});
		rightButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "rightButton " + x + ", " + y);
				//TODO : right
			}
		});
	}
}