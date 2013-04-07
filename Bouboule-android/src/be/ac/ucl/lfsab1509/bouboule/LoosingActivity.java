package be.ac.ucl.lfsab1509.bouboule;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

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

		Typeface myFontBout = Typeface.createFromAsset(getAssets(),
				"chineyen.ttf");

		((TextView) findViewById (R.id.LoosingMenuButton))
		.setTypeface (myFontBout);
		((TextView) findViewById (R.id.LoosingNextLevelButton))
		.setTypeface (myFontBout);

		//Hide the bouboules until the animation begin 
		
		int NbLifes = GlobalSettings.PROFILE.getNbLifes ();

		if (NbLifes == 2) {
			findViewById(R.id.coeur1).setVisibility(View.INVISIBLE);
			findViewById(R.id.coeur3).setVisibility(View.INVISIBLE);
		}
		else if (NbLifes == 1) {
			findViewById(R.id.coeur2).setVisibility(View.INVISIBLE);
			findViewById(R.id.coeur3).setVisibility(View.INVISIBLE);
		}
		else { // TODO: more than 3 lifes?
			findViewById(R.id.coeur1).setVisibility(View.INVISIBLE);
			findViewById(R.id.coeur2).setVisibility(View.INVISIBLE);
		}
		// no life? => GameOverActivity
	}

	private View.OnTouchListener fireListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(final View view, final MotionEvent motionEvent) {
			setResult (view.getId ());
			finish(); // finish activity => return to MainActivity
			return false;
		}
	};

}
