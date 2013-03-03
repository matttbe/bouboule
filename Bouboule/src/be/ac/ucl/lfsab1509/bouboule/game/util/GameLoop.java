package be.ac.ucl.lfsab1509.bouboule.game.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;


public class GameLoop {
	
	GraphicManager 		graphicManager;
	Box2DDebugRenderer 	debugRenderer;
	Matrix4 			debugMatrix;
	
	SpriteBatch			batch;
	
	Bouboule		 	bouboule;
	TextureRegion 		boubouleImg;
	
	//Texture 			arenaImg;
	
	
	public GameLoop(OrthographicCamera cam){

		batch 			= new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		
		debugMatrix		= new Matrix4(cam.combined);
		debugMatrix.scale(GraphicManager.GAME_TO_WORLD, GraphicManager.GAME_TO_WORLD, 1f);

		debugRenderer	= new Box2DDebugRenderer();
		
		graphicManager = new GraphicManager();
		
		init();
	}
	
	private void init() {
		
		initArena();
		initBall();
		
	}

	
	private void initBall() {
		
		boubouleImg = new TextureRegion( new Texture(Gdx.files.internal("images/boub.png")));
		
				
		int gRadius		= 50;
		int gPositionX	= 400;
		int gPositionY	= 280;
		
		bouboule = new Bouboule(gRadius, BodyType.DynamicBody,
				1, 1, gPositionX, gPositionY, 0, boubouleImg);
		
		//ball.SetTextureDimension(TextureDimensions.BALL_WIDTH, TextureDimensions.BALL_HEIGHT);
		graphicManager.addBody(bouboule);
		
	}
	
	private void initArena() {
		
		//TODO: set Up;
		

		
		
	}
	
	public void update(float dt) {
		
		Gdx.app.log ("World INFO", "Gravity = "+ GraphicManager.getWorld().getGravity() + "\n Nunber of Obj " 
				+ GraphicManager.getWorld().getBodyCount() +"" + GraphicManager.getWorld().getBodies().next().getMass() );
		
		Gdx.app.log("Bouboule Mass", "Mass " + bouboule.body.getMass());
		
		Gdx.app.log("Bouboule Active ?","Active ? = " + bouboule.body.isActive());
		
		//bouboule.body.applyForceToCenter(new Vector2(1000,-2000));
		
		Gdx.app.log("Bouboule Velocity","Velocity " + bouboule.body.getLinearVelocity());

		graphicManager.update(dt);
	}
	
	public void render() {

		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		batch.disableBlending();
		//arena.Draw(batch);
		
		batch.enableBlending();
		graphicManager.draw(batch);
		
		
		batch.end();
		
		batch.begin();
		debugRenderer.render(GraphicManager.getWorld(), debugMatrix);
		batch.end();
	}
	

	public void dispose() {
		// TODO Auto-generated method stub
		debugRenderer.dispose();
		graphicManager.dispose();
	}

	
	
}
