package be.ac.ucl.lfsab1509.bouboule.game.level;

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
 *    Helene Verhaeghe <helene.verhaeghe@student.uclouvain.be>
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


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import be.ac.ucl.lfsab1509.bouboule.game.ai.AI;
import be.ac.ucl.lfsab1509.bouboule.game.ai.MapNode;
import be.ac.ucl.lfsab1509.bouboule.game.body.Arena;
import be.ac.ucl.lfsab1509.bouboule.game.body.Bouboule;
import be.ac.ucl.lfsab1509.bouboule.game.body.Obstacle;
import be.ac.ucl.lfsab1509.bouboule.game.entity.Entity;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;
import be.ac.ucl.lfsab1509.bouboule.game.profile.BoubImages;
import be.ac.ucl.lfsab1509.bouboule.game.timer.TimerListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * Class that create an XML reader to load all a level on the appropriate time.
 */
public class LevelLoader {

	private XmlReader 			reader;			//XML Reader
	private Element 			root;			//root of the global levelfile
	private Element 			file;			//current level
	private ArrayList<TimerListener> timerListenerArray; //Used to produce new objects

	/**
	 * Contructor of the xml loader.
	 * Automatically load the file : level/newlevels.xml
	 * 
	 * @catch : ParseException
	 * 
	 * 	public LevelLoader()
	 */
	public LevelLoader() {
		this.reader = new XmlReader();
		try {
			root = reader.parse(
					Gdx.files.internal("level/newlevels.xml")
					);
			GlobalSettings.NBLEVELS = root.getChildCount();
		} catch (IOException e) {
			e.printStackTrace();
		}
		timerListenerArray = new ArrayList<TimerListener>(2);

	}

	/**
	 * Load the 'level' in the Element file.
	 * Update the bonus option
	 * 
	 * 	public void loadLevel(String level)
	 */
	public void loadLevel(final String level) {
		if (!timerListenerArray.isEmpty()) { // can be used to produce new obstacles
			for (TimerListener timerListener : timerListenerArray) {
				GlobalSettings.GAME.getTimer().removeTimerListener (timerListener);
			}
			timerListenerArray.clear();
		}


		file = root.getChildByName(level);
		if (file == null) {
			Gdx.app.log("Matth", "Level not found");
			throw new GdxRuntimeException("Level not found");
		}

		readLevelSettings(file);
	}

	private void readLevelSettings(final Element file2) {
		// Bonus
		GraphicManager.ALLOW_BONUS = Boolean.parseBoolean(file.getAttribute("bonus", "false"));
		GraphicManager.BONUS_SPAWN_RATE = Integer.parseInt(file.getAttribute("bonusrate", "0"));
		String bonusEnabled = file.getAttribute("bonusEnabled", null);
		GraphicManager.BONUS_ENABLED = bonusEnabled != null
				? new ArrayList<String>(Arrays.asList(bonusEnabled.split(",")))
						: null;

		// Level
		GraphicManager.TIME = Integer.parseInt(file.getAttribute("time", "30"));

		// AI
		AI.ACC_MAX_AI = Float.parseFloat(file.getAttribute("accmaxai", "0.5f")); 
		AI.ACC_MAX_PLAYER = Float.parseFloat(file.getAttribute("accmaxplayer", "0.5f"));
		AI.countframe = 0;
		AI.IS_INIT=false;

		Gdx.app.log("Settings", "Bonus =" + GraphicManager.ALLOW_BONUS);	
	}

