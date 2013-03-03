package be.ac.ucl.lfsab1509.bouboule.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class GameBody {
	
	protected Body body;
	
	public Boolean isAlive;
	
	/*object direction depending on the force applied to it*/
	public Vector2 positionVector;
	
	
	public GameBody(){
		
		isAlive			= true;
		positionVector 	= new Vector2();
	}
	
	public void MakeBody(float width,float  height,float radius,BodyDef.BodyType bodyType,
			float density,float elasticity, Vector2 pos,float angle){
		
		World world = GraphicManager.getWorld();
		
		
		//Set up of a body that has a physical interpretation
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.type 	= bodyType;
		bodyDef.angle	= angle;
		bodyDef.position.set(GraphicManager.convertToBox(pos.x), GraphicManager.convertToBox(pos.y));
		//dont forget to use the game dimension instead of real world dimension
		
		//Storage in the main variable
		body = world.createBody(bodyDef);
		
		if(radius==0)
		{
			makeRectBody(width,height,bodyType,density,elasticity,pos,angle);
	 		
	 	}else{
	 		makeCircleBody(radius,bodyType,density,elasticity,pos,angle);
	 	}
 

		//Set up of the Vector2 that define the object durection
		
		positionVector.set(GraphicManager.convertToWorld(body.getPosition().x),
				GraphicManager.convertToWorld(body.getPosition().y));
		
	}
	
	public Vector2 getPositionVector(){
		
		return positionVector;
	}
	
	void makeRectBody(float width,float height,BodyDef.BodyType bodyType,
			float density,float restitution, Vector2 pos,float angle){
		
		
		/** IN case of future need**/
	}
	
	void makeCircleBody(float radius,BodyDef.BodyType bodyType,
			float density,float elasticity, Vector2 pos,float angle){
		
		//Basoic Object definition for Physics
		FixtureDef fixtureDef	= new FixtureDef();
 		fixtureDef.density		= density;
 		fixtureDef.restitution	= elasticity;
 		fixtureDef.shape		= new CircleShape();
 		
 		//Game adimenstionalition
 		fixtureDef.shape.setRadius(GraphicManager.convertToBox(radius));
 		
 		body.createFixture(fixtureDef);
		fixtureDef.shape.dispose();
	}
	
	public void DestroyBody(){
		
		if(body!=null)
		{
			isAlive=false;
			GraphicManager.getWorld().destroyBody(body);
			body=null;
		}
	}
	
	public void updatePositionVector(){
		
		Gdx.app.log ("updatePositionVector", positionVector.x + " " +positionVector.y );
		
		positionVector.set(GraphicManager.convertToWorld(body.getPosition().x),
				GraphicManager.convertToWorld(body.getPosition().y));	
	}
	
	public void SetPosition(float wx,float wy){
		
		wx=GraphicManager.convertToBox(wx);
		wy=GraphicManager.convertToBox(wy);

		body.setTransform(wx, wy, body.getAngle());
		updatePositionVector();
	}
	
	public void SetPosition(Vector2 v){
		SetPosition(v.x, v.y);
	}
	
	public abstract void draw(SpriteBatch batch);
	
	
	/**TODO: WEIRD !!!**/
	public void update(float dt){ 
		
		Gdx.app.log ("GameBody", "updatePositionVector");
		
		updatePositionVector();
	}

}
