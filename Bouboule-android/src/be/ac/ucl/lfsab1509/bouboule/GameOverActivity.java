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
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

public class GameOverActivity extends Activity {

	public static GameExitStatus exitStatus = GameExitStatus.NONE;

	private int endScore;
	private String endScoreText;
	private TextView score;
	private TextView highScoreTextView;
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
		findViewById(R.id.GameOverMenuButton).setOnClickListener(
				fireListener);
		findViewById(R.id.GameOverRestartButton).setOnClickListener(
				fireListener);

		endScore = GlobalSettings.PROFILE.getEndGameScore ();
		endScoreText = Integer.toString (endScore);

		Typeface font = Typeface.createFromAsset(getAssets(), "chineyen.ttf"); // "osaka-re.ttf");
		score = (TextView) findViewById (R.id.GameOverScore);
		score.setTypeface (font);
		score.setText (endScoreText);
		score.setShadowLayer(1, 3, 3, Color.BLACK);
		score.setOnClickListener(scoreListener);

		//_______ HighScore
		highScoreTextView = (TextView) findViewById(R.id.GameOverHighScore);

		if (GlobalSettings.PROFILE.isNewHighScore())
			animateHighScore();
		else
			highScoreTextView.setVisibility(View.INVISIBLE);
	}

	private void animateHighScore() {
		// ratio
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		float ratio = size.y / GlobalSettings.APPHEIGHT;

		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "menu_font.ttf");
		highScoreTextView.setTypeface(myTypeface);
		highScoreTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 35 * ratio);

		AnimationSet animationSetForHighScore = new AnimationSet(true);
		ScaleAnimation anim = new ScaleAnimation(1.0f, 1.10f, 1.0f, 1.10f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anim.setDuration(1000);
		anim.setRepeatCount(Animation.INFINITE);
		anim.setRepeatMode(Animation.REVERSE);
		animationSetForHighScore.addAnimation(anim);
		highScoreTextView.startAnimation(animationSetForHighScore);

		highScoreTextView.setOnClickListener(scoreListener);
	}

	private View.OnClickListener fireListener = new View.OnClickListener() {
		@Override
		public void onClick(final View view) {
			MyAndroidMenus.onClickGeneric (GameOverActivity.this, view.getId ());
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
					getString(R.string.last_score) + " " + endScore + "!",
					getResources()));
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
