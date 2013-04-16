package be.ac.ucl.lfsab1509.bouboule;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import android.util.*;
import java.util.*;

public class MenuParametre_user extends Activity {

	private Spinner user_selectprofile_spin;
	private ArrayList<String> listProfile;
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_parametre_user);
		
		// link the listeners
		findViewById(R.id.button_user_create).setOnTouchListener(fireListener);
		
		user_selectprofile_spin = (Spinner) findViewById(R.id.user_selectprofile_spin);
		
		user_selectprofile_spin.setOnItemSelectedListener(spinnerListener);
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		Log.d("LN","onResume");
		listProfile = GlobalSettings.PROFILE_MGR.getAllProfilesAL();
		int i = listProfile.indexOf(GlobalSettings.PROFILE.getName());
		listProfile.remove(i);
		listProfile.add(0,GlobalSettings.PROFILE.getName());
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listProfile);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		user_selectprofile_spin.setAdapter(adapter);
	}
	
	private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected (AdapterView<?> parent, View view, int position, long id){
			
				switch (view.getId()) {
				
					case R.id.user_selectprofile_spin :
						Log.d("LN",position + " + " + id);
						GlobalSettings.PROFILE_MGR.loadProfile(listProfile.get((int) id));
						break;
					default :
						break;
				}
		}
		@Override
		public void onNothingSelected (AdapterView<?> parent) {}
	};
	
	private View.OnTouchListener fireListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(final View view, final MotionEvent motionEvent) {
			
			if (view.isPressed ())
			{
				switch (view.getId()) {
				
					case R.id.button_user_create :
						createNewProfile();
						break;
					default :
						break;
				}
			}
			return false;
		}
	};
	
	// TODO : care about the unacceptable carracter, blanc name and other wrong names
	private void createNewProfile(){
		String text = ((EditText) findViewById(R.id.user_newname)).getText().toString();
		Log.d("LN","new profile sauvegarde : "+text);
		GlobalSettings.PROFILE_MGR.createAndLoadNewProfile(text); // new profile create
		onResume(); // refresh th view
	}
	
	
}
