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
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

public class MenuParametre_global extends Activity {

	
	private static Switch sound_switch;
	private static Switch rotate_switch;
	private static SeekBar sensitivity_seekbar;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_parametre_global);
		
		
		// find the different views
		sound_switch = (Switch) findViewById(R.id.global_sound_switch);
		rotate_switch = (Switch) findViewById(R.id.global_rotate_switch);
		sensitivity_seekbar = (SeekBar) findViewById(R.id.global_sensitivity_bar);
		
		// set the originals values
		sound_switch.setChecked(!GlobalSettings.SOUND_IS_MUTED);
		rotate_switch.setChecked(!GlobalSettings.FIXED_ROTATION);
		sensitivity_seekbar.setMax(GlobalSettings.SENSITIVITY_MAX - GlobalSettings.SENSITIVITY_MIN);
		sensitivity_seekbar.setProgress(GlobalSettings.SENSITIVITY - GlobalSettings.SENSITIVITY_MIN);
		
		// set the listeners
		sound_switch.setOnCheckedChangeListener(switchListener);
		rotate_switch.setOnCheckedChangeListener(switchListener);
		sensitivity_seekbar.setOnSeekBarChangeListener(seekBarListener);
		
		// change of type font
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "menu_font.ttf");
		((TextView) findViewById(R.id.global_sound_txt)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.global_rotate_txt)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.global_sensitivity_txt)).setTypeface(myTypeface);
		
		
	}
	
	// listener for the switchs
	private CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (buttonView == sound_switch){
				GlobalSettings.PROFILE_MGR.getProfileGlobal ().changeSoundSettings(!sound_switch.isChecked());
				if (GlobalSettings.SOUND_IS_MUTED)
					MyAndroidMenus.onStopMusic(MenuParametre_global.this);
				else
					MyAndroidMenus.onResumeMusic(MenuParametre_global.this);
			} else if (buttonView == rotate_switch){
				GlobalSettings.PROFILE_MGR.getProfileGlobal ().changeFixedRotation(!rotate_switch.isChecked());
			}
		}
	};
	
	// listener for seekbar
	private SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
			if (seekBar == sensitivity_seekbar) {
				GlobalSettings.PROFILE_MGR.getProfileGlobal ().changeSensibilitySettings(sensitivity_seekbar.getProgress() + GlobalSettings.SENSITIVITY_MIN);
			}
		}
		public void onStartTrackingTouch (SeekBar seekBar) {}
		public void onStopTrackingTouch (SeekBar seekBar){}
	};

	protected void onStop () {
		super.onStop ();
		MyAndroidMenus.onStopMusic (this);
	}

	@Override
	protected void onResume () {
		super.onResume ();
		MyAndroidMenus.onResumeMusic (this);
	}
}
