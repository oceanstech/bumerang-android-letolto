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
package com.bumerang.util.db;

import java.util.*;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;
import android.text.*;

import com.bumerang.*;

public class MUSOROKContentProvider extends ContentProvider {

	private MusorDBHelper dbHelper;
	private static HashMap<String, String> MUSOROK_PROJECTION_MAP;
	private static final String TABLE_NAME = "musorok";
	private static final String AUTHORITY = "com.bumerang.musorokcontentprovider";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);
	public static final Uri MUSOR_ID_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/musor_id");
	public static final Uri CIM_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/cim");
	public static final Uri FILE_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/file");
	public static final Uri DAY_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/day");
	public static final Uri SORSZAM_FIELD_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + TABLE_NAME.toLowerCase() + "/sorszam");

	public static final String DEFAULT_SORT_ORDER = "MUSOR_ID ASC";

	private static final UriMatcher URL_MATCHER;

	private static final int MUSOROK = 1;
	private static final int MUSOROK_MUSOR_ID = 2;
	private static final int MUSOROK_CIM = 3;
	private static final int MUSOROK_FILE = 4;
	private static final int MUSOROK_DAY = 5;
	private static final int MUSOROK_SORSZAM = 6;

	// Content values keys (using column names)
	public static final String MUSOR_ID = "MUSOR_ID";
	public static final String CIM = "cim";
	public static final String FILE = "file";
	public static final String DAY = "day";
	public static final String SORSZAM = "sorszam";

	public boolean onCreate() {
		dbHelper = new MusorDBHelper(getContext(), true);
		return (dbHelper == null) ? false : true;
	}

	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		SQLiteDatabase mDB = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URL_MATCHER.match(url)) {
		case MUSOROK:
			qb.setTables(TABLE_NAME);
			qb.setProjectionMap(MUSOROK_PROJECTION_MAP);
			break;
		case MUSOROK_MUSOR_ID:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("musor_id='" + url.getPathSegments().get(2) + "'");
			break;
		case MUSOROK_CIM:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("cim='" + url.getPathSegments().get(2) + "'");
			break;
		case MUSOROK_FILE:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("file='" + url.getPathSegments().get(2) + "'");
			break;
		case MUSOROK_DAY:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("day='" + url.getPathSegments().get(2) + "'");
			break;
		case MUSOROK_SORSZAM:
			qb.setTables(TABLE_NAME);
			qb.appendWhere("sorszam='" + url.getPathSegments().get(2) + "'");
			break;

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
		case MUSOROK:
			return "vnd.android.cursor.dir/vnd.com.bumerang.musorok";
		case MUSOROK_MUSOR_ID:
			return "vnd.android.cursor.item/vnd.com.bumerang.musorok";
		case MUSOROK_CIM:
			return "vnd.android.cursor.item/vnd.com.bumerang.musorok";
		case MUSOROK_FILE:
			return "vnd.android.cursor.item/vnd.com.bumerang.musorok";
		case MUSOROK_DAY:
			return "vnd.android.cursor.item/vnd.com.bumerang.musorok";
		case MUSOROK_SORSZAM:
			return "vnd.android.cursor.item/vnd.com.bumerang.musorok";

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
		if (URL_MATCHER.match(url) != MUSOROK) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("musorok", "musorok", values);
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
		case MUSOROK:
			count = mDB.delete(TABLE_NAME, where, whereArgs);
			break;
		case MUSOROK_MUSOR_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"musor_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case MUSOROK_CIM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"cim="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case MUSOROK_FILE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"file="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case MUSOROK_DAY:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"day="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case MUSOROK_SORSZAM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.delete(TABLE_NAME,
					"sorszam="
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
		case MUSOROK:
			count = mDB.update(TABLE_NAME, values, where, whereArgs);
			break;
		case MUSOROK_MUSOR_ID:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"musor_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case MUSOROK_CIM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"cim="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case MUSOROK_FILE:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"file="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case MUSOROK_DAY:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"day="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case MUSOROK_SORSZAM:
			segment = "'" + url.getPathSegments().get(2) + "'";
			count = mDB.update(TABLE_NAME, values,
					"sorszam="
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), MUSOROK);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/musor_id"
				+ "/*", MUSOROK_MUSOR_ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/cim" + "/*",
				MUSOROK_CIM);
		URL_MATCHER.addURI(AUTHORITY,
				TABLE_NAME.toLowerCase() + "/file" + "/*", MUSOROK_FILE);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/day" + "/*",
				MUSOROK_DAY);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/sorszam"
				+ "/*", MUSOROK_SORSZAM);

		MUSOROK_PROJECTION_MAP = new HashMap<String, String>();
		MUSOROK_PROJECTION_MAP.put(MUSOR_ID, "musor_id");
		MUSOROK_PROJECTION_MAP.put(CIM, "cim");
		MUSOROK_PROJECTION_MAP.put(FILE, "file");
		MUSOROK_PROJECTION_MAP.put(DAY, "day");
		MUSOROK_PROJECTION_MAP.put(SORSZAM, "sorszam");

	}
}
