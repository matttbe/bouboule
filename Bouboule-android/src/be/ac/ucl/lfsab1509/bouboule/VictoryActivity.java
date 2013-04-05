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

			Intent intent;
			switch (view.getId()) {

			case R.id.VictoryMenuButton: // cas ou on stoppe
				intent = new Intent(VictoryActivity.this, Menu.class);
				startActivity(intent);
				finish();
				break;

			case R.id.VictoryNextLevelButton:
				intent = new Intent(VictoryActivity.this, MainActivity.class);
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
