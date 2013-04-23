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
import java.util.Iterator;

import be.ac.ucl.lfsab1509.bouboule.game.body.Arena;
import be.ac.ucl.lfsab1509.bouboule.game.body.Bouboule;
import be.ac.ucl.lfsab1509.bouboule.game.body.Obstacle;
import be.ac.ucl.lfsab1509.bouboule.game.entity.Entity;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;
import be.ac.ucl.lfsab1509.bouboule.game.ia.IA;
import be.ac.ucl.lfsab1509.bouboule.game.ia.MapNode;
import be.ac.ucl.lfsab1509.bouboule.game.profile.BoubImages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/*
 * Class that create an XML reader to load all a level on the appropriate time.
 */
public class LevelLoader {

	private XmlReader 			reader;			//XML Reader
	private Element 			root;			//root of the global levelfile
	private Element 			file;			//current level
	private int 				iNbLevels;		//Number of levels

	/**
	 * Contructor of the xml loader
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
			this.iNbLevels = root.getChildCount ();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Return the number of levels
	 * 
	 * 	public int getNbLevels ()
	 */
	public int getNbLevels () {
		return iNbLevels;
	}

	/**
	 * Load the 'level' in the Element file
	 * Update the bonus option
	 * 
	 * 	public void loadLevel(String level)
	 */
	public void loadLevel(String level) {
		file = root.getChildByName(level);
		if (file == null) {
			Gdx.app.log("Matth", "Level not found");
			throw new GdxRuntimeException ("Level not found");
		}
		
		GraphicManager.ALLOW_BONUS = Boolean.parseBoolean(file.getAttribute("bonus","false"));
		GraphicManager.BONUS_SPAWN_RATE = Integer.parseInt(file.getAttribute("bonusrate","0"));
		GraphicManager.TIME = Integer.parseInt(file.getAttribute("time", "30"));
		IA.FORCE_MAX_IA = Float.parseFloat(file.getAttribute("forcemaxia", "0.5f"));
		IA.FORCE_MAX_PLAYER = Float.parseFloat(file.getAttribute("forcemaxplayer", "0.5f"));
		
		Gdx.app.log("Settings", "Bonus ="+GraphicManager.ALLOW_BONUS);	

	}

	/**
	 * Load all the Bouboule containt in the 'file' level
	 * 
	 * 	public void readLevelBouboule(GraphicManager graphicManager)
	 */
	public void readLevelBouboule(GraphicManager graphicManager) {


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
			int IALevel				= Integer.parseInt(boub.getAttribute("IALevel"));
			short entity			= Short.parseShort(boub.getAttribute("entity"));
			
			String texRegionPath;			
			String type				= boub.getAttribute("type");
			String directory		= (type.equals("normal")) ? 
											BoubImages.BOUB_DIR_NORMAL : (type.equals("small")) ?
													BoubImages.BOUB_DIR_SMALL : BoubImages.BOUB_DIR_GIANT;
			
			String jsonFile			= directory+BoubImages.BOUB_JSON_EXT;
			
			
			if (entity == Entity.PLAYER)
				
				texRegionPath = directory+GlobalSettings.PROFILE.getBoubName ()+BoubImages.BOUB_EXTENTION;

			else
				texRegionPath 		= directory+boub.getAttribute("texRegionPath");


			Gdx.app.log("XML",

					"radius :"+
							radius			+" bodyType :"+
							bodyType		+" density :"+
							density			+" elasticity:"+
							elasticity		+" px:"+
							px				+" py :"+
							py				+" angle :"+
							angle				+" tex :"+
							texRegionPath 	+" jsonFile :"+
							jsonFile 		+ "jsonName :"+
							"boub_"+type    +" entity :"+
							entity			+" IA :"+
							IALevel
					);
			
			graphicManager.addBody( new Bouboule(radius, bodyType, density,
					elasticity, px, py, angle,texRegionPath, 
					jsonFile, "boub_"+type, entity, IALevel));




			


		}
	}

	private static String extractJSonName (String jsonFile) {
		return jsonFile.substring (jsonFile.lastIndexOf ('/') + 1,
				jsonFile.lastIndexOf ('.'));
	}

	/**
	 * Load the arena of the 'file' level
	 * 
	 * 	public void readLevelArena(GraphicManager graphicManager)
	 */
	public void readLevelArena(GraphicManager graphicManager) {

		Element aren = file.getChildByName("Arena");	


		float radius			= Float.parseFloat(aren.getAttribute("radius"));
		float px				= Float.parseFloat(aren.getAttribute("px"));
		float py				= Float.parseFloat(aren.getAttribute("py"));
		float angle				= Float.parseFloat(aren.getAttribute("angle"));
		String texRegionPath 	= aren.getAttribute("texRegionPath");
		String jsonFile 		= aren.getAttribute("jsonFile");
		String jsonName 		= extractJSonName (jsonFile);

		graphicManager.addBody(new Arena( radius, px, py,  angle, texRegionPath, 
				jsonFile,  jsonName));

		Gdx.app.log("XML","Arena Loaded :"+jsonName);
		
	}

	/**
	 * Load the nodes for the IA
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
	 * Load the obstacles of the 'file' level
	 * 
	 * 	public void readLevelObstacles(GraphicManager graphicManager)
	 */
	public void readLevelObstacles(GraphicManager graphicManager) {

		Element obstaclesGroup = file.getChildByName("Obstacles");	

		Array<Element> obstaclesArray = obstaclesGroup.getChildrenByName("Obstacle");

		Element  newobstacle;

		for (Iterator<Element> obstacleElem = obstaclesArray.iterator(); obstacleElem
				.hasNext();) {

			newobstacle = obstacleElem.next();
			
			BodyType bodyType		= (newobstacle.getAttribute("bodyType") == ("Dynamic")) 
					? BodyType.DynamicBody : BodyType.StaticBody;
			
			float density			= Float.parseFloat(newobstacle.getAttribute("density"));
			float elasticity		= Float.parseFloat(newobstacle.getAttribute("elasticity"));
			float px				= Float.parseFloat(newobstacle.getAttribute("px"));
			float py				= Float.parseFloat(newobstacle.getAttribute("py"));
			float angle				= Float.parseFloat(newobstacle.getAttribute("angle"));
			String texRegionPath 	= newobstacle.getAttribute("texRegionPath");
			String jsonFile 		= newobstacle.getAttribute("jsonFile");
			String jsonName 		= extractJSonName (jsonFile);


			graphicManager.addBody( new Obstacle(bodyType, density,
					elasticity, px, py, angle,texRegionPath, 
					jsonFile, jsonName));
			
			Gdx.app.log("XML", "Obstacle loaded : "+jsonName);
		}
	}
}
