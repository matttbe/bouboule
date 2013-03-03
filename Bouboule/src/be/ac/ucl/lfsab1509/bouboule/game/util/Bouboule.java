package be.ac.ucl.lfsab1509.bouboule.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Bouboule extends GameBody{
	
	TextureRegion texture;
	float radius;
	
	
	public Bouboule(float radius, BodyType bodyType,float density,
			float elasticity,float px,float py, float angle,TextureRegion texRegion) {
		
		super();
		
		Vector2 pos	= new Vector2(px,py);
		texture		= new TextureRegion(texRegion);//, pos); TODO:VERIF
		this.radius	= radius;

		MakeBody(0, 0, radius, bodyType, density, elasticity, pos, angle);
	}
	
	public void draw(SpriteBatch sp) {
		// TODO Auto-generated method stub
		if(isAlive){
			
			sp.draw(texture, positionVector.x-radius, positionVector.y-radius);
			//TODO: SET up;
		}
	}
	
	public void update(float dt){
		if(isAlive){
			
			Gdx.app.log ("Bouboule", "Update boubloule");
			super.update(dt);
			
			
			//TODO: set UP;
		}
		
	}
	
	public float GetBodyRotationInDegrees(){
		return body.getAngle()*MathUtils.radiansToDegrees;
	}
	
}