	/**
	 * Load all the Bouboule contained in the 'file' level.
	 * 
	 * 	public void readLevelBouboule(GraphicManager graphicManager)
	 */
	public void readLevelBouboule(final GraphicManager graphicManager) {


		Array<Element> maps = file.getChildrenByName("Bouboule");

		//iterate on all the children 
		for (Iterator<Element> map = maps.iterator(); map
				.hasNext();) {
			Element boub = map.next();

			float radius			= Float.parseFloat(boub.getAttribute("radius"));
			BodyType bodyType		= BodyType.DynamicBody;
			float density			= Float.parseFloat(boub.getAttribute("density"));
			float elasticity		= Float.parseFloat(boub.getAttribute("elasticity"));
			float px				= Float.parseFloat(boub.getAttribute("px"));
			float py				= Float.parseFloat(boub.getAttribute("py"));
			float angle				= Float.parseFloat(boub.getAttribute("angle"));
			int AILevel				= Integer.parseInt(boub.getAttribute("AILevel"));
			short entity			= Short.parseShort(boub.getAttribute("entity"));
			boolean inverted		= Boolean.parseBoolean(boub.getAttribute("inverted", "False"));

			String texRegionPath;			
			String type				= boub.getAttribute("type");
			String directory		= (type.equals("normal")) ? 
					BoubImages.BOUB_DIR_NORMAL : (type.equals("small")) ?
							BoubImages.BOUB_DIR_SMALL : BoubImages.BOUB_DIR_GIANT;

			String jsonFile			= directory + BoubImages.BOUB_JSON_EXT;


			if (entity == Entity.PLAYER) {

				texRegionPath = directory + GlobalSettings.PROFILE.getBoubName()
						+ BoubImages.BOUB_EXTENTION;

			} else {
				
				texRegionPath 		= directory + boub.getAttribute("texRegionPath");
			}



			Bouboule body = new Bouboule(radius, bodyType, density,
					elasticity, px, py, angle, texRegionPath, 
					jsonFile, "boub_" + type, entity, AILevel);
			graphicManager.addBody(body);

			if (inverted) {
				((Sprite) body.getBody().getFixtureList().get(0).getUserData()).rotate90(true);
				((Sprite) body.getBody().getFixtureList().get(0).getUserData()).rotate90(true);
				AI.setInvertedOrientation();

			}
			else // revert axe if it's inverted
				AI.setNormalOrientation();
		}
	}

	private static String extractFileName(final String jsonFile) {
		return jsonFile.substring(jsonFile.lastIndexOf('/') + 1);
	}

	/**
	 * Load the arena of the 'file' level.
	 * 
	 * 	public void readLevelArena(GraphicManager graphicManager)
	 */
	public void readLevelArena(final GraphicManager graphicManager) {

		Element aren = file.getChildByName("Arena");	


		float radius			= Float.parseFloat(aren.getAttribute("radius"));
		float px				= Float.parseFloat(aren.getAttribute("px"));
		float py				= Float.parseFloat(aren.getAttribute("py"));
		float angle				= Float.parseFloat(aren.getAttribute("angle"));
		String file				= aren.getAttribute("file");
		String texRegionPath 	= file + ".jpg";
		String jsonFile 		= file + ".json";
		String jsonName 		= extractFileName(file);
		String cMusicName 		= aren.getAttribute ("music", null);

		graphicManager.addBody(new Arena(radius, px, py,  angle, texRegionPath, 
				jsonFile,  jsonName));

		GlobalSettings.GAME.setNewLoopMusic(cMusicName); // e.g. klez.mp3

		Gdx.app.log("XML", "Arena Loaded :" + jsonName);

	}

	/**
	 * Load the nodes for the AI.
	 * 
	 * public void readLevelMapNodes()
	 */
	public void readLevelMapNodes() {

		Element mapNodes = file.getChildByName("MapNodes");	

		Array<Element> node = mapNodes.getChildrenByName("NodeAllow");

		MapNode mapNode;
		Element newNode;

		GlobalSettings.ARENAWAYPOINTALLOW.clear();

		//iterate on all the children 
		for (Iterator<Element> nodes = node.iterator(); nodes
				.hasNext();) {

			newNode = nodes.next();

			float px			= Float.parseFloat(newNode.getAttribute("px"));
			float py			= Float.parseFloat(newNode.getAttribute("py"));
			float weight		= Float.parseFloat(newNode.getAttribute("weight"));

			mapNode = new MapNode(px, py, weight);
			GlobalSettings.ARENAWAYPOINTALLOW.add(mapNode);
			Gdx.app.log("XML NODES", mapNode.toString());
		}

	}

