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

import java.io.IOException;
import java.io.InputStream;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.widget.ViewSwitcher.ViewFactory;

public class MenuParameters_global extends Activity implements ViewFactory {

	
	private Switch sound_switch;
	private Switch rotate_switch;
	private SeekBar sensitivity_seekbar;
	private ImageSwitcher switcherBonus;
	private TextView bonusDescription;

	private String[][] bonusInfo = {
			{"bonus/elasticity/elasticity_high.png", "Collision are more elastic"},
			{"bonus/elasticity/elasticity_low.png",  "Collision are less elastic"},
			{"bonus/heart/heart.png", "One more life"},
			{"bonus/inverse/inverse.png", "The axes are inverted"},
			{"bonus/invincible/invincible.png", "Bouboule is invincible"},
			{"bonus/invisible/invisible.png", "Bouboule is invisible"},
			{"bonus/speed/speed_high.png", "Bouboule runs faster"},
			{"bonus/speed/speed_low.png", "Bouboule runs slower"},
			{"bonus/star/star.png", "More points"},
			{"bonus/time/timeup.png", "More time before the end of the game"},
			{"bonus/time/timedown.png", "Less time before the end of the game"},
			{"bonus/weight/weight_high.png", "Bouboule is heavier"},
			{"bonus/weight/weight_low.png", "Bouboule is lighter"}
		};
	
	private int currId = 0, downX, upX; // needed for the switcherBonus

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_parameters_global);
		
		
		// find the different views
		sound_switch = (Switch) findViewById(R.id.global_sound_switch);
		rotate_switch = (Switch) findViewById(R.id.global_rotate_switch);
		sensitivity_seekbar = (SeekBar) findViewById(R.id.global_sensitivity_bar);
		
		// set the originals values
		sound_switch.setChecked(!GlobalSettings.SOUND_IS_MUTED);
		rotate_switch.setChecked(!GlobalSettings.FIXED_ROTATION);
		sensitivity_seekbar.setMax(GlobalSettings.SENSITIVITY_MAX - GlobalSettings.SENSITIVITY_MIN);
		sensitivity_seekbar.setProgress(GlobalSettings.SENSITIVITY - GlobalSettings.SENSITIVITY_MIN);
		
		// set the listeners
		sound_switch.setOnCheckedChangeListener(switchListener);
		rotate_switch.setOnCheckedChangeListener(switchListener);
		sensitivity_seekbar.setOnSeekBarChangeListener(seekBarListener);
		
		// change of type font
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "menu_font.ttf");
		((TextView) findViewById(R.id.global_sound_txt)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.global_rotate_txt)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.global_sensitivity_txt)).setTypeface(myTypeface);
		((TextView) findViewById(R.id.global_bonus_txt)).setTypeface(myTypeface);

		bonusDescription = ((TextView) findViewById(R.id.global_bonus_description));
		
		// Bonus: imageview
		switcherBonus = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		switcherBonus.setFactory((ViewFactory) this);
		switcherBonus.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		switcherBonus.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));
		switcherBonus.setImageDrawable(getDrawableBonus(0)); // set first image
		setTextBonus();

		switcherBonus.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					downX = (int) event.getX();
					Log.d("Matth", " downX " + downX);
					return true;
				}
				else if (event.getAction() == MotionEvent.ACTION_UP) {
					upX = (int) event.getX();
					Log.i("Matth", " upX " + upX);

					if (upX - downX > 100) {
						// curIndex current image index in array viewed by user
						currId--;
						if (currId < 0) {
							currId = bonusInfo.length - 1;
						}

						switcherBonus.setImageDrawable(getDrawableBonus(currId));
						setTextBonus();
					}

					else if (downX - upX > -100) {

						currId++;
						if (currId > 4) {
							currId = 0;
						}
						switcherBonus.setImageDrawable(getDrawableBonus(currId));
						setTextBonus();
					}
					return true;
				}
				return false;
			}
		});
	}
	
	// listener for the switchs
	private CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (buttonView == sound_switch){
				GlobalSettings.PROFILE_MGR.getProfileGlobal ().changeSoundSettings(!sound_switch.isChecked());
				if (GlobalSettings.SOUND_IS_MUTED)
					MyAndroidMenus.onStopMusic(MenuParameters_global.this);
				else
					MyAndroidMenus.onResumeMusic(MenuParameters_global.this);
			} else if (buttonView == rotate_switch){
				GlobalSettings.PROFILE_MGR.getProfileGlobal ().changeFixedRotation(!rotate_switch.isChecked());
			}
		}
	};
	
	// listener for seekbar
	private SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
			if (seekBar == sensitivity_seekbar) {
				GlobalSettings.PROFILE_MGR.getProfileGlobal ().changeSensibilitySettings(sensitivity_seekbar.getProgress() + GlobalSettings.SENSITIVITY_MIN);
			}
		}
		public void onStartTrackingTouch (SeekBar seekBar) {}
		public void onStopTrackingTouch (SeekBar seekBar){}
	};

	protected void onStop () {
		super.onStop ();
		MyAndroidMenus.onStopMusic (this);
	}

	@Override
	protected void onResume () {
		super.onResume ();
		MyAndroidMenus.onResumeMusic (this);
	}

	// BONUS

	private Drawable getDrawableBonus (int id) {
		InputStream inputStream = null;
		Drawable drawable = null;
		try {
			inputStream = getAssets().open(bonusInfo[id][0]);
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			drawable = new BitmapDrawable(this.getResources(), bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return drawable;
	}

	public void setTextBonus() {
		bonusDescription.setText(bonusInfo[currId][1]);
	}

	@Override
	public View makeView() {
		ImageView iView = new ImageView(this);
		iView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		iView.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return iView;
	}
}
