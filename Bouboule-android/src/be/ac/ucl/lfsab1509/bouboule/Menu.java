package be.ac.ucl.lfsab1509.bouboule;

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

    // Create runnable for posting
    final Runnable animationUpdate = new Runnable() {
    	@Override
        public void run() {
    		updateAnimationOnUI();
            Log.i("Run","finish runnable");
        }
    };
    
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

		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_menu);

		
		final View contentView = findViewById(R.id.fullscreen_content);
		
		//Changement de la police
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "menu_font.ttf");

		((TextView) contentView).setTypeface(myTypeface);
		
		contentView.setRotation(-15);
		
		
		
		
		findViewById(R.id.PlayButton).setOnTouchListener(
				 mDelayHideTouchListener);
		
		
		mHandler.postDelayed(animationUpdate,10*1000);
		
		
	}

	
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
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
	} };
	
	
	protected void startFunWithUi() {

        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
		//Here the name animation
        //Thread t = new Thread() {
        //    public void run() {
            	
		if (whatToShow == 0){

			//TODO: Get User Name instead
			nameToShow = "HELLO\nDUCIS01";
			whatToShow = 1;

		} else {

			nameToShow = "BOUBOULE";            		
			whatToShow = 0;

		}

		TextView myTextView = (TextView)findViewById(R.id.fullscreen_content);
		myTextView.setText(nameToShow);


		mHandler.postDelayed(animationUpdate,10*1000);
        //   }
        //};
        //t.start();
    }
	
	
	private void updateAnimationOnUI() {

		TextView myTextView = (TextView)findViewById(R.id.fullscreen_content);
		
		
		//Animation
		AnimationSet animationSet = new AnimationSet(true);
		
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
		//and end at the same time with the last roration
		translateAnimation.setStartOffset(500);
		translateAnimation.setDuration(1000);
		                               
		animationSet.addAnimation(translateAnimation);
		                               
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setStartOffset(500);
		alphaAnimation.setDuration(2000);
		                               
		animationSet.addAnimation(alphaAnimation);
		
		
		
		
		
		myTextView.startAnimation(animationSet);
				
		//myTextView.setText(nameToShow);
		
		//relaunch
		mHandler.postDelayed(nameUpdate,5*1000);
    }
	
	
	
	
	
	
	
	
	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
	
}
