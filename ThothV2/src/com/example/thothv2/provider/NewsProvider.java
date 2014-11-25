package com.example.thothv2.provider;


import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NewsProvider extends ThothProvider{

	private final static int ROOT_NEWS  = 10;
	private final static int NEWS = 11;
	private final static int CLASSES_NEWS = 12;
	private static final List<Integer> uriCodes = new LinkedList<Integer>();
	
	static {
		uriCodes.add(NEWS);
		uriCodes.add(CLASSES_NEWS);
		_urimatcher.addURI(AUTHORITY,"news",NEWS);
		_urimatcher.addURI(AUTHORITY, "classes/#/news", CLASSES_NEWS);
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = _sql.getReadableDatabase();
		Cursor c = null;
		
		switch(_urimatcher.match(uri)){
				
			case NEWS:
				c = db.query("news", projection, 
						selection, selectionArgs, 
						null, null, sortOrder);
				return c;	
				
			case CLASSES_NEWS:
				c = db.query("news", projection, 
						"_classes = ?", new String[]{uri.getPathSegments().get(1)}, 
						null, null, sortOrder);
				return c;	
				
			default: return null;
		}
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = _sql.getWritableDatabase();
	    // insert row
	    long tag_id = db.insert("news",null, values);
	    
	    String uri_id = uri.toString() + "/" + Long.toString(tag_id);
	    return Uri.parse(uri_id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		SQLiteDatabase db = _sql.getWritableDatabase();
	    // delete row
	    return db.delete("news",selection, selectionArgs);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		SQLiteDatabase db = _sql.getWritableDatabase();
	    // update row
	    return db.update("news", values, selection, selectionArgs);
	}
	
	@Override
	public boolean onCreate() {
		return true;
	}
	
	@Override
	protected ThothProvider getProvider(int uriCode)
	{
		if(uriCodes.contains(uriCode))
			return this;
		
		return null;
		
	}

}
