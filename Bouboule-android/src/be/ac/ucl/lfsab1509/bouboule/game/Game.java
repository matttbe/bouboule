package be.ac.ucl.lfsab1509.bouboule.game;

import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

public class Game implements ApplicationListener {

	final static int appWidth = 480;//Gdx.graphics.getWidth();
	final static int appHeigth = 800;//Gdx.graphics.getHeight();
	
	Texture				boubouleImg;
	Texture				plateauImg;
	Circle 				bouboule;
	Circle				plateau;
	SpriteBatch 		batch;
	OrthographicCamera 	camera;
	

	@Override
	public void create() {

		boubouleImg = new Texture(Gdx.files.internal("images/bouboule.png"));
		plateauImg = new Texture(Gdx.files.internal("images/plateau.png"));

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, appWidth, appHeigth);
		
		//Batch
		batch = new SpriteBatch();

		
		// create a Circle to logically represent Bouboule
		
		bouboule = new Circle(appWidth/2-40/2, 60, 64/2);
		plateau = new Circle(0, (appHeigth-appWidth) /2, appWidth/2);
	
		 
		
		
	}




	@Override
	public void render() {
		
		// clear the screen with a dark blue color. The
	      // arguments to glClearColor are the red, green
	      // blue and alpha component in the range [0,1]
	      // of the color to be used to clear the screen.
	      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	      
	      // tell the camera to update its matrices.
	      camera.update();
	      
	      // tell the SpriteBatch to render in the
	      // coordinate system specified by the camera.
	      batch.setProjectionMatrix(camera.combined);
	      // begin a new batch and draw Bouboule and
	      batch.begin();
	      batch.draw(boubouleImg, bouboule.x, bouboule.y);
	      batch.draw(plateauImg, plateau.x , plateau.y , plateau.radius*2 , plateau.radius*2 ); 
	      batch.end();
	      
	     
	      
	      //Movement 
	      
	      //EN X
	      float accelX = Gdx.input.getAccelerometerX();
	      float accelY = Gdx.input.getAccelerometerY();
	      float accelZ = Gdx.input.getAccelerometerZ();
	      
	      Log.d("Accelerometer", accelX+"  "+accelY+"  "+accelZ+"  ");
	      
	      if(accelX >  0 ) bouboule.x -= 200 * Gdx.graphics.getDeltaTime();
	      if(accelX <= 0 ) bouboule.x += 200 * Gdx.graphics.getDeltaTime();
	      
	      // make sure the bucket stays within the screen bounds
	      if(bouboule.x < 0) bouboule.x = 0;
	      if(bouboule.x > (appWidth - bouboule.radius) ) bouboule.x = appWidth - bouboule.radius;
	      
	      
	      
	      //EN Y
	      if(accelY >  0 ) bouboule.y -= 200 * Gdx.graphics.getDeltaTime();
	      if(accelY <= 0 ) bouboule.y += 200 * Gdx.graphics.getDeltaTime();
	      
	      // make sure the bucket stays within the screen bounds
	      if(bouboule.y < 0) bouboule.y = 0;
	      if(bouboule.y > (appHeigth - bouboule.radius) ) bouboule.y = appHeigth - bouboule.radius ;
	      
	      
	}




	@Override
	public void dispose() {
		
		boubouleImg.dispose();
		plateauImg.dispose();
		batch.dispose();
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}


