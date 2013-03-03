package be.ac.ucl.lfsab1509.bouboule.game.gameManager;

import java.util.ArrayList;

import be.ac.ucl.lfsab1509.bouboule.game.body.GameBody;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GraphicManager {
	
	static World world;
	static final float GAME_TO_WORLD=100.0f;
	static final float WORLD_TO_GAME=0.01f;
	
	public ArrayList<GameBody> bodies;
	
	boolean isPaused;
	
	
	public GraphicManager(){
		world=new World(new Vector2(0,0), true);
		bodies=new ArrayList<GameBody>();
		isPaused=false;
	}
	
	public static World getWorld(){
		return world;
	}

	public static float convertToBox(float x){
		return x*WORLD_TO_GAME;
	}
	
	public static float convertToWorld(float x){
		return x*GAME_TO_WORLD;
	}
	
	public void pause(){
		isPaused=true;
	}
	
	public void resume(){
		isPaused=false;
	}
	
	public void dispose(){
		for(GameBody body:bodies){
			body.DestroyBody();
		}
		bodies.clear();
	}
	
	public void addBody(GameBody body){
		bodies.add(body);
	}
	
	public void update(float dt){
		for(GameBody body:bodies){
			
			world.step(GlobalSettings.BOX_STEP,GlobalSettings.VELOCITY_ITERATIONS, GlobalSettings.POSITION_ITERATIONS);
			body.update(dt);
		}
	}
	
	public void draw(SpriteBatch batch){
		for(GameBody body:bodies){
			body.draw(batch);
		}
	}
	


	
}
