package com.ecs.android.sample.twitter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelpter extends SQLiteOpenHelper {

	static final String Key_rowid = "_id";
	static final String Key_eventname = "eventname";
	static final String Key_hashtag = "hashtag";
	static final String Key_time = "stime";

	static final String TAG = "DbAdapter";

	static final String Database_name = "tweettag.db";
	static final String Database_table = "records";
	static final int Database_version = 1;

	static final String Database_create = "create table records(_id integer primary key autoincrement,"
			+ "eventname text not null, hashtag text not null, stime integer not null);";

	public DatabaseHelpter(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DatabaseHelpter(Context context) {
		// TODO Auto-generated constructor stub
		super(context, Database_name, null, Database_version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(Database_create);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(TAG, "Upgrading database from version" + oldVersion + " to "
				+ newVersion + "which destroy all old data");
		db.execSQL("drop table if exists records");
		onCreate(db);

	}

}
