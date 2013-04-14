package be.ac.ucl.lfsab1509.bouboule;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

public class MenuParametre_global extends Activity {

	// to contains the changes before they got saved
	private static boolean newSoundIsMuted;
	private static int newSensitivity;
	
	private static Switch sound_switch;
	private static SeekBar sensitivity_seekbar;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_parametre_global);
		
		// set the save button
		findViewById(R.id.button_global_save).setOnTouchListener(fireListener);
		
		// ask the preview values
		newSoundIsMuted = GlobalSettings.SOUND_IS_MUTED;
		newSensitivity = GlobalSettings.SENSITIVITY;
		
		// find the different views
		sound_switch = (Switch) findViewById(R.id.global_sound_switch);
		sensitivity_seekbar = (SeekBar) findViewById(R.id.global_sensitivity_bar);
		
		// set the originals values
		sound_switch.setChecked(newSoundIsMuted);
		sensitivity_seekbar.setMax(GlobalSettings.SENSITIVITY_MAX);
		sensitivity_seekbar.setProgress(newSensitivity);
	}
	
	private View.OnTouchListener fireListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(final View view, final MotionEvent motionEvent) {
			
			if (view.isPressed ())
			{
				switch (view.getId()) {
				
					case R.id.button_global_save :
						//got the values back from views
						newSoundIsMuted = sound_switch.isChecked();
						newSensitivity = sensitivity_seekbar.getProgress();
						//save the values
						GlobalSettings.PROFILE_MGR.getProfileGlobal ().changeSoundSettings(newSoundIsMuted);
						GlobalSettings.PROFILE_MGR.getProfileGlobal ().changeSensibilitySettings(newSensitivity);
						break;
						
					default :
						
						break;
				}
			}
			return false;
			
		}
	};
	
	
}
//import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
//GlobalSettings.PROFILE_MGR.getProfileGlobal ().toggleSoundSettings ();