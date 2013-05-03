package be.ac.ucl.lfsab1509.bouboule;

import com.badlogic.gdx.Gdx;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.EndGameListener;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.profile.Profile;
import android.app.Activity;
import android.graphics.Color;
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
		image= new int[20];
		buttonprev = (ImageButton) findViewById(R.id.choose_lvl_left);
		buttonnext = (ImageButton) findViewById(R.id.choose_lvl_right);
		buttongogogo = (Button) findViewById(R.id.choose_lvl_button1);
		currentmap = (ImageView) findViewById(R.id.choose_lvl_mid);
		textlevel = (TextView) findViewById(R.id.textView2);

		image[0] = R.drawable.mini1;
		image[1] = R.drawable.mini2;
		image[2] = R.drawable.mini3;
		image[3] = R.drawable.mini4;
		image[4] = R.drawable.mini1;
		image[5] = R.drawable.mini2;
		image[6] = R.drawable.mini3;
		image[7] = R.drawable.mini4;
		image[8] = R.drawable.mini1;
		image[9] = R.drawable.mini2;
		image[10] = R.drawable.mini3;
		image[11] = R.drawable.mini4;
		image[12] = R.drawable.mini1;
		image[13] = R.drawable.mini2;
		image[14] = R.drawable.mini3;
		image[15] = R.drawable.mini4;
		image[16] = R.drawable.mini1;
		image[17] = R.drawable.mini2;
		image[18] = R.drawable.mini3;
		image[19] = R.drawable.mini4;

		currentlevel=0;

		updateimage();

		buttongogogo.setOnClickListener(clickListener);
		buttonprev.setOnClickListener(clickListener);
		buttonnext.setOnClickListener(clickListener);



	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.choose_lvl_button1:{
				
				if(GlobalSettings.GAME.getScreen() != null)
				EndGameListener.resetGame();
				
				GlobalSettings.PROFILE.setLevel(currentlevel+1);
				
				
				
				setResult(currentlevel);
				finish();
				break;
			}
			case R.id.choose_lvl_left:{
				currentlevel += maxlevel -1;
				currentlevel %= maxlevel;
				
				updateimage();
				
				textlevel.setText("Level " + (currentlevel+1));
				break;
			}
			case R.id.choose_lvl_right:{
				currentlevel += 1;
				currentlevel %= maxlevel;
				
				updateimage();
				
				textlevel.setText("Level " + (currentlevel+1));
				break;
			}
			}

		}
	};
	
	private void updateimage(){
		buttonprev.setImageResource(image[(currentlevel+19)%20]);
		buttonnext.setImageResource(image[(currentlevel+1)%20]);
		currentmap.setImageResource(image[(currentlevel)%20]);

	}

	public void onBackPressed() {
		setResult(-1);
		finish ();
	}

}