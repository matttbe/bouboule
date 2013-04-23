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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.util.Log;

public class MenuParametre extends Activity {
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_parametre_menu);
		
		
		findViewById(R.id.button_param_user).setOnClickListener(clickListener);
		findViewById(R.id.button_param_global).setOnClickListener(clickListener);
		findViewById(R.id.button_param_about).setOnClickListener(clickListener);
		
		
		
	}
	private View.OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick (View view)
		{
			Intent intent;
			Log.d("LN","click");
			switch (view.getId()) {
				case R.id.button_param_user :
					intent = new Intent(MenuParametre.this, MenuParametre_user.class);
					startActivity(intent);
					break;
					
				case R.id.button_param_global :
					
					intent = new Intent(MenuParametre.this, MenuParametre_global.class);
					startActivity(intent);
					break;
					
				case R.id.button_param_about :
					intent = new Intent(MenuParametre.this, MenuParametre_about.class);
					startActivity(intent);
					break;
					
				default :
					
					break;
			}
		}
	};
	
	
	
	
}
