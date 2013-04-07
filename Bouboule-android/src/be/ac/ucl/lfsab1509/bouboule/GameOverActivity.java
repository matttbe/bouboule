package be.ac.ucl.lfsab1509.bouboule;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameOverActivity extends Activity {

	
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

		// TODO: create a menu for GameOver
		((TextView) findViewById (R.id.LoosingMenuButton))
		.setTypeface (myFontBout);
		((TextView) findViewById (R.id.LoosingNextLevelButton))
		.setTypeface (myFontBout);

		//Hide the bouboules until the animation begin 
		
		int NbLifes = GlobalSettings.PROFILE.getNbLifes ();

		if (NbLifes == GlobalSettings.INIT_LIFES) { // = 3
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
		else { // loose and gameover... TODO: or on LoosingActivity?
			findViewById(R.id.coeur1).setVisibility(View.INVISIBLE);
			findViewById(R.id.coeur2).setVisibility(View.INVISIBLE);
			findViewById(R.id.coeur3).setVisibility(View.INVISIBLE);
		}

		Context context = getApplicationContext();
		CharSequence text = "HighScore Menu must be enabled"; // TODO
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		findViewById(R.id.coeur1).setVisibility(View.INVISIBLE);
		findViewById(R.id.coeur2).setVisibility(View.INVISIBLE);
		findViewById(R.id.coeur3).setVisibility(View.INVISIBLE);

		
		((Button) findViewById(R.id.LoosingNextLevelButton)).setEnabled(false);
		((Button) findViewById(R.id.LoosingNextLevelButton)).setText("Game Over");
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
