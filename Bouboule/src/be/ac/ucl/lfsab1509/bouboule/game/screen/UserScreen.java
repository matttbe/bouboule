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

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.EndGameListener;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.profile.BoubImages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UserScreen extends AbstractScreen {

	private static final String FONT_TITLE = "chinyen-font";
	private static final float  FONT_SCALE = .5f * GlobalSettings.HD;
	private static final char[] unacceptedChars = { '/', '\n', '\r', '\t', '\0',
		'\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':'};
	private static final String DEFAULT_NORMAL_BOUBOULE =
			BoubImages.BOUB_DIR_NORMAL + GlobalSettings.DEFAULT_BOUB_NAME
			+ BoubImages.BOUB_EXTENTION;

	private TextButton resetButton;
	private Image leftImage;
	private Image centerImage;
	private Image rightImage;
	private CheckBox soundCheckBox;
	private TextField newUserTextField;
	private SelectBox chooseUserBox;
	private boolean bIsRefreshing = false;

	private ArrayList<String> listProfiles;
	private ArrayList<String> boubouleFiles;
	private int iNbBoubouleFiles;
	private int iCurrentBoub;
	private TextureRegionDrawable leftBoubDraw, centerBoubDraw, rightBoubDraw;

	private int iX = (int) (20 * GlobalSettings.HD);
	private int iY = (int) (1115 * GlobalSettings.HD);

	public UserScreen() {
		super(false);
	}

	@Override
	public void show() {
		super.show();

		// Set Background
		addBackGroundShift("GdxMenus/settings/settingsbg.jpg");

		int iLessY = (int) (185 * GlobalSettings.HD);

		/* libGDX only supports the onScreenKeyboard of Android and the keyboard
		 * of the desktop. But this option is not so important.
		 * (And we can only have one user if we want to use Apple's Game Center)
		 */
		if (Gdx.app.getType() == ApplicationType.Android
				|| Gdx.app.getType() == ApplicationType.Desktop) {
			addChooseUserOptions();
			iY -= iLessY;

			addNewUserOptions();
			iY -= iLessY;
		}

		addChooseBoubouleOptions();
		iY -= centerImage.getHeight() + iLessY - (GlobalSettings.ISHD ? 75 : 0);

		addResetOptions();
		iY -= iLessY;

		addSoundOptions();

		addBackButton(false);
	}

	@Override
	public void dispose() {
		super.dispose();
		disposeBouboulesImages();
	}

	// called when selecting a existed/new user => only on Android/Desktop
	private void refreshScreen() {
		// Choose User
		bIsRefreshing = true; // avoid multiple refresh
		listProfiles = GlobalSettings.PROFILE_MGR.getAllProfilesAL();
		chooseUserBox.setItems(listProfiles.toArray());
		chooseUserBox.setSelection(listProfiles.indexOf(
				GlobalSettings.PROFILE.getName()));
		bIsRefreshing = false;
		// New User
		newUserTextField.setText("");
		// Bouboules
		setDefaultImages();
	}

	//_________________________________ CHOOSE USER

	private void addChooseUserOptions() {
		addLabel("SELECT  USER", FONT_TITLE, FONT_SCALE, Color.WHITE, iX, iY)
				.setTouchable(null);

		listProfiles = GlobalSettings.PROFILE_MGR.getAllProfilesAL();

		chooseUserBox = new SelectBox(listProfiles.toArray(), getSkin(), "default");
		chooseUserBox.setSelection(GlobalSettings.PROFILE.getName());
		chooseUserBox.setSize(GlobalSettings.APPWIDTH * 3 / 4,
				(int) (50 * GlobalSettings.HD));
		chooseUserBox.setX(GlobalSettings.APPWIDTH / 8);
		chooseUserBox.setY(iY + (GlobalSettings.ISHD ? -100 : -10));
		chooseUserBox.addListener(chooseUserListener);

		this.stage.addActor(chooseUserBox);
	}

	private ChangeListener chooseUserListener = new ChangeListener() {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			if (bIsRefreshing)
				return;
			String cNewUser = chooseUserBox.getSelection();
			if (! GlobalSettings.PROFILE.getName().equals(cNewUser)) { // new user
				Gdx.app.log("SCREEN", "Change user: was: "
						+ GlobalSettings.PROFILE.getName() + " - new: "
						+ cNewUser);
				GlobalSettings.PROFILE_MGR.changeProfile(cNewUser);
				refreshScreen();
				new Dialog("New User Selected", getSkin(), "default") {} // close the dialogue
						.text("Hello " + cNewUser + "!")
						.button("Close", null)
						.show(stage);
			}
		}
	};

	//_________________________________ NEW USER

	private void addNewUserOptions() {
		addLabel("CREATE  NEW  USER", FONT_TITLE, FONT_SCALE, Color.WHITE, iX, iY)
				.setTouchable(null);

		newUserTextField = new TextField("", getSkin(), "default");
		newUserTextField.setSize(GlobalSettings.APPWIDTH * 3 / 4,
				(int) (50 * GlobalSettings.HD));
		newUserTextField.setX(GlobalSettings.APPWIDTH / 8);
		newUserTextField.setY(iY + (GlobalSettings.ISHD ? -100 : -10));
		// text that will be drawn in the text field if no text has been entered
		newUserTextField.setMessageText("Enter a new user");
		newUserTextField.setTextFieldListener(newUserTextFieldListener);
		newUserTextField.setTextFieldFilter(newUserTextFilter);
		this.stage.addActor(newUserTextField);
	}

	private TextField.TextFieldListener newUserTextFieldListener =
			new TextField.TextFieldListener() {
		public void keyTyped(TextField textField, char key) {
			if (key == '\n') {
				textField.getOnscreenKeyboard().show(false);
				addNewUser();
			}
		}
	};

	private TextFieldFilter newUserTextFilter = new TextFieldFilter() {
		@Override
		public boolean acceptChar(TextField textField, char key) {
			for (char unacceptedChar : unacceptedChars) {
				if (unacceptedChar == key)
					return false;
			}
			return true;
		}
	};

	private String checkNewUser() {
		String cNewUser = newUserTextField.getText();
		if (cNewUser == null || cNewUser.isEmpty())
			return "Username is empty";
		if (cNewUser.equals(".") || cNewUser.equals(".."))
			return "Username can't be '.' or '..'";

		ArrayList<String> existedNames =
				GlobalSettings.PROFILE_MGR.getAllProfilesAndExceptions ();
		if (existedNames.contains (cNewUser))
			return "This username already exists!";

		return null;
	}

	private void addNewUser() {
		String error = checkNewUser();
		if (error == null) {
			GlobalSettings.PROFILE_MGR.createAndLoadNewProfile(
					newUserTextField.getText());
			refreshScreen();
			new Dialog("New User Created", getSkin(), "default") {} // close the dialogue
					.text("New user created with succes!")
					.button("Close", null)
					.show(stage);
		}
		else
			new Dialog("Error", getSkin(), "default") {
				protected void result(Object object) {
					refreshScreen();
				}
			}.text(error).button("Close", null).show(stage);
	}

	//_________________________________ BOUBOULES IMAGES

	private void addChooseBoubouleOptions() {
		addLabel("YOUR  BOUBOULE", FONT_TITLE, FONT_SCALE, Color.WHITE, iX, iY)
				.setTouchable(null);

		leftBoubDraw = getDrawableFromFile(DEFAULT_NORMAL_BOUBOULE, false);
		leftImage = new Image(leftBoubDraw);
		stage.addActor(leftImage);
		leftImage.addListener(leftImageClick);

		centerBoubDraw = getDrawableFromFile(BoubImages.BOUB_DIR_GIANT
				+ GlobalSettings.PROFILE.getBoubName()
				+ BoubImages.BOUB_EXTENTION, false);
		centerImage = new Image(centerBoubDraw);
		stage.addActor(centerImage);

		rightBoubDraw = getDrawableFromFile(DEFAULT_NORMAL_BOUBOULE, false);
		rightImage = new Image(rightBoubDraw);
		stage.addActor(rightImage);
		rightImage.addListener(rightImageClick);

		int iYBoub = (int) (iY - centerImage.getHeight());
		// => center each image: | o O o |
		int iXCenter = (int) (GlobalSettings.APPWIDTH / 2
				- centerImage.getWidth() / 2);
		leftImage.setPosition((int) (iXCenter / 2 - leftImage.getWidth() / 2),
				iYBoub);
		centerImage.setPosition(iXCenter, iYBoub);
		rightImage.setPosition((int) (GlobalSettings.APPWIDTH - iXCenter
				+ iXCenter / 2 - rightImage.getWidth() / 2),
				iYBoub);

		setDefaultImages();
	}

	private ClickListener leftImageClick = new ClickListener() {
		public void clicked(InputEvent event, float x, float y) {
			Gdx.app.log("SCREEN", "leftButton " + x + ", " + y);
			if (iNbBoubouleFiles == 1)
				displayDialogOneImage();
			else
				setBouboulesImages(iCurrentBoub - 1, true);
		}
	};

	private ClickListener rightImageClick = new ClickListener() {
		public void clicked(InputEvent event, float x, float y) {
			Gdx.app.log("SCREEN", "rightButton " + x + ", " + y);
			if (iNbBoubouleFiles == 1)
				displayDialogOneImage();
			else
				setBouboulesImages(iCurrentBoub + 1, true);
		}
	};

	private void displayDialogOneImage() {
		new Dialog("No other skin available", getSkin(), "default") {} // close the dialogue
				.text("You've to win against other bouboules!")
				.button("Close", null)
				.show(stage);
	}

	private void setDefaultImages() {
		boubouleFiles = BoubImages.getBoubName ();
		iNbBoubouleFiles = boubouleFiles.size();
		setBouboulesImages(boubouleFiles.indexOf(
				GlobalSettings.PROFILE.getBoubName()),
				false);
	}

	/**
	 * @return value between 0 and size - 1
	 */
	private int getRealId(int id) {
		if (id < 0)
			return iNbBoubouleFiles - 1;
		else
			return id % iNbBoubouleFiles;
	}

	private String getFullPathNormallBoub (String boubName) {
		return BoubImages.BOUB_DIR_NORMAL + boubName + BoubImages.BOUB_EXTENTION;
	}

	private void disposeBouboulesImages() {
		if (leftBoubDraw != null)
			leftBoubDraw.getRegion().getTexture().dispose();
		if (centerBoubDraw != null)
			centerBoubDraw.getRegion().getTexture().dispose();
		if (rightBoubDraw != null)
			rightBoubDraw.getRegion().getTexture().dispose();
	}

	private void setBouboulesImages(int id, boolean bChangeImage) {
		iCurrentBoub = getRealId(id);

		if (bChangeImage)
			GlobalSettings.PROFILE.setBoubName(boubouleFiles.get(iCurrentBoub));

		disposeBouboulesImages();

		leftBoubDraw = getDrawableFromFile(getFullPathNormallBoub(
				boubouleFiles.get(getRealId(iCurrentBoub - 1))), false);
		leftImage.setDrawable(leftBoubDraw);

		centerBoubDraw = getDrawableFromFile(BoubImages.BOUB_DIR_GIANT
				+ GlobalSettings.PROFILE.getBoubName()
				+ BoubImages.BOUB_EXTENTION, false);
		centerImage.setDrawable(centerBoubDraw);

		rightBoubDraw = getDrawableFromFile(getFullPathNormallBoub(
				boubouleFiles.get(getRealId(iCurrentBoub + 1))), false);
		rightImage.setDrawable(rightBoubDraw);
	}

	//_________________________________ RESET

	private void addResetOptions() {
		addLabel("RESTART", FONT_TITLE, FONT_SCALE, Color.WHITE, iX, iY)
				.setTouchable(null);

		resetButton = new TextButton("End Current Game", getSkin(), "default");
		// resetButton.setSize(300, 100);
		resetButton.setPosition(GlobalSettings.APPWIDTH / 2
				- resetButton.getWidth() / 2,
				iY - (GlobalSettings.ISHD ? 50 : 0));
		this.stage.addActor(resetButton);
		resetButton.addListener(resetClickListener);
	}

	private ClickListener resetClickListener = new ClickListener() {
		public void clicked(InputEvent event, float x, float y) {
			new Dialog("Tutorial", getSkin(), "default") {
				protected void result(Object object) {
					if ((Boolean) object == true)
						EndGameListener.resetGame ();
				}
			}.text("It will restart the game to level 1\n"
					+ " and keep all unlocked worlds.\n"
					+ "Do you want to continue?")
			.button("Yes", true).button("No", false).show(stage);
		}
	};

	// _________________________________ SOUND

	private void addSoundOptions() {
		addLabel("SOUND", FONT_TITLE, FONT_SCALE, Color.WHITE, iX, iY)
				.setTouchable(null);

		soundCheckBox = addCheckBox("Music", ! GlobalSettings.SOUND_IS_MUTED,
				iX, iY + (GlobalSettings.ISHD ? -50 : 5));
		soundCheckBox.setX((int) (GlobalSettings.APPWIDTH / 2 // center
				- soundCheckBox.getWidth() / 2));
		soundCheckBox.addListener(soundClickListener);
	}
	
	ClickListener soundClickListener = new ClickListener() {
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
}