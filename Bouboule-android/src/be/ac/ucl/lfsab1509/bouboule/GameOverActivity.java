package be.ac.ucl.lfsab1509.bouboule;

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
 *    Helene Verhaeghe <helene.verhaeghe@student.uclouvain.be>
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

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings.GameExitStatus;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class GameOverActivity extends Activity {

	public static GameExitStatus exitStatus = GameExitStatus.NONE;

	private int endScore;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_gameover);

		// Listeners for the Game Launcher
		findViewById(R.id.GameOverMenuButton).setOnTouchListener(
				fireListener);
		findViewById(R.id.GameOverRestartButton).setOnTouchListener(
				fireListener);

		endScore = GlobalSettings.PROFILE.getEndGameScore ();

		Typeface font = Typeface.createFromAsset(getAssets(), "chineyen.ttf"); // "osaka-re.ttf");
		TextView score = (TextView) findViewById (R.id.GameOverScore);
		score.setTypeface (font);
		score.setText (Integer.toString (endScore));
		score.setOnTouchListener(scoreListener);
	}

	private View.OnTouchListener fireListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(final View view, final MotionEvent motionEvent) {
			return MyAndroidMenus.onTouchGeneric (GameOverActivity.this, view.getId ());
		}
	};

	/*private void addScreenShot(View view, Intent shareIntent)
	{
		View rootView = view.getRootView();
		rootView.setDrawingCacheEnabled(true);
		Bitmap bitmap = rootView.getDrawingCache();

		// save it somewhere (with compression)
		String cImagePath = Environment.getExternalStorageDirectory().toString()
				+ "/" + "bouboule_score_" + endScore + ".jpg";
		File imageFile = new File(cImagePath);
		Uri imageUri = Uri.fromFile(imageFile);
		if (imageFile.exists()) {
			shareIntent.setType("* /*");
			shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
		}
		else {
			FileOutputStream outputStream;
			try {
				outputStream = new FileOutputStream(imageFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);
				outputStream.flush();
				outputStream.close();
				String media = MediaStore.Images.Media.insertImage(
						getContentResolver(), cImagePath,
						"Bouboule Score " + endScore,
						"End score at Bouboule game");
				shareIntent.setType("* /*");
				shareIntent.putExtra(Intent.EXTRA_STREAM, media);
			} catch (FileNotFoundException e) {
				Log.e("Activity", "File Not Found: " + cImagePath);
			} catch (IOException e) {
				Log.e("Activity", "IO error: " + cImagePath);
			}
		}
	}*/

	private View.OnTouchListener scoreListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(final View view, final MotionEvent motionEvent) {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_TEXT,
					"This is my last score at Bouboule Game: "
					+ endScore + "! \n"
					+ "Try to beat my at this mobile game! \n"
					+ "http://is.gd/1sYcq6"); // TODO: link to the official website
			shareIntent.setType("text/plain");

			// take Screenshot
			// => It needs rights to write files (and also root rights I think)
			// addScreenShot(view, shareIntent);

			startActivity(shareIntent);
			return true;
		}
	};

	@Override
	public void onBackPressed() {
		MyAndroidMenus.onBackPressedGeneric (this, R.id.GameOverMenuButton);
	}

	protected void onStop () {
		super.onStop ();
		MyAndroidMenus.onStopMusic (this);
	}

	@Override
	protected void onResume () {
		super.onResume ();
		MyAndroidMenus.onResumeMusic (this, exitStatus);
	}
}
