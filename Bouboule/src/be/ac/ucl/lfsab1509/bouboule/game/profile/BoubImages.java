package be.ac.ucl.lfsab1509.bouboule.game.profile;

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

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings;
import be.ac.ucl.lfsab1509.bouboule.game.level.LevelLoader;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;

public class BoubImages {

	public static final String BOUB_EXTENTION = ".png";

	public static final String BOUB_DIR_NORMAL = "boub/normal/";
	public static final String BOUB_DIR_GIANT  = "boub/giant/";
	public static final String BOUB_DIR_SMALL  = "boub/small/";

	public static final String BOUB_JSON_EXT   = "boub.json";
	
	/**
	 * @return a list of String, each is the name of a bouboule that can be used for the current player
	 */
	public static ArrayList<String> getBoubName() {
		
		ArrayList<String> str = new ArrayList<String>();
		
		// default must always be available
		str.add(GlobalSettings.DEFAULT_BOUB_NAME);
		
		// check all the levels up to the last win
		LevelLoader levelXML = GlobalSettings.GAME.getLevel();
		for (int i = 1; i < GlobalSettings.PROFILE.getBestLevel(); i++) {
			String levelName = "Level" + i;
			Element elementLevel = levelXML.getRoot().getChildByName(levelName); // <LevelX>
			if (elementLevel == null)
				continue;// should not happen, only if missing level
			
			// check all the bouboule in the level
			Array<Element> mapsBouboule = elementLevel.getChildrenByName("Bouboule"); // <Bouboule>
			for (Element elementBouboule : mapsBouboule) {
				short entity = Short.parseShort(elementBouboule.getAttribute("entity")); // 1 for the user
				
				if (entity == -1){ // check if it is the other bouboule
					String temp = elementBouboule.getAttribute("texRegionPath");
					str.add(temp.substring(0, temp.lastIndexOf('.')));
					break; // only one bouboule to parse
				}
			}
		}
		return str;
	}
	
	
	
}
