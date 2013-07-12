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


import be.ac.ucl.lfsab1509.bouboule.game.body.Bonus;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GlobalScreen extends AbstractScreen {

	private static final String FONT_TITLE = "chinyen-font";
	private static final float  FONT_SCALE = .82f;
	private static final float  FONT_SCALE_OSAKA = 1.6384f; // TODO: scale => 1 => we need a bigger font

	private CheckBox soundCheckBox;
	private CheckBox rotationCheckBox;
	private Slider sensitivitySlider;

	int iX = 33, iY = 1900;

	public GlobalScreen() {
		super(false); // without music delay
	}

	@Override
	public void show() {
		super.show();

		// Set Background
		addBackGround("drawable-xhdpi/settings_blank.jpg");

		addSoundOptions();
		iY -= 238;

		addRotationOptions();
		iY -= 238;

		addSensitivityOptions();
		iY -= 238;

		addBonusImages();
		iY -= 238;

		// BACK
		addBackButton(false);
	}

	// _________________________________ SOUND

	private void addSoundOptions() {
		addLabel("SOUND", FONT_TITLE, FONT_SCALE, Color.WHITE, iX, iY)
				.setTouchable(null);

		soundCheckBox = addCheckBox("Music", ! GlobalSettings.SOUND_IS_MUTED,
				iX * 2, iY - 33);
		soundCheckBox.getLabel().setFontScale(FONT_SCALE_OSAKA); // TODO => rm when osaka will be bigger
		soundCheckBox.addListener(soundClickListener);
	}

	private ClickListener soundClickListener = new ClickListener() {
		public void clicked(InputEvent event, float x, float y) {
			Gdx.app.log("SCREEN", "sound click");
			GlobalSettings.PROFILE_MGR.getProfileGlobal().changeSoundSettings(
					!soundCheckBox.isChecked());

			if (soundCheckBox.isChecked()) // was muted, we need music
				getMusic().play();
			else
				getMusic().stop();
		}
	};

	// _________________________________ ROTATION

	private void addRotationOptions() {
		addLabel("ROTATION", FONT_TITLE, FONT_SCALE, Color.WHITE, iX, iY)
			.setTouchable(null);

		rotationCheckBox = addCheckBox("Rotation of the balls",
				!GlobalSettings.FIXED_ROTATION, iX * 2, iY - 33);
		rotationCheckBox.getLabel().setFontScale(FONT_SCALE_OSAKA); // TODO => rm when osaka will be bigger
		rotationCheckBox.addListener(rotationClickListener);
	}

	private ClickListener rotationClickListener = new ClickListener() {
		public void clicked(InputEvent event, float x, float y) {
			GlobalSettings.PROFILE_MGR.getProfileGlobal().changeFixedRotation(
					!rotationCheckBox.isChecked());
		}
	};

	// _________________________________ SENSITIVITY

	private void addSensitivityOptions() {
		addLabel("SENSITIVITY", FONT_TITLE, FONT_SCALE, Color.WHITE, iX, iY)
				.setTouchable(null);

		sensitivitySlider = new Slider(
				GlobalSettings.SENSITIVITY_MIN,
				GlobalSettings.SENSITIVITY_MAX,
				(GlobalSettings.SENSITIVITY_MAX - GlobalSettings.SENSITIVITY_MIN) / 50,
				false, getSkin(), "default");
		sensitivitySlider.setX(GlobalSettings.APPWIDTH / 8);
		sensitivitySlider.setY(iY - 33);
		sensitivitySlider.setWidth(GlobalSettings.APPWIDTH - GlobalSettings.APPWIDTH / 4);
		sensitivitySlider.setValue(GlobalSettings.SENSITIVITY);
		sensitivitySlider.addListener(sensitivityClickListener);
		stage.addActor(sensitivitySlider);
	}

	private ChangeListener sensitivityClickListener = new ChangeListener() {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			GlobalSettings.PROFILE_MGR.getProfileGlobal()
					.changeSensibilitySettings(
							(int) sensitivitySlider.getValue());
		}
	};

	// ________________________________ BONUS

	private void addBonusImages() {
		addLabel("BONUS", FONT_TITLE, FONT_SCALE, Color.WHITE, iX, iY)
				.setTouchable(null);

		iX = 180;
		iY -= 53;
		for (String[] cBonus : Bonus.bonusInfo) {
			addImage(cBonus[0], iX, iY);
			addLabel(cBonus[1], "osaka-font", FONT_SCALE_OSAKA, Color.WHITE, iX + 100, iY + 15);
			iY -= 90;
		}
	}
}
