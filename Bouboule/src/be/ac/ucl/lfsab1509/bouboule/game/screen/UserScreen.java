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

public class UserScreen extends AbstractScreen {

	private static final String FONT_TITLE = "chinyen-font";
	private static final float  FONT_SCALE = .5f;
	private static final char[] unacceptedChars = { '/', '\n', '\r', '\t', '\0',
		'\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':'};
	private static final String DEFAULT_NORMAL_BOUBOULE =
			BoubImages.BOUB_DIR_NORMAL + GlobalSettings.DEFAULT_BOUB_NAME
			+ BoubImages.BOUB_EXTENTION;

	private TextButton resetButton;
	private Image leftImage;
	private Image centerImage;
	private Image rightImage;
	private CheckBox tutorialCheckBox;
	private TextField newUserTextField;
	private SelectBox chooseUserBox;
	private boolean bIsRefreshing = false;

	private ArrayList<String> listProfiles;
	private ArrayList<String> boubouleFiles;
	private int iNbBoubouleFiles;
	private int iCurrentBoub;

	public UserScreen() {
		super(false);
	}

	@Override
	public void show() {
		super.show();

		// Set Background
		addBackGround("drawable-xhdpi/settings_blank.jpg");

		addChooseUserOptions();

		addNewUserOptions();

		addChooseBoubouleOptions();

		addResetOptions();

		addTutorialOptions();

		addBackButton(false);
	}

	// called when selecting a existed/new user
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
		// Tuto
		tutorialCheckBox.setChecked(GlobalSettings.PROFILE.needTutorial());
	}

	//_________________________________ CHOOSE USER

	private void addChooseUserOptions() {
		addLabel("SELECT  USER", FONT_TITLE, FONT_SCALE, Color.WHITE, 40, 1050)
				.setTouchable(null);

		listProfiles = GlobalSettings.PROFILE_MGR.getAllProfilesAL();

		chooseUserBox = new SelectBox(listProfiles.toArray(), getSkin(), "default");
		chooseUserBox.setSelection(GlobalSettings.PROFILE.getName());
		chooseUserBox.setSize((int) GlobalSettings.APPWIDTH - (2 * 40), 70);
		chooseUserBox.setPosition(40, 1000);
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
		addLabel("CREATE  NEW  USER", FONT_TITLE, FONT_SCALE, Color.WHITE, 40, 900)
				.setTouchable(null);

		newUserTextField = new TextField("", getSkin(), "default");
		newUserTextField.setSize(GlobalSettings.APPWIDTH - (2 * 40), 70);
		newUserTextField.setPosition(40, 850);
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
		addLabel("CHOOSE  YOUR  BOUBOULE", FONT_TITLE, FONT_SCALE, Color.WHITE, 40, 700)
				.setTouchable(null);

		leftImage = addImage(DEFAULT_NORMAL_BOUBOULE, 30, 500);
		leftImage.addListener(leftImageClick);

		centerImage = addImage(BoubImages.BOUB_DIR_GIANT
				+ GlobalSettings.PROFILE.getBoubName()
				+ BoubImages.BOUB_EXTENTION, 100, 500);

		rightImage = addImage(DEFAULT_NORMAL_BOUBOULE, 530, 500);
		rightImage.addListener(rightImageClick);

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

	private void setBouboulesImages(int id, boolean bChangeImage) {
		iCurrentBoub = getRealId(id);

		if (bChangeImage)
			GlobalSettings.PROFILE.setBoubName(boubouleFiles.get(iCurrentBoub));

		leftImage.setDrawable(getDrawableFromFile(getFullPathNormallBoub(
				boubouleFiles.get(getRealId(iCurrentBoub - 1)))));
		centerImage.setDrawable(getDrawableFromFile(BoubImages.BOUB_DIR_GIANT
				+ GlobalSettings.PROFILE.getBoubName()
				+ BoubImages.BOUB_EXTENTION));
		rightImage.setDrawable(getDrawableFromFile(getFullPathNormallBoub(
				boubouleFiles.get(getRealId(iCurrentBoub + 1)))));
	}

	//_________________________________ RESET

	private void addResetOptions() {
		addLabel("RESTART", FONT_TITLE, FONT_SCALE, Color.WHITE, 40, 700)
				.setTouchable(null);

		resetButton = new TextButton("Restart to level 1!", getSkin(), "default");
		resetButton.setSize(300, 100);
		resetButton.setPosition(30, 200);
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
			}.text("It will restart the game on level 1\nDo you want to continue?")
			.button("Yes", true).button("No", false).show(stage);
		}
	};

	//_________________________________ TUTORIAL

	private void addTutorialOptions() {
		addLabel("TUTORIAL", FONT_TITLE, FONT_SCALE, Color.WHITE, 40, 700)
				.setTouchable(null);
		tutorialCheckBox = addCheckBox("Show the tutorial!",
				GlobalSettings.PROFILE.needTutorial(), 40, 300);
		tutorialCheckBox.addListener(tutorialClickListener);
	}

	private ClickListener tutorialClickListener = new ClickListener() {
		public void clicked(InputEvent event, float x, float y) {
			new Dialog("Tutorial", getSkin(), "default") {
				protected void result(Object object) {
					if ((Boolean) object == true) {
						EndGameListener.resetGame ();
						GlobalSettings.PROFILE.setNeedTutorial(
								tutorialCheckBox.isChecked());
					}
					else
						tutorialCheckBox.setChecked(false);
				}
			}.text("It will restart the game on level 1\nDo you want to continue?")
			.button("Yes", true).button("No", false).show(stage);
		}
	};
}