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
import java.util.Iterator;

import be.ac.ucl.lfsab1509.bouboule.game.body.Arena;
import be.ac.ucl.lfsab1509.bouboule.game.body.Bouboule;
import be.ac.ucl.lfsab1509.bouboule.game.body.Obstacle;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;
import be.ac.ucl.lfsab1509.bouboule.game.ia.MapNode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class LevelLoader {

	private XmlReader 			reader;
	private Element 			root;
	private Element 			file;
	private int 				iNbLevels;

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

	public int getNbLevels () {
		return iNbLevels;
	}

	public void loadLevel(String level) {
		file = root.getChildByName(level);
		if (file == null)
			throw new GdxRuntimeException ("Level not found");

	}

	public void readLevelBouboule(GraphicManager graphicManager) {


		Array<Element> maps = file.getChildrenByName("Bouboule");

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
			String texRegionPath 	= boub.getAttribute("texRegionPath");
			String jsonFile 		= boub.getAttribute("jsonFile");
			String jsonName 		= boub.getAttribute("jsonName");
			short entity			= Short.parseShort(boub.getAttribute("entity"));
			int IALevel				= Integer.parseInt(boub.getAttribute("IALevel"));


			graphicManager.addBody( new Bouboule(radius, bodyType, density,
					elasticity, px, py, angle,texRegionPath, 
					jsonFile, jsonName, entity, IALevel));




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
							jsonFile 		+" name :"+
							jsonName 		+" entity :"+
							entity			+" IA :"+
							IALevel
					);


		}
	}

	public void readLevelArena(GraphicManager graphicManager) {

		Element aren = file.getChildByName("Arena");	


		float radius			= Float.parseFloat(aren.getAttribute("radius"));
		float px				= Float.parseFloat(aren.getAttribute("px"));
		float py				= Float.parseFloat(aren.getAttribute("py"));
		float angle				= Float.parseFloat(aren.getAttribute("angle"));
		String texRegionPath 	= aren.getAttribute("texRegionPath");
		String jsonFile 		= aren.getAttribute("jsonFile");
		String jsonName 		= aren.getAttribute("jsonName");
		short entity			= Short.parseShort(aren.getAttribute("entity"));

		graphicManager.addBody(new Arena( radius, px, py,  angle, texRegionPath, 
				jsonFile,  jsonName, entity));

	}

	public void readLevelMapNodes() {

		Gdx.app.log("3DEBUG", "ENTERING MAPD NODE READER");

		Element mapNodes = file.getChildByName("MapNodes");	

		Array<Element> node = mapNodes.getChildrenByName("NodeAllow");

		MapNode mapNode;
		Element newNode;

		GlobalSettings.ARENAWAYPOINTALLOW = new ArrayList<MapNode>();

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
			String jsonName 		= newobstacle.getAttribute("jsonName");
			short entity			= Short.parseShort(newobstacle.getAttribute("entity"));


			graphicManager.addBody( new Obstacle(bodyType, density,
					elasticity, px, py, angle,texRegionPath, 
					jsonFile, jsonName, entity));
		}
	}
}
