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
	Bouboule		 	bouboule2;
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
		initBall2();
		
	}

	
	private void initBall() {
		
		boubouleImg = new TextureRegion( new Texture(Gdx.files.internal("images/boub.png")));
		
				
		int gRadius		= 50;
		int gPositionX	= 400;
		int gPositionY	= 1000;
		
		bouboule = new Bouboule(gRadius, BodyType.DynamicBody,
				1, 0.8f, gPositionX, gPositionY, 0, boubouleImg);
		
		//ball.SetTextureDimension(TextureDimensions.BALL_WIDTH, TextureDimensions.BALL_HEIGHT);
		graphicManager.addBody(bouboule);
		
	}
	
	private void initArena() {
				
	}

	private void initBall2() {
		
		boubouleImg = new TextureRegion( new Texture(Gdx.files.internal("images/boub.png")));
		
				
		int gRadius		= 50;
		int gPositionX	= 400;
		int gPositionY	= 0;
		
		bouboule2 = new Bouboule(gRadius, BodyType.DynamicBody,
				1, 0.9f, gPositionX, gPositionY, 0, boubouleImg);
		
		//ball.SetTextureDimension(TextureDimensions.BALL_WIDTH, TextureDimensions.BALL_HEIGHT);
		graphicManager.addBody(bouboule);
		
	}
	
	
	
	public void update(float dt) {
		
		float accelX = Gdx.input.getAccelerometerX();
		float accelY = Gdx.input.getAccelerometerY();
		//float accelZ = Gdx.input.getAccelerometerZ();
		
		
		//bouboule.body.applyForceToCenter(new Vector2(0,-1));
		bouboule2.body.applyForceToCenter(new Vector2(-accelX*0.5f,-accelY*0.5f));
		
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
