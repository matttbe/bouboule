package be.ac.ucl.lfsab1509.bouboule;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class LoosingActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_loosing);

		// Listeners for the Game Launcher
		findViewById (R.id.LoosingMenuButton).setOnTouchListener (
				fireListener);
		findViewById (R.id.LoosingNextLevelButton).setOnTouchListener (fireListener);
		findViewById (R.id.LoosingQuitButton).setOnTouchListener (fireListener);

		// TODO: Change the font !!
		Typeface myFontBout = Typeface.createFromAsset (getAssets (),
				"chineyen.ttf");

		((TextView) findViewById (R.id.LoosingMenuButton))
		.setTypeface (myFontBout);
		((TextView) findViewById (R.id.LoosingNextLevelButton))
		.setTypeface (myFontBout);
		((TextView) findViewById (R.id.LoosingQuitButton))
		.setTypeface (myFontBout);
	}

	View.OnTouchListener fireListener = new View.OnTouchListener () {
		@Override
		public boolean onTouch (View view, MotionEvent motionEvent)
		{


			switch (view.getId ()) {

			case R.id.LoosingMenuButton: // cas ou on stoppe
				Intent intent = new Intent(LoosingActivity.this, Menu.class);
				startActivity(intent);
				finish ();
				break;
			case R.id.LoosingQuitButton: // just quit without new activity => quit
				finish ();

				break;

			default:
				break;
			}



			finish (); // finish activity

			return false;
		}
	};

}
