package be.ac.ucl.lfsab1509.bouboule.game.screen;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

// inspired by tuto in steigert.blogspot.com

/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen implements Screen {
	protected final Stage stage;

	private BitmapFont font;
	private SpriteBatch batch;
	private Skin skin;

	private Timer timerMusic;
	private boolean bMusicNeedsDelay;

	public AbstractScreen(boolean bMusicNeedsDelay) {
		this.bMusicNeedsDelay = bMusicNeedsDelay;
		this.stage = new Stage(ScreenGame.APPWIDTH, ScreenGame.APPHEIGHT, true);
	}

	protected String getName() {
		return getClass().getSimpleName();
	}

	// Lazily loaded collaborators

	public BitmapFont getFont() {
		if (font == null) {
			font = new BitmapFont();
		}
		return font;
	}

	public SpriteBatch getBatch() {
		if (batch == null) {
			batch = new SpriteBatch();
		}
		return batch;
	}

	protected Skin getSkin() {
		if (skin == null) {
			FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
			skin = new Skin(skinFile);
		}
		return skin;
	}
	
	protected Button createButton(String skinType, int sizeX, int sizeY, int x, int y) {
		
		Button button = new Button(getSkin(), skinType );
		button.setSize(sizeX, sizeY);
		button.setX(x);
		button.setY(y);
		
		this.stage.addActor(button);
		
		return button;
	}
	
	protected void addBackGround(String imagePath) {
		Image img = new Image(new Texture(imagePath));
		this.stage.addActor(img);
		
	}

	protected Music getMusic() {
		return GlobalSettings.GAME.getMenusMusic();
	}

	protected void playMusicWithDelay (float seconds) {
		if (timerMusic != null) {
			timerMusic.clear();
		}
		timerMusic = new Timer();
		Task task = new Timer.Task() {
			
			@Override
			public void run() {
				// handle the case where we already return to the screen game (we have to be quick!)
				if (! GlobalSettings.GAME.isGameScreen())
					getMusic().play();
			}
		};
		timerMusic.scheduleTask(task, seconds);
	}

	// Screen implementation

	@Override
	public void show() {
		Gdx.app.log("SCREEN", "Showing screen: " + getName());
		// set the stage as the input processor
		Gdx.input.setInputProcessor(stage);
		if (bMusicNeedsDelay)
			playMusicWithDelay(2);
		else
			getMusic().play();
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log("SCREEN", "Resizing screen: " + getName());
	}

	@Override
	public void render(float delta) {
		// (1) process the game logic

		// update the actors
		stage.act(delta);

		// (2) draw the result

		// clear the screen with the given RGB color (black)
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// draw the actors
		stage.draw();
	}

	@Override
	public void hide() {
		Gdx.app.log("SCREEN", "Hiding screen: " + getName());

		// dispose the screen when leaving the screen;
		// note that the dipose() method is not called automatically by the
		// framework, so we must figure out when it's appropriate to call it
		dispose();
	}

	@Override
	public void pause() {
		Gdx.app.log("SCREEN", "Pausing screen: " + getName());
		getMusic().pause();
	}

	@Override
	public void resume() {
		Gdx.app.log("SCREEN", "Resuming screen: " + getName());
		getMusic().play();
	}

	@Override
	public void dispose() {
		Gdx.app.log("SCREEN", "Disposing screen: " + getName());

		// as the collaborators are lazily loaded, they may be null
		if (font != null)
			font.dispose();
		if (batch != null)
			batch.dispose();
		if (skin != null)
			skin.dispose();
	}
}
