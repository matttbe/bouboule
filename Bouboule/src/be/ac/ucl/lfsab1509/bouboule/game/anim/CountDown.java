package be.ac.ucl.lfsab1509.bouboule.game.anim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CountDown {

	private static final int        FRAME_COLS = 2;
	private static final int        FRAME_ROWS = 2;

	Animation                       countDownAnimation;
	Texture                         countDownSheet;
	TextureRegion[]                 countDownFrames;
	TextureRegion                   currentFrame;

	float STEPTIME = 0.7f;
	float stateTime;

	
	public CountDown() {
		countDownSheet = new Texture("images/anim/countdown.png");
		TextureRegion[][] tmp = TextureRegion.split(countDownSheet, countDownSheet.getWidth() / 
				FRAME_COLS, countDownSheet.getHeight() / FRAME_ROWS);
		countDownFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				countDownFrames[index++] = tmp[i][j];
			}
		}
		countDownAnimation = new Animation(STEPTIME, countDownFrames);
		stateTime = 0f;
	}

	

	public boolean draw(SpriteBatch batch) {
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = countDownAnimation.getKeyFrame(stateTime, true);
		batch.draw(currentFrame,400-currentFrame.getRegionWidth()/2,625-currentFrame.getRegionHeight()/2);
		
		if (stateTime > 4*STEPTIME) 
			stateTime = 0f;
		
		return stateTime != 0f;
	}

	
}