package be.ac.ucl.lfsab1509.bouboule;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class Menu extends Activity {


	// Need handler for callbacks to the UI thread
	final Handler mHandler = new Handler();

	// Create runnable for posting to the AnimationUpdater
	final Runnable animationUpdate = new Runnable() {
		@Override
		public void run() {
			updateAnimationOnUI();
			Log.i("Run","finish runnable");
		}
	};

	// Create runnable for posting to the nameUpdater
	final Runnable nameUpdate = new Runnable() {
		@Override
		public void run() {
			startFunWithUi();
			Log.i("Run","finish runnable");
		}
	};

	//String animated
	String 	nameToShow;
	int 	whatToShow	= 0;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		//Request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_menu);


		final View contentView = findViewById(R.id.fullscreen_content);

		//Changement de la police
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "menu_font.ttf");
		((TextView) contentView).setTypeface(myTypeface);

		contentView.setRotation(-15);

		//Listener for the Game Launcher
		findViewById(R.id.PlayButton).setOnTouchListener(
				gameFireListener);


		mHandler.post(nameUpdate);


	}


	/*
	 * Start the Game 
	 */
	View.OnTouchListener gameFireListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {

			Context context = getApplicationContext();
			CharSequence text = "Hello toast!";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();

			Intent intent = new Intent(Menu.this, MainActivity.class);
			startActivity(intent);

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
		AnimationSet animationSet = new AnimationSet(true);

		ScaleAnimation g = new ScaleAnimation(1.0f, 1.10f, 1.0f, 1.10f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		g.setDuration(1000);
		g.setRepeatCount(10);
		g.setRepeatMode(Animation.REVERSE);

		animationSet.addAnimation(g);
		myTextView.startAnimation(animationSet);

		//Launch the new UI thread to set the Animation on 
		//the appropriate time.
		mHandler.postDelayed(animationUpdate,10*1000);
	}


	private void updateAnimationOnUI() {

		//Get the Text to update
		TextView myTextView = (TextView)findViewById(R.id.fullscreen_content);

		//Animation
		AnimationSet animationSet = new AnimationSet(true);

		Random rand = new Random();

		switch (rand.nextInt(5)) {

		//Furnish 5 different animation 
		//to add to the Text =)
		case 1:  
			
			// Rotating fading and translating animation
			
			float ROTATE_FROM = 0.0f;
			float ROTATE_TO = 10.0f * 360.0f;
			RotateAnimation r = new RotateAnimation(ROTATE_FROM, ROTATE_TO,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			r.setDuration(5000);     
			r.setRepeatCount(0);

			animationSet.addAnimation(r);

			TranslateAnimation translateAnimation = new TranslateAnimation(0, -400, 0, 0);

			//setting offset and duration to start after first rotation completed,
			//and end at the same time with the last rotation
			translateAnimation.setStartOffset(500);
			translateAnimation.setDuration(1000);

			animationSet.addAnimation(translateAnimation);

			AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
			alphaAnimation.setStartOffset(500);
			alphaAnimation.setDuration(2000);

			animationSet.addAnimation(alphaAnimation);
			break;
			
		case 2:  
			
			//Translating the animation
			
			TranslateAnimation translateAnimation1 = new TranslateAnimation(0, 0, 0, -600);
			translateAnimation1.setDuration(5000);
			animationSet.addAnimation(translateAnimation1);
			break;
			
		case 3: 
			
			//Fading the animation
			
			AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
			alphaAnimation1.setDuration(5000);
			animationSet.addAnimation(alphaAnimation1);
			break;
			
		case 4:
			
			//Upscaling and fading animation
			
			ScaleAnimation g = new ScaleAnimation(1.0f, 15f, 1.0f, 15f,
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
					0.5f);
			g.setDuration(5000);
			
			AlphaAnimation alphaAnimation2 = new AlphaAnimation(1, 0);
			alphaAnimation2.setDuration(3000);
			
			animationSet.addAnimation(g);
			animationSet.addAnimation(alphaAnimation2);
			break;
			
		
		default: 
			
			//Schrinking animation
			
			ScaleAnimation g1 = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
					0.5f);
			g1.setDuration(5000);
			animationSet.addAnimation(g1);
			
		break;
		}

		//Fire the right animation
		myTextView.startAnimation(animationSet);

		//relaunch
		mHandler.postDelayed(nameUpdate,5*1000);
	}




	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

}
