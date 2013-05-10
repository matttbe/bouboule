package be.ac.ucl.lfsab1509.bouboule;

import java.io.IOException;
import java.io.InputStream;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.EndGameListener;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;




public class MyLevelFragment extends Fragment {

	int mCurrentPage;
	int lastUnlockedWorld;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Setting max level */
		lastUnlockedWorld = GlobalSettings.PROFILE.getBestLevel() / 4 + 1;
		
		/* Getting the arguments to the Bundle object */
		Bundle data = getArguments();

		/* Getting integer data of the key current_page from the bundle */
		mCurrentPage = data.getInt("current_page", 1);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.myfragment_layout, container,false);
		
		InputStream bitmap = null;
		
		try {
			bitmap = getActivity().getAssets().open("level_image/world" + mCurrentPage + ".jpg");
			Bitmap bit = BitmapFactory.decodeStream(bitmap);

			((ImageView) v.findViewById(R.id.levelimage)).setImageBitmap(bit);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(bitmap != null)
				try {
					bitmap.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}	

		
		if ( mCurrentPage <= lastUnlockedWorld) {
			
			ImageButton play = (ImageButton) v.findViewById(R.id.play);

			play.setOnClickListener(clickListener);
			
			ImageButton lock = (ImageButton) v.findViewById(R.id.lock);
			lock.setVisibility(View.INVISIBLE);

		} else { 
			ImageButton play = (ImageButton) v.findViewById(R.id.play);
			play.setVisibility(View.INVISIBLE);
			
			ImageView imageLvl = (ImageView) v.findViewById(R.id.levelimage);
			imageLvl.setColorFilter(new LightingColorFilter(Color.DKGRAY, 1));
		}




		return v;		
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick (View view) {
			if(GlobalSettings.GAME.getScreen() != null)
				EndGameListener.resetGame();
			
			GlobalSettings.PROFILE.setLevel((mCurrentPage-1) * 4 + 1);
			
			getActivity().setResult(Menu.PLAY_GAME);
			
			getActivity().finish();

		}
	};
}
