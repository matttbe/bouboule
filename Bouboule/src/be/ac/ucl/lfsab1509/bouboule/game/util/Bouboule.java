package be.ac.ucl.lfsab1509.bouboule.game.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Bouboule extends GameBody{
	
	TextureRegion texture;
	
	
	public Bouboule(float radius, BodyType bodyType,float density,
			float elasticity,float px,float py, float angle,TextureRegion texRegion) {
		
		super();
		
		Vector2 pos	= new Vector2(px,py);
		texture		= new TextureRegion(texRegion);//, pos); TODO:VERIF

		MakeBody(0, 0, radius, bodyType, density, elasticity, pos, angle);
	}
	
	public void draw(SpriteBatch sp) {
		// TODO Auto-generated method stub
		if(isAlive){
			
			
			//TODO: SET up;
		}
	}
	
	public void Update(float dt){
		if(isAlive){
			super.update(dt);
			
			
			//TODO: set UP;
		}
		
	}
	
	public float GetBodyRotationInDegrees(){
		return body.getAngle()*MathUtils.radiansToDegrees;
	}
	
}
