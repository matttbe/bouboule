package be.ac.ucl.lfsab1509.bouboule;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MenuParametre extends Activity {
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Request the fullScreen for the Main Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_parametre_menu);
		
		findViewById(R.id.button_param_user).setOnTouchListener(fireListener);
		findViewById(R.id.button_param_global).setOnTouchListener(fireListener);
		findViewById(R.id.button_param_about).setOnTouchListener(fireListener);
		
		
		
	}
	
	
	private View.OnTouchListener fireListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(final View view, final MotionEvent motionEvent) {
			
			if (view.isPressed ())
			{
				Intent intent;
				switch (view.getId()) {
				
					case R.id.button_param_user :
						break;
						
					case R.id.button_param_global :
						
						intent = new Intent(MenuParametre.this, MenuParametre_global.class);
						startActivity(intent);
						break;
						
					case R.id.button_param_about :
						intent = new Intent(MenuParametre.this, MenuParametre_about.class);
						startActivity(intent);
						break;
						
					default :
						
						break;
				}
			}
			return false;
			
		}
	};
	
	
}