	/**
	 * Load the obstacles of the 'file' level.
	 * 
	 * 	public void readLevelObstacles(GraphicManager graphicManager)
	 */
	public void readLevelObstacles(final GraphicManager graphicManager) {

		Element obstaclesGroup = file.getChildByName("Obstacles");
		if (obstaclesGroup == null) {
			return;
		}

		Array<Element> obstaclesArray = obstaclesGroup.getChildrenByName("Obstacle");

		Element  newobstacle;

		for (Iterator<Element> obstacleElem = obstaclesArray.iterator(); obstacleElem
				.hasNext();) {

			newobstacle = obstacleElem.next();

			BodyType bodyType		= (newobstacle.getAttribute("bodyType").equals("Dynamic")) 
					? BodyType.DynamicBody : BodyType.StaticBody;

			float density			= Float.parseFloat(newobstacle.getAttribute("density"));
			float elasticity		= Float.parseFloat(newobstacle.getAttribute("elasticity"));
			float px				= Float.parseFloat(newobstacle.getAttribute("px"));
			float py				= Float.parseFloat(newobstacle.getAttribute("py"));
			float angle				= Float.parseFloat(newobstacle.getAttribute("angle"));
			float initAccX			= Float.parseFloat(newobstacle.getAttribute("accx", "0"));
			float initAccY			= Float.parseFloat(newobstacle.getAttribute("accy", "0"));
			float time				= Float.parseFloat(newobstacle.getAttribute("time", "0"));
			String file				= newobstacle.getAttribute("file");
			String texRegionPath 	= file + ".png";
			String jsonFile 		= file + ".json";
			String jsonName 		= extractFileName(file);
			Boolean produce 		= Boolean.parseBoolean(newobstacle.getAttribute("produce",
					"false"));
			Boolean blink	 		= Boolean.parseBoolean(newobstacle.getAttribute("blink",
					"false"));


			if (produce) {
				addContinuousObstacle(graphicManager, time,
						bodyType, density,
						elasticity, px, py, angle, texRegionPath, 
						jsonFile, jsonName, initAccX, initAccY);

			} else {

				Obstacle obs = new Obstacle(bodyType, density,
						elasticity, px, py, angle, texRegionPath, 
						jsonFile, jsonName, initAccX, initAccY);

				graphicManager.addBody(obs);

				if (blink) {
					setBlink(obs , time);
				}

			}

			Gdx.app.log("XML", "Obstacle loaded : " 
						+ produce + jsonName + bodyType.toString() + time);
		}
	}

	
	/**
	 * Generate constantly obstacles : eg. CanonBalls
	 * 
	 * @param graphicManager	: The GraphicManager
	 * @param time				: dt before each generation
	 * @param bodyType			: BodyType
	 * 
	 * 	Parameters of the Obstacle object created
	 * 
	 * @param density 			
	 * @param elasticity		 
	 * @param px
	 * @param py
	 * @param angle
	 * @param texRegionPath
	 * @param jsonFile
	 * @param jsonName
	 * @param initAccX
	 * @param initAccY
	 */
	private void addContinuousObstacle(final GraphicManager graphicManager, final float time,
			final BodyType bodyType, final float density,
			final float elasticity, final float px, final float py, final float angle,
			final String texRegionPath, final String jsonFile, final String jsonName,
			final float initAccX, final float initAccY) {

		TimerListener timerListener = new TimerListener() {
			private int iTimerInc;

			@Override
			public void run() {
				if (iTimerInc % time == 0) {
					
					Gdx.app.log("Obstacle", "reset Obstacle");

					Obstacle obs = new Obstacle(bodyType, density,
							elasticity, px, py, angle, texRegionPath, 
							jsonFile, jsonName, initAccX, initAccY);

					graphicManager.addBody(obs);
				}
				iTimerInc++;
			}

			@Override
			public void newTimer(final int iRemainingTime) {
				iTimerInc = 0;
			}
		};
		GlobalSettings.GAME.getTimer().addTimerListener(timerListener);
		timerListenerArray.add(timerListener);
	}

	/**
	 * Make an obstacle blink
	 * @param obs	: Obstacle to blink
	 * @param time	: blink dt
	 * 
	 * setBlink(final Obstacle obs, final float time)
	 */
	private void setBlink(final Obstacle obs, final float time) {
		TimerListener timerListener = new TimerListener() {
			int iTimerInc;

			@Override
			public void run() {
				iTimerInc++;
				if (iTimerInc % time == 0) {
					obs.inverseBlink();
				}
			}

			@Override
			public void newTimer(final int iRemainingTime) {
				iTimerInc = 0;
				obs.inverseBlink();
			}
		};
		GlobalSettings.GAME.getTimer().addTimerListener(timerListener);
		timerListenerArray.add(timerListener);
	}
	
	/**
	 * 
	 * @return The root of the XML
	 */
	public Element getRoot() {
		return root;
	}
}
