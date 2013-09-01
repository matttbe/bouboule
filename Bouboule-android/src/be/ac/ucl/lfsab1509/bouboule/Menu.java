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
 *    Hélène Verhaeghe <helene.verhaeghe@student.uclouvain.be>
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

package be.ac.ucl.lfsab1509.bouboule;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Random;

import com.badlogic.gdx.Gdx;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.profile.HighScoreInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.MenuItem;
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


public class Menu extends Activity {

	private final int MENU_CHOOSING_LEVEL = 0; 
	public static final int RETURN_MENU = 1; 
	public static final int PLAY_GAME = 2; 

	// Need handler for callback to the UI thread
	private final Handler mHandler = new Handler();

	// Create runnable for posting to the AnimationUpdater
	private final Runnable animationUpdate = new Runnable() {
		@Override
		public void run() {
			updateAnimationOnUI();
		}
	};

	// Create runnable for posting to the nameUpdater
	private final Runnable nameUpdate = new Runnable() {
		@Override
		public void run() {
			startFunWithUi();
		}
	};
	
	// Create runnable for animating to the bouboules
	private final Runnable boubouleUpdate = new Runnable() {
		@Override
		public void run() {
			mHandler.removeCallbacks(this);
			//Hide the bouboules until the animation begin 
			animateBouboules();
		}
	};

	//String animated
	private TextView titleView;
	private String 	nameToShow;
	private int 	whatToShow	= 0;
	private static final int maxWhatToShow = 4;
	private Random rand = new Random();
	
	
	//Animation containers
	private AnimationSet
					animationSetForTitle 	= new AnimationSet(true),
					animationSetCase1		= new AnimationSet(true),
					animationSetCase2		= new AnimationSet(true),
					animationSetCase3		= new AnimationSet(true),
					animationSetCase4		= new AnimationSet(true),
					animationSetCaseD		= new AnimationSet(true);

