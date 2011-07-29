/*
 ******************************************************************************
 * Parts of this code sample are licensed under Apache License, Version 2.0   *
 * Copyright (c) 2009, Android Open Handset Alliance. All rights reserved.    *
 *																			  *																			*
 * Except as noted, this code sample is offered under a modified BSD license. *
 * Copyright (C) 2010, Motorola Mobility, Inc. All rights reserved.           *
 * 																			  *
 * For more details, see MOTODEV_Studio_for_Android_LicenseNotices.pdf        * 
 * in your installation folder.                                               *
 ******************************************************************************
 */
package com.bumerang.util;

import java.util.*;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;
import android.text.*;

import com.bumerang.util.*;

public class DOWNLOADSContentProvider extends ContentProvider {

	private FilesOpenHelper dbHelper;
	private static HashMap<String, String> DOWNLOADS_PROJECTION_MAP;
	private static final String TABLE_NAME = "downloads";
	private static final String AUTHORITY = "com.bumerang.util.downloadscontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	public static final Uri DL_ID_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/dl_id");
	public static final Uri TITLE_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/title");
	public static final Uri FILENAME_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/filename");
	public static final Uri SIZE_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/size");
	public static final Uri DATE_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/date");
	public static final Uri POSITION_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/position");

	public static final String DEFAULT_SORT_ORDER = "DL_ID ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int DOWNLOADS = 1;
	private static final int DOWNLOADS_DL_ID = 2;
	private static final int DOWNLOADS_TITLE = 3;
	private static final int DOWNLOADS_FILENAME = 4;
	private static final int DOWNLOADS_SIZE = 5;
	private static final int DOWNLOADS_DATE = 6;
	private static final int DOWNLOADS_POSITION = 7;
	private static final int DOWNLOADS_ALBUMS = 8;
	

	// Content values keys (using column names)
	public static final String DL_ID = "DL_ID";
	public static final String TITLE = "TITLE";
	public static final String FILENAME = "FILENAME";
	public static final String SIZE = "SIZE";
	public static final String DATE = "DATE";
	public static final String POSITION = "POSITION";
	public static final String SUM_SIZE = "SUM_SIZE";	
	

	public boolean onCreate() {
		dbHelper = new FilesOpenHelper(getContext(), true);
		return (dbHelper == null) ? false : true;
	}

	
	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		SQLiteDatabase mDB = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URL_MATCHER.match(url)) {
		case DOWNLOADS:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(DOWNLOADS_PROJECTION_MAP);
			break;
		case DOWNLOADS_DL_ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("dl_id='" + url.getPathSegments().get(2) + "'");
			break;
		case DOWNLOADS_TITLE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("title='" + url.getPathSegments().get(2) + "'");
			break;
		case DOWNLOADS_FILENAME:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("filename='" + url.getPathSegments().get(2) + "'");
			break;
		case DOWNLOADS_SIZE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("size='" + url.getPathSegments().get(2) + "'");
			break;
		case DOWNLOADS_DATE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("date='" + url.getPathSegments().get(2) + "'");
			break;
		case DOWNLOADS_POSITION:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("position='" + url.getPathSegments().get(2) + "'");
			break;
		case DOWNLOADS_ALBUMS:
			return mDB.rawQuery("SELECT date,sum(size) as SUM_SIZE  FROM downloads GROUP BY date ORDER BY date ASC", null);
			

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		String orderBy = "";
		if (TextUtils.isEmpty(sort)) {
			orderBy = DEFAULT_SORT_ORDER;
		} else {
			orderBy = sort;
		}
		Cursor c = qb.query(mDB, projection, selection, selectionArgs, null,
				null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), url);
		return c;
	}

	public String getType(Uri url) {
		switch (URL_MATCHER.match(url)) {
		case DOWNLOADS:
			return "vnd.android.cursor.dir/vnd.com.bumerang.util.downloads";
		case DOWNLOADS_DL_ID:
			return "vnd.android.cursor.item/vnd.com.bumerang.util.downloads";
		case DOWNLOADS_TITLE:
			return "vnd.android.cursor.item/vnd.com.bumerang.util.downloads";
		case DOWNLOADS_FILENAME:
			return "vnd.android.cursor.item/vnd.com.bumerang.util.downloads";
		case DOWNLOADS_SIZE:
			return "vnd.android.cursor.item/vnd.com.bumerang.util.downloads";
		case DOWNLOADS_DATE:
			return "vnd.android.cursor.item/vnd.com.bumerang.util.downloads";
		case DOWNLOADS_POSITION:
			return "vnd.android.cursor.item/vnd.com.bumerang.util.downloads";
		case DOWNLOADS_ALBUMS:
			return "vnd.android.cursor.item/vnd.com.bumerang.util.downloads";

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
	}

	public Uri insert(Uri url, ContentValues initialValues) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		long rowID;
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		if (URL_MATCHER.match(url) != DOWNLOADS) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("downloads", "downloads", values);
		if (rowID > 0) {
			Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}
		throw new SQLException("Failed to insert row into " + url);
	}

	public int delete(Uri url, String where, String[] whereArgs) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
		case DOWNLOADS:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case DOWNLOADS_DL_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"dl_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case DOWNLOADS_TITLE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"title="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case DOWNLOADS_FILENAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"filename="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case DOWNLOADS_SIZE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"size="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case DOWNLOADS_DATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"date="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case DOWNLOADS_POSITION:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"position="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	public int update(Uri url, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
		case DOWNLOADS:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case DOWNLOADS_DL_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"dl_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case DOWNLOADS_TITLE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"title="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case DOWNLOADS_FILENAME:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"filename="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case DOWNLOADS_SIZE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"size="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case DOWNLOADS_DATE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"date="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case DOWNLOADS_POSITION:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"position="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	static {
		URL_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), DOWNLOADS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase()+"/albums", DOWNLOADS_ALBUMS);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/dl_id"
				+ "/*", DOWNLOADS_DL_ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/title"
				+ "/*", DOWNLOADS_TITLE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/filename"
				+ "/*", DOWNLOADS_FILENAME);
		URL_MATCHER.addURI(AUTHORITY,
				TABLE_NAME.toLowerCase() + "/size" + "/*", DOWNLOADS_SIZE);
		URL_MATCHER.addURI(AUTHORITY,
				TABLE_NAME.toLowerCase() + "/date" + "/*", DOWNLOADS_DATE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/position"
				+ "/*", DOWNLOADS_POSITION);

		DOWNLOADS_PROJECTION_MAP = new HashMap<String, String>();
		DOWNLOADS_PROJECTION_MAP.put(DL_ID, "dl_id");
		DOWNLOADS_PROJECTION_MAP.put(TITLE, "title");
		DOWNLOADS_PROJECTION_MAP.put(FILENAME, "filename");
		DOWNLOADS_PROJECTION_MAP.put(SIZE, "size");
		DOWNLOADS_PROJECTION_MAP.put(DATE, "date");
		DOWNLOADS_PROJECTION_MAP.put(POSITION, "position");

	}
}
