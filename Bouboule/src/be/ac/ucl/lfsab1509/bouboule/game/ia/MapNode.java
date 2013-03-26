package be.ac.ucl.lfsab1509.bouboule.game.ia;

import com.badlogic.gdx.math.Vector2;

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

	public String toString(){
		return "px :"+this.vector.x+" py :"+this.vector.y+" weigth :"+ this.weight;
	}
}
