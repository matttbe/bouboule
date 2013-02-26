package be.ac.ucl.lfsab1509.bouboule.game;

import com.badlogic.gdx.math.Intersector;

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

	Texture                         boubouleImg;
	Texture                         plateauImg;
	Circle                          bouboule;
	Circle                          plateau;
	SpriteBatch             		batch;
	OrthographicCamera      		camera;
	Intersector						intersector;


	@Override
	public void create() {

		intersector = new Intersector();
		
		boubouleImg = new Texture(Gdx.files.internal("images/boub.png"));
		plateauImg = new Texture(Gdx.files.internal("images/plateau.png"));

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, appWidth, appHeigth);

		//Batch
		batch = new SpriteBatch();


		// create a Circle to logically represent Bouboule

		plateau = new Circle(appWidth/2 , (appHeigth-appWidth) /2 + appWidth/2, appWidth/2 - 80/2);
		bouboule = new Circle(plateau.x, plateau.y, 80/2);




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
		batch.draw(plateauImg, plateau.x - plateau.radius, plateau.y - plateau.radius, plateau.radius*2 , plateau.radius*2 ); 
		batch.draw(boubouleImg, bouboule.x - 64, bouboule.y - 64);
		batch.end();



		//Mouvement 

		//EN X
		float accelX = Gdx.input.getAccelerometerX();
		float accelY = Gdx.input.getAccelerometerY();
		float accelZ = Gdx.input.getAccelerometerZ();

		Gdx.app.log ("Accelerometer", accelX+"  "+accelY+"  "+accelZ+"  ");

		bouboule.x -= 100 * accelX * Gdx.graphics.getDeltaTime();
		bouboule.y -= 100 * accelY * Gdx.graphics.getDeltaTime();
		
		/*
		if(!intersector.overlapCircles(plateau, bouboule)){
			bouboule.x=plateau.x;
			bouboule.y=plateau.y;
		}
		*/
		
		//remetre la boule au centre quand elle tombe hors du ring
		if((bouboule.x-plateau.x)*(bouboule.x-plateau.x) + (bouboule.y-plateau.y)*(bouboule.y-plateau.y) >= plateau.radius*plateau.radius){
			bouboule.x=plateau.x;
			bouboule.y=plateau.y;
		}

		


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