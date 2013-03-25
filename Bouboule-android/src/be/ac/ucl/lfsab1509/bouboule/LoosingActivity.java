package be.ac.ucl.lfsab1509.bouboule;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class LoosingActivity extends Activity {

	
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

		// TODO: Change the font !!
		Typeface myFontBout = Typeface.createFromAsset(getAssets(),
				"chineyen.ttf");

		((TextView) findViewById (R.id.LoosingMenuButton))
		.setTypeface (myFontBout);
		((TextView) findViewById (R.id.LoosingNextLevelButton))
		.setTypeface (myFontBout);

		//Hide the bouboules until the animation begin 
		
		int NbLifes = GlobalSettings.GAME.getScore ().getNbLifes ();

		if (NbLifes == GlobalSettings.INIT_LIVES) { // = 3
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
		else {
			Context context = getApplicationContext();
			CharSequence text = "HighScore Menu must be enabled";
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	private View.OnTouchListener fireListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(final View view, final MotionEvent motionEvent) {

			Intent intent;

			switch (view.getId()) {

			case R.id.LoosingMenuButton: // cas ou on stoppe
				intent = new Intent(LoosingActivity.this, Menu.class);
				startActivity(intent);
				finish ();
				break;
			case R.id.LoosingNextLevelButton: 
				intent = new Intent(LoosingActivity.this, MainActivity.class);//MainActivity.class);
				startActivity(intent);
				finish();

				break;

			default:
				break;
			}



			finish(); // finish activity

			return false;
		}
	};

}
