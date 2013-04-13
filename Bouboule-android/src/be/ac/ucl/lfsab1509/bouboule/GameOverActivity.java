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
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameOverActivity extends Activity {

	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_loosing);

		// Listeners for the Game Launcher
		findViewById(R.id.LoosingMenuButton).setOnTouchListener(
				fireListener);
		findViewById(R.id.LoosingNextLevelButton).setOnTouchListener(
				fireListener);

		Typeface myFontBout = Typeface.createFromAsset(getAssets(),
				"chineyen.ttf");

		// TODO: create a menu for GameOver
		((TextView) findViewById (R.id.LoosingMenuButton))
		.setTypeface (myFontBout);
		((TextView) findViewById (R.id.LoosingNextLevelButton))
		.setTypeface (myFontBout);

		//Hide the bouboules until the animation begin 
		
		int NbLifes = GlobalSettings.PROFILE.getNbLifes ();

		if (NbLifes == GlobalSettings.INIT_LIFES) { // = 3
			findViewById(R.id.coeur1).setVisibility(View.INVISIBLE);
			findViewById(R.id.coeur2).setVisibility(View.INVISIBLE);
		}
		else if (NbLifes == 2) {
			findViewById(R.id.coeur1).setVisibility(View.INVISIBLE);
			findViewById(R.id.coeur3).setVisibility(View.INVISIBLE);
		}
		else if (NbLifes == 1) {
			findViewById(R.id.coeur2).setVisibility(View.INVISIBLE);
			findViewById(R.id.coeur3).setVisibility(View.INVISIBLE);
		}
		else { // loose and gameover... TODO: or on LoosingActivity?
			findViewById(R.id.coeur1).setVisibility(View.INVISIBLE);
			findViewById(R.id.coeur2).setVisibility(View.INVISIBLE);
			findViewById(R.id.coeur3).setVisibility(View.INVISIBLE);
		}

		Context context = getApplicationContext();
		CharSequence text = "HighScore Menu must be enabled\nBackground should be changed"; // TODO
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		findViewById(R.id.coeur1).setVisibility(View.INVISIBLE);
		findViewById(R.id.coeur2).setVisibility(View.INVISIBLE);
		findViewById(R.id.coeur3).setVisibility(View.INVISIBLE);

		
		((Button) findViewById(R.id.LoosingNextLevelButton)).setEnabled(false);
		((Button) findViewById(R.id.LoosingNextLevelButton)).setText("Game Over");
		
		((TextView) findViewById (R.id.LoosingScore)).setText (Integer.toString (GlobalSettings.PROFILE.getEndGameScore ()));
	}

	private View.OnTouchListener fireListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(final View view, final MotionEvent motionEvent) {
			setResult (view.getId ());
			finish(); // finish activity => return to MainActivity
			return false;
		}
	};

	@Override
	public void onBackPressed() {
		setResult (R.id.LoosingMenuButton); // TODO: EndGameMenuButton?
		finish ();
	}
}
