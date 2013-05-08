/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// from easyang project

package be.ac.ucl.lfsab1509.bouboule.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import be.ac.ucl.lfsab1509.bouboule.R;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GlobalSettings.GameExitStatus;

import java.util.LinkedList;

/**
 * Plays a series of audio URIs, but does all the hard work on another thread
 * so that any slowness with preparing or loading doesn't block the calling thread.
 */
public class AsyncPlayer {
	private static final int PLAY = 1;
	private static final int STOP = 2;
	private static final int PAUSE = 3;
	private static final boolean mDebug = false;

	private static final class Command {
		int code;
		Context context;
		Uri uri;
		boolean looping;
		int stream;
		long requestTime;
		boolean play = false;
		GameExitStatus exitStatus;

		OnErrorListener errorListener;
		OnBufferingUpdateListener bufferingUpdateListener;
		OnCompletionListener completionListener;

		public String toString() {
			return "{ code=" + code + " looping=" + looping + " stream=" + stream
					+ " uri=" + uri + " }";
		}
	}

	private LinkedList<Command> mCmdQueue = new LinkedList<Command>();

	private MediaPlayer getMediaPlayer (int stream, Context context, Uri uri,
			boolean looping, OnErrorListener errorListener,
			OnBufferingUpdateListener bufferingUpdateListener,
			OnCompletionListener completionListener) throws Exception {
		MediaPlayer player = new MediaPlayer();
		player.setAudioStreamType(stream);
		player.setDataSource(context, uri);
		player.setLooping(looping);
		player.setOnErrorListener(errorListener);
		player.setOnBufferingUpdateListener(bufferingUpdateListener);
		player.setOnCompletionListener(completionListener);
		player.prepare();
		return player;
	}

