package be.ac.ucl.lfsab1509.bouboule;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.EndGameListener;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoosingActivity extends Activity {


	private static ImageView buttonprev;
	private static ImageView buttonnext;
	private static ImageView buttonmid;
	//private static Button buttongogogo;
	private static ImageView currentmap;
	private static TextView textlevel;
	private static int maxlevel;
	private static int currentlevel;

	private static int image [];
	private static int imagelock;

	protected void onCreate(final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);



		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_choosing);

		maxlevel=GlobalSettings.PROFILE.getBestLevel();
		image= new int[20];
		buttonprev = (ImageView) findViewById(R.id.choose_lvl_left);
		buttonnext = (ImageView) findViewById(R.id.choose_lvl_right);
		//buttongogogo = (Button) findViewById(R.id.choose_lvl_button1);
		buttonmid = (ImageView) findViewById(R.id.choose_lvl);
		currentmap = (ImageView) findViewById(R.id.choose_lvl_lock);
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
		imagelock = R.drawable.minilock;

		currentlevel=0;

		updateimage();
		currentmap.setImageResource(imagelock);

		//buttongogogo.setOnClickListener(clickListener);
		buttonprev.setOnClickListener(clickListener);
		buttonnext.setOnClickListener(clickListener);
		//buttonmid.setOnClickListener(clickListener);
		currentmap.setOnClickListener(clickListener);



	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		public void onClick(View view) {
			switch (view.getId()) {
			//case R.id.choose_lvl_button1:
			case R.id.choose_lvl_lock:{
				
				
				if(currentlevel + 1 > maxlevel)
					return;
				
				if(GlobalSettings.GAME.getScreen() != null)
				EndGameListener.resetGame();
				
				GlobalSettings.PROFILE.setLevel(currentlevel+1);
				
				
				
				setResult(currentlevel);
				finish();
				break;
			}
			case R.id.choose_lvl_left:{
				currentlevel += 20-1;
				currentlevel %= 20;
				
				updateimage();
				
				textlevel.setText("Level " + (currentlevel+1));
				break;
			}
			case R.id.choose_lvl_right:{
				currentlevel += 1;
				currentlevel %= 20;
				
				updateimage();
				
				textlevel.setText("Level " + (currentlevel+1));
				break;
			}
			}

		}
	};
	
	// @SuppressWarnings("deprecation")
	private void updateimage(){
		buttonprev.setImageResource(image[(currentlevel+19)%20]);
		buttonnext.setImageResource(image[(currentlevel+1)%20]);
		buttonmid.setImageResource(image[(currentlevel)%20]);

		if(currentlevel +1 > maxlevel){
			currentmap.setAlpha(255); // TODO: deprecated
		}else{
			currentmap.setAlpha(0);
		}
		
	}

	public void onBackPressed() {
		setResult(-1);
		finish ();
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