	// share score with a screenshot
	private ShareScore shareScore;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_menu);

		titleView = (TextView) findViewById(R.id.fullscreen_content);

		//Font Update
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "menu_font.ttf");
		titleView.setTypeface(myTypeface);
		
		float ratio = getDisplayVector().y / GlobalSettings.APPHEIGHT;
		
		titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 45*ratio);

		//Incline the blue text
		titleView.setRotation(-15);

		// relaunch the animations on click
		titleView.setOnClickListener(clickListener);
		findViewById(R.id.boubleft) .setOnClickListener(clickListener);
		findViewById(R.id.boubright).setOnClickListener(clickListener);

		//Hide the bouboules until the animation begin 
		findViewById(R.id.boubleft) .setVisibility(View.INVISIBLE);
		findViewById(R.id.boubright).setVisibility(View.INVISIBLE);

		//Listeners for the Game Launcher
		findViewById(R.id.PlayButton).setOnClickListener(clickListener);
		findViewById(R.id.ParameterButton).setOnClickListener(clickListener);
		
		// HighScoreContextMenu: long click
		registerForContextMenu (findViewById(R.id.HighScoreButton));

		setAllTheAnimationAtOnce();

		mHandler.post(nameUpdate);
		mHandler.postDelayed(boubouleUpdate, 1750);


	}
	
	/**
	 * Function to get the screen size
	 * @return a Point that contains the screen size in px
	 */
	private Point getDisplayVector() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		return size;
	}


	/**
	 * Start the Game // Parameters // HighScore
	 */
	private View.OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick (View view)
		{
			switch (view.getId()) {
				
				case R.id.PlayButton :
					// launch ChoosingLevel only if we're not playing a game: Game -> Menu -> Game
					if (GlobalSettings.GAME.isPlayingGame())
						onActivityResult (MENU_CHOOSING_LEVEL, PLAY_GAME, null);
					else
						startActivityForResult(new Intent(Menu.this,ChoosingActivity.class), 
								MENU_CHOOSING_LEVEL);
					break;
					
				case R.id.ParameterButton :
					Intent intent = new Intent(Menu.this, MenuParameters.class);
					startActivity(intent);
					break;
				
				case R.id.fullscreen_content : // title
					startFunWithUi();
					break;
				case R.id.boubleft :
				case R.id.boubright : // bouboules
					boubouleUpdate.run();
					break;
				default :
					
					break;
			}
			
		}
	};
	
	protected void onActivityResult(final int requestCode, final int resultCode,
			final Intent data) {
		if(requestCode == MENU_CHOOSING_LEVEL) { // it's the id of the button
			
			switch (resultCode) {
			case RETURN_MENU:
				// Nothing to to, we stay here 
				break;

			case PLAY_GAME:
				
				mHandler.removeCallbacks(animationUpdate);
				mHandler.removeCallbacks(nameUpdate);
				setResult(R.id.PlayButton);
				finish();
				break;
				
			default:
				break;
			}
			
			
		}
	}




	@SuppressLint("SimpleDateFormat")
	private void addScoreInMenu (android.view.Menu menu) {
		SimpleDateFormat dateFormat = new SimpleDateFormat ("dd/MM"); ///yyyy");
		HighScoreInfo highscores[] = GlobalSettings.PROFILE_MGR.getProfileGlobal ().getAllHighScores (false);

		// no highscore: display a message even if there is no high scores
		if (highscores.length == 0 || highscores[0] == null) { // the first time, we receive only one elem which is null
			menu.add (getString (R.string.no_highscore));
			return;
		}

		int i;
		for (i = 0; i < highscores.length; i++) {
			HighScoreInfo info = highscores[i];

			if (info == null)
				break;

			/*String cTitle = getString (R.string.Score) + " " + info.getScore () + "\t"
					+ getString (R.string.by_someone) + " " + info.getName () + "\t"
					+ getString (R.string.at_level_x) + " " + info.getLevel () + "\t("
					+ dateFormat.format (info.getDate ()) + ")";// info.getDate ()
			String cTitleCondensed = getString (R.string.Score) + " "
					+ info.getScore () + " "
					+ getString (R.string.by_someone) + " " + info.getName ();*/

			String cTitle = info.getName () + ":\t" + info.getScore ()
					+ " (lvl. " + info.getLevel () + " - "
					+ dateFormat.format (info.getDate ()) + ")";

			if (i == 0 && highscores[1] == null) // only one highscore => only used spaces
				cTitle = cTitle.replaceAll ("\t", " ");

			menu.add (0, i, android.view.Menu.NONE, cTitle);
			//.setTitleCondensed (cTitleCondensed); // for smaller screens => doesn't work...
		}

		menu.add(0, i+1, android.view.Menu.NONE, "Click here to share your high score!");
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
		menu.setHeaderTitle(getString (R.string.HighScore));
		addScoreInMenu (menu);

		// take a screenshot with the score: used special title + delay screenshot
		whatToShow = maxWhatToShow;
		startFunWithUi();
		mHandler.postDelayed(screenShotDelay, 500);
	}

	// Create runnable to delay the screenshot
	private final Runnable screenShotDelay = new Runnable() {
		@Override
		public void run() {
			shareScore = new ShareScore(Menu.this); // take screenshot
		}
	};

	@Override
	public boolean onContextItemSelected (MenuItem item) {
		String cTitle;
		HighScoreInfo highscores[] = GlobalSettings.PROFILE_MGR.getProfileGlobal ().getAllHighScores (false);

		if (highscores.length == 0 || highscores[0] == null) // no high score yet
			cTitle = (String) item.getTitle ();
		else {
			int iItemID = item.getItemId();
			if (iItemID >= highscores.length) // last entry text: display info about the first one
				iItemID = 0;
			HighScoreInfo info = highscores[iItemID];
			cTitle = getString (R.string.Score) + " " + info.getScore () + " "
					+ getString (R.string.by_someone) + " " + info.getName () + " "
					+ getString (R.string.at_level_x) + " " + info.getLevel () + " ("
					+ info.getDate ()+ ")";

			// Share info of the highest score (of the current user!!)
			if (shareScore == null) // should not happen
				shareScore = new ShareScore(this);
			startActivity(shareScore.getIntent(
					"This is my highscore at Bouboule Game: "
					+ GlobalSettings.PROFILE.getHighScore() + "!"));
		}
		Toast.makeText (this, cTitle, Toast.LENGTH_LONG).show ();
		return true;
	}

	public void showPopup (View v) {
		openContextMenu (v); // maybe better a context menu?

		/*PopupMenu popupMenu = new PopupMenu (this, v);
		popupMenu.inflate (R.menu.high_score_popup);
		addScoreInMenu (popupMenu.getMenu ());
		popupMenu.show ();*/
	}

	@Override
	public void onBackPressed() {
		Gdx.app.exit (); // or do nothing but by default, it will (re)start the game => we can use the 'play' button for that.
		finish ();
	}

	@SuppressLint("DefaultLocale")
	private String normaliseTextForTitle(String text) {
		return Normalizer.normalize(text, Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase();
	}

	/**
	 * Update the Animated String and add a 
	 * bouncing animation
	 */
	protected void startFunWithUi() {
		mHandler.removeCallbacks(animationUpdate);
		switch (whatToShow) {
		case 0:
			nameToShow = "BOUBOULE";
			
			break;
		case 1:
			nameToShow = getString(R.string.hello) + "\n"
					+ normaliseTextForTitle(GlobalSettings.PROFILE.getName ());
			// for this font, we need only chars in capital letters and digits
			break;
		case 2:
			nameToShow = getString(R.string.be_with_your);
			break;
		case 3:
			nameToShow = normaliseTextForTitle(GlobalSettings.PROFILE.getName ()) + "\n"
					+ getString(R.string.you_best);
			break;
		case maxWhatToShow: // display the score for the screenshot
			String cHighScore;
			int iHighScore = GlobalSettings.PROFILE.getHighScore();
			if (iHighScore == Integer.MIN_VALUE)
				cHighScore = "NO HIGH SCORE YET";
			else
				cHighScore = Integer.toString(iHighScore);
			String cName = normaliseTextForTitle(GlobalSettings.PROFILE.getName());
			if (cName.length() > 16)
				cName = cName.substring(0, 16); // max 16 chars to limit too long text...
			nameToShow = cName + "\nHIGH SCORE:\n" + cHighScore;
			break;
		}
		whatToShow = (whatToShow + 1) % maxWhatToShow;

		//Get the text and update the name
		titleView.setText(nameToShow);

		//Setting the animation
		titleView.startAnimation(animationSetForTitle);

		//Launch the new UI thread to set the Animation on 
		//the appropriate time.
		mHandler.postDelayed(animationUpdate, 10 * 1000);
	}


	private void updateAnimationOnUI() {
		//Animation Launcher
		switch (rand.nextInt(5)) {
		//Fire the right animation
		case 1:
			titleView.startAnimation(animationSetCase1);
			break;
		case 2:
			titleView.startAnimation(animationSetCase2);
			break;
		case 3:
			titleView.startAnimation(animationSetCase3);
			break;
		case 4:
			titleView.startAnimation(animationSetCase4);
			break;
		default:
			titleView.startAnimation(animationSetCaseD);
			break;
		}

		//re launch
		mHandler.postDelayed(nameUpdate, 5 * 1000);
	}


	private void animateBouboules() {

		TranslateAnimation translateR = new TranslateAnimation(  900, 0, -400, 0);
		TranslateAnimation translateL = new TranslateAnimation( -900, 0, -400, 0);

		translateR.setDuration(1000);
		translateL.setDuration(1000);
		
		findViewById(R.id.boubleft) .setVisibility(View.VISIBLE);
		findViewById(R.id.boubright).setVisibility(View.VISIBLE);
		
		findViewById(R.id.boubright).startAnimation(translateR);
		findViewById(R.id.boubleft) .startAnimation(translateL);

	}
	
	private void setAllTheAnimationAtOnce() {
		
		//Furnish 5 different animation 
		//to add to the Text =)
		
		/**Animation for the Title Update*/
		//Animatin for the title
		
		ScaleAnimation g = new ScaleAnimation(1.0f, 1.10f, 1.0f, 1.10f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		g.setDuration(1000);
		g.setRepeatCount(10);
		g.setRepeatMode(Animation.REVERSE);

		animationSetForTitle.addAnimation(g);
		
		
		/**Animation for CASE 1*/
		// Rotating fading and translating animation

		float ROTATE_FROM = 0.0f;
		float ROTATE_TO   = 10.0f * 360.0f;
		RotateAnimation r = new RotateAnimation(ROTATE_FROM, ROTATE_TO,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		r.setDuration(5000);
		r.setRepeatCount(0);

		animationSetCase1.addAnimation(r);

		TranslateAnimation translateAnimation = new TranslateAnimation(0, -400, 0, 0);

		//setting offset and duration to start after first rotation completed,
		//and end at the same time with the last rotation
		translateAnimation.setStartOffset(500);
		translateAnimation.setDuration(1000);

		animationSetCase1.addAnimation(translateAnimation);

		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setStartOffset(500);
		alphaAnimation.setDuration(2000);

		animationSetCase1.addAnimation(alphaAnimation);

		
		/**Animation for CASE 2*/
		//Translating the animation
		
		TranslateAnimation translateAnimation1 = new TranslateAnimation(0, 0, 0, -600);
		translateAnimation1.setDuration(5000);
		animationSetCase2.addAnimation(translateAnimation1);
		
		
		/**Animation for CASE 3*/
		//Fading the animation
		
		AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
		alphaAnimation1.setDuration(5000);
		animationSetCase3.addAnimation(alphaAnimation1);
		
		
		
		/**Animation for CASE 4*/
		//Upscaling and fading animation
		
		ScaleAnimation grow = new ScaleAnimation(1.0f, 5f, 1.0f, 5f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		grow.setDuration(5000);
		
		AlphaAnimation alphaAnimation2 = new AlphaAnimation(1, 0);
		alphaAnimation2.setDuration(3000);
		
		animationSetCase4.addAnimation(grow);
		animationSetCase4.addAnimation(alphaAnimation2);
		
		
		
		/**Animation for CASE D*/
		//Schrinking animation
		
		ScaleAnimation g1 = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		g1.setDuration(5000);
		animationSetCaseD.addAnimation(g1);
		
	}

	protected void onStop () {
		super.onStop ();
		MyAndroidMenus.onStopMusic (this);
	}

	@Override
	protected void onResume () {
		super.onResume ();
		MyAndroidMenus.onResumeMusic (this);
	}
}
