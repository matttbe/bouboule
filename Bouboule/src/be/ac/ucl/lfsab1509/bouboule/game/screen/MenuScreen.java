package be.ac.ucl.lfsab1509.bouboule.game.screen;


import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;

public class MenuScreen extends AbstractScreen {
	
	private Table table;

	public MenuScreen()
	{
		super();
	}

	@Override
	public void show()
	{
		super.show();

		// retrieve the custom skin for our 2D widgets
		Skin skin = super.getSkin();

		// create the table actor and add it to the stage
		table = new Table( skin );
		table.setWidth(stage.getWidth());
		table.setHeight(stage.getHeight());
		stage.addActor( table );

		// retrieve the table's layout
		//TableLayout layout = table.getTableLayout();

		// register the button "start game"
		//TextButton startGameButton = new TextButton( "Start game", getSkin() );
		
		TextButton startGameButton = new TextButton( "transparent", getSkin(), "transparent" );
		startGameButton.setWidth(400);
		startGameButton.setHeight(400);
		
		
		startGameButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "click " + x + ", " + y);
				GlobalSettings.GAME.setScreenGameResume();
				GlobalSettings.GAME.setScreenGame();
				GlobalSettings.GAME.setScreenGameResume();
			}
		});
		
		//startGameButton.setHeight(100);
		//startGameButton.setPosition(100, 100);


		startGameButton.setColor(Color.RED);
		
		table.add(startGameButton);

		//layout.register( "startGameButton", startGameButton );
		
	}
}
