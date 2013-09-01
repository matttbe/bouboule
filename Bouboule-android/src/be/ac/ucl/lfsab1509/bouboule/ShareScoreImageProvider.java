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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

// http://stackoverflow.com/questions/17082417

public class ShareScoreImageProvider extends ContentProvider {
	public static final String AUTHORITY = "be.ac.ucl.lfsab1509.bouboule";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/img/bouboule_score.jpg");

	private UriMatcher uriMatcher;

	@Override
	public boolean onCreate() {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "img/*", 1);


		return true;
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode)
			throws FileNotFoundException {

		switch (uriMatcher.match(uri)) {
		case 1:
			String fileLocation = getContext().getFilesDir() + File.separator
					+ uri.getLastPathSegment();
			ParcelFileDescriptor image = ParcelFileDescriptor.open(new File(
					fileLocation), ParcelFileDescriptor.MODE_READ_ONLY);
			return image;
		default:
			throw new FileNotFoundException("Unsupported uri: "
					+ uri.toString());
		}
	}

	@Override
	public int update(Uri uri, ContentValues contentvalues, String s,
			String[] as) {
		return 0;
	}

	@Override
	public int delete(Uri uri, String s, String[] as) {
		if (uriMatcher.match(uri) == 1) {
			String fileLocation = getContext().getFilesDir() + File.separator
					+ uri.getLastPathSegment();
			File file = new File(fileLocation);
			if (file.exists()) {
				file.delete();
				return 1;
			}
		}
		return 0;
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentvalues) {
		return null;
	}

	@Override
	public String getType(Uri uri) {
		return "image/jpeg";
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String s, String[] as1,
			String s1) {


		MatrixCursor result = new MatrixCursor(projection);

		String fileLocation = getContext().getFilesDir() + File.separator
				+ uri.getLastPathSegment();
		File file = new File(fileLocation);
		if (! file.exists())
			return result;

		Object[] row = new Object[projection.length];
		for (int i = 0; i < projection.length; i++) {
			if (projection[i].equalsIgnoreCase(MediaStore.MediaColumns.DISPLAY_NAME))
				row[i] = "Score at Bouboule Game";
			else if (projection[i].equalsIgnoreCase(MediaStore.MediaColumns.SIZE))
				row[i] = file.length();
			else if (projection[i].equalsIgnoreCase(MediaStore.MediaColumns.DATA))
				row[i] = file;
			else if (projection[i].equalsIgnoreCase(MediaStore.MediaColumns.MIME_TYPE))
				row[i] = "image/jpeg";
			else if (projection[i].equalsIgnoreCase("datetaken"))
				row[i] = System.currentTimeMillis();
		}

		result.addRow(row);
		return result;
	}

	@Override
	public String[] getStreamTypes(Uri uri, String mimeTypeFilter) {
		return null;
	}

}
