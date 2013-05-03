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

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MenuParametre_about extends Activity{

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_parametre_about);
		
		// change of type font
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "menu_font.ttf");
		((TextView) findViewById(R.id.about_nameapp)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.about_authors)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.about_graphism)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.about_music)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.about_licence)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.about_version)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.about_date)).setTypeface(myTypeface);
		
		
		// Get the version number direct from the Manifest
		PackageInfo pInfo = null;
		try{
			pInfo = getPackageManager().getPackageInfo("be.ac.ucl.lfsab1509.bouboule",PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			pInfo = null;
		}
		if(pInfo != null) {
			((TextView) findViewById(R.id.about_version_txt)).setText(getString(R.string.about_version_txt) + " " + pInfo.versionCode);
		}
		
	}
	
	
}
