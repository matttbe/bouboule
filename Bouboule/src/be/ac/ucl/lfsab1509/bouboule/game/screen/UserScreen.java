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
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class UserScreen extends AbstractScreen{

	private String font = "chinyen-font";
	private float fontScale = 0.3f;
	
	TextButton resetButton;
	Button leftButton;
	Button rightButton;
	CheckBox tutorialCheck;
	TextField newUserText;
	TextButton createButton;
	SelectBox choseBox;
	
	public UserScreen() {
		super(false);
	}

	@Override
	public void show() {
		super.show();

		//Set Background
		addBackGround("drawable-xhdpi/settings_blank.jpg");
		
		addLabel("SELECT  USER", font, fontScale, Color.WHITE,40,1050);
		addLabel("CREATE  NEW  USER", font, fontScale, Color.WHITE,40,900);
		addLabel("CHOSE  YOUR  BOUBOULE", font, fontScale, Color.WHITE,40,700);
		resetButton = createTButton("default", 300,100 , 30,200,"Reset your game!");
		
		addImage("boub/small/boub_basic.png",30,500);
		leftButton = createButton("default",100,100,30,500);
		
		addImage("boub/small/boub_basic.png",530,500);
		rightButton = createButton("default",100,100,530,500);
		
		tutorialCheck = addCheckBox("Show the tutorial!", true, 40,300);
		
		newUserText = addTextField("", (int) GlobalSettings.APPWIDTH - (2*40 + 70+10), 70, 40, 850);
		createButton = createTButton("default",70,70,(int) GlobalSettings.APPWIDTH - (40 + 70),850,"OK!");
		Object[] tab = {"un","deux","trois"};
		choseBox = addSelectBox(tab, (int) GlobalSettings.APPWIDTH - (2*40), 70, 40, 1000);
		
		
		
		addBackButton(false);
		
		
		choseBox.addCaptureListener(new InputListener(){
			
			public boolean handle(Event e){
				//TODO : select
				return false;
			}
			
		});
		
		
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
		tutorialCheck.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "tuto" + x + ", " + y);
				//TODO : tuto
			}
		});
		createButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log ("SCREEN", "rightButton " + x + ", " + y);
				//TODO : new user
			}
		});
	}
}