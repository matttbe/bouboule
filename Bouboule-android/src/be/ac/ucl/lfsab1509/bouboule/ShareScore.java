package be.ac.ucl.lfsab1509.bouboule;

/*
 * This file is part of Bouboule.
 * 
 * Copyright 2013 UCLouvain
 * 
 * Authors:
 *  * Group 7 - Course: http://www.uclouvain.be/en-cours-2013-lfsab1509.html
 *    Matthieu Baerts <matthieu.baerts@student.uclouvain.be>
 *    Baptiste Remy <baptiste.remy@student.uclouvain.be>
 *    Nicolas Van Wallendael <nicolas.vanwallendael@student.uclouvain.be>
 *    Helene Verhaeghe <helene.verhaeghe@student.uclouvain.be>
 * 
 * Bouboule is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.View;

public class ShareScore {

	private Activity activity;
	private static final String imageName = "bouboule_score.jpg";

	public ShareScore(Activity activity) {
		this.activity = activity;

		prepareScreenshot();
	}

	private void prepareScreenshot() {
		File imageFile = new File(activity.getFilesDir(), imageName);
		if (imageFile.exists())
			imageFile.delete();

		Bitmap bitmap = getScreenshotBitMap();
		//		+ " " + bitmap.getByteCount());

		// ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
		// byte[] data = stream.toByteArray();

		// save it somewhere (with compression)
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);
			// outputStream.write(data);
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			Log.e("Activity", "File Not Found: " + imageFile.getAbsolutePath());
		} catch (IOException e) {
			Log.e("Activity", "IO error: " + imageFile.getAbsolutePath());
		}
	}

	// http://stackoverflow.com/questions/10296711
	private Bitmap getScreenshotBitMap() {
		View decorView = activity.getWindow().getDecorView();
		decorView.setDrawingCacheEnabled(true);
		decorView.buildDrawingCache();
		Bitmap bitmap = decorView.getDrawingCache();

		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		Point size = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(size);
		int width = size.x;
		int height = size.y;

		Bitmap screenshot = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height  - statusBarHeight);
		decorView.destroyDrawingCache();
		return screenshot;
	}

	public Intent getIntent (String text, Resources r) {
	//public static Intent getShareScoreIntent (String text) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT,
				text + "\n"
				+ r.getString(R.string.beat_me) + "\n"
				+ "http://is.gd/bouboule"); // Android Play Store.
				// stats: http://is.gd/bouboule-
		// shareIntent.setType("text/plain");
		shareIntent.setType("image/jpeg");
		shareIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.parse("content://" + ShareScoreImageProvider.AUTHORITY
						+ File.separator + "img" + File.separator + imageName));
		// shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		return shareIntent;
	}
}
