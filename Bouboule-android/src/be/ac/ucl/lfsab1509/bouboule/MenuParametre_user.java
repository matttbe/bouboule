package be.ac.ucl.lfsab1509.bouboule;

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

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.EndGameListener;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.profile.BoubImages;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.text.InputFilter;
import android.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



public class MenuParametre_user extends Activity {

	private Spinner user_selectprofile_spin;
	private ImageButton user_boub_left;
	private ImageButton user_boub_right;
	private ImageView user_boub;
	private EditText user_newname;
	private EditText user_choose_level;
	private Button user_reset;
	private Button user_tuto;
	private AlertDialog user_alert_reset;
	private AlertDialog user_alert_tuto;
	
	private ArrayList<String> listProfile;
	
	private ArrayList<String> boub_str;
	private int boub_index;
	private int BOUB_INDEX_MAX;
	
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_parametre_user);			
		
		// find the different views
		user_selectprofile_spin = (Spinner) findViewById(R.id.user_selectprofile_spin);
		user_boub_left = (ImageButton) findViewById(R.id.user_boub_left);
		user_boub_right = (ImageButton) findViewById(R.id.user_boub_right);
		user_boub = (ImageView) findViewById(R.id.user_boub);
		user_newname = (EditText) findViewById(R.id.user_newname);
		user_choose_level = (EditText) findViewById (R.id.user_choose_level);
		user_reset = (Button) findViewById (R.id.user_resetgame_button);
		user_tuto = (Button) findViewById (R.id.user_tuto_button);
		
		// link the listeners
		user_boub_left.setOnClickListener(clickListener);
		user_boub_right.setOnClickListener(clickListener);
		user_selectprofile_spin.setOnItemSelectedListener(spinnerListener);
		user_newname.setOnKeyListener (onkeyListener);
		user_choose_level.setOnKeyListener (onkeyListener);
		user_reset.setOnClickListener (clickListener);
		user_tuto.setOnClickListener (clickListener);
		
		// change of type font
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "menu_font.ttf");
		((TextView) findViewById(R.id.user_newUser_txt)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.user_activeUser_txt)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.user_playerball_txt)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.user_choose_level_txt)).setTypeface(myTypeface);
		user_reset.setTypeface(myTypeface);
		user_tuto.setTypeface(myTypeface);
		
		// hide the keyboard
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		refreshScreen();
	}
	
	/**
	 * Method to refresh all the informations on the screen
	 */
	private void refreshScreen(){
		
		Log.d("LN","USER SETTINGS : refresh in progress");
		
		// refresh the user selected
		listProfile = GlobalSettings.PROFILE_MGR.getAllProfilesAL();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listProfile);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		user_selectprofile_spin.setAdapter(adapter);
		user_selectprofile_spin.setSelection (listProfile.indexOf(GlobalSettings.PROFILE.getName())); // select the current user
		
		// refresh the choice of bouboule
		boub_str = BoubImages.getBoubName ();
		BOUB_INDEX_MAX = boub_str.size();
		boub_index = boub_str.indexOf(GlobalSettings.PROFILE.getBoubName());
		if (boub_index == -1){ // if the precedent selected ball no more takable, set the default, must not happen
			Log.d("LN","USER SETTINGS : choice of bouboule: require access to unaccessible bouboule (must not happen)");
			GlobalSettings.PROFILE.setBoubName(GlobalSettings.DEFAULT_BOUB_NAME);
			boub_index = boub_str.indexOf(GlobalSettings.PROFILE.getBoubName());
		}
		if (BOUB_INDEX_MAX == 1){ // disable buttons if only one bouboule choice
			user_boub_left.setColorFilter(new LightingColorFilter(Color.DKGRAY, 1));
			user_boub_left.setEnabled(false);
			user_boub_right.setColorFilter(new LightingColorFilter(Color.DKGRAY, 1));
			user_boub_right.setEnabled(false);
		} else {
			user_boub_left.setColorFilter(null);
			user_boub_left.setEnabled(true);
			user_boub_right.setColorFilter(null);
			user_boub_right.setEnabled(true);
		}
		updateBouboule();
		
		// refresh the new user edittext
		user_newname.setText(""); // remove text

		// refresh the curent level edittext
		user_choose_level.setText(""); // remove text
		int iBestLevel = GlobalSettings.PROFILE.getBestLevel();
		user_choose_level.setHint (getString (R.string.user_choose_level_current)
				+ GlobalSettings.PROFILE.getLevel() + " ("
				+ getString (R.string.user_choose_level_max)
				+ iBestLevel + ")");
		user_choose_level.setFilters (new InputFilter[] {
				new InputFilterMinMax (1, iBestLevel)});
		
		Log.d("LN","USER SETTINGS : refresh done");
	}
	
	/**
	 * Method to refresh the bouboule selector on the screen
	 */
	private void updateBouboule(){
		openPictureFromAssets(user_boub,boub_str.get (boub_index),true);
		openPictureFromAssets(user_boub_left,boub_str.get (getPrevIndex(boub_index)),false);
		openPictureFromAssets(user_boub_right,boub_str.get (getNextIndex(boub_index)),false);
	}
	
	/**
	 * Open the picture of bouboule asked from the asset direcory
	 * @param view : the ImageView where it has to be set
	 * @param name : name of the bouboule chosen
	 * @param big : if true, uses big bouboule, if false, uses small ones
	 */
	private void openPictureFromAssets (ImageView view, String name, boolean big){
		String size;
		if (big)
			size = "giant";
		else
			size = "small";
		
		InputStream bitmap=null;

		try {
		    bitmap=getAssets().open("boub/" + size + "/" + name + ".png");
		    Bitmap bit=BitmapFactory.decodeStream(bitmap);
		    view.setImageBitmap(bit);
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if(bitmap!=null)
				try {
					bitmap.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * Set a Toast centered verticaly on the screen
	 * @param s : string to set on the Toast
	 */
	private void makeToast(String s){
		Toast genericToast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
		genericToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		genericToast.show();
		
	}
	
	/**
	 * Method to test if a String can be used as new username
	 * @param name : string to be tested
	 * @return 0 means no problem
	 * 			1 name already used
	 * 			2 unsupported file name
	 * 			3 contains unusable character
	 */
	private int testName(String name){
		ArrayList<String> unusable = GlobalSettings.PROFILE_MGR.getAllProfilesAndExceptions (); 
		// check if already taken
		if(unusable.contains (name))
			return 1;
		// check if unsupported file name
		if(name.equals("") || name.equals(".") || name.equals(".."))
			return 2;
		// check if contains unusable character
		String[] unusableChar ={ "/", "\n", "\r", "\t", "\0", "\f", "`", "?", "*", "\\", "<", ">", "|", "\"", ":"};
		for (String var : unusableChar)
		{
			if(name.contains(var))
				return 3;
		}
		return 0;
	}

	/**
	 * @param index : current index
	 * @return previous index, if index == 0 then previous is MAX - 1
	 */
	private int getPrevIndex (int index) {
		return ((index + BOUB_INDEX_MAX - 1) % BOUB_INDEX_MAX) ;
	}
	
	/**
	 * @param index : current index
	 * @return next index, if index == MAX - 1 then next is 0
	 */
	private int getNextIndex (int index) {
		return ((boub_index + 1) % BOUB_INDEX_MAX);
	}

	protected void onStop () {
		super.onStop ();
		MyAndroidMenus.onStopMusic (this);
	}

	@Override
	protected void onResume () {
		super.onResume ();
		MyAndroidMenus.onResumeMusic (this);
	}
	
	/*
	 * Different listeners needed
	 */
	
	// listener for the spinners
	private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected (AdapterView<?> parent, View view, int position, long id){
			if (!GlobalSettings.PROFILE.getName().equals(listProfile.get((int) id))) {
				Log.d("LN","USER SETTINGS : change of user : previous : " + GlobalSettings.PROFILE.getName() + " - new : " + listProfile.get((int) id));
				GlobalSettings.PROFILE_MGR.changeProfile (listProfile.get((int) id));
				refreshScreen();
			}
		}
		@Override
		public void onNothingSelected (AdapterView<?> parent) {}
	};
	
	// listener for the edittexts
	private View.OnKeyListener onkeyListener = new View.OnKeyListener() {
		@Override
		public boolean onKey (View v, int keyCode, KeyEvent event)
		{
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_ENTER:
						if (v.getId () == R.id.user_newname) {
							// catch the name
							String text = user_newname.getText().toString();
							switch (testName(text)){
								// treat the name following the case
								case 0:
									GlobalSettings.PROFILE_MGR.createAndLoadNewProfile(text); // new profile create
									Log.d("LN","USER SETTINGS : change of user : created new user : " + text);
									makeToast(getString (R.string.user_namenewuser));
									refreshScreen();
									break;
								case 1:
									makeToast(getString (R.string.user_nameidenticalerror));
									break;
								case 2:
									makeToast(getString (R.string.user_nameemptyerror));
									break;
								case 3:
									makeToast(getString (R.string.user_namecharerror));
									break;
								default :
									break; 
							}
							InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
							return true;
						} else if (v.getId () == R.id.user_choose_level) {
							String cInputText = user_choose_level.getText ().toString ();
							if (cInputText == null || cInputText.isEmpty ())
								return false;
							int iNewLevel = Integer.parseInt (cInputText);
							if (iNewLevel != GlobalSettings.PROFILE.getLevel ()) {
								Log.d("LN","USER SETTINGS : change of level : " + iNewLevel);
								EndGameListener.resetGame ();
								GlobalSettings.PROFILE.setLevel (iNewLevel);
								makeToast (getString(R.string.user_resetgame_notif));
							}
							return true;
						}
					case KeyEvent.KEYCODE_BACK : 
						finish();
						break;
				}
			}
			return false;
		}
	};
	
	// listener for buttons
	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick (View view)
		{
			switch (view.getId()) {
				case R.id.user_boub_left :
					boub_index = getPrevIndex(boub_index);
					GlobalSettings.PROFILE.setBoubName(boub_str.get(boub_index));
					Log.d("LN","USER SETTINGS : change of boub : left - new index " + boub_index);
					updateBouboule();
					break;
				case R.id.user_boub_right :
					boub_index = getNextIndex(boub_index);
					GlobalSettings.PROFILE.setBoubName(boub_str.get(boub_index));
					Log.d("LN","USER SETTINGS : change of boub : right - new index " + boub_index);
					updateBouboule();
					break;
				case R.id.user_resetgame_button :
					AlertDialog.Builder builderReset = new AlertDialog.Builder(view.getContext());
					builderReset.setMessage(getString(R.string.user_continuereset));
					builderReset.setPositiveButton(R.string.user_yes, dialoglistener); 
					builderReset.setNegativeButton(R.string.user_no, dialoglistener);
					user_alert_reset = builderReset.show();
					break;
				case R.id.user_tuto_button :
					AlertDialog.Builder builderTuto = new AlertDialog.Builder(view.getContext());
					builderTuto.setMessage(getString(R.string.user_continuetuto));
					builderTuto.setPositiveButton(R.string.user_yes, dialoglistener); 
					builderTuto.setNegativeButton(R.string.user_no, dialoglistener);
					user_alert_tuto = builderTuto.show();
				default :
					break;
			}
		}
	};
	
	// listener for dialog interfaces
	DialogInterface.OnClickListener dialoglistener = new DialogInterface.OnClickListener() { 
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which){
			case -1 : // yes button
				EndGameListener.resetGame ();
				if (dialog == user_alert_tuto){
					GlobalSettings.PROFILE.setNeedTutorial(true);
					makeToast(getString(R.string.user_tuto_notif));
					Log.d("LN","USER SETTINGS : game reset + new tuto");
				} else if (dialog == user_alert_reset) {
					makeToast(getString(R.string.user_resetgame_notif));
					Log.d("LN","USER SETTINGS : game reset");
				}
				refreshScreen();
				break;
			case -2 : // no button
				break;
			default :
				break;
			}
		}
	};

}
