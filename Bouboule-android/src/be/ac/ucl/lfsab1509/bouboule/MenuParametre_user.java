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
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.*;
import android.util.*;
import java.util.ArrayList;




public class MenuParametre_user extends Activity {

	private Spinner user_selectprofile_spin;
	private ArrayList<String> listProfile;
	private ArrayAdapter<String> adapter;
	private Toast toastChar;
	private Toast toastIdentical;
	private Toast toastEmpty;
	int duration;
	Context context;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_parametre_user);
		
		// link the listeners
		findViewById(R.id.button_user_create).setOnClickListener(clickListener);
		
		user_selectprofile_spin = (Spinner) findViewById(R.id.user_selectprofile_spin);
		
		user_selectprofile_spin.setOnItemSelectedListener(spinnerListener);
		
		// toast
		context = getApplicationContext();
		duration = Toast.LENGTH_LONG;
		toastChar = Toast.makeText(context, getString (R.string.user_namecharerror), duration);
		toastChar.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toastIdentical = Toast.makeText(context, getString (R.string.user_nameidenticalerror), duration);
		toastIdentical.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toastEmpty = Toast.makeText(context, getString (R.string.user_nameemptyerror), duration);
		toastEmpty.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		Log.d("LN","onResume");
		listProfile = GlobalSettings.PROFILE_MGR.getAllProfilesAL();

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listProfile);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		user_selectprofile_spin.setAdapter(adapter);
		
		user_selectprofile_spin.setSelection (listProfile.indexOf(GlobalSettings.PROFILE.getName())); // select the current user
	}
	
	private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected (AdapterView<?> parent, View view, int position, long id){
			Log.d("LN",position + " + " + id);
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
					// on recupere le text
					String text = ((EditText) findViewById(R.id.user_newname)).getText().toString();
					switch (testName(text)){
					case 0:
						Log.d("LN","new profile sauvegarde : "+text);
						GlobalSettings.PROFILE_MGR.createAndLoadNewProfile(text); // new profile create
						finish (); // quit the view (there is no more option and we have to understand that it's done)
						break;
					case 1:
						toastIdentical.show();
						break;
					case 2:
						toastEmpty.show();
						break;
					case 3:
						toastChar.show();
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
	
	
	
	// TODO : care about the unacceptable carracter, blanc name and other wrong names
	// cName should not be included in PROFILES_KEY and can't contain invalid char:
	// => String.IndexOfAny (System.IO.Path.GetInvalidPathChars ()) == 0
	private int testName(String name){
		ArrayList<String> unusable = GlobalSettings.PROFILE_MGR.getAllProfilesAndExceptions (); 
		if(unusable.contains (name))
			return 1;
		if(name.equals(""))
			return 2;
		String[] unusableChar ={ "/", "\n", "\r", "\t", "\0", "\f", "`", "?", "*", "\\", "<", ">", "|", "\"", ":" };
		for (String var : unusableChar)
		{
			if(name.contains(var))
				return 3;
		}
		return 0;
	}
	

	
	
}