	private OnCompletionListener getCompletionListener (final Command cmd) {
		return new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (mState == PLAY) { // only if the menu has not been stopped before
					cmd.exitStatus = GameExitStatus.NONE; // start menu sound
					cmd.requestTime = SystemClock.uptimeMillis(); // avoid warnings
					startSound(cmd);
				}
			}
		};
	}

	private void startSound(Command cmd) {
		// Preparing can be slow, so if there is something else
		// is playing, let it continue until we're done, so there
		// is less of a glitch.
		try {
			if (mDebug) Log.d(mTag, "Starting playback");
			MediaPlayer player;
			switch (cmd.exitStatus) { // launch a sound before
			case WIN:
			case GAMEOVER_END:
				Uri uriWin = Uri.parse("android.resource://be.ac.ucl.lfsab1509.bouboule/" + R.raw.win);
				player = getMediaPlayer(cmd.stream, cmd.context, uriWin,
						false, cmd.errorListener, cmd.bufferingUpdateListener,
						cmd.completionListener);
				player.setOnCompletionListener(getCompletionListener(cmd));
				player.start();
				break;
			case LOOSE:
			case GAMEOVER_LOOSE:
				Log.d(mTag, "Starting gameover sound");
				Uri uriLoose = Uri.parse("android.resource://be.ac.ucl.lfsab1509.bouboule/" + R.raw.loose);
				player = getMediaPlayer(cmd.stream, cmd.context, uriLoose,
						false, cmd.errorListener, cmd.bufferingUpdateListener,
						cmd.completionListener);
				player.setOnCompletionListener(getCompletionListener(cmd));
				player.start();
				break;
			default:
				player = getMediaPlayer(cmd.stream, cmd.context, cmd.uri,
						cmd.looping, cmd.errorListener, cmd.bufferingUpdateListener,
						cmd.completionListener);
				if (cmd.play)
					player.start();
				break;
			}
			// release the previous player if any
			if (mPlayer != null) {
				mPlayer.release();
			}
			mPlayer = player;
			long delay = SystemClock.uptimeMillis() - cmd.requestTime;
			if (delay > 1000) {
				Log.w(mTag, "Notification sound delayed by " + delay + "msecs");
			}
		}
		catch (Exception e) {
			Log.w(mTag, "error loading sound for " + cmd.uri, e);
		}
	}

	private final class Thread extends java.lang.Thread {
		Thread() {
			super("AsyncPlayer-" + mTag);
		}

		public void run() {
			while (true) {
				Command cmd = null;

				synchronized (mCmdQueue) {
					if (mDebug) Log.d(mTag, "RemoveFirst");
					cmd = mCmdQueue.removeFirst();
				}

				switch (cmd.code) {
					case PLAY:
						if (mDebug) Log.d(mTag, "PLAY");
						startSound(cmd);
						break;
					case STOP:
						if (mDebug) Log.d(mTag, "STOP");
						if (mPlayer != null) {
							long delay = SystemClock.uptimeMillis() - cmd.requestTime;
							if (delay > 1000) {
								Log.w(mTag, "Notification stop delayed by " + delay + "msecs");
							}
							mPlayer.stop();
							mPlayer.release();
							mPlayer = null;
						} else {
							Log.w(mTag, "STOP command without a player");
						}
						break;
					/*case PAUSE:
						if (mDebug) Log.d(mTag, "PAUSE " + (mPlayer != null));
						if (mPlayer != null) {
							long delay = SystemClock.uptimeMillis() - cmd.requestTime;
							if (delay > 1000) {
								Log.w(mTag, "Notification pause delayed by " + delay + "msecs");
							}
							mPlayer.pause();
						} else {
							Log.w(mTag, "PAUSE command without a player");
						}
						break;*/
				}

				synchronized (mCmdQueue) {
					if (mCmdQueue.size() == 0) {
						// nothing left to do, quit
						// doing this check after we're done prevents the case where they
						// added it during the operation from spawning two threads and
						// trying to do them in parallel.
						mThread = null;
						releaseWakeLock();
						return;
					}
				}
			}
		}
	}

	private String mTag;
	private Thread mThread;
	private MediaPlayer mPlayer;
	private PowerManager.WakeLock mWakeLock;

	// The current state according to the caller.  Reality lags behind
	// because of the asynchronous nature of this class.
	private int mState = STOP;
	private Context context;

	/**
	 * Construct an AsyncPlayer object.
	 *
	 * @param tag a string to use for debugging
	 */
	public AsyncPlayer(String tag) {
		if (tag != null) {
			mTag = tag;
		} else {
			mTag = "AsyncPlayer";
		}
	}

	/**
	 * Start playing the sound.  It will actually start playing at some
	 * point in the future.  There are no guarantees about latency here.
	 * Calling this before another audio file is done playing will stop
	 * that one and start the new one.
	 *
	 * @param context Your application's context.
	 * @param uri The URI to play.  (see {@link MediaPlayer#setDataSource(Context, Uri)})
	 * @param looping Whether the audio should loop forever.  
	 *          (see {@link MediaPlayer#setLooping(boolean)})
	 * @param stream the AudioStream to use.
	 *          (see {@link MediaPlayer#setAudioStreamType(int)})
	 * @param play start playing directly or stay in pause
	 * @param exitStatus NONE to launch the music of the menu and another to
	 *          play a sound just before (win/loose/gameover).
	 */
	public void create (Context context, Uri uri, boolean looping, int stream,
			boolean play, GameExitStatus exitStatus) {
		this.context = context;

		Command cmd = new Command();
		cmd.requestTime = SystemClock.uptimeMillis();
		cmd.code = PLAY; // create new
		cmd.play = play;
		cmd.context = context;
		cmd.uri = uri;
		cmd.looping = looping;
		cmd.stream = stream;
		cmd.exitStatus = exitStatus;
		synchronized (mCmdQueue) {
			enqueueLocked(cmd);
			mState = play ? PLAY : PAUSE; // but real state is pause
		}
	}

	/**
	 * Stop a previously played sound.  It can't be played again or unpaused
	 * at this point.  Calling this multiple times has no ill effects.
	 */
	public void stop(Context context) {
		synchronized (mCmdQueue) {
			// This check allows stop to be called multiple times without starting
			// a thread that ends up doing nothing.
			if (mState != STOP && this.context == context) {
				Command cmd = new Command();
				cmd.requestTime = SystemClock.uptimeMillis();
				cmd.code = STOP;
				enqueueLocked(cmd);
				mState = STOP;
			}
		}
	}

	public void pause() {
		if (mDebug) Log.d ("Sound", "Pause? " + mState);
		if (mState != STOP && mPlayer != null) {
			mPlayer.pause ();
			mState = PAUSE;
		}
	}

	/**
	 * It will restart the current player or create a new one like the previous one
	 * @param exitStatus 
	 * @param context, the current context that will launch the sound
	 */
	public void play (Context context, Uri uri, boolean looping, int stream, GameExitStatus exitStatus) {
		if (mDebug) Log.d ("Sound", "replay? " + mState + " " + (mPlayer != null));
		this.context = context;
		if (mState != PLAY) {
			if (mPlayer != null) // pause
				mPlayer.start ();
			else // stop
				create (context, uri, looping, stream, true, exitStatus);
				// exist status is only needed when starting a new song => win / loose / gameover
		}
	}

	public void seekTo(int msec) {
		synchronized (mCmdQueue) {
			if (mPlayer != null) {
				mPlayer.seekTo(msec);
			}
		}
	}

	private void enqueueLocked(Command cmd) {
		mCmdQueue.add(cmd);
		if (mThread == null) {
			acquireWakeLock();
			mThread = new Thread();
			mThread.start();
		}
	}

	/**
	 * We want to hold a wake lock while we do the prepare and play.  The stop probably is
	 * optional, but it won't hurt to have it too.  The problem is that if you start a sound
	 * while you're holding a wake lock (e.g. an alarm starting a notification), you want the
	 * sound to play, but if the CPU turns off before mThread gets to work, it won't.  The
	 * simplest way to deal with this is to make it so there is a wake lock held while the
	 * thread is starting or running.  You're going to need the WAKE_LOCK permission if you're
	 * going to call this.
	 *
	 * This must be called before the first time play is called.
	 *
	 * @hide
	 */
	public void setUsesWakeLock(Context context) {
		if (mWakeLock != null || mThread != null) {
			// if either of these has happened, we've already played something.
			// and our releases will be out of sync.
			throw new RuntimeException("assertion failed mWakeLock=" + mWakeLock
					+ " mThread=" + mThread);
		}
		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, mTag);
	}

	private void acquireWakeLock() {
		if (mWakeLock != null) {
			mWakeLock.acquire();
		}
	}

	private void releaseWakeLock() {
		if (mWakeLock != null) {
			mWakeLock.release();
		}
	}
}

