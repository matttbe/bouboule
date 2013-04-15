package be.ac.ucl.lfsab1509.bouboule;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import android.app.Activity;
import android.os.Bundle;
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
		
		// set the listeners
		sound_switch.setOnCheckedChangeListener(switchListener);
		sensitivity_seekbar.setOnSeekBarChangeListener(seekBarListener);
	}
	
	private CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (buttonView == sound_switch){
				newSoundIsMuted = sound_switch.isChecked();
				GlobalSettings.PROFILE_MGR.getProfileGlobal ().changeSoundSettings(newSoundIsMuted);
			}
		}
	};
	
	private SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
			if (seekBar == sensitivity_seekbar) {
				newSensitivity = sensitivity_seekbar.getProgress();
				GlobalSettings.PROFILE_MGR.getProfileGlobal ().changeSensibilitySettings(newSensitivity);
			}
		}
		public void onStartTrackingTouch (SeekBar seekBar) {}
		public void onStopTrackingTouch (SeekBar seekBar){}
	};
	
	
	
}
