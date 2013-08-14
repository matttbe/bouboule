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
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings.GameExitStatus;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class LoosingActivity extends Activity {

	private String score;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_loosing);

		// Listeners for the Game Launcher
		findViewById(R.id.LoosingMenuButton).setOnClickListener(
				fireListener);
		findViewById(R.id.LoosingNextLevelButton).setOnClickListener(
				fireListener);
		
		Typeface myFontBout = Typeface.createFromAsset(getAssets(),
				"osaka-re.ttf");
		TextView pScoreView = (TextView) findViewById (R.id.LoosingScore);
		
		float ratio = getDisplayVector().y / GlobalSettings.APPHEIGHT;

		score = Integer.toString (GlobalSettings.PROFILE.getScore ());
		pScoreView.setText (score);
		pScoreView.setTypeface (myFontBout);
		pScoreView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 35 * ratio);
		
		int NbLifes = GlobalSettings.PROFILE.getNbLifes ();

		if (NbLifes == 2) {
			findViewById(R.id.heart1).setVisibility(View.INVISIBLE);
			findViewById(R.id.heart3).setVisibility(View.INVISIBLE);
		}
		else if (NbLifes == 1) {
			findViewById(R.id.heart2).setVisibility(View.INVISIBLE);
			findViewById(R.id.heart3).setVisibility(View.INVISIBLE);
		}
		else {
			findViewById(R.id.heart1).setVisibility(View.INVISIBLE);
			findViewById(R.id.heart2).setVisibility(View.INVISIBLE);
		}
		// no life? => GameOverActivity

		pScoreView.setOnClickListener(scoreListener);
	}

	private View.OnClickListener scoreListener = new View.OnClickListener() {
		@Override
		public void onClick(final View view) {
			ShareScore shareScore = new ShareScore(LoosingActivity.this);
			startActivity(shareScore.getIntent(
					"This is my last score at Bouboule Game: " + score + "!"));
		}
	};
	
	private Point getDisplayVector() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		return size;
	}

	private View.OnClickListener fireListener = new View.OnClickListener() {
		@Override
		public void onClick(final View view) {
			MyAndroidMenus.onClickGeneric (LoosingActivity.this, view.getId ());
		}
	};

	@Override
	public void onBackPressed() {
		MyAndroidMenus.onBackPressedGeneric (this, R.id.LoosingMenuButton);
	}

	protected void onStop () {
		super.onStop ();
		MyAndroidMenus.onStopMusic (this);
	}

	@Override
	protected void onResume () {
		super.onResume ();
		MyAndroidMenus.onResumeMusic (this, GameExitStatus.LOOSE);
	}
}
