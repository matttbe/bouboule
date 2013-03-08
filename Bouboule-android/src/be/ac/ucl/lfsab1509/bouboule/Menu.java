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
 *    Hélène Verhaeghe <helene.verhaeghe@student.uclouvain.be>
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

import android.app.Activity;
//import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
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
//import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class Menu extends Activity {


	// Need handler for callback to the UI thread
	final Handler mHandler = new Handler();

	// Create runnable for posting to the AnimationUpdater
	final Runnable animationUpdate = new Runnable() {
		@Override
		public void run() {
			updateAnimationOnUI();
			Log.i("Run","finish runnable updateAnimation");
		}
	};

	// Create runnable for posting to the nameUpdater
	final Runnable nameUpdate = new Runnable() {
		@Override
		public void run() {
			startFunWithUi();
			Log.i("Run","finish runnable nameUpdate");
		}
	};
	
	// Create runnable for animating to the bouboules
	final Runnable boubouleUpdate = new Runnable() {
		@Override
		public void run() {
			Log.d("Run","START BOUBOULE UPDATE");
			animateBouboules();
			Log.i("Run","finish animate boubouleAnim");
		}
	};

	//String animated
	String 	nameToShow;
	int 	whatToShow	= 0;
	
	
	//Animation containers
	AnimationSet	animationSetForTitle 	= new AnimationSet(true),
					animationSetCase1		= new AnimationSet(true),
					animationSetCase2		= new AnimationSet(true),
					animationSetCase3		= new AnimationSet(true),
					animationSetCase4		= new AnimationSet(true),
					animationSetCaseD		= new AnimationSet(true);




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		//Request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_menu);


		final View contentView = findViewById(R.id.fullscreen_content);

		//Font Update
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "menu_font.ttf");
		((TextView) contentView).setTypeface(myTypeface);

		//Incline the blue text
		contentView.setRotation(-15);

		//Listeners for the Game Launcher
		findViewById(R.id.PlayButton).setOnTouchListener(
				fireListener);
		findViewById(R.id.ParameterButton).setOnTouchListener(
				fireListener);
		findViewById(R.id.HighScoreButton).setOnTouchListener(
				fireListener);
		
		
		
		//TODO: Change the font !!
		Typeface myFontBout = Typeface.createFromAsset(getAssets(), "chineyen.ttf");
		
		((TextView) findViewById(R.id.PlayButton)).setTypeface(myFontBout);
		((TextView) findViewById(R.id.ParameterButton)).setTypeface(myFontBout);
		((TextView) findViewById(R.id.HighScoreButton)).setTypeface(myFontBout);
		
		//Hide the bouboules until the animation begin 
		findViewById(R.id.boubleft ).setVisibility(View.INVISIBLE);
		findViewById(R.id.boubright).setVisibility(View.INVISIBLE);

		setAllTheAnimationAtOnce();

		mHandler.post(nameUpdate);
		mHandler.postDelayed(boubouleUpdate, 1750);


	}


	/*
	 * Start the Game // Parameters // HighScore
	 */
	View.OnTouchListener fireListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {

			switch (view.getId() ){
			
				case R.id.PlayButton :
					Intent intent = new Intent(Menu.this, MainActivity.class);
					startActivity(intent);
					
					break;
					
				case R.id.ParameterButton :
					
					break;
					
				case R.id.HighScoreButton :
					
					break;
					
				default :
					
					break;
			}
			finish ();

			return false;
		} 
	};


	/*
	 * Update the Animated String and add a 
	 * bouncing animation
	 */
	protected void startFunWithUi() {
		
		if (whatToShow == 0){

			nameToShow = "BOUBOULE";
			whatToShow = 1;

		} else {

			//TODO: Get User Name instead
			nameToShow = "HELLO\nDUCIS01";            		
			whatToShow = 0;

		}

		//Get the text and update the name
		TextView myTextView = (TextView)findViewById(R.id.fullscreen_content);
		myTextView.setText(nameToShow);

		//Setting the animation
		myTextView.startAnimation(animationSetForTitle);

		//Launch the new UI thread to set the Animation on 
		//the appropriate time.
		mHandler.postDelayed(animationUpdate,10*1000);
	}
	
	
	
	


	private void updateAnimationOnUI() {

		//Get the Text to update
		TextView myTextView = (TextView)findViewById(R.id.fullscreen_content);

		//Animation Launcher

		Random rand = new Random();

		switch (rand.nextInt(5)) {
		//Fire the right animation
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

		//re launch
		mHandler.postDelayed(nameUpdate,5*1000);
	}


	private void animateBouboules() {

		TranslateAnimation translateR = new TranslateAnimation(900, 0, -400, 0);
		TranslateAnimation translateL = new TranslateAnimation( -900, 0, -400, 0);

		translateR.setDuration(1000);
		translateL.setDuration(1000);
		
		findViewById(R.id.boubleft ).setVisibility(View.VISIBLE);
		findViewById(R.id.boubright).setVisibility(View.VISIBLE);
		
		findViewById(R.id.boubright).startAnimation(translateR);
		findViewById(R.id.boubleft ).startAnimation(translateL);

	}
	
	private void setAllTheAnimationAtOnce(){
		
		//Furnish 5 different animation 
		//to add to the Text =)
		
		/**Animation for the Title Update*/
		//Animatin for the title
		
		ScaleAnimation g = new ScaleAnimation(1.0f, 1.10f, 1.0f, 1.10f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		g.setDuration(1000);
		g.setRepeatCount(10);
		g.setRepeatMode(Animation.REVERSE);

		animationSetForTitle.addAnimation(g);
		
		
		/**Animation for CASE 1*/
		// Rotating fading and translating animation

		float ROTATE_FROM = 0.0f;
		float ROTATE_TO = 10.0f * 360.0f;
		RotateAnimation r = new RotateAnimation(ROTATE_FROM, ROTATE_TO,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		r.setDuration(5000);     
		r.setRepeatCount(0);

		animationSetCase1.addAnimation(r);

		TranslateAnimation translateAnimation = new TranslateAnimation(0, -400, 0, 0);

		//setting offset and duration to start after first rotation completed,
		//and end at the same time with the last rotation
		translateAnimation.setStartOffset(500);
		translateAnimation.setDuration(1000);

		animationSetCase1.addAnimation(translateAnimation);

		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setStartOffset(500);
		alphaAnimation.setDuration(2000);

		animationSetCase1.addAnimation(alphaAnimation);

		
		/**Animation for CASE 2*/
		//Translating the animation
		
		TranslateAnimation translateAnimation1 = new TranslateAnimation(0, 0, 0, -600);
		translateAnimation1.setDuration(5000);
		animationSetCase2.addAnimation(translateAnimation1);
		
		
		/**Animation for CASE 3*/
		//Fading the animation
		
		AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
		alphaAnimation1.setDuration(5000);
		animationSetCase3.addAnimation(alphaAnimation1);
		
		
		
		/**Animation for CASE 4*/
		//Upscaling and fading animation
		
		ScaleAnimation grow = new ScaleAnimation(1.0f, 15f, 1.0f, 15f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		grow.setDuration(5000);
		
		AlphaAnimation alphaAnimation2 = new AlphaAnimation(1, 0);
		alphaAnimation2.setDuration(3000);
		
		animationSetCase4.addAnimation(grow);
		animationSetCase4.addAnimation(alphaAnimation2);
		
		
		
		/**Animation for CASE D*/
		//Schrinking animation
		
		ScaleAnimation g1 = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		g1.setDuration(5000);
		animationSetCaseD.addAnimation(g1);
		
	}

	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

}
