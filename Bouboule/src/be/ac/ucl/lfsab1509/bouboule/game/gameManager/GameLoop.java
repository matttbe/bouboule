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

package be.ac.ucl.lfsab1509.bouboule.game.gameManager;

import java.util.Random;

import be.ac.ucl.lfsab1509.bouboule.game.anim.CountDown;
import be.ac.ucl.lfsab1509.bouboule.game.body.Bonus;
import be.ac.ucl.lfsab1509.bouboule.game.body.GameBody;
import be.ac.ucl.lfsab1509.bouboule.game.entity.Entity;
import be.ac.ucl.lfsab1509.bouboule.game.level.LevelLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GameLoop {

	private static GraphicManager graphicManager;
	public static int iBonus = -1;
	private Box2DDebugRenderer debugRenderer;
	private Matrix4 debugMatrix;

	private CountDown countDown;
	private CountDown tutorial;
	private BitmapFont fontOswald;
	private BitmapFont fontOsaka;
	private BitmapFont fontOsakaRed;
	private BitmapFont fontPause;
	private Texture texturePause;
	private float fadePause;
	private Sprite spriteFade;
	private Pixmap pixmapFade;
	private SpriteBatch batch;

	// background
	private Texture background;
	private TextureRegion backgroundRegion;

	private Texture scoreboard;

	private static Random random;

	/**
	 * Launch the creation of the batch thanks to the camera. if debug == true,
	 * set up the debugger matrix
	 * 
	 * @param cam 	: the OrthographicCamera of the game
	 * @param debug : load the debugging matrix
	 * 
	 * GameLoop(OrthographicCamera cam, boolean debug)
	 */
	public GameLoop(final OrthographicCamera cam, final boolean debug) {

		// creation of the batch and matrix (physical edges of the bodies)
		// debugger
		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);

		if (debug) {
			debugMatrix = new Matrix4(cam.combined);
			debugMatrix.scale(GraphicManager.getGameToWorld(),
					GraphicManager.getGameToWorld(), 1f);

			debugRenderer = new Box2DDebugRenderer();
		}

		// Create only once the graphicManager
		graphicManager = new GraphicManager();

		// Load the font
		fontOswald = new BitmapFont(
				Gdx.files.internal("fonts/Oswald/Oswald.fnt"),
				Gdx.files.internal("fonts/Oswald/Oswald.png"), false);
		fontOsaka = new BitmapFont(Gdx.files.internal("fonts/Osaka/Osaka.fnt"),
				Gdx.files.internal("fonts/Osaka/Osaka.png"), false);

		fontOsakaRed = new BitmapFont(
				Gdx.files.internal("fonts/Osaka/Osaka.fnt"),
				Gdx.files.internal("fonts/Osaka/Osaka.png"), false);
		fontOsakaRed.setColor(.95f, .05f, .05f, 1f);

		// Pause
		if (GlobalSettings.GAME.isGdxMenus()) {
			fontPause = new BitmapFont(
					Gdx.files.internal("fonts/OsakaBig/OsakaBig.fnt"),
					Gdx.files.internal("fonts/OsakaBig/OsakaBig.png"), false);
			fontPause.setColor(.95f, .05f, .05f, 1f);
			texturePause = new Texture("buttons/home_button.png");
			pixmapFade = new Pixmap(1, 1, Format.RGB888);
			spriteFade = new Sprite(new Texture(pixmapFade));
			spriteFade.setColor(0, 0, 0, 0);
			spriteFade.setSize(GlobalSettings.APPWIDTH * 2,
					GlobalSettings.APPHEIGHT * 2); // bigger size to cover all the zone
			spriteFade.setPosition(- GlobalSettings.APPWIDTH / 2,
					- GlobalSettings.APPHEIGHT / 2);
		}

		// load the scoreboard
		scoreboard = new Texture("terrain/ScoreBoard/scoreboard.png");

		// load the counter
		countDown = new CountDown(2, 2, 1f, "anim/countdown.png", true); // 3 sec

		// load the tuto
		tutorial = new CountDown(2, 1, 4f, "anim/tuto.png", false);

		// new randomGenerator
		random = new Random();
	}

	/**
	 * Used to (re)start a new game.
	 */
	public void start() {

		Gdx.app.log("Matth", "Dipose of the graphicManager");

		// Clear the graphic Manager for a new use.
		graphicManager.dispose(false);

		// Clear the background for a new use.
		if (background != null)
			background.dispose();
		
		if (backgroundRegion != null && backgroundRegion.getTexture() != null)
			backgroundRegion.getTexture().dispose();

		// Reset EndGame Listener
		EndGameListener.resetListener();

		// load level
		int iLevel = GlobalSettings.PROFILE.getLevel();
		LevelLoader level = GlobalSettings.GAME.getLevel();
		try {
			level.loadLevel("Level" + iLevel);
		} catch (GdxRuntimeException e) {
			level.loadLevel("Level1"); // should not happen...
		}
		String arenaName = level.readLevelArena(graphicManager);
		level.readLevelBouboule(graphicManager);
		level.readLevelObstacles(graphicManager);
		level.readLevelMapNodes();

		background = new Texture(arenaName + "bg.jpg");
		backgroundRegion = new TextureRegion(background);
	}

	/**
	 * Update the ball position thanks to the accelerometer and launch the
	 * physical update function of dt the time between 2 frames
	 * 
	 * update()
	 */
	public void update() {
		graphicManager.update();

		if (GraphicManager.ALLOW_BONUS)
			bonus(false);

	}

	/**
	 * Draw all the needed bodies of the game
	 * @param pause 	: pause the game refresh
	 * @param delta		: delta time between 2 frames
	 *  	
	 * render(final boolean pause, float delta)
	 */
	public boolean render(final boolean pause, float delta) {

		boolean status = false;

		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();


		// Draw all the know bodies

		batch.disableBlending();
		// Allow to draw the background fast because it disable
		// the color blending (override the background).
		
		//If the background is upscaled, we must use a TextureRegion
		if (GlobalSettings.SCALE != 1f) {
			batch.draw(backgroundRegion, GlobalSettings.SHIFT_BG_WIDTH,
					GlobalSettings.SHIFT_BG_HEIGHT, 0f, 0f,
					backgroundRegion.getRegionWidth(),
					backgroundRegion.getRegionHeight(), GlobalSettings.SCALE,
					GlobalSettings.SCALE, 0f);

		} else {
			batch.draw(background, GlobalSettings.SHIFT_BG_WIDTH, 0f);
		}
		
		batch.enableBlending();

		batch.draw(scoreboard, 0, 0);

		graphicManager.draw(batch);

		writeText();

		if (pause) { // draw the countdown or tuto

			if (GlobalSettings.PROFILE.needTutorial()) {
				GlobalSettings.PROFILE.setNeedTutorial(tutorial.draw(batch,
						delta));
				status = true;
			}
			else if (GlobalSettings.GAME.isGeneralPause()) { // only for GdxMenus
				displayPause();
				status = true;
			}
			else {
				if (removingPause())
					status = true;
				else
					status = countDown.draw(batch, delta);
			}
		}

		batch.end();

		/*
		 * batch.begin(); //Draw the debugging matrix
		 * debugRenderer.render(GraphicManager.getWorld(), debugMatrix);
		 * batch.end();
		 */
		return status;
	}

	private void displayBackground() {
		spriteFade.setColor(spriteFade.getColor().r, spriteFade.getColor().g,
				spriteFade.getColor().b, fadePause);
		spriteFade.draw(batch);
	}
	// only for GdxMenus
	private void displayPause() {
		// background => fade to .5 alpha
		if (fadePause < .5f)
			fadePause = Math.min(fadePause + Gdx.graphics.getDeltaTime(), .5f);
		displayBackground();

		fontPause.draw(batch, "PAUSE", GlobalSettings.APPWIDTH / 4,
				GlobalSettings.APPHEIGHT / 2 - 50); // text
		batch.draw(texturePause, 10 * GlobalSettings.HD,
				10 * GlobalSettings.HD); // home button
	}

	/**
	 * @return true if the pause background is being removed
	 */
	private boolean removingPause() {
		if (fadePause <= 0)
			return false;

		fadePause = Math.max(fadePause - Gdx.graphics.getDeltaTime(), 0f);
		displayBackground();

		return true;
	}

	/**
	 * Check if the bonus is eligeable for the current level.
	 * 
	 * @param cBonus	: Bonus to be enabled
	 * @return if the bonus can be used
	 */
	private static boolean canEnabledBonus(final String cBonus) {
		return (GraphicManager.BONUS_ENABLED == null || GraphicManager.BONUS_ENABLED
				.contains(cBonus));
	}

	/**
	 * Create a Bonus instance in the GraphicManager if the spawn rate is
	 * reached
	 * 
	 * private void bonus()
	 */
	public static void bonus(final boolean bForce) {

		if (bForce || random.nextInt(GraphicManager.BONUS_SPAWN_RATE) == 5) {
			// add a new bonus to get more lifes only if we have less than 3
			// lifes
			int nextInt, iNBonus = Entity.BonusType.values().length;
			if (iBonus == -1)
				nextInt = random.nextInt(iNBonus + 2); // 2 lifes
			else
				nextInt = iBonus;

			switch (nextInt) {
			case 0:
				if (GlobalSettings.PROFILE.getNbLifes() < GlobalSettings.MAX_LIFES
						&& canEnabledBonus("life")) {
					Gdx.app.log("bonus", "new heart created");
					graphicManager.addBody(new Bonus(0,
							"bonus/heart/heart.png", "bonus/heart/heart.json",
							"heart", Entity.BonusType.LIVE_UP));
					break;
				}
			case 1:
				if (canEnabledBonus("speed")) {
					Gdx.app.log("bonus", "new speed-created");
					graphicManager.addBody(new Bonus(0,
							"bonus/speed/speed_low.png",
							"bonus/speed/speed_low.json", "speed_low",
							Entity.BonusType.SPEED_LOW));
					break;
				}
			case 2:
				if (canEnabledBonus("invincible")) {
					Gdx.app.log("bonus", "new invincible created");
					graphicManager.addBody(new Bonus(0,
							"bonus/invincible/invincible.png",
							"bonus/invincible/invincible.json", "invincible",
							Entity.BonusType.INVINCIBLE));
					break;
				}
			case 3:
				if (canEnabledBonus("elast")) {
					Gdx.app.log("bonus", "new elasticity+ created");
					graphicManager
							.addBody(new Bonus(0,
									"bonus/elasticity/elasticity_high.png",
									"bonus/elasticity/elasticity_high.json",
									"elasticity_high",
									Entity.BonusType.ELASTICITY_HIGH));
					break;
				}
			case 4:
				if (canEnabledBonus("weight")) {
					Gdx.app.log("bonus", "new weight+ created");
					graphicManager.addBody(new Bonus(0,
							"bonus/weight/weight_high.png",
							"bonus/weight/weight_high.json", "weight_high",
							Entity.BonusType.WEIGHT_HIGH));
					break;
				}
			case 5:
				if (canEnabledBonus("timedown")) {
					Gdx.app.log("bonus", "new timedown created");
					graphicManager.addBody(new Bonus(0,
							"bonus/time/timedown.png",
							"bonus/time/timedown.json", "timedown",
							Entity.BonusType.TIME_DOWN));
					break;
				}
			case 6:
				if (canEnabledBonus("weight")) {
					Gdx.app.log("bonus", "new weight- created");
					graphicManager.addBody(new Bonus(0,
							"bonus/weight/weight_low.png",
							"bonus/weight/weight_low.json", "weight_low",
							Entity.BonusType.WEIGHT_LOW));
					break;
				}
			case 7:
				if (GlobalSettings.PROFILE.getNbLifes() < GlobalSettings.MAX_LIFES
						&& canEnabledBonus("life")) {
					Gdx.app.log("bonus", "new heart created");
					graphicManager.addBody(new Bonus(0,
							"bonus/heart/heart.png", "bonus/heart/heart.json",
							"heart", Entity.BonusType.LIVE_UP));
					break;
				}
			case 8:
				if (canEnabledBonus("elast")) {
					Gdx.app.log("bonus", "new elasticity- created");
					graphicManager.addBody(new Bonus(0,
							"bonus/elasticity/elasticity_low.png",
							"bonus/elasticity/elasticity_low.json",
							"elasticity_low", Entity.BonusType.ELASTICITY_LOW));
					break;
				}
			case 9:
				if (canEnabledBonus("speed")) {
					Gdx.app.log("bonus", "new speed+created");
					graphicManager.addBody(new Bonus(0,
							"bonus/speed/speed_high.png",
							"bonus/speed/speed_high.json", "speed_high",
							Entity.BonusType.SPEED_HIGH));
					break;
				}
			case 10:
				if (canEnabledBonus("invisible")) {
					Gdx.app.log("bonus", "new invisible created");
					graphicManager.addBody(new Bonus(0,
							"bonus/invisible/invisible.png",
							"bonus/invisible/invisible.json", "invisible",
							Entity.BonusType.INVISIBLE));
					break;
				}
			case 11:
				if (canEnabledBonus("timeup")) {
					Gdx.app.log("bonus", "new invisible created");
					graphicManager.addBody(new Bonus(0,
							"bonus/time/timeup.png", "bonus/time/timeup.json",
							"timeup", Entity.BonusType.TIME_UP));
					break;
				}
			case 12:
				if (canEnabledBonus("inverse")) {
					Gdx.app.log("bonus", "new inverse created");
					graphicManager.addBody(new Bonus(0,
							"bonus/inverse/inverse.png",
							"bonus/inverse/inverse.json", "inverse",
							Entity.BonusType.INVERSE));
					break;
				}
			default:
				if (canEnabledBonus("star")) {
					Gdx.app.log("bonus", "new star created");
					graphicManager.addBody(new Bonus(0, "bonus/star/star.png",
							"bonus/star/star.json", "star",
							Entity.BonusType.POINT));
					break;
				}
			}
		}
	}

	public static void removeBody (GameBody body) {
		graphicManager.removeBody(body);
	}

	private CharSequence getTimerCharFromInt(int iNumber) {
		if (iNumber == 0)
			return "00";
		else
			return Integer.toString(iNumber);
	}

	/**
	 * Write the lives/levels/score and Timer text on the screen
	 */
	private void writeText() {
		// avoid crashes and displaying the wrong level at the end of the game
		if (!GlobalSettings.GAME.getTimer().isRunning())
			return;

		CharSequence lives = Integer.toString(GlobalSettings.PROFILE
				.getNbLifes());
		CharSequence levelD = Integer.toString(GlobalSettings.PROFILE
				.getLevel() / 10);
		CharSequence levelU = Integer.toString(GlobalSettings.PROFILE
				.getLevel() % 10);
		CharSequence score = Integer
				.toString(GlobalSettings.PROFILE.getScore());

		int timer = GlobalSettings.PROFILE.getRemainingTime();

		if (timer < 0) {
			EndGameListener.looseGame();
			return;
		}

		CharSequence timerM = Integer.toString(timer / 60);
		CharSequence timerS = getTimerCharFromInt(timer % 60);

		int iPosX = (int) (630 * GlobalSettings.HD);
		int iPosY = (int) (1122 * GlobalSettings.HD);
		if (timer < 6) // last 5 seconds
			fontOsakaRed.draw(batch, timerS + "''",iPosX, iPosY);
		else if (timer < 60)
			fontOsaka.draw(batch, timerS + "''", iPosX, iPosY);
		else
			fontOsaka.draw(batch, timerM + "' " + timerS + "''", iPosX, iPosY);

		iPosY = (int) (1180 * GlobalSettings.HD);
		fontOsaka.draw(batch, lives, iPosX, (int) (1167 * GlobalSettings.HD));
		fontOsaka.draw(batch, score, iPosX, (int) (1205 * GlobalSettings.HD));
		fontOswald.draw(batch, levelD, (int) (285 * GlobalSettings.HD), iPosY);
		fontOswald.draw(batch, levelU, (int) (345 * GlobalSettings.HD), iPosY);

	}

	/**
	 * Remove all the memory used object
	 * 
	 * dispose()
	 */
	public void dispose() {
		// Remove the memory of the managed object and the debug matrix
		if (debugRenderer != null)
			debugRenderer.dispose();
		graphicManager.dispose(true);
		if (background != null)
			background.dispose();
		if (backgroundRegion != null && backgroundRegion.getTexture() != null)
			backgroundRegion.getTexture().dispose();
		scoreboard.dispose();
		countDown.dispose();
		tutorial.dispose();
		fontOsaka.dispose();
		fontOsakaRed.dispose();
		fontOswald.dispose();
		if (fontPause != null) // can be null => Android menus
			fontPause.dispose();
		if (texturePause != null)
			texturePause.dispose();
		if (spriteFade != null)
			spriteFade.getTexture().dispose();
		if (pixmapFade != null)
			pixmapFade.dispose();
		batch.dispose();
	}

	public boolean isCountDownLaunched() {
		return countDown.isLaunched();
	}

	/**
	 * Reset the timer and remove the black background if any
	 */
	public void resumeGame() {
		countDown.reset();
		fadePause = 0;
	}

}
