package be.ac.ucl.lfsab1509.bouboule.game.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;


public class GameLoop {
	
	GraphicManager 		graphicManager;
	Box2DDebugRenderer 	debugRenderer;
	Matrix4 			debugMatrix;
	
	SpriteBatch			batch;
	
	Bouboule		 	bouboule;
	Bouboule		 	bouboule2;
	TextureRegion 		boubouleImg;
	
	private Body bottleModel;
    private Vector2 bottleModelOrigin;
    Texture bottleTexture;
    Sprite bottleSprite;
	
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
		
		boubouleImg = new TextureRegion( new Texture(Gdx.files.internal("images/boub2.png")));
		
				
		int gRadius		= 50;
		int gPositionX	= 400;
		int gPositionY	= 1000;
		
		bouboule = new Bouboule(gRadius, BodyType.DynamicBody,
				1, 0.8f, gPositionX, gPositionY, 0, boubouleImg);
		
		//ball.SetTextureDimension(TextureDimensions.BALL_WIDTH, TextureDimensions.BALL_HEIGHT);
		graphicManager.addBody(bouboule);
		
	}
	
	private void initArena() {
		
		// 0. Create a loader for the file saved from the editor.
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/test.json"));

		// 1. Create a BodyDef, as usual.
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;

		// 2. Create a FixtureDef, as usual.
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.3f;

		// 3. Create a Body, as usual.
		bottleModel = GraphicManager.getWorld().createBody(bd);

		// 4. Create the body fixture automatically by using the loader.
		loader.attachFixture(bottleModel, "test01", fd, 8);
		bottleModelOrigin = loader.getOrigin("test01", 8).cpy();
		
		bottleTexture = new Texture(Gdx.files.internal("data/gfx/bottle.png"));
        bottleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        bottleSprite = new Sprite(bottleTexture);
        bottleSprite.setSize(8, 8*bottleSprite.getHeight()/bottleSprite.getWidth());


	}

	private void initBall2() {
		
		boubouleImg = new TextureRegion( new Texture(Gdx.files.internal("images/boub2.png")));
		
				
		int gRadius		= 50;
		int gPositionX	= 400;
		int gPositionY	= 0;
		
		bouboule2 = new Bouboule(gRadius, BodyType.DynamicBody,
				1, 0.9f, gPositionX, gPositionY, 0, boubouleImg);
		
		//ball.SetTextureDimension(TextureDimensions.BALL_WIDTH, TextureDimensions.BALL_HEIGHT);
		graphicManager.addBody(bouboule2);
		
	}
	
	
	
	public void update(float dt) {
		
		float accelX = Gdx.input.getAccelerometerX();
		float accelY = Gdx.input.getAccelerometerY();
		//float accelZ = Gdx.input.getAccelerometerZ();
		
		
		//bouboule.body.applyForceToCenter(new Vector2(0,-1));
		bouboule2.body.applyForceToCenter(new Vector2(-accelX*0.3f,-accelY*0.3f));
		
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
