package com.example.thothv2.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class SQLRunner extends SQLiteOpenHelper{

	public SQLRunner(Context context) {
		super(context, "myDB.db", null, 2);		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	
		db.execSQL("CREATE TABLE classes(_id integer primary key,_selected integer, name text, semester text, newsitemsURL text);"); // _selected representa um boolean
		db.execSQL("CREATE TABLE news(_id integer primary key, _class integer, title text, postDate text, content text, _read integer, foreign key(_class)references classes(_id));");
		System.out.println();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		return;
	}
}

