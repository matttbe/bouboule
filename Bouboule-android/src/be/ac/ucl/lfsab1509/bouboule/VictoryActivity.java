package be.ac.ucl.lfsab1509.bouboule;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class VictoryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_victory);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.victory, menu);
		return true;
	}

}
