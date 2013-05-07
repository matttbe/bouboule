package be.ac.ucl.lfsab1509.bouboule;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class CopyOfChoosingActivity extends FragmentActivity {

	protected void onCreate(final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		/** Getting a reference to the ViewPager defined the layout file */        
		ViewPager pager = (ViewPager) findViewById(R.id.pager);

		/** Getting fragment manager */
		FragmentManager fm = getSupportFragmentManager();

		/** Instantiating FragmentPagerAdapter */
		MyLevelPagerAdapter pagerAdapter = new MyLevelPagerAdapter(fm);

		/** Setting the pagerAdapter to the pager object */
		pager.setAdapter(pagerAdapter);
		
		
		//TODO : THE ACTIVITY SHOULD FINISH IF THE GAME IS ALREADY LANCHED !

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	public void onBackPressed() {
		setResult(be.ac.ucl.lfsab1509.bouboule.Menu.RETURN_MENU);
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