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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GlobalScreen extends AbstractScreen {

	private static final String FONT_TITLE = "chinyen-font";
	private static final float  FONT_SCALE = .5f * GlobalSettings.HD;

	private CheckBox soundCheckBox;
	/*private CheckBox rotationCheckBox;
	private Slider sensitivitySlider;*/

	private int iX, iY;

	public GlobalScreen() {
		super(false); // without music delay
		if (GlobalSettings.ISHD) {
			iX = 33;
			iY = 1870;
		}
		else {
			iX = 20;
			iY = 1100;
		}
	}

	@Override
	public void show() {
		super.show();

		// Set Background
		addBackGroundShift("GdxMenus/settings/settingsbg.jpg");

		int iLessY = (int) (155 * GlobalSettings.HD);

		addSoundOptions();
		iY -= iLessY;

		/* Not so useful...
		addRotationOptions();
		iY -= iLessY;

		addSensitivityOptions();
		iY -= iLessY;
		*/

		addBonusImages();

		// BACK
		addBackButton(false);
	}

	// _________________________________ SOUND

	private void addSoundOptions() {
		addLabel("SOUND", FONT_TITLE, FONT_SCALE, Color.WHITE, iX, iY)
				.setTouchable(null);

		soundCheckBox = addCheckBox("Music", ! GlobalSettings.SOUND_IS_MUTED,
				iX, iY + (GlobalSettings.ISHD ? -33 : 5));
		soundCheckBox.getLabel().setFontScale(GlobalSettings.HD); // TODO => rm when osaka will be bigger
		soundCheckBox.setX((int) (GlobalSettings.APPWIDTH / 2 // center
				- soundCheckBox.getWidth() / 2));
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

/*
	private void addRotationOptions() {
		addLabel("ROTATION", FONT_TITLE, FONT_SCALE, Color.WHITE, iX, iY)
			.setTouchable(null);

		rotationCheckBox = addCheckBox("Rotation of the balls",
				!GlobalSettings.FIXED_ROTATION,
				iX * 2, iY + (GlobalSettings.ISHD ? -33 : 5));
		rotationCheckBox.getLabel().setFontScale(GlobalSettings.HD); // TODO => rm when osaka will be bigger
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
		sensitivitySlider.setY(iY + (GlobalSettings.ISHD ? -33 : 10));
		sensitivitySlider.setWidth(GlobalSettings.APPWIDTH * 3 / 4);
		sensitivitySlider.setValue(GlobalSettings.SENSITIVITY);
		sensitivitySlider.setAnimateDuration(.250f);
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
*/

	// ________________________________ BONUS

	private void addBonusImages() {
		addLabel("BONUS", FONT_TITLE, FONT_SCALE, Color.WHITE, iX, iY)
				.setTouchable(null);

		if (GlobalSettings.ISHD) {
			iX = 180;
			iY -= 53;
		}
		else {
			iX = 125;
			iY -= 20;
		}
		for (String[] cBonus : Bonus.bonusInfo) {
			addImage(cBonus[0], iX, iY);
			addLabel(cBonus[1], "osaka-font", GlobalSettings.HD, // TODO => set "1f" when osaka will be bigger
					Color.WHITE, iX + (GlobalSettings.ISHD ? 100 : 80), iY + 15);
			iY -= 60 * GlobalSettings.HD;
		}
	}
}
