package be.ac.ucl.lfsab1509.bouboule;

import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class VictoryActivity extends Activity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_victory);


		// Listeners for the Game Launcher
		findViewById(R.id.VictoryMenuButton).setOnTouchListener(
				fireListener);
		findViewById(R.id.VictoryNextLevelButton).setOnTouchListener(
				fireListener);
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
