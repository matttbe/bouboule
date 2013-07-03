package be.ac.ucl.lfsab1509.bouboule.game.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;


public class MenuScreen extends AbstractScreen {
	

	public MenuScreen(boolean bMusicNeedsDelay) {
		super(bMusicNeedsDelay);
	}

	@Override
	public void show() {
		super.show();

		//Set Background
		
		addBackGround("drawable-xhdpi/bgmenu.jpg");
		
		//Create all Buttons - Play Button
		
		Button playButton 		= createButton("transparent", 430, 160, 200, 725);
		Button paramButton 		= createButton("transparent", 430, 160, 200, 555);
		Button scoreButton 	= createButton("transparent", 430, 160, 200, 385);
		
		
		playButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				System.out.println("clickPlay " + x + ", " + y);
				//START NEW GAME;
			}
		});	
		
		paramButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				System.out.println("clickParam " + x + ", " + y);
				//START NEW GAME;
			}
		});	
		
		scoreButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				System.out.println("clickScore " + x + ", " + y);
				//START NEW GAME;
			}
		});	
				
	}
}
