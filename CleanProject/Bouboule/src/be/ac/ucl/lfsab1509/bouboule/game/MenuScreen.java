package be.ac.ucl.lfsab1509.bouboule.game;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class MenuScreen extends AbstractScreen {

	//private Table table;

	public MenuScreen()
	{
		super();
	}

	@Override
	public void show()
	{
		super.show();

		//Create all Buttons - Play Button

		Button startGameButton = new Button(getSkin(), "transparent" );

		startGameButton.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				System.out.println("click " + x + ", " + y);
				//START NEW GAME;
			}
		});			


		//Parameter Button 

		Button startGameButton2 = new Button(getSkin(), "transparent" );

		startGameButton2.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				System.out.println("click " + x + ", " + y);
				//START NEW GAME;
			}
		});


		//HighScoreButton

		Button startGameButton3 = new Button(getSkin(), "transparent" );

		startGameButton3.addListener( new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				System.out.println("click " + x + ", " + y);
				//START NEW GAME;
			}
		});
		
		//Create Image for BackGround
		
		Image img = new Image(new Texture("skin/bgmenu.png"));

		//Set Buttons Position
		//1
		startGameButton.setSize(430, 160);
		startGameButton.setX(200);
		startGameButton.setY(725);
		//2
		startGameButton2.setSize(430, 160);
		startGameButton2.setX(200);
		startGameButton2.setY(555);
		//3
		startGameButton3.setSize(430, 160);
		startGameButton3.setX(200);
		startGameButton3.setY(385);
		
		

		//Setting all the Buttons;
		stage.addActor( img);
		stage.addActor( startGameButton);
		stage.addActor( startGameButton2);
		stage.addActor( startGameButton3);


	}
}
