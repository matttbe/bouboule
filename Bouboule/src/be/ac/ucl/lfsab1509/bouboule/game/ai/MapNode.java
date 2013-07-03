package be.ac.ucl.lfsab1509.bouboule.game.ai;

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


import com.badlogic.gdx.math.Vector2;

/*
 * Class that construct an Object witch contains a position and 
 * a weight of points on the screen that limitate the action on restricted
 * area of the game level arena. 
 */
public class MapNode {
	
	private Vector2 vector;
	private float weight;
	
	public MapNode(final float px, final float py, final float poids) {
		
		this.vector = new Vector2(px, py);
		this.weight	= poids;
	}

	public Vector2 getVector() {
		return vector;
	}

	public float getWeight() {
		return weight;
	}

	public String toString() {
		return "px :" + this.vector.x + " py :" + this.vector.y + " weigth :" + this.weight;
	}
	
	public float xToPixel() {
		
		return 64.762f + 99.085f * vector.x;
	}
	
	public float yToPixel() {
		
		return (float) (55.784+100.588 * vector.y);
	}
	
	public float weightToPixel() {
		
		return 100 * weight;
	}
}
