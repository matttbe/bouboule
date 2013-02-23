package be.ac.ucl.lfsab1509.bouboule;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import be.ac.ucl.lfsab1509.bouboule.game.*;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        //cfg.useGL20 = false; 
        
        cfg.useGL20 = true;
        cfg.useAccelerometer = true;
        cfg.useCompass = false;
        
        initialize(new Game(), cfg);
    }
    
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	   
	   Context context = getApplicationContext();
		CharSequence text = "Return Pressed !\n Pause must be ENABLE !";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}