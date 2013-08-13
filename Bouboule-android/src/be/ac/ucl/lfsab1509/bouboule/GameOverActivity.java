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
import android.os.Handler;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class GameOverActivity extends Activity {

	public static GameExitStatus exitStatus = GameExitStatus.NONE;

	private int endScore;
	private String endScoreText;
	private TextView score;
	// Need handler for callback to the UI thread
	private final Handler mHandler = new Handler();

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_gameover);

		// Listeners for the Game Launcher
		findViewById(R.id.GameOverMenuButton).setOnTouchListener(
				fireListener);
		findViewById(R.id.GameOverRestartButton).setOnTouchListener(
				fireListener);

		endScore = GlobalSettings.PROFILE.getEndGameScore ();
		if (exitStatus == GameExitStatus.GAMEOVER_END)
			endScoreText = "End! " + endScore;
		else
			endScoreText = Integer.toString (endScore);

		Typeface font = Typeface.createFromAsset(getAssets(), "chineyen.ttf"); // "osaka-re.ttf");
		score = (TextView) findViewById (R.id.GameOverScore);
		score.setTypeface (font);
		score.setText (endScoreText);
		score.setOnClickListener(scoreListener);
	}

	private View.OnTouchListener fireListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(final View view, final MotionEvent motionEvent) {
			return MyAndroidMenus.onTouchGeneric (GameOverActivity.this, view.getId ());
		}
	};

	private View.OnClickListener scoreListener = new View.OnClickListener() {
		@Override
		public void onClick(final View view) {
			score.setText(GlobalSettings.PROFILE.getName() + ": " + endScoreText);
			mHandler.postDelayed(screenShotDelay, 250);
		}
	};

	// Create runnable to delay the screenshot
	private final Runnable screenShotDelay = new Runnable() {
		@Override
		public void run() {
			ShareScore shareScore = new ShareScore(GameOverActivity.this);
			startActivity(shareScore.getIntent(
					// ShareScore.getShareScoreIntent(
					"This is my last score at Bouboule Game: " + endScore + "!"));
			score.setText (endScoreText);
		}
	};

	@Override
	public void onBackPressed() {
		MyAndroidMenus.onBackPressedGeneric (this, R.id.GameOverMenuButton);
	}

	protected void onStop () {
		super.onStop ();
		MyAndroidMenus.onStopMusic (this);
	}

	@Override
	protected void onResume () {
		super.onResume ();
		MyAndroidMenus.onResumeMusic (this, exitStatus);
	}
}
