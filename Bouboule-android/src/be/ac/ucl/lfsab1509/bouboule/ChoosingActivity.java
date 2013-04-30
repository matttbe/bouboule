package be.ac.ucl.lfsab1509.bouboule;

import com.badlogic.gdx.Gdx;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.EndGameListener;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.profile.Profile;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ChoosingActivity extends Activity {


	private static ImageButton buttonprev;
	private static ImageButton buttonnext;
	private static Button buttongogogo;
	private static ImageView currentmap;
	private static TextView textlevel;
	private static int maxlevel;
	private static int currentlevel;

	private static int image [];

	protected void onCreate(final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);



		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_choosing);

		maxlevel=GlobalSettings.PROFILE.getBestLevel();
		image= new int[4];
		buttonprev = (ImageButton) findViewById(R.id.choose_lvl_left);
		buttonnext = (ImageButton) findViewById(R.id.choose_lvl_right);
		buttongogogo = (Button) findViewById(R.id.choose_lvl_button1);
		currentmap = (ImageView) findViewById(R.id.choose_lvl_mid);
		textlevel = (TextView) findViewById(R.id.textView2);

		image[0] = R.drawable.mini1;
		image[1] = R.drawable.mini2;
		image[2] = R.drawable.mini3;
		image[3] = R.drawable.mini4;

		currentlevel=0;

		buttonprev.setImageResource(image[(currentlevel+3)%4]);
		buttonnext.setImageResource(image[(currentlevel+1)%4]);
		currentmap.setImageResource(image[(currentlevel)%4]);

		buttongogogo.setOnClickListener(clickListener);
		buttonprev.setOnClickListener(clickListener);
		buttonnext.setOnClickListener(clickListener);



	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.choose_lvl_button1:{
				GlobalSettings.PROFILE.setLevel(currentlevel+1);
				setResult(currentlevel);
				finish();
				break;
			}
			case R.id.choose_lvl_left:{
				currentlevel += maxlevel -1;
				currentlevel %= maxlevel;
				
				buttonprev.setImageResource(image[(currentlevel+3)%4]);
				buttonnext.setImageResource(image[(currentlevel+1)%4]);
				currentmap.setImageResource(image[(currentlevel)%4]);
				textlevel.setText("Level " + (currentlevel+1));
				break;
			}
			case R.id.choose_lvl_right:{
				currentlevel += 1;
				currentlevel %= maxlevel;
				
				buttonprev.setImageResource(image[(currentlevel+3)%4]);
				buttonnext.setImageResource(image[(currentlevel+1)%4]);
				currentmap.setImageResource(image[(currentlevel)%4]);
				textlevel.setText("Level " + (currentlevel+1));
				break;
			}
			}

		}
	};

	public void onBackPressed() {
		setResult(-1);
		finish ();
	}

}