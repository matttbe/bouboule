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

package be.ac.ucl.lfsab1509.bouboule;

import java.util.Random;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class MenuPause extends Activity {

	// Need handler for callback to the UI thread
	private final Handler mHandler = new Handler();

	// Create runnable for posting to the AnimationUpdater
	private final Runnable animationUpdate = new Runnable() {
		@Override
		public void run() {

			updateAnimationOnUI();
		}
	};

	// Create runnable for posting to the nameUpdater
	private final Runnable nameUpdate = new Runnable() {
		@Override
		public void run() {

			startFunWithUi();
		}
	};


	// String animated
	private String nameToShow;

	// Animation containers
	private AnimationSet 
	animationSetForTitle = new AnimationSet(true),
	animationSetCase1 	 = new AnimationSet(true),
	animationSetCase2 	 = new AnimationSet(true),
	animationSetCase3 	 = new AnimationSet(true),
	animationSetCase4 	 = new AnimationSet(true),
	animationSetCaseD 	 = new AnimationSet(true);

	@Override
	public void onCreate (final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_pause_menu);

		//size of the font
		float ratio = 45 * getDisplayVector().y / GlobalSettings.APPHEIGHT;
		
		final View contentView = findViewById(R.id.fullscreen_content_pause);

		// Font Update
		Typeface myTypeface = Typeface.createFromAsset(getAssets(),
				"menu_font.ttf");
		((TextView) contentView).setTypeface(myTypeface);
		((TextView) contentView).setTextSize(TypedValue.COMPLEX_UNIT_PX, ratio);
		// Incline the blue text
		contentView.setRotation(-15);

		// Listeners for the Game Launcher
		findViewById(R.id.PauseContinueButton).setOnClickListener(
				fireListener);
		findViewById(R.id.PauseMenuButton).setOnClickListener(
				fireListener);
		findViewById(R.id.PauseQuitButton).setOnClickListener(
				fireListener);

		
		// Create font for the other buttons
		Typeface myFontBout = Typeface.createFromAsset(getAssets(),
				"chineyen.ttf");
		
		//Set Font
		((TextView) findViewById(R.id.PauseContinueButton))
			.setTypeface(myFontBout);
		((TextView) findViewById(R.id.PauseMenuButton))
			.setTypeface(myFontBout);
		((TextView) findViewById(R.id.PauseQuitButton))
			.setTypeface(myFontBout);

		
		//Set size Font
		((TextView) findViewById(R.id.PauseContinueButton))
			.setTextSize(TypedValue.COMPLEX_UNIT_PX, ratio);
		((TextView) findViewById(R.id.PauseMenuButton))
			.setTextSize(TypedValue.COMPLEX_UNIT_PX, ratio);
		((TextView) findViewById(R.id.PauseQuitButton))
			.setTextSize(TypedValue.COMPLEX_UNIT_PX, ratio);

		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND); // deprecated
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount=0.75f;
		// lp.screenBrightness = 1.0f; // brighness
		getWindow ().setAttributes(lp);
		getWindow ().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		setAllTheAnimationAtOnce();

		mHandler.post(nameUpdate);

	}

	private Point getDisplayVector() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		return size;
	}

	/*
	 * Start the Game // Parameters // HighScore
	 */
	private View.OnClickListener fireListener = new View.OnClickListener() {
		@Override
		public void onClick(final View view) {
			setResult(view.getId()); // give the id of the button
			finish(); // finish activity
		}
	};

	/*
	 * Update the Animated String and add a bouncing animation
	 */
	protected void startFunWithUi() {

		nameToShow = getResources().getString(R.string.pause);

		// Get the text and update the name
		TextView myTextView = (TextView) findViewById(R.id.fullscreen_content_pause);
		myTextView.setText(nameToShow);

		// Setting the animation
		myTextView.startAnimation(animationSetForTitle);

		// Launch the new UI thread to set the Animation on
		// the appropriate time.
		mHandler.postDelayed(animationUpdate, 10 * 1000);
	}

	private void updateAnimationOnUI() {


		// Get the Text to update
		TextView myTextView = (TextView) findViewById(R.id.fullscreen_content_pause);

		// Animation Launcher

		Random rand = new Random();

		switch (rand.nextInt(5))
		{
		// Fire the right animation
		case 1:
			myTextView.startAnimation(animationSetCase1);
			break;
		case 2:
			myTextView.startAnimation(animationSetCase2);
			break;
		case 3:
			myTextView.startAnimation(animationSetCase3);
			break;
		case 4:
			myTextView.startAnimation(animationSetCase4);
			break;
		default:
			myTextView.startAnimation(animationSetCaseD);
			break;
		}

		// re launch
		mHandler.postDelayed(nameUpdate, 5 * 1000);
	}


	private void setAllTheAnimationAtOnce()	{


		// Furnish 5 different animation
		// to add to the Text =)

		/** Animation for the Title Update */
		// Animatin for the title

		ScaleAnimation g = new ScaleAnimation(1.0f, 1.10f, 1.0f, 1.10f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		g.setDuration(1000);
		g.setRepeatCount(10);
		g.setRepeatMode(Animation.REVERSE);

		animationSetForTitle.addAnimation(g);

		/** Animation for CASE 1 */
		// Rotating fading and translating animation

		float ROTATE_FROM 	= 0.0f;
		float ROTATE_TO 	= 10.0f * 360.0f;
		RotateAnimation r = new RotateAnimation(ROTATE_FROM, ROTATE_TO,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		r.setDuration(5000);
		r.setRepeatCount(0);

		animationSetCase1.addAnimation(r);

		TranslateAnimation translateAnimation = new TranslateAnimation (0,
				-400, 0, 0);

		// setting offset and duration to start after first rotation completed,
		// and end at the same time with the last rotation
		translateAnimation.setStartOffset(500);
		translateAnimation.setDuration(1000);

		animationSetCase1.addAnimation(translateAnimation);

		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setStartOffset(500);
		alphaAnimation.setDuration(2000);

		animationSetCase1.addAnimation(alphaAnimation);

		/** Animation for CASE 2 */
		// Translating the animation

		TranslateAnimation translateAnimation1 = new TranslateAnimation (0, 0,
				0, -600);
		translateAnimation1.setDuration(5000);
		animationSetCase2.addAnimation(translateAnimation1);

		/** Animation for CASE 3 */
		// Fading the animation

		AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
		alphaAnimation1.setDuration(5000);
		animationSetCase3.addAnimation(alphaAnimation1);

		/** Animation for CASE 4 */
		// Upscaling and fading animation

		ScaleAnimation grow = new ScaleAnimation(1.0f, 15f, 1.0f, 15f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		grow.setDuration(5000);

		AlphaAnimation alphaAnimation2 = new AlphaAnimation(1, 0);
		alphaAnimation2.setDuration(3000);

		animationSetCase4.addAnimation(grow);
		animationSetCase4.addAnimation(alphaAnimation2);

		/** Animation for CASE D */
		// Schrinking animation

		ScaleAnimation g1 = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		g1.setDuration(5000);
		animationSetCaseD.addAnimation(g1);

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
}
