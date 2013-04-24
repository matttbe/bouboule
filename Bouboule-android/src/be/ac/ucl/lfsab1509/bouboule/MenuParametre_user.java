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

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import android.util.*;
import java.util.ArrayList;

public class MenuParametre_user extends Activity {

	private Spinner user_selectprofile_spin;
	
	private ArrayList<String> listProfile;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_parametre_user);
		
		// find the different views
		user_selectprofile_spin = (Spinner) findViewById(R.id.user_selectprofile_spin);
		
		// link the listeners
		findViewById(R.id.button_user_create).setOnClickListener(clickListener);
		user_selectprofile_spin.setOnItemSelectedListener(spinnerListener);
	}
	
	@Override
	protected void onResume(){ // used as refresh of the view
		super.onResume();
		Log.d("LN","onResume");
		// set the list into the spinner
		listProfile = GlobalSettings.PROFILE_MGR.getAllProfilesAL();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listProfile);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		user_selectprofile_spin.setAdapter(adapter);
		//set the selected item on the spinner
		user_selectprofile_spin.setSelection (listProfile.indexOf(GlobalSettings.PROFILE.getName())); // select the current user
	}
	
	private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected (AdapterView<?> parent, View view, int position, long id){
			GlobalSettings.PROFILE_MGR.changeProfile (listProfile.get((int) id));
		}
		@Override
		public void onNothingSelected (AdapterView<?> parent) {}
	};
	
	private View.OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick (View view)
		{
			switch (view.getId()) {
				case R.id.button_user_create :
					// catch the name
					String text = ((EditText) findViewById(R.id.user_newname)).getText().toString();
					switch (testName(text)){
						// treat the name following the case
						case 0:
							GlobalSettings.PROFILE_MGR.createAndLoadNewProfile(text); // new profile create
							finish (); // quit the view (there is no more option and we have to understand that it's done)
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
					break; 
				default :
					break;
			}
		}
	};
	
	private void makeToast(String s){
		Toast genericToast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
		genericToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		genericToast.show();
		
	}
	
	/* Method to test if a String can be used as new username
	 * @pre: name, the string to test
	 * @post: 0 means no problem, 1 name already used, 2 usuported file name, 3 contains unsusable character
	 */
	private int testName(String name){
		ArrayList<String> unusable = GlobalSettings.PROFILE_MGR.getAllProfilesAndExceptions (); 
		if(unusable.contains (name))
			return 1;
		if(name.equals("") || name.equals(".") || name.equals(".."))
			return 2;
		String[] unusableChar ={ "/", "\n", "\r", "\t", "\0", "\f", "`", "?", "*", "\\", "<", ">", "|", "\"", ":"};
		for (String var : unusableChar)
		{
			if(name.contains(var))
				return 3;
		}
		return 0;
	}
	

	
	
}
