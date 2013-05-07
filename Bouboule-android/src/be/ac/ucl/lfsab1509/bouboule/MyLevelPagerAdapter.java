package be.ac.ucl.lfsab1509.bouboule;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyLevelPagerAdapter extends FragmentPagerAdapter{

	/** Constructor of the class */
	public MyLevelPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	/** This method will be invoked when a page is requested to create */
	@Override
	public Fragment getItem(int arg0) {
		
		MyLevelFragment myLevelFragment = new MyLevelFragment();
		Bundle data = new Bundle();
		data.putInt("current_page", arg0+1);
		myLevelFragment.setArguments(data);
		return myLevelFragment;
	}

	/** Returns the number of pages */
	@Override
	public int getCount() {
		return GlobalSettings.NBLEVELS;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {		
		return "    Level " + ( position + 1 ) + "    ";
	}
	
	
	
	
}
