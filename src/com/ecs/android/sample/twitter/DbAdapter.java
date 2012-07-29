package com.ecs.android.sample.twitter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapter {
	static final String Key_rowid = "_id";
	static final String Key_eventname = "eventname";
	static final String Key_hashtag = "hashtag";
	static final String Key_time = "stime";
	static final String TAG = "DbAdapter";

	static final String Database_name = "tweettag.db";
	static final String Database_table = "records";
	static final int Database_version = 1;

	final Context context;

	DatabaseHelpter dbhelper;
	SQLiteDatabase database;

	public DbAdapter(Context ctx) {
		this.context = ctx;
		dbhelper = new DatabaseHelpter(context);
	}

	public DbAdapter open() throws SQLException {
		database = dbhelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbhelper.close();
	}

	public long insertEntry(String ename, String htag) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(Key_eventname, ename);
		initialValues.put(Key_hashtag, htag);
		initialValues.put(Key_time,
				DateTimeUtils.GetCurrentTime_UTCMilliSeconds());
		return database.insert(Database_table, null, initialValues);
	}

	public Cursor getHistory() {

		return database.query(Database_table, new String[] { Key_rowid,
				Key_eventname, Key_hashtag, Key_time }, null, null, null, null,
				null);

	}

}