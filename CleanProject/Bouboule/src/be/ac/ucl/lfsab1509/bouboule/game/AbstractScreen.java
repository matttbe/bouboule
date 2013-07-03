package be.ac.ucl.lfsab1509.bouboule.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen implements Screen {
	protected final Stage stage;

	private BitmapFont font;
	private SpriteBatch batch;
	private Skin skin;
	private TextureAtlas atlas;

	public AbstractScreen() {
		final int width = 800; // TODO: GEt SIZE from CAMERA
		final int height = 1250;
		this.stage = new Stage(width, height, true);

	}

	protected String getName() {
		return getClass().getSimpleName();
	}

	protected boolean isGameScreen() {
		return false;
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

	public TextureAtlas getAtlas() {
		if (atlas == null) {
			atlas = new TextureAtlas(
					Gdx.files.internal("image-atlases/pages-info"));
		}
		return atlas;
	}

	protected Skin getSkin() {
		if (skin == null) {
			FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
			// TextureAtlas textureFile = Gdx.files.internal( "skin/uiskin.png"
			// );
			skin = new Skin(skinFile);

			// Skin skin = new Skin();
			// skin.add("logo", new Texture("logo.png"));
		}
		return skin;
	}

	// Screen implementation

	@Override
	public void show() {
		// Gdx.app.log( Tyrian.LOG, "Showing screen: " + getName() );

		// set the stage as the input processor
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize(int width, int height) {
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
		// Gdx.app.log( Tyrian.LOG, "Hiding screen: " + getName() );

		// dispose the screen when leaving the screen;
		// note that the dipose() method is not called automatically by the
		// framework, so we must figure out when it's appropriate to call it
		dispose();
	}

	@Override
	public void pause() {
		// Gdx.app.log( Tyrian.LOG, "Pausing screen: " + getName() );
	}

	@Override
	public void resume() {
		// Gdx.app.log( Tyrian.LOG, "Resuming screen: " + getName() );
	}

	@Override
	public void dispose() {
		// Gdx.app.log( Tyrian.LOG, "Disposing screen: " + getName() );

		// as the collaborators are lazily loaded, they may be null
		if (font != null)
			font.dispose();
		if (batch != null)
			batch.dispose();
		if (skin != null)
			skin.dispose();
		if (atlas != null)
			atlas.dispose();
	}
}
