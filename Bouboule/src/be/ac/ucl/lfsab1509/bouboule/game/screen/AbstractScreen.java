package be.ac.ucl.lfsab1509.bouboule.game.screen;

/*
 * This file is part of Bouboule.
 * 
 * Copyright 2013 UCLouvain
 * 
 * Authors:
 *  * Group 7 - Course: http://www.uclouvain.be/en-cours-2013-lfsab1509.html
 *    Matthieu Baerts <matthieu.baerts@student.uclouvain.be>
 *    Baptiste Remy <baptiste.remy@student.uclouvain.be>
 *    Nicolas Van Wallendael <nicolas.vanwallendael@student.uclouvain.be>
 *    Hélène Verhaeghe <helene.verhaeghe@student.uclouvain.be>
 * 
 * Bouboule is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
	private ArrayList<Texture> textureList;

	// fade
	private Sprite spriteFade;
	private float fFade = 1f; // with fade in
	private SpriteBatch fadeBatch;
	private Screen nextScreen;
	private boolean bNeedFading;

	private Timer timerMusic;
	private boolean bMusicNeedsDelay;

	public AbstractScreen(boolean bMusicNeedsDelay) {
		this.bMusicNeedsDelay = bMusicNeedsDelay;
		this.stage = new Stage(GlobalSettings.APPWIDTH,
				GlobalSettings.APPHEIGHT, true);
		// Added the camera to enter the screen (black borders on each side)
		this.stage.setCamera(GlobalSettings.GAME.getCamera());

		if (GlobalSettings.GAME.isGdxMenus()) { // for the transition
			spriteFade = new Sprite(new Texture(new Pixmap(1, 1, Format.RGB888)));
			spriteFade.setColor(Color.BLACK);
			spriteFade.setSize(GlobalSettings.APPWIDTH, GlobalSettings.APPHEIGHT);
		}
		textureList = new ArrayList<Texture>();
	}

	protected String getName() {
		return getClass().getSimpleName(); // @@COMMENT_GWT@@
//		return ""; // @@UNCOMMENT_GWT@@ // let the comment at the beginning!!!
	}

	// Lazily loaded collaborators

	public BitmapFont getFont() {
		if (font == null) {
			font = new BitmapFont();
		}
		return font;
	}

	protected CheckBoxStyle getCheckBoxStyle() {
		TextureRegionDrawable checkboxOn =
				getDrawableFromFile("skin/checkboxon.png", true);
		TextureRegionDrawable checkboxOff =
				getDrawableFromFile("skin/checkboxoff.png", true);
		return new CheckBoxStyle(checkboxOff, checkboxOn, getSkin().getFont(
				"osaka-font"), Color.WHITE);
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

	protected void setSkin(String path) {
		if (skin != null)
			skin.dispose();
		FileHandle skinFile = Gdx.files.internal(path);
		skin = new Skin(skinFile);
	}

	/**
	 * @return a TextureRegionDrawable (Drawable) which is disposable via:
	 *           `getRegion().getTexture().dispose();`
	 *          if bAutomaticallyDisposeTexture is false.
	 */
	protected TextureRegionDrawable getDrawableFromFile(String path,
			boolean bAutomaticallyDisposeTexture) {
		Texture texture = new Texture(path);
		if (bAutomaticallyDisposeTexture)
			textureList.add(texture);
		return new TextureRegionDrawable(new TextureRegion(texture));
	}

	protected Label addLabel(String text, String fontName, float scale,
			Color color, int x, int y) {

		Label label = new Label(text, getSkin(), fontName, color);
		label.setX(x);
		label.setY(y);
		if (scale != 0)
			label.setFontScale(scale);
		stage.addActor(label);

		return label;
	}

	protected Button createButton(String skinType, int sizeX, int sizeY, int x,
			int y) {
		Button button = new Button(getSkin(), skinType);
		if (sizeX != 0 && sizeY != 0)
			button.setSize(sizeX, sizeY);
		button.setX(x);
		button.setY(y);

		this.stage.addActor(button);

		return button;
	}

	/**
	 * Display a 'back button'
	 * @param bReturnToInitMenu true => init menu ; false => ParamScreen
	 */
	protected void addBackButton(final boolean bReturnToInitMenu) {

		// Image backButton = addImage(TODO, 10, 10); // TODO add image!
		//______ REMOVE
		Button backButton = new Button(getSkin(), "default");
		backButton.setColor(1, 0, 0, 1);
		backButton.setSize(100, 100);
		backButton.setX(10);
		backButton.setY(10);
		this.stage.addActor(backButton);
		//______ REMOVE

		backButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("SCREEN", "clickBack " + x + ", " + y);
				if (bReturnToInitMenu)
					nextScreen = new MenuScreen();
				else
					nextScreen = new ParamScreen();
				bNeedFading = true;

				/* => to launch the right screen by given its name
				 *    e.g. "MenuScreen".
				 *    But we only need to return to the InitMenu or ParamScreen
				 */
				/*
				try {
					GlobalSettings.GAME.setScreen((Screen) Class.forName(
							this.getClass().getPackage().getName() + "."
									+ screenName).newInstance());
				} catch (InstantiationException e) {
					Gdx.app.error("SCREEN", "error instantiation " + screenName);
				} catch (IllegalAccessException e) {
					Gdx.app.error("SCREEN", "error illegalaccess " + screenName);
				} catch (ClassNotFoundException e) {
					Gdx.app.error("SCREEN", "class not found " + screenName);
				}*/
			}
		});
	}

	protected Image addBackGround(String imagePath) {
		return addImage(imagePath, 0, 0);

	}

	/**
	 * @return an Image created from a Texture which will be automatically
	 *         disposed.
	 */
	protected Image addImage(String imagePath, int x, int y) {
		Texture texture = new Texture(imagePath);
		textureList.add(texture);

		Image img = new Image(texture);
		img.setPosition(x, y);
		this.stage.addActor(img);
		return img;
	}

	protected CheckBox addCheckBox(String text, boolean bChecked, int x, int y) {
		CheckBox checkBox = new CheckBox(" " + text, getCheckBoxStyle());
		checkBox.setX(x);
		checkBox.setY(y);
		checkBox.setChecked(bChecked);
		stage.addActor(checkBox);
		return checkBox;
	}

	protected Music getMusic() {
		return GlobalSettings.GAME.getMenusMusic();
	}

	protected void playMusicWithDelay(float seconds) {
		if (timerMusic != null) {
			timerMusic.clear();
		}
		timerMusic = new Timer();
		Task task = new Timer.Task() {

			@Override
			public void run() {
				// handle the case where we already return to the screen game
				// (we have to be quick!)
				if (!GlobalSettings.GAME.isGameScreen() && 
						! GlobalSettings.SOUND_IS_MUTED)
					getMusic().play();
			}
		};
		timerMusic.scheduleTask(task, seconds);
	}

	/**
	 * Set the next screen with a fading
	 * @param nextScreen the next screen or null to launch the game
	 */
	protected void setScreenWithFading(Screen nextScreen) {
		this.nextScreen = nextScreen;
		bNeedFading = true;
	}

	// Screen implementation

	@Override
	public void show() {
		Gdx.app.log("SCREEN", "Showing screen: " + getName());
		// set the stage as the input processor
		Gdx.input.setInputProcessor(stage);
		if (! GlobalSettings.SOUND_IS_MUTED) {
			if (bMusicNeedsDelay)
				playMusicWithDelay(2);
			else
				getMusic().play();
		}
		if (GlobalSettings.GAME.isGdxMenus()) {
			fadeBatch = new SpriteBatch();
			fadeBatch.getProjectionMatrix().setToOrtho2D(0, 0, 2, 2);
		}
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

		// Fade => like paxbritanica
		if (!bNeedFading && fFade > 0) { // new screen
			fFade = Math.max(fFade - Gdx.graphics.getDeltaTime() / 2f, 0); // slower
			fadeBatch.begin();
			spriteFade.setColor(spriteFade.getColor().r, spriteFade.getColor().g,
					spriteFade.getColor().b, fFade);
			spriteFade.draw(fadeBatch);
			fadeBatch.end();
		}

		if (bNeedFading) { // quit screen
			fFade = Math.min(fFade + Gdx.graphics.getDeltaTime() * 2f, 1); // faster
			fadeBatch.begin();
			spriteFade.setColor(spriteFade.getColor().r, spriteFade.getColor().g,
					spriteFade.getColor().b, fFade);
			spriteFade.draw(fadeBatch);
			fadeBatch.end();
			if (fFade >= 1) { // switch
				if (nextScreen != null)
					GlobalSettings.GAME.setScreen(nextScreen);
				else
					GlobalSettings.GAME.setScreenGame();
			}
		}
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
		if (! GlobalSettings.SOUND_IS_MUTED)
			getMusic().pause();
	}

	@Override
	public void resume() {
		Gdx.app.log("SCREEN", "Resuming screen: " + getName());
		if (! GlobalSettings.SOUND_IS_MUTED)
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
		if (fadeBatch != null)
			fadeBatch.dispose();
		for (Texture texture : textureList) {
			texture.dispose();
		}
	}
}